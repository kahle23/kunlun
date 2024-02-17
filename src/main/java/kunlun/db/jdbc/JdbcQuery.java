/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.db.jdbc;

import java.io.Serializable;

public class JdbcQuery implements Serializable {
    private String   sql;
    private Object[] arguments;
    private Boolean  toCamel;
    private String   configCode;

    public JdbcQuery(String sql, Object[] arguments, String configCode) {
        this.configCode = configCode;
        this.arguments = arguments;
        this.sql = sql;
    }

    public JdbcQuery(String sql, Object[] arguments) {
        this.arguments = arguments;
        this.sql = sql;
    }

    public JdbcQuery(String sql) {

        this.sql = sql;
    }

    public JdbcQuery() {

    }

    public String getSql() {

        return sql;
    }

    public void setSql(String sql) {

        this.sql = sql;
    }

    public Object[] getArguments() {

        return arguments;
    }

    public void setArguments(Object[] arguments) {

        this.arguments = arguments;
    }

    public Boolean getToCamel() {

        return toCamel;
    }

    public void setToCamel(Boolean toCamel) {

        this.toCamel = toCamel;
    }

    public String getConfigCode() {

        return configCode;
    }

    public void setConfigCode(String configCode) {

        this.configCode = configCode;
    }

}
