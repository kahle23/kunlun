package kunlun.data.currency.support;

import kunlun.data.currency.model.CurrencyColumn;
import kunlun.data.currency.model.CurrencyRow;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * DefaultCurrencyColumnProcessor 的纯内存测试（不依赖汇率数据源）.
 * 覆盖：基准币种二选一（{@code baseCurrency} 直接值优先，为空按 {@code baseCurrencyField}
 * 反射读行内币种）、行内币种逐行不同、行内币种为空该行该字段跳过、
 * 行内字段名不存在或类型不符（非字符串/枚举）该配置跳过、枚举取 name 解析、
 * 两者皆空白该配置跳过、克隆隔离（入参配置不回写）.
 *
 * @author Kahle
 * @since 2026-09-15
 */
public class DefaultCurrencyColumnProcessorTest {

    @Test
    public void baseCurrencyFieldResolvedPerRow() {
        // 行内币种逐行不同：各行按自己的币种换算（CNY 行查服务端汇率，USD 行目标币种相同按原值不查）
        FakeRateProcessor processor = new FakeRateProcessor();
        processor.rates.put("CNY->USD", new BigDecimal("0.14"));
        RowCurrencyRow cnyRow = row(1L, "10000", "CNY");
        RowCurrencyRow usdRow = row(2L, "100", "USD");
        List<CurrencyRow> rows = processor.process(
                Arrays.asList(config("amount", null, "currency", item("USD", null))),
                "id", Arrays.asList(cnyRow, usdRow));
        assertEquals(2, rows.size());
        CurrencyColumn cnyColumn = columnOf(rows, "1");
        assertEquals("CNY", cnyColumn.getBaseCurrency());
        assertEquals(0, cnyColumn.getTargetAmountItems().get(0)
                .getTargetAmount().compareTo(new BigDecimal("1400.00")));
        CurrencyColumn usdColumn = columnOf(rows, "2");
        assertEquals("USD", usdColumn.getBaseCurrency());
        // 目标币种与基准币种相同：汇率 1，不问汇率源（问过的只有 CNY 行这一回）
        assertEquals(0, usdColumn.getTargetAmountItems().get(0)
                .getExchangeRate().compareTo(BigDecimal.ONE));
        assertEquals(1, processor.asked.size());
        assertEquals("CNY->USD", processor.asked.get(0));
    }

    @Test
    public void directBaseCurrencyWinsOverBaseCurrencyField() {
        // 二选一：baseCurrency 直接值优先，行内币种字段即使有值也不看
        FakeRateProcessor processor = new FakeRateProcessor();
        processor.rates.put("CNY->USD", new BigDecimal("0.14"));
        RowCurrencyRow row = row(1L, "100", "USD");
        CurrencyColumn config = config("amount", "CNY", "currency", item("USD", null));
        List<CurrencyRow> rows = processor.process(Arrays.asList(config), "id", Arrays.asList(row));
        assertEquals("CNY", columnOf(rows, "1").getBaseCurrency());
        assertEquals(1, processor.asked.size());
        assertEquals("CNY->USD", processor.asked.get(0));
    }

    @Test
    public void blankRowCurrencySkipsField() {
        // 行内币种为空白：该行该字段跳过并告警，不问汇率源
        FakeRateProcessor processor = new FakeRateProcessor();
        RowCurrencyRow row = row(1L, "100", " ");
        assertTrue(processor.process(
                Arrays.asList(config("amount", null, "currency", item("USD", "0.14"))),
                "id", Arrays.asList(row)).isEmpty());
        assertTrue(processor.asked.isEmpty());
    }

    @Test
    public void unknownOrNonStringCurrencyFieldSkipsConfig() {
        // 行内币种字段名不存在 / 非字符串（枚举）类型：该配置整体跳过
        FakeRateProcessor processor = new FakeRateProcessor();
        RowCurrencyRow row = row(1L, "100", "CNY");
        assertTrue(processor.process(
                Arrays.asList(config("amount", null, "notExistField", item("USD", "0.14"))),
                "id", Arrays.asList(row)).isEmpty());
        assertTrue(processor.process(
                Arrays.asList(config("amount", null, "numericCurrency", item("USD", "0.14"))),
                "id", Arrays.asList(row)).isEmpty());
        assertTrue(processor.asked.isEmpty());
    }

