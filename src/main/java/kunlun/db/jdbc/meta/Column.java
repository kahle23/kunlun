/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.db.jdbc.meta;

import java.io.Serializable;
import java.util.Map;

/**
 * 数据库表结构中的列元数据，数据来源于 JDBC 的 DatabaseMetaData#getColumns 结果集.
 * @author Kahle
 */
public class Column implements Serializable {
    /**
     * 列名（JDBC：COLUMN_NAME）
     */
    private String name;
    /**
     * 列在表中的序号位置
     */
    private Integer order;
    /**
     * 列的数据库类型名，如 VARCHAR（JDBC：TYPE_NAME）
     */
    private String type;
    /**
     * 列长度，如 VARCHAR(100) 中的 100（JDBC：COLUMN_SIZE）
     */
    private Integer size;
    /**
     * 小数位数，如 DECIMAL(11,2) 中的 2（JDBC：DECIMAL_DIGITS）
     */
    private Integer decimalDigits;
    /**
     * 是否允许为 NULL；未知时为 null（JDBC：IS_NULLABLE）
     */
    private Boolean nullable;
    /**
     * 列的默认值表达式（JDBC：COLUMN_DEF）
     */
    private String defaultValue;
    /**
     * 列注释（JDBC：REMARKS）
     */
    private String comment;
    /**
     * 是否为主键列
     */
    private Boolean primaryKey;
    /**
     * 是否自增（JDBC：IS_AUTOINCREMENT）
     */
    private Boolean autoincrement;
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

    public Integer getOrder() {

        return order;
    }

    public void setOrder(Integer order) {

        this.order = order;
    }

    public String getType() {

        return type;
    }

    public void setType(String type) {

        this.type = type;
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

    public Boolean getNullable() {

        return nullable;
    }

    public void setNullable(Boolean nullable) {

        this.nullable = nullable;
    }

    public String getDefaultValue() {

        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {

        this.defaultValue = defaultValue;
    }

    public String getComment() {

        return comment;
    }

    public void setComment(String comment) {

        this.comment = comment;
    }

    public Boolean getPrimaryKey() {

        return primaryKey;
    }

    public void setPrimaryKey(Boolean primaryKey) {

        this.primaryKey = primaryKey;
    }

    public Boolean getAutoincrement() {

        return autoincrement;
    }

    public void setAutoincrement(Boolean autoincrement) {

        this.autoincrement = autoincrement;
    }

    public Map<String, Object> getAttributes() {

        return attributes;
    }

    public void setAttributes(Map<String, Object> attributes) {

        this.attributes = attributes;
    }

}
