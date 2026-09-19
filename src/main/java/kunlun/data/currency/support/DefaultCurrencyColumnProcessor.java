/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.data.currency.support;

import kunlun.data.currency.CurrencyColumnProcessor;
import kunlun.data.currency.model.CurrencyColumn;
import kunlun.data.currency.model.CurrencyRow;
import kunlun.logging.Logger;
import kunlun.logging.LoggerFactory;
import kunlun.reflect.ReflectUtil;
import kunlun.util.CollUtil;
import kunlun.util.NumberUtil;
import kunlun.util.StrUtil;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 金额多币种处理器的默认实现：反射读行数据 + 白名单校验 + 逐行换算（纯计算，不做填充）.
 * <p><b>解析规则（按优先级）</b>：
 * <ul>
 * <li><b>字段白名单</b>：{@code amountField} 须为行类型上真实存在的数值属性
 * （BigDecimal / Number / 数值原生类型），未命中跳过该项并告警——绝不反射任意字段；</li>
 * <li><b>行 id</b>：按入参 {@code rowIdField}（空白按 {@code id}）反射读行 id，
 * 缺失或为空的行跳过并告警；</li>
 * <li><b>基准币种</b>：{@code baseCurrency}（ISO4217）直接值优先；为空时按
 * {@code baseCurrencyField} 反射读行内币种（各行币种不同的列表页用，字段名须为行类型上
 * 真实存在的字符串/枚举属性，未命中跳过该配置并告警）；两者取值皆空白的行该字段跳过并告警——
 * 不默认、不将错就错；</li>
 * <li><b>汇率</b>：手输 exchangeRate（须 &gt; 0）→ 服务端汇率源（{@link #resolveRate}，
 * 默认无数据源、恒返回 null）→ 跳过该币种并告警；
 * 目标币种与基准币种相同时按原值展示（汇率 1，不查汇率源）；</li>
 * <li><b>只做换算</b>：币种名称、符号等展示信息不产出（避免接口返回值膨胀冗余），
 * 需要展示的由调用方按币种服务（{@link kunlun.data.currency.CurrencyUtil}）自行补全；
 * 目标金额只出数值原值（按 amountScale 定标），千分位等格式化交给前端。</li>
 * </ul>
 * <p>换算结果写入配置的克隆（入参对象本身不回写，避免多行共用同一份配置时互相覆盖）；
 * 单项失败只跳过该项，不中断其余行/字段/币种，也不影响调用方本身。
 * <p>需要接入服务端汇率源时继承本类覆写 {@link #resolveRate}，或另行实现
 * {@link CurrencyColumnProcessor} 并注入门面。
 *
 * @author Kahle
 */
public class DefaultCurrencyColumnProcessor implements CurrencyColumnProcessor {
    private static final Logger log = LoggerFactory.getLogger(DefaultCurrencyColumnProcessor.class);
    private static final String DEFAULT_ID_FIELD = "id";
    private static final int DEFAULT_AMOUNT_SCALE = 2;
    private static final Set<Class<?>> NUMERIC_TYPES;

    static {
        Set<Class<?>> numerics = new HashSet<Class<?>>();
        numerics.add(BigDecimal.class);
        numerics.add(Byte.class);      numerics.add(byte.class);
        numerics.add(Short.class);     numerics.add(short.class);
        numerics.add(Integer.class);   numerics.add(int.class);
        numerics.add(Long.class);      numerics.add(long.class);
        numerics.add(Float.class);     numerics.add(float.class);
        numerics.add(Double.class);    numerics.add(double.class);
        NUMERIC_TYPES = Collections.unmodifiableSet(numerics);
    }

    @Override
    public List<CurrencyRow> process(List<CurrencyColumn> columns, String rowIdField, List<?> data) {
        List<CurrencyRow> result = new ArrayList<CurrencyRow>();
        if (data == null || data.isEmpty() || CollUtil.isEmpty(columns)) { return result; }
        // 以首个非空行的类型解析（同批行类型一致）
        Class<?> rowType = firstRowType(data);
        if (rowType == null) { return result; }
        // 字段白名单 + 行 id 字段
        List<CurrencyColumn> validColumns = resolveColumns(rowType, columns);
        if (validColumns.isEmpty()) { return result; }
        String idField = StrUtil.isNotBlank(rowIdField) ? rowIdField.trim() : DEFAULT_ID_FIELD;
        boolean hasIdField = ReflectUtil.hasField(rowType, idField);
        // 逐行换算：每行一项（rowId + 各金额字段的换算结果），顺序同行数据
        for (Object row : data) {
            if (row == null) { continue; }
            Object id = hasIdField ? ReflectUtil.getFieldValue(row, idField) : null;
            if (id == null) {
                log.warn("CurrencyColumn: row of {} has no id, skipped", rowType.getName());
                continue;
            }
            List<CurrencyColumn> convertedColumns = convertColumns(row, validColumns);
            if (convertedColumns.isEmpty()) { continue; }
            CurrencyRow currencyRow = new CurrencyRow();
            currencyRow.setRowId(String.valueOf(id));
            currencyRow.setColumns(convertedColumns);
            result.add(currencyRow);
        }
        return result;
    }

    /**
     * 首个非空行的类型（行 id 与金额字段的反射依据）.
     */
    private Class<?> firstRowType(List<?> data) {
        for (Object row : data) {
            if (row != null) { return row.getClass(); }
        }
        return null;
    }

    /**
     * 字段白名单：真实存在 + 数值类型；基准币种二选一校验（直接值或行内字段名，
     * 皆无或行内字段名未命中的配置跳过并告警）.
     */
    private List<CurrencyColumn> resolveColumns(Class<?> rowType, List<CurrencyColumn> columns) {
        List<CurrencyColumn> valid = new ArrayList<CurrencyColumn>();
        for (CurrencyColumn config : columns) {
            if (config == null) { continue; }
            String amountField = trimToNull(config.getAmountField());
            if (amountField == null || CollUtil.isEmpty(config.getTargetAmountItems())) { continue; }
            Field reflectField = ReflectUtil.getField(rowType, amountField);
            if (reflectField == null || !NUMERIC_TYPES.contains(reflectField.getType())) {
                log.warn("CurrencyColumn: '{}' is not a numeric field of {}, skipped", amountField, rowType.getName());
                continue;
            }
            if (trimToNull(config.getBaseCurrency()) != null) { valid.add(config); continue; }
            // 基准币种无直接值：走行内字段名解析，先按白名单校验字段名（绝不反射任意字段）
            String currencyField = trimToNull(config.getBaseCurrencyField());
            if (currencyField == null) {
                log.warn("CurrencyColumn: base currency and base currency field of [{}] are both blank, skipped", amountField);
                continue;
            }
            Field currencyReflectField = ReflectUtil.getField(rowType, currencyField);
            if (currencyReflectField == null || !isCurrencyValueType(currencyReflectField.getType())) {
                log.warn("CurrencyColumn: '{}' is not a string/enum field of {}, skipped", currencyField, rowType.getName());
                continue;
            }
            valid.add(config);
        }
        return valid;
    }

    /**
     * 行内币种属性的类型白名单：字符串或枚举（枚举取 name 按编码解析）.
     */
    private static boolean isCurrencyValueType(Class<?> type) {

        return type == String.class || type.isEnum();
    }

    /**
     * 单行各金额字段换算.
     */
    private List<CurrencyColumn> convertColumns(Object row, List<CurrencyColumn> columns) {
        List<CurrencyColumn> converted = new ArrayList<CurrencyColumn>(columns.size());
        for (CurrencyColumn config : columns) {
            CurrencyColumn result = convertRow(row, config);
            if (result != null) { converted.add(result); }
        }
        return converted;
    }

    /**
     * 单行单字段换算：克隆配置并回填各目标币种的换算结果.
     *
     * @param row 行数据（如结果 DTO）
     * @param config 按列配置（不回写）
     * @return 换算结果（克隆），无可换算内容返回 null
     */
    protected CurrencyColumn convertRow(Object row, CurrencyColumn config) {
        Object value = ReflectUtil.getFieldValue(row, config.getAmountField().trim());
        // 金额为空（业务上常见）静默跳过该行该字段
        BigDecimal sourceAmount = value == null ? null : NumberUtil.toBigDecimal(value);
        if (sourceAmount == null) { return null; }
        String baseCurrency = resolveBaseCurrency(row, config);
        if (baseCurrency == null) {
            log.warn("CurrencyColumn: base currency of [{}] is blank (baseCurrencyField: {}), skipped",
                    config.getAmountField(), config.getBaseCurrencyField());
            return null;
        }
        CurrencyColumn result = new CurrencyColumn();
        result.setAmountField(config.getAmountField());
        result.setBaseCurrency(baseCurrency);
        List<CurrencyColumn.TargetAmountItem> items = new ArrayList<CurrencyColumn.TargetAmountItem>();
        for (CurrencyColumn.TargetAmountItem item : config.getTargetAmountItems()) {
            if (item == null) { continue; }
            String target = normalizeCurrency(item.getTargetCurrency());
            if (target == null) { continue; }
            // 手输汇率优先（须大于 0），缺省走服务端汇率源
            BigDecimal exchangeRate = item.getExchangeRate();
            if (exchangeRate != null && exchangeRate.compareTo(BigDecimal.ZERO) <= 0) {
                log.warn("CurrencyColumn: manual rate of [{}] must be positive, skipped", target);
                continue;
            }
            if (exchangeRate == null && !target.equals(baseCurrency)) {
                exchangeRate = resolveRate(row, baseCurrency, target);
                if (exchangeRate == null) {
                    log.warn("CurrencyColumn: rate [{} -> {}] not found, skipped", baseCurrency, target);
                    continue;
                }
            }
            // 目标币种与基准币种相同：按原值展示（汇率 1，不查汇率源）
            if (exchangeRate == null) { exchangeRate = BigDecimal.ONE; }
            Integer amountScale = item.getAmountScale();
            int scale = amountScale != null && amountScale >= 0 ? amountScale : DEFAULT_AMOUNT_SCALE;
            BigDecimal targetAmount = sourceAmount.multiply(exchangeRate).setScale(scale, RoundingMode.HALF_UP);
            CurrencyColumn.TargetAmountItem itemResult = new CurrencyColumn.TargetAmountItem();
            itemResult.setTargetCurrency(target);
            itemResult.setExchangeRate(exchangeRate);
            itemResult.setAmountScale(scale);
            itemResult.setTargetAmount(targetAmount);
            items.add(itemResult);
        }
        if (items.isEmpty()) { return null; }
        result.setTargetAmountItems(items);
        return result;
    }

    /**
     * 基准币种解析：优先 {@code baseCurrency} 的直接值；为空时按 {@code baseCurrencyField}
     * 反射读行内币种（各行币种不同的列表页用），仍为空返回 null（调用方跳过并告警）.
     */
    private String resolveBaseCurrency(Object row, CurrencyColumn config) {
        String direct = normalizeCurrency(config.getBaseCurrency());
        if (direct != null) { return direct; }
        String currencyField = trimToNull(config.getBaseCurrencyField());
        if (currencyField == null) { return null; }
        Object currency = ReflectUtil.getFieldValue(row, currencyField);
        return currency == null ? null : normalizeCurrency(String.valueOf(currency));
    }

    /**
     * 服务端汇率解析（可覆写）：默认无汇率数据源、恒返回 null（调用方跳过该币种并告警），
     * 需要服务端汇率的集成方覆写本方法接入汇率源（整体防御，取数失败返回 null 即可）.
     *
     * @param row 当前正在换算的行数据（如需按行创建时间取历史汇率，在此反射读取）
     * @param baseCurrency 基准币种（统一大写）
     * @param targetCurrency 目标币种（统一大写）
     * @return 可用汇率，取不到返回 null
     */
    protected BigDecimal resolveRate(Object row, String baseCurrency, String targetCurrency) {

        return null;
    }

    /**
     * 币种编码归一：trim + 大写，空白返回 null.
     */
    private String normalizeCurrency(String currency) {
        String normalized = trimToNull(currency);
        return normalized == null ? null : normalized.toUpperCase();
    }

    /**
     * trim 后为空白串返回 null.
     */
    private static String trimToNull(String text) {
        if (text == null) { return null; }
        String trimmed = text.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

}