    @Test
    public void enumRowCurrencyResolvedByName() {
        // 行内币种为枚举：取 name 按编码解析
        EnumCurrencyRow row = new EnumCurrencyRow();
        row.id = 1L;
        row.amount = new BigDecimal("100");
        row.currency = RowCurrency.CNY;
        FakeRateProcessor processor = new FakeRateProcessor();
        processor.rates.put("CNY->USD", new BigDecimal("0.14"));
        List<CurrencyRow> rows = processor.process(
                Arrays.asList(config("amount", null, "currency", item("USD", null))),
                "id", Arrays.asList(row));
        assertEquals("CNY", columnOf(rows, "1").getBaseCurrency());
    }

    @Test
    public void bothBaseCurrencySourcesBlankSkipsConfig() {
        // baseCurrency 与 baseCurrencyField 皆空白：该配置不可能成功，整体跳过
        FakeRateProcessor processor = new FakeRateProcessor();
        RowCurrencyRow row = row(1L, "100", "CNY");
        assertTrue(processor.process(
                Arrays.asList(config("amount", null, null, item("USD", "0.14"))),
                "id", Arrays.asList(row)).isEmpty());
        assertTrue(processor.asked.isEmpty());
    }

    @Test
    public void directValueStructureAndCloneIsolation() {
        // 直接值配置：基础结构与克隆隔离（换算结果写在克隆上，入参不回写）
        RowCurrencyRow row = row(1L, "10000", "USD");
        CurrencyColumn config = config("amount", "CNY", null, item("USD", "0.14"));
        List<CurrencyRow> rows = new DefaultCurrencyColumnProcessor()
                .process(Arrays.asList(config), "id", Arrays.asList(row));
        CurrencyColumn column = columnOf(rows, "1");
        assertEquals("amount", column.getAmountField());
        assertEquals("CNY", column.getBaseCurrency());
        assertEquals(0, column.getTargetAmountItems().get(0)
                .getTargetAmount().compareTo(new BigDecimal("1400.00")));
        assertEquals(2, column.getTargetAmountItems().get(0).getAmountScale().intValue());
        assertNull(config.getTargetAmountItems().get(0).getTargetAmount());
    }


    // region ======== 测试数据与工具 ========

    /** 覆写服务端汇率解析的假处理器（记录询问、按表返回） */
    private static class FakeRateProcessor extends DefaultCurrencyColumnProcessor {
        final Map<String, BigDecimal> rates = new HashMap<String, BigDecimal>();
        final List<String> asked = new ArrayList<String>();

        @Override
        protected BigDecimal resolveRate(Object row, String baseCurrency, String targetCurrency) {
            asked.add(baseCurrency + "->" + targetCurrency);
            return rates.get(baseCurrency + "->" + targetCurrency);
        }
    }

    private static CurrencyColumn columnOf(List<CurrencyRow> rows, String rowId) {
        for (CurrencyRow row : rows) {
            if (row.getRowId().equals(rowId)) { return row.getColumns().get(0); }
        }
        return null;
    }

    private static CurrencyColumn config(String amountField, String baseCurrency,
                                         String baseCurrencyField, CurrencyColumn.TargetAmountItem item) {
        CurrencyColumn config = new CurrencyColumn();
        config.setAmountField(amountField);
        config.setBaseCurrency(baseCurrency);
        config.setBaseCurrencyField(baseCurrencyField);
        config.setTargetAmountItems(new ArrayList<CurrencyColumn.TargetAmountItem>(
                Arrays.asList(item)));
        return config;
    }

    private static CurrencyColumn.TargetAmountItem item(String targetCurrency, String exchangeRate) {
        CurrencyColumn.TargetAmountItem item = new CurrencyColumn.TargetAmountItem();
        item.setTargetCurrency(targetCurrency);
        item.setExchangeRate(exchangeRate == null ? null : new BigDecimal(exchangeRate));
        return item;
    }

    private static RowCurrencyRow row(Long id, String amount, String currency) {
        RowCurrencyRow row = new RowCurrencyRow();
        row.id = id;
        row.amount = amount == null ? null : new BigDecimal(amount);
        row.currency = currency;
        return row;
    }

    /**
     * 模拟列表页的结果 DTO（行内币种字段 + 一个数值币种字段用于类型白名单反例）.
     */
    private static class RowCurrencyRow {
        private Long id;
        private BigDecimal amount;
        private String currency;
        private BigDecimal numericCurrency;
    }

    /**
     * 行内币种为枚举的模拟 DTO.
     */
    private static class EnumCurrencyRow {
        private Long id;
        private BigDecimal amount;
        private RowCurrency currency;
    }

    /**
     * 行内币种枚举.
     */
    private enum RowCurrency { CNY, USD }
    // endregion

}
