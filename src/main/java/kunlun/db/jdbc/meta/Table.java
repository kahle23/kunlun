/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.db.jdbc.meta;

import java.io.Serializable;
import java.util.List;

/**
 * The table structure information of the database.
 * @author Kahle
 */
public class Table implements Serializable {
    /**
     * The table name.
     */
    private String name;
    /**
     * The comment on the table structure.
     */
    private String comment;
    /**
     * The table primary keys (if is composite primary key, use "," separate).
     */
    private String primaryKeys;
    /**
     * The column information for a table structure.
     */
    private List<Column> columns;

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

}
