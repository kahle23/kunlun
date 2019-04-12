package artoria.jdbc;

import artoria.common.AbstractAttributable;

import java.util.List;

/**
 * Database table necessary information.
 * @author Kahle
 */
public class TableMeta extends AbstractAttributable {
    /**
     * "TABLE_NAME"
     */
    private String name;
    /**
     * "REMARKS"
     */
    private String remarks;
    /**
     * Table primary keys.
     * If is Composite Primary Key, use "," separate.
     */
    private String primaryKey;
    /**
     * Table's column meta list.
     */
    private List<ColumnMeta> columnMetaList;

    public String getName() {

        return this.name;
    }

    public void setName(String name) {
        this.name = name;
        this.put("name", name);
    }

    public String getRemarks() {

        return this.remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
        this.put("remarks", remarks);
    }

    public String getPrimaryKey() {

        return this.primaryKey;
    }

    public void setPrimaryKey(String primaryKey) {
        this.primaryKey = primaryKey;
        this.put("primaryKey", primaryKey);
    }

    public List<ColumnMeta> getColumnMetaList() {

        return this.columnMetaList;
    }

    public void setColumnMetaList(List<ColumnMeta> columnMetaList) {
        this.columnMetaList = columnMetaList;
        this.put("columnMetaList", columnMetaList);
    }

}
