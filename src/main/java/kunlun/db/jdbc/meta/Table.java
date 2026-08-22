/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.db.jdbc.meta;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 数据库表结构信息，包含表名、注释、主键、列与索引.
 * @author Kahle
 */
public class Table implements Serializable {
    /**
     * 表名（JDBC：TABLE_NAME）
     */
    private String name;
    /**
     * 表注释（JDBC：REMARKS）
     */
    private String comment;
    /**
     * 主键列名；联合主键时以英文逗号分隔
     */
    private String primaryKeys;
    /**
     * 表的列信息
     */
    private List<Column> columns;
    /**
     * 表的索引信息
     */
    private List<Index>  indexes;
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

    public String getComment() {

        return comment;
    }

    public void setComment(String comment) {

        this.comment = comment;
    }

    public String getPrimaryKeys() {

        return primaryKeys;
    }

    public void setPrimaryKeys(String primaryKeys) {

        this.primaryKeys = primaryKeys;
    }

    public List<Column> getColumns() {

        return columns;
    }

    public void setColumns(List<Column> columns) {

        this.columns = columns;
    }

    public List<Index> getIndexes() {

        return indexes;
    }

    public void setIndexes(List<Index> indexes) {

        this.indexes = indexes;
    }

    public Map<String, Object> getAttributes() {

        return attributes;
    }

    public void setAttributes(Map<String, Object> attributes) {

        this.attributes = attributes;
    }

}
