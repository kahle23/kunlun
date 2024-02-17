/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.db.jdbc;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Jdbc callback.
 * @param <T> Call return type
 * @author Kahle
 */
public abstract class JdbcCallback<T> implements Serializable {
    private final String configCode;

    public JdbcCallback(String configCode) {

        this.configCode = configCode;
    }

    public JdbcCallback() {

        this(null);
    }

    public String getConfigCode() {

        return configCode;
    }

    /**
     * Callback will invoke.
     * @param connection Jdbc connection
     * @return Result you write
     * @throws SQLException Sql run error
     */
    public abstract T call(Connection connection) throws SQLException;

}
