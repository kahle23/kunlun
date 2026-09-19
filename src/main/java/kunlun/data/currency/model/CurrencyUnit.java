/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.data.currency.model;

import java.io.Serializable;

/**
 * 通用币种定义 ——「名称 + 数字码 + 字母码 + 符号 + 默认小数位」的组合，如 人民币 / 156 / CNY / ￥ / 2，美元 / 840 / USD / $ / 2。
 *
 * <p>命名与规范字段参考 <a href="https://www.iso.org/iso-4217-currency-codes.html">ISO 4217</a>（alphabetic code、numeric code、minor unit）及
 * JSR 354 的 <a href="https://javamoney.github.io/apidocs/javax/money/CurrencyUnit.html">javax.money.CurrencyUnit</a>，并扩展展示层字段：名称与符号。
 * 不绑定 ISO 4217 —— 积分、虚拟币等自定义"币种"同样可以表达：
 * 字母码用自定义编码，数字码与默认小数位未有定义时以 -1 表示。</p>
 *
 * @author Kahle
 */
public class CurrencyUnit implements Serializable {

    /**
     * 名称：如 人民币、美元。
     */
    private String name;
    /**
     * 数字码（ISO 4217 术语 Numeric Code）：如 CNY=156、USD=840；
     * -1 表示未定义（对齐 JSR 354 约定，0 是合法值，须与"未填"区分）。
     */
    private int    numericCode = -1;
    /**
     * 字母码（ISO 4217 术语 Alphabetic Code）：如 CNY、USD；
     * ISO 内为三位字母码（前两位同国家码），非 ISO 币种用自定义编码。
     */
    private String alphabeticCode;
    /**
     * 符号：如 ￥、$（币种规范之外的展示层扩展）。
     */
    private String symbol;
    /**
     * 默认小数位（ISO 4217 的 Minor unit）：JPY=0、KWD=3、多数为 2；
     * -1 表示未定义（0 是合法值，见 JPY）。
     */
    private int    defaultFractionDigits = -1;

    public String getName() {

        return name;
    }

    public void setName(String name) {

        this.name = name;
    }

    public int getNumericCode() {

        return numericCode;
    }

    public void setNumericCode(int numericCode) {

        this.numericCode = numericCode;
    }

    public String getAlphabeticCode() {

        return alphabeticCode;
    }

    public void setAlphabeticCode(String alphabeticCode) {

        this.alphabeticCode = alphabeticCode;
    }

    public String getSymbol() {

        return symbol;
    }

    public void setSymbol(String symbol) {

        this.symbol = symbol;
    }

    public int getDefaultFractionDigits() {

        return defaultFractionDigits;
    }

    public void setDefaultFractionDigits(int defaultFractionDigits) {

        this.defaultFractionDigits = defaultFractionDigits;
    }

}
