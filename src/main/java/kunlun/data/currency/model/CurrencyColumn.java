/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.data.currency.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 金额多币种显示的按列配置（同时充当入参与出参的数据对象，存储无关）.
 * <p>由列表页的"多币种显示"小工具按金额列生成，随查询对象传入后端；
 * 处理器（{@link kunlun.data.currency.CurrencyColumnProcessor}）逐行换算后克隆本对象并回填各目标币种的
 * 换算结果（{@link TargetAmountItem}），按行 id 挂载到 {@code Page.others}（key：{@code currencyRows}）。
 *
 * @author Kahle
 */
public class CurrencyColumn implements Serializable {

    /**
     * 金额字段：行（结果 DTO）上的原始金额字段名，驼峰，须为数值属性（未命中白名单时跳过该项并告警）
     */
    private String amountField;
    /**
     * 基准币种：原始金额的币种（ISO4217），与 {@code baseCurrencyField} 二选一，
     * 优先取本字段的直接值（两者取值皆空白则该字段整体跳过并告警）
     */
    private String baseCurrency;
    /**
     * 基准币种字段名：行（结果 DTO）上的基准币种属性名（驼峰，属性值须为字符串或枚举，
     * 枚举取 name 按编码解析），与 {@code baseCurrency} 二选一——
     * {@code baseCurrency} 为空时反射读本字段，得到各行自己的基准币种（各行币种不同的列表页用）
     */
    private String baseCurrencyField;
    /**
     * 目标金额项列表（顺序即展示顺序；入参为目标币种配置，出参回填换算结果）
     */
    private List<TargetAmountItem> targetAmountItems;

    public String getAmountField() {

        return amountField;
    }

    public void setAmountField(String amountField) {

        this.amountField = amountField;
    }

    public String getBaseCurrency() {

        return baseCurrency;
    }

    public void setBaseCurrency(String baseCurrency) {

        this.baseCurrency = baseCurrency;
    }

    public String getBaseCurrencyField() {

        return baseCurrencyField;
    }

    public void setBaseCurrencyField(String baseCurrencyField) {

        this.baseCurrencyField = baseCurrencyField;
    }

    public List<TargetAmountItem> getTargetAmountItems() {

        return targetAmountItems;
    }

    public void setTargetAmountItems(List<TargetAmountItem> targetAmountItems) {

        this.targetAmountItems = targetAmountItems;
    }


    /**
     * 目标金额项：单个目标币种的配置与换算结果（入参 + 出参）.
     *
     * @author Kahle
     */
    public static class TargetAmountItem implements Serializable {

        /**
         * 目标币种（ISO4217，解析时 trim 并统一大写）
         */
        private String targetCurrency;
        /**
         * 汇率（入参可选手输，须大于 0，目标金额 = 原始金额 × 汇率；
         * 出参为实际生效汇率——含服务端取的；目标币种与基准币种相同时为 1）
         */
        private BigDecimal exchangeRate;
        /**
         * 金额小数位（入参可选，默认 2，四舍五入；出参回填实际生效值）
         */
        private Integer amountScale;
        /**
         * 目标金额（出参：原始金额 × 生效汇率，按金额小数位四舍五入；
         */
        private BigDecimal targetAmount;

        public String getTargetCurrency() {

            return targetCurrency;
        }

        public void setTargetCurrency(String targetCurrency) {

            this.targetCurrency = targetCurrency;
        }

        public BigDecimal getExchangeRate() {

            return exchangeRate;
        }

        public void setExchangeRate(BigDecimal exchangeRate) {

            this.exchangeRate = exchangeRate;
        }

        public Integer getAmountScale() {

            return amountScale;
        }

        public void setAmountScale(Integer amountScale) {

            this.amountScale = amountScale;
        }

        public BigDecimal getTargetAmount() {

            return targetAmount;
        }

        public void setTargetAmount(BigDecimal targetAmount) {

            this.targetAmount = targetAmount;
        }

    }

}
