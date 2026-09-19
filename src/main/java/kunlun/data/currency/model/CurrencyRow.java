/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.data.currency.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 单行数据的金额多币种换算结果（Java Bean）：行 id + 该行各金额字段的换算结果.
 * <p>挂载到 {@code Page.others} 后序列化形状：
 * <pre>{@code
 * "currencyRows": [
 *   { "rowId": "1", "columns": [ CurrencyColumn, ... ] }
 * ]
 * }</pre>
 *
 * @author Kahle
 */
public class CurrencyRow implements Serializable {

    /**
     * 行 id（行数据主键的字符串形式，前后端的 join key）
     */
    private String rowId;
    /**
     * 该行各金额字段的换算结果（每个金额字段一项，含 amountField 自描述）
     */
    private List<CurrencyColumn> columns = new ArrayList<CurrencyColumn>();

    public String getRowId() {

        return rowId;
    }

    public void setRowId(String rowId) {

        this.rowId = rowId;
    }

    public List<CurrencyColumn> getColumns() {

        return columns;
    }

    public void setColumns(List<CurrencyColumn> columns) {

        this.columns = columns;
    }

}
