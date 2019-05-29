package artoria.jdbc;

import java.io.Serializable;
import java.sql.ResultSetMetaData;

/**
 * Database table's column necessary information.
 * @author Kahle
 */
public class ColumnMeta implements Serializable {
    /**
     * "COLUMN_NAME".
     */
    private String name;
    /**
     * "TYPE_NAME".
     */
    private String type;
    /**
     * The fully-qualified name of the Java class.
     * @see ResultSetMetaData#getColumnClassName(int)
     */
    private String className;
    /**
     * "COLUMN_SIZE" maybe is "null".
     */
    private Integer size;
    /**
     * "DECIMAL_DIGITS".
     */
    private Integer decimalDigits;
    /**
     * "IS_NULLABLE" is "NO" or "YES".
     */
    private String nullable;
    /**
     * "COLUMN_DEF".
     */
    private String defaultValue;
    /**
     * "IS_AUTOINCREMENT" is "NO" or "YES".
     */
    private String autoincrement;
    /**
     * Is primary key.
     */
    private Boolean primaryKey;
    /**
     * "REMARKS".
     */
    private String remarks;

    public String getName() {

        return name;
    }

    public void setName(String name) {

        this.name = name;
    }

    public String getType() {

        return type;
    }

    public void setType(String type) {

        this.type = type;
    }

    public String getClassName() {

        return className;
    }

    public void setClassName(String className) {

        this.className = className;
    }

    public Integer getSize() {

        return size;
    }

    public void setSize(Integer size) {

        this.size = size;
    }

    public Integer getDecimalDigits() {

        return decimalDigits;
    }

    public void setDecimalDigits(Integer decimalDigits) {

        this.decimalDigits = decimalDigits;
    }

    public String getNullable() {

        return nullable;
    }

    public void setNullable(String nullable) {

        this.nullable = nullable;
    }

    public String getDefaultValue() {

        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {

        this.defaultValue = defaultValue;
    }

    public String getAutoincrement() {

        return autoincrement;
    }

    public void setAutoincrement(String autoincrement) {

        this.autoincrement = autoincrement;
    }

    public Boolean getPrimaryKey() {

        return primaryKey;
    }

    public void setPrimaryKey(Boolean primaryKey) {

        this.primaryKey = primaryKey;
    }

    public String getRemarks() {

        return remarks;
    }

    public void setRemarks(String remarks) {

        this.remarks = remarks;
    }

}
