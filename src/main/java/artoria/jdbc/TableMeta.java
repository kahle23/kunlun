package artoria.jdbc;

import java.util.HashMap;
import java.util.List;

/**
 * Database table necessary information.
 * @author Kahle
 */
public class TableMeta extends HashMap<String, Object> {

    /**
     * "TABLE_NAME"
     */
    public String getName() {

        return (String) this.get("name");
    }

    public void setName(String name) {

        this.put("name", name);
    }

    /**
     * "REMARKS"
     */
    public String getRemarks() {

        return (String) this.get("remarks");
    }

    public void setRemarks(String remarks) {

        this.put("remarks", remarks);
    }

    /**
     * Table primary keys.
     * If is Composite Primary Key, use "," separate.
     */
    public String getPrimaryKey() {

        return (String) this.get("primaryKey");
    }

    public void setPrimaryKey(String primaryKey) {

        this.put("primaryKey", primaryKey);
    }

    /**
     * Table's column meta list.
     */
    @SuppressWarnings("unchecked")
    public List<ColumnMeta> getColumnList() {

        return (List<ColumnMeta>) this.get("columnList");
    }

    public void setColumnList(List<ColumnMeta> columnList) {

        this.put("columnList", columnList);
    }

}
