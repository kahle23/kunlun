/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.data.currency;

import kunlun.data.currency.model.CurrencyColumn;
import kunlun.data.currency.model.CurrencyRow;

import java.util.List;

/**
 * 金额多币种处理器（纯计算，不做自动填充）：按列配置（{@link CurrencyColumn}）逐行换算
 * 金额字段为目标币种，返回各行的换算结果（{@link CurrencyRow} 列表）.
 * <p>与 {@link kunlun.extension.convert.exchangerate.ExRateFiller} 的"填充"定位不同，
 * 本处理器只负责"数据 + 配置 → 结果"，把结果填到哪里由调用方决定
 * （如挂载到 {@code Page.others}，key：{@code currencyRows}）——普通 {@code List} 数据同样可处理。
 * <p>门面见 {@link CurrencyUtil#processCurrencyColumns(List, String, List)}；实现可整体替换，
 * 通用换算管线与汇率源扩展点见默认实现。
 *
 * @author Kahle
 * @see kunlun.data.currency.support.DefaultCurrencyColumnProcessor
 */
public interface CurrencyColumnProcessor {

    /**
     * 换算数据的目标币种金额（纯计算，不修改入参，也不做任何填充）.
     *
     * @param columns 按列配置（可为空，为空时返回空列表）
     * @param rowIdField 行 id 字段名（结果 rowId 的来源，空白按 "id" 处理）
     * @param data 行数据（任意对象的列表，如结果 DTO；金额字段名与行 id 字段名按其属性解析）
     * @return 换算结果（每行一项，顺序同行数据），无可换算内容返回空列表
     */
    List<CurrencyRow> process(List<CurrencyColumn> columns, String rowIdField, List<?> data);

}
