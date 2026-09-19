/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.data.currency;

import kunlun.data.currency.model.CurrencyColumn;
import kunlun.data.currency.model.CurrencyRow;
import kunlun.data.currency.model.CurrencyUnit;
import kunlun.data.currency.support.DefaultCurrencyColumnProcessor;
import kunlun.data.currency.support.JdkCurrencyService;
import kunlun.logging.Logger;
import kunlun.logging.LoggerFactory;
import kunlun.util.Assert;

import java.util.List;

/**
 * 币种工具 —— {@link CurrencyService} 的静态门面，并承载金额多币种换算（{@link CurrencyColumnProcessor}）的门面入口。
 *
 * <p>服务可替换：需要稳定符号、补全名称或自定义币种（积分、虚拟币）时，
 * 通过 {@link #setCurrencyService(CurrencyService)} 替换服务；
 * 处理器同理，通过 {@link #setCurrencyColumnProcessor(CurrencyColumnProcessor)} 替换。</p>
 *
 * @author Kahle
 */
public class CurrencyUtil {
    private static final Logger log = LoggerFactory.getLogger(CurrencyUtil.class);
    private static volatile CurrencyService currencyService;
    private static volatile CurrencyColumnProcessor currencyColumnProcessor;

    // region ======== get / set service ========

    public static CurrencyService getCurrencyService() {
        if (currencyService != null) { return currencyService; }
        synchronized (CurrencyUtil.class) {
            if (currencyService != null) { return currencyService; }
            CurrencyUtil.setCurrencyService(new JdkCurrencyService());
            return currencyService;
        }
    }

    public static void setCurrencyService(CurrencyService currencyService) {
        Assert.notNull(currencyService, "Parameter \"currencyService\" must not null. ");
        log.debug("Set currency service: {}", currencyService.getClass().getName());
        CurrencyUtil.currencyService = currencyService;
    }
    // endregion


    // region ======== service methods ========

    /**
     * 按字母码查币种定义。
     *
     * @param alphabeticCode 字母码，如 "CNY"、"USD"
     * @return 币种定义；编码为空或不存在时返回 null
     */
    public static CurrencyUnit getByAlphabeticCode(String alphabeticCode) {

        return getCurrencyService().getByAlphabeticCode(alphabeticCode);
    }

    /**
     * 按数字码查币种定义。
     *
     * @param numericCode 数字码，如 156（CNY）、840（USD）
     * @return 币种定义；数字码不存在时返回 null
     * @throws UnsupportedOperationException 服务不支持按数字码查询时抛出
     */
    public static CurrencyUnit getByNumericCode(int numericCode) {

        return getCurrencyService().getByNumericCode(numericCode);
    }

    /**
     * 列出全部可用币种，按字母码升序。
     *
     * @return 币种定义列表，无可用币种时返回空列表
     */
    public static List<CurrencyUnit> listAll() {

        return getCurrencyService().listAll();
    }
    // endregion


    // region ======== currency column process ========

    public static CurrencyColumnProcessor getCurrencyColumnProcessor() {
        if (currencyColumnProcessor != null) { return currencyColumnProcessor; }
        synchronized (CurrencyUtil.class) {
            if (currencyColumnProcessor != null) { return currencyColumnProcessor; }
            CurrencyUtil.setCurrencyColumnProcessor(new DefaultCurrencyColumnProcessor());
            return currencyColumnProcessor;
        }
    }

    /**
     * 替换金额多币种处理器.
     *
     * @param currencyColumnProcessor 处理器
     */
    public static void setCurrencyColumnProcessor(CurrencyColumnProcessor currencyColumnProcessor) {
        Assert.notNull(currencyColumnProcessor, "Parameter \"currencyColumnProcessor\" must not null. ");
        log.debug("Set currency column processor: {}", currencyColumnProcessor.getClass().getName());
        CurrencyUtil.currencyColumnProcessor = currencyColumnProcessor;
    }

    /**
     * 金额多币种换算的门面入口：按列配置（{@link CurrencyColumn}）逐行换算金额
     * （纯计算，不做填充，结果填到哪里由调用方决定）.
     *
     * @param columns 按列配置（可为空，为空时返回空列表）
     * @param rowIdField 行 id 字段名（结果 rowId 的来源，空白按 "id" 处理）
     * @param data 行数据（任意对象的列表，如结果 DTO）
     * @return 换算结果（每行一项，顺序同行数据），无可换算内容返回空列表
     */
    public static List<CurrencyRow> processCurrencyColumns(List<CurrencyColumn> columns, String rowIdField, List<?> data) {

        return getCurrencyColumnProcessor().process(columns, rowIdField, data);
    }
    // endregion

}
