/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.db.jdbc.meta;

import java.io.Serializable;

/**
 * The information about columns of a table structure in a database.
 * @author Kahle
 */
public class Column implements Serializable {
    /**
     * The column name of the table structure.
     */
    private String name;
    /**
     * The order of the table columns.
     */
    private Integer order;
    /**
     * The column type of the table structure (like "varchar").
     */
    private String type;
    /**
     * The size of the table structure column (like 100 in "varchar(100)").
     */
    private Integer size;
    /**
     * The decimal digits of the table structure column (like 2 in "decimal(11,2)").
     */
    private Integer decimalDigits;
    /**
     * Whether the column can be null (true or false or null, like "NOT NULL").
     */
    private Boolean nullable;
    /**
     * The default value for the column (like "DEFAULT '0'").
     */
    private String defaultValue;
    /**
     * The comment for column of the table structure.
     */
    private String comment;
    /**
     * Whether the column is the primary key (true or false or null, like "PRIMARY KEY (`id`)").
     */
    private Boolean primaryKey;
    /**
     * Whether the column is autoincrement (true or false or null, like "AUTO_INCREMENT").
     */
    private Boolean autoincrement;


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

}
