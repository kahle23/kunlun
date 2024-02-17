/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.db.jdbc;

import kunlun.db.AbstractDbHandler;
import kunlun.exception.ExceptionUtils;
import kunlun.logging.Logger;
import kunlun.logging.LoggerFactory;
import kunlun.util.Assert;
import kunlun.util.CloseUtils;
import kunlun.util.StringUtils;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static kunlun.common.constant.Numbers.ONE;
import static kunlun.common.constant.Numbers.ZERO;

/**
 * The abstract jdbc database handler.
 * @author Kahle
 */
public abstract class AbstractJdbcDbHandler extends AbstractDbHandler implements JdbcDbHandler {
    private static final Logger log = LoggerFactory.getLogger(AbstractJdbcDbHandler.class);

    /**
     * Get the jdbc configuration by config code.
     * @param configCode The config code
     * @return The jdbc configuration
     */
    protected abstract JdbcConfig getJdbcConfig(String configCode);

    protected void fillStatement(PreparedStatement prepStmt, Object[] arguments) throws SQLException {
        if (arguments == null || arguments.length == ZERO) { return; }
        for (int i = ZERO; i < arguments.length; i++) {
            prepStmt.setObject(i + ONE, arguments[i]);
        }
    }

    protected Object getColumnValue(ResultSet resultSet, int columnType, int columnIndex) throws SQLException {
        // Types.CLOB, Types.NCLOB, Types.BLOB
        // columnType < Types.BLOB
        return resultSet.getObject(columnIndex);
    }

    @Override
    public Boolean transaction(JdbcTx jdbcTx) {
        // Verification arguments.
        Assert.notNull(jdbcTx, "Parameter \"jdbcTx\" must not null. ");
        Assert.notNull(jdbcTx.getAtom(), "Parameter \"jdbcTx.atom\" must not null. ");
        Integer  transactionLevel = jdbcTx.getLevel();
        JdbcAtom atom = jdbcTx.getAtom();
        // Set default value.
        if (transactionLevel == null) {
            transactionLevel = Connection.TRANSACTION_REPEATABLE_READ;
        }
        // Get configuration.
        JdbcConfig config = getJdbcConfig(jdbcTx.getConfigCode());
        // Nested transaction support.
        Connection connection = config.getThreadLocalConnection();
        if (connection != null) {
            try {
                if (connection.getTransactionIsolation() < transactionLevel) {
                    connection.setTransactionIsolation(transactionLevel);
                }
                boolean result = atom.run();
                if (result) { return true; }
                // Important: can not return false
                throw new IllegalStateException(
                        "Notice the outer transaction that the nested transaction return false"
                );
            }
            catch (Exception e) {
                throw ExceptionUtils.wrap(e);
            }
        }
        // Normal transaction support.
        Boolean autoCommit = null;
        try {
            // Get and set connection.
            connection = config.getConnection();
            config.setThreadLocalConnection(connection);
            // Set transactionLevel and autoCommit.
            autoCommit = connection.getAutoCommit();
            connection.setTransactionIsolation(transactionLevel);
            connection.setAutoCommit(false);
            // Run atom.
            boolean result = atom.run();
            if (result) {
                connection.commit();
            }
            else {
                connection.rollback();
            }
            return result;
        }
        catch (Exception e) {
            transactionCatch(config, connection);
            throw ExceptionUtils.wrap(e);
        }
        finally { transactionFinally(config, connection, autoCommit); }
    }

    private void transactionCatch(JdbcConfig config, Connection connection) {
        if (connection == null) { return; }
        try {
            connection.rollback();
        }
        catch (Exception e) {
            log.error("Execution \"transactionCatch\" error. ", e);
        }
    }

    private void transactionFinally(JdbcConfig config, Connection connection, Boolean autoCommit) {
        if (connection == null) { return; }
        try {
            if (autoCommit != null) {
                connection.setAutoCommit(autoCommit);
            }
            connection.close();
        }
        catch (Exception e) {
            // Can not throw exception here.
            log.error("Execution \"transactionFinally\" error. ", e);
        }
        finally {
            config.removeThreadLocalConnection();
        }
    }

    @Override
    public <T> T callback(JdbcCallback<T> jdbcCallback) {
        // Verification arguments.
        Assert.notNull(jdbcCallback, "Parameter \"jdbcCallback\" must not null. ");
        // Get configuration.
        JdbcConfig config = getJdbcConfig(jdbcCallback.getConfigCode());
        // Do callback.
        Connection connection = null;
        try {
            connection = config.getConnection();
            return jdbcCallback.call(connection);
        }
        catch (SQLException e) {
            throw ExceptionUtils.wrap(e);
        }
        finally {
            config.closeConnection(connection);
        }
    }

