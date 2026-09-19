/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.data.currency;

import kunlun.data.currency.model.CurrencyUnit;

import java.util.List;

/**
 * 币种信息查询服务 —— 币种定义（{@link kunlun.data.currency.model.CurrencyUnit}）的只读查询口。
 *
 * <p>实现负责回答"币种数据从哪来"：ISO 币种、内嵌全量表、远程接口均可；积分、虚拟币等自定义币种也通过实现表达。门面见 {@link CurrencyUtil}。</p>
 *
 * @author Kahle
 */
public interface CurrencyService {

    /**
     * 按字母码查币种定义。
     *
     * @param alphabeticCode 字母码，如 "CNY"、"USD"
     * @return 币种定义；编码为空或不存在时返回 null
     */
    CurrencyUnit getByAlphabeticCode(String alphabeticCode);

    /**
     * 按数字码查币种定义。
     *
     * @param numericCode 数字码，如 156（CNY）、840（USD）
     * @return 币种定义；数字码不存在时返回 null
     * @throws UnsupportedOperationException 实现不支持按数字码查询时抛出
     */
    CurrencyUnit getByNumericCode(int numericCode);

    /**
     * 列出全部可用币种，按字母码升序。
     *
     * @return 币种定义列表，无可用币种时返回空列表
     */
    List<CurrencyUnit> listAll();

}
