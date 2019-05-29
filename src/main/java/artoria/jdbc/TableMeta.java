package artoria.jdbc;

import java.io.Serializable;
import java.util.List;

/**
 * Database table necessary information.
 * @author Kahle
 */
public class TableMeta implements Serializable {
    /**
     * "TABLE_NAME".
     */
    private String name;
    /**
     * Table alias.
     */
    private String alias;
    /**
     * "REMARKS".
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
    private List<ColumnMeta> columnList;

    public String getName() {

        return name;
    }

    public void setName(String name) {

        this.name = name;
    }

    public String getAlias() {

        return alias;
    }

    public void setAlias(String alias) {

        this.alias = alias;
    }

    public String getRemarks() {

        return remarks;
    }

    public void setRemarks(String remarks) {

        this.remarks = remarks;
    }

    public String getPrimaryKey() {

        return primaryKey;
    }

    public void setPrimaryKey(String primaryKey) {

        this.primaryKey = primaryKey;
    }

    public List<ColumnMeta> getColumnList() {

        return columnList;
    }

    public void setColumnList(List<ColumnMeta> columnList) {

        this.columnList = columnList;
    }

}
