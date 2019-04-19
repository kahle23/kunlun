package artoria.jdbc;

import java.sql.ResultSetMetaData;
import java.util.HashMap;

/**
 * Database table's column necessary information.
 * @author Kahle
 */
public class ColumnMeta extends HashMap<String, Object> {

    /**
     * "COLUMN_NAME"
     */
    public String getName() {

        return (String) this.get("name");
    }

    public void setName(String name) {

        this.put("name", name);
    }

    /**
     * "TYPE_NAME"
     */
    public String getType() {

        return (String) this.get("type");
    }

    public void setType(String type) {

        this.put("type", type);
    }

    /**
     * The fully-qualified name of the Java class.
     * @see ResultSetMetaData#getColumnClassName(int)
     */
    public String getClassName() {

        return (String) this.get("className");
    }

    public void setClassName(String className) {

        this.put("className", className);
    }

    /**
     * "COLUMN_SIZE" maybe is "null"
     */
    public Integer getSize() {

        return (Integer) this.get("size");
    }

    public void setSize(Integer size) {

        this.put("size", size);
    }

    /**
     * "DECIMAL_DIGITS"
     */
    public Integer getDecimalDigits() {

        return (Integer) this.get("decimalDigits");
    }

    public void setDecimalDigits(Integer decimalDigits) {

        this.put("decimalDigits", decimalDigits);
    }

    /**
     * "IS_NULLABLE" is "NO" or "YES"
     */
    public String getNullable() {

        return (String) this.get("nullable");
    }

    public void setNullable(String nullable) {

        this.put("nullable", nullable);
    }

    /**
     * "COLUMN_DEF"
     */
    public String getDefaultValue() {

        return (String) this.get("defaultValue");
    }

    public void setDefaultValue(String defaultValue) {

        this.put("defaultValue", defaultValue);
    }

    /**
     * "IS_AUTOINCREMENT" is "NO" or "YES"
     */
    public String getAutoincrement() {

        return (String) this.get("autoincrement");
    }

    public void setAutoincrement(String autoincrement) {

        this.put("autoincrement", autoincrement);
    }

    /**
     * Is primary key.
     */
    public Boolean getPrimaryKey() {

        return (Boolean) this.get("primaryKey");
    }

    public void setPrimaryKey(Boolean primaryKey) {

        this.put("primaryKey", primaryKey);
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

}
