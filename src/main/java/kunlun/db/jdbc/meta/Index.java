/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.db.jdbc.meta;

import java.util.Map;

/**
 * 数据库表结构中的索引元数据，数据来源于 JDBC 的 DatabaseMetaData#getIndexInfo 结果集.
 * @author Kahle
 */
public class Index {
    /**
     * 索引名（JDBC：INDEX_NAME）
     */
    private String name;
    /**
     * 索引类型（JDBC：TYPE）
     */
    private String type;
    /**
     * 是否为非唯一索引，即允许重复值；为 true 表示非唯一（JDBC：NON_UNIQUE）
     */
    private Boolean nonUnique;
    /**
     * 索引所属的表名（JDBC：TABLE_NAME）
     */
    private String tableName;
    /**
     * 被索引的列名（JDBC：COLUMN_NAME）
     */
    private String columnName;
    /**
     * 列在该索引中的序号位置（JDBC：ORDINAL_POSITION）
     */
    private String ordinalPosition;
    /**
     * 其他由驱动扩展、未在上述字段中体现的属性
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
