package com.apyhs.artoria.jdbc;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Db callback.
 * @param <T> Call return type
 * @author Kahle
 */
public interface DbCallback<T> {

    /**
     * Callback will invoke.
     * @param connection Jdbc connection
     * @return Result you write
     * @throws SQLException Sql run error
     */
    T call(Connection connection) throws SQLException;

}
