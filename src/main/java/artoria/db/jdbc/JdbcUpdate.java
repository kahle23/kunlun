package artoria.db.jdbc;

import java.io.Serializable;

public class JdbcUpdate implements Serializable {
    private String   sql;
    private Object[] arguments;
    private String   configCode;

    public JdbcUpdate(String sql, Object[] arguments, String configCode) {
        this.configCode = configCode;
        this.arguments = arguments;
        this.sql = sql;
    }

    public JdbcUpdate(String sql, Object[] arguments) {
        this.arguments = arguments;
        this.sql = sql;
    }

    public JdbcUpdate(String sql) {

        this.sql = sql;
    }

    public JdbcUpdate() {

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

    public String getConfigCode() {

        return configCode;
    }

    public void setConfigCode(String configCode) {

        this.configCode = configCode;
    }

}
