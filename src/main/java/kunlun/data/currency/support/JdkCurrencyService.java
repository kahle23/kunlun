/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.data.currency.support;

import kunlun.data.currency.CurrencyService;
import kunlun.data.currency.model.CurrencyUnit;
import kunlun.util.StrUtil;

import java.util.*;

/**
 * 基于 {@code java.util.Currency} 的币种查询服务（ISO 4217）。
 *
 * <p>名称与符号取 {@code getDisplayName()} / {@code getSymbol()}，随 JVM 默认 Locale 变化（中文环境如 人民币、￥）；
 * 可用币种为百级规模，按数字码线性查找即可，无需索引。
 * 需要稳定名称或符号时继承本类覆写 {@link #toCurrencyUnit(Currency)}，
 * 需要自定义币种时另行实现 {@link kunlun.data.currency.CurrencyService}。</p>
 *
 * @author Kahle
 */
public class JdkCurrencyService implements CurrencyService {

    private static final Comparator<CurrencyUnit> ALPHABETIC_CODE_COMPARATOR = new Comparator<CurrencyUnit>() {
        @Override
        public int compare(CurrencyUnit left, CurrencyUnit right) {
            return left.getAlphabeticCode().compareTo(right.getAlphabeticCode());
        }
    };

    /**
     * 将 JDK 币种转为币种定义：名称与符号随 JVM 默认 Locale；子类可覆写以稳定名称或符号。
     *
     * @param currency JDK 币种
     * @return 币种定义
     */
    protected CurrencyUnit toCurrencyUnit(Currency currency) {
        CurrencyUnit unit = new CurrencyUnit();
        unit.setName(currency.getDisplayName());
        unit.setAlphabeticCode(currency.getCurrencyCode());
        unit.setSymbol(currency.getSymbol());
        unit.setNumericCode(currency.getNumericCode());
        unit.setDefaultFractionDigits(currency.getDefaultFractionDigits());
        return unit;
    }

    @Override
    public CurrencyUnit getByAlphabeticCode(String alphabeticCode) {
        if (StrUtil.isBlank(alphabeticCode)) { return null; }
        try {
            return toCurrencyUnit(Currency.getInstance(alphabeticCode));
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    @Override
    public CurrencyUnit getByNumericCode(int numericCode) {
        for (Currency currency : Currency.getAvailableCurrencies()) {
            if (currency.getNumericCode() == numericCode) {
                return toCurrencyUnit(currency);
            }
        }
        return null;
    }

    @Override
    public List<CurrencyUnit> listAll() {
        Set<Currency> available = Currency.getAvailableCurrencies();
        List<CurrencyUnit> units = new ArrayList<CurrencyUnit>(available.size());
        for (Currency currency : available) {
            units.add(toCurrencyUnit(currency));
        }
        Collections.sort(units, ALPHABETIC_CODE_COMPARATOR);
        return units;
    }

}