    @Override
    public Integer executeUpdate(JdbcUpdate jdbcUpdate) {
        // Verification arguments.
        Assert.notNull(jdbcUpdate, "Parameter \"jdbcUpdate\" must not null. ");
        Assert.notBlank(jdbcUpdate.getSql(), "Parameter \"jdbcUpdate.sql\" must not blank. ");
        // Get configuration.
        JdbcConfig config = getJdbcConfig(jdbcUpdate.getConfigCode());
        // Execute update.
        PreparedStatement prepStmt = null;
        Connection connection = null;
        try {
            connection = config.getConnection();
            prepStmt = connection.prepareStatement(jdbcUpdate.getSql());
            fillStatement(prepStmt, jdbcUpdate.getArguments());
            return prepStmt.executeUpdate();
        }
        catch (SQLException e) {
            throw ExceptionUtils.wrap(e);
        }
        finally {
            CloseUtils.closeQuietly(prepStmt);
            config.closeConnection(connection);
        }
    }

    @Override
    public List<Map<String, Object>> executeQuery(JdbcQuery jdbcQuery) {
        // Verification arguments.
        Assert.notNull(jdbcQuery, "Parameter \"jdbcQuery\" must not null. ");
        Assert.notBlank(jdbcQuery.getSql(), "Parameter \"jdbcQuery.sql\" must not blank. ");
        String   sql = jdbcQuery.getSql();
        Object[] arguments = jdbcQuery.getArguments();
        Boolean  toCamel = jdbcQuery.getToCamel();
        // Set default value.
        toCamel = toCamel != null ? toCamel : true;
        // Get configuration.
        JdbcConfig config = getJdbcConfig(jdbcQuery.getConfigCode());
        // Execute query.
        PreparedStatement prepStmt = null;
        Connection connection = null;
        ResultSet resSet = null;
        try {
            // Execute SQL.
            connection = config.getConnection();
            prepStmt = connection.prepareStatement(sql);
            fillStatement(prepStmt, arguments);
            resSet = prepStmt.executeQuery();
            // Handle column labels and column types.
            ResultSetMetaData resMetaData = resSet.getMetaData();
            int columnCount = resMetaData.getColumnCount();
            int[] columnTypes = new int[columnCount];
            String[] columnLabels = new String[columnCount];
            for (int i = ZERO; i < columnCount; i++) {
                //columnLabels[i] = resMetaData.getColumnName(i + ONE)
                columnLabels[i] = resMetaData.getColumnLabel(i + ONE);
                if (toCamel) {
                    columnLabels[i] = StringUtils.underlineToCamel(columnLabels[i]);
                }
                columnTypes[i] = resMetaData.getColumnType(i + ONE);
            }
            // Build result.
            List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
            while (resSet.next()) {
                Map<String, Object> data = new LinkedHashMap<String, Object>(columnCount);
                for (int i = ZERO; i < columnCount; i++) {
                    int colType = columnTypes[i], index = i + ONE;
                    Object value = getColumnValue(resSet, colType, index);
                    data.put(columnLabels[i], value);
                }
                result.add(data);
            }
            return result;
        }
        catch (SQLException e) {
            throw ExceptionUtils.wrap(e);
        }
        finally {
            CloseUtils.closeQuietly(resSet);
            CloseUtils.closeQuietly(prepStmt);
            config.closeConnection(connection);
        }
    }

    /**
     * The jdbc configuration.
     * @author Kahle
     */
    public interface JdbcConfig {

        DataSource getDataSource();

        Connection getConnection() throws SQLException;

        void closeConnection(Connection connection);

        Connection getThreadLocalConnection();

        void setThreadLocalConnection(Connection connection);

        void removeThreadLocalConnection();

    }

    /**
     * The abstract jdbc configuration.
     * @author Kahle
     */
    public static abstract class AbstractConfig implements JdbcConfig {

        @Override
        public Connection getConnection() throws SQLException {
            Connection connection = getThreadLocalConnection();
            if (connection == null) {
                connection = getDataSource().getConnection();
            }
            return connection;
        }

        @Override
        public void closeConnection(Connection connection) {
            if (getThreadLocalConnection() == null) {
                // Indicates that no transaction was executed.
                CloseUtils.closeQuietly(connection);
            }
            // Ignore close if there is a transaction going on.
        }

    }

}
