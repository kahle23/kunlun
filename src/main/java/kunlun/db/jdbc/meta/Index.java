/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.db.jdbc.meta;

import java.util.Map;

/**
 * The information about indexes of a table structure in a database.
 * @author Kahle
 */
public class Index {
    /**
     * The index name of the table (INDEX_NAME).
     */
    private String name;
    /**
     * The index type of the table (TYPE).
     */
    private String type;
    /**
     * Whether this is a unique index (NON_UNIQUE).
     */
    private Boolean nonUnique;
    /**
     * The table name (TABLE_NAME).
     */
    private String tableName;
    /**
     * The column name of the index (COLUMN_NAME).
     */
    private String columnName;
    /**
     * The ordinal position of column name (ORDINAL_POSITION).
     */
    private String ordinalPosition;
    /**
     * The other attributes.
     */
    private Map<String, Object> attributes;


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

    public Boolean getNonUnique() {

        return nonUnique;
    }

    public void setNonUnique(Boolean nonUnique) {

        this.nonUnique = nonUnique;
    }

    public String getTableName() {

        return tableName;
    }

    public void setTableName(String tableName) {

        this.tableName = tableName;
    }

    public String getColumnName() {

        return columnName;
    }

    public void setColumnName(String columnName) {

        this.columnName = columnName;
    }

    public String getOrdinalPosition() {

        return ordinalPosition;
    }

    public void setOrdinalPosition(String ordinalPosition) {

        this.ordinalPosition = ordinalPosition;
    }

    public Map<String, Object> getAttributes() {

        return attributes;
    }

    public void setAttributes(Map<String, Object> attributes) {

        this.attributes = attributes;
    }

}
