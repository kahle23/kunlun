package artoria.jdbc;

import artoria.beans.BeanUtils;
import artoria.exception.ExceptionUtils;
import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import artoria.util.Assert;
import artoria.util.CloseUtils;
import artoria.util.StringUtils;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static artoria.common.Constants.COMMA;

/**
 * Database client.
 * @author Kahle
 */
public class DatabaseClient {
    private static final int DEFAULT_TRANSACTION_LEVEL = Connection.TRANSACTION_REPEATABLE_READ;
    private static Logger log = LoggerFactory.getLogger(DatabaseClient.class);
    private final ThreadLocal<Connection> threadConnection = new ThreadLocal<Connection>();
    private DataSource dataSource;

    public DatabaseClient(DataSource dataSource) {

        setDataSource(dataSource);
    }

    public DataSource getDataSource() {

        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        Assert.notNull(dataSource, "Parameter \"dataSource\" must not null. ");
        this.dataSource = dataSource;
    }

    public List<TableMeta> getTableMetaList() throws SQLException {
        List<TableMeta> tableMetaList = new ArrayList<TableMeta>();
        String[] types = new String[]{"TABLE"};
        ResultSet tableResultSet = null;
        Connection connection = null;
        try {
            connection = getConnection();
            DatabaseMetaData databaseMetaData = connection.getMetaData();
            tableResultSet = databaseMetaData.getTables((String) null, (String) null, (String) null, types);
            while (tableResultSet.next()) {
                Map<String, ColumnMeta> columnMap = new HashMap<String, ColumnMeta>((int) 16);
                String tableName = tableResultSet.getString((String) "TABLE_NAME");
                String remarks = tableResultSet.getString((String) "REMARKS");
                TableMeta tableMeta = new TableMeta();
                tableMetaList.add(tableMeta);
                tableMeta.setName(tableName);
                tableMeta.setRemarks(remarks);
                fillColumnMeta(databaseMetaData, tableMeta, columnMap);
                fillClassName(connection, tableName, columnMap);
            }
            return tableMetaList;
        }
        finally {
            CloseUtils.closeQuietly(tableResultSet);
            closeConnection(connection);
        }
    }

    public boolean transaction(DatabaseAtom atom) throws SQLException {

        return transaction(atom, DEFAULT_TRANSACTION_LEVEL);
    }

    public boolean transaction(DatabaseAtom atom, int transactionLevel) throws SQLException {
        Connection connection = threadConnection.get();
        // Nested transaction support.
        if (connection != null) {
            if (connection.getTransactionIsolation() < transactionLevel) {
                connection.setTransactionIsolation(transactionLevel);
            }
            boolean result = atom.run();
            if (result) {
                return true;
            }
            // Important: can not return false
            throw new DatabaseException("Notice the outer transaction that the nested transaction return false");
        }
        // Normal transaction support.
        Boolean autoCommit = null;
        try {
            connection = getConnection();
            autoCommit = connection.getAutoCommit();
            threadConnection.set(connection);
            connection.setTransactionIsolation(transactionLevel);
            connection.setAutoCommit(false);
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
            rollbackTransaction(connection);
            throw ExceptionUtils.wrap(e, DatabaseException.class);
        }
        finally {
            closeTransaction(connection, autoCommit);
        }
    }

    public <T> List<T> executeQuery(Class<T> clazz, String sql, Object... params) throws SQLException {
        List<Map<String, Object>> list = executeQuery(sql, params);
        return BeanUtils.mapToBeanInList(list, clazz);
    }

    public List<Map<String, Object>> executeQuery(String sql, Object... params) throws SQLException {
        PreparedStatement prepStat = null;
        ResultSet resSet = null;
        Connection conn = null;
        try {
            conn = getConnection();
            prepStat = conn.prepareStatement(sql);
            for (int i = 0; i < params.length; i++) {
                prepStat.setObject(i + 1, params[i]);
            }
            resSet = prepStat.executeQuery();
            ResultSetMetaData metaData = resSet.getMetaData();
            int count = metaData.getColumnCount();
            // Handle keys.
            String[] keys = new String[count];
            for (int i = 0; i < count; i++) {
                String key = metaData.getColumnName(i + 1);
                keys[i] = StringUtils.underlineToCamel(key);
            }
            // Handle result to map.
            List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
            while (resSet.next()) {
                Map<String, Object> data = new HashMap<String, Object>(count);
                for (int i = 0; i < count; i++) {
                    data.put(keys[i], resSet.getObject(i + 1));
                }
                result.add(data);
            }
            return result;
        }
        finally {
            CloseUtils.closeQuietly(resSet);
            CloseUtils.closeQuietly(prepStat);
            closeConnection(conn);
        }
    }

    public int executeUpdate(String sql, Object... params) throws SQLException {
        PreparedStatement prepStat = null;
        Connection connection = null;
        try {
            connection = getConnection();
            prepStat = connection.prepareStatement(sql);
            for (int i = 0; i < params.length; i++) {
                prepStat.setObject(i + 1, params[i]);
            }
            return prepStat.executeUpdate();
        }
        finally {
            CloseUtils.closeQuietly(prepStat);
            closeConnection(connection);
        }
    }

    public <T> T execute(DatabaseCallback<T> callback) throws SQLException {
        Connection connection = null;
        try {
            connection = getConnection();
            return callback.call(connection);
        }
        finally {
            closeConnection(connection);
        }
    }

    private Connection getConnection() throws SQLException {
        Connection connection = threadConnection.get();
        if (connection == null) {
            connection = dataSource.getConnection();
        }
        return connection;
    }

    private void closeConnection(Connection connection) {
        if (threadConnection.get() == null) {
            // Indicates that no transaction was executed.
            CloseUtils.closeQuietly(connection);
        }
        // Ignore close if there is a transaction going on.
    }

    private void rollbackTransaction(Connection connection) {
        if (connection == null) { return; }
        try {
            connection.rollback();
        }
        catch (Exception e) {
            log.error("Execution \"rollbackTransaction\" error. ", e);
        }
    }

    private void closeTransaction(Connection connection, Boolean autoCommit) {
        if (connection == null) { return; }
        try {
            if (autoCommit != null) {
                connection.setAutoCommit(autoCommit);
            }
            connection.close();
        }
        catch (Exception e) {
            log.error("Execution \"closeTransaction\" error. ", e);
        }
        finally {
            threadConnection.remove();
        }
    }

    private String findPrimaryKey(String tableName, DatabaseMetaData dbMetaData) throws SQLException {
        StringBuilder primaryKey = new StringBuilder();
        ResultSet primaryKeysResultSet = null;
        try {
            primaryKeysResultSet = dbMetaData.getPrimaryKeys(null, null, tableName);
            while (primaryKeysResultSet.next()) {
                primaryKey.append(primaryKeysResultSet.getString("COLUMN_NAME")).append(COMMA);
            }
            if (primaryKey.length() > 0) {
                primaryKey.deleteCharAt(primaryKey.length() - 1);
            }
            return primaryKey.toString();
        }
        finally {
            CloseUtils.closeQuietly(primaryKeysResultSet);
        }
    }

    private ColumnMeta createColumnMeta(String primaryKey, ResultSet columnResultSet) throws SQLException {
        ColumnMeta columnMeta = new ColumnMeta();
        String columnName = columnResultSet.getString("COLUMN_NAME");
        columnMeta.setName(columnName);
        columnMeta.setType(columnResultSet.getString("TYPE_NAME"));
        columnMeta.setSize(columnResultSet.getInt("COLUMN_SIZE"));
        columnMeta.setDecimalDigits(columnResultSet.getInt("DECIMAL_DIGITS"));
        columnMeta.setRemarks(columnResultSet.getString("REMARKS"));
        columnMeta.setNullable(columnResultSet.getString("IS_NULLABLE"));
        columnMeta.setAutoincrement(columnResultSet.getString("IS_AUTOINCREMENT"));
        columnMeta.setPrimaryKey(primaryKey.contains(columnName));
        return columnMeta;
    }

    private void fillClassName(Connection connection, String tableName, Map<String, ColumnMeta> columnMap) throws SQLException {
        String sql = "select * from " + tableName + " where 1 = 2";
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);
            ResultSetMetaData metaData = resultSet.getMetaData();
            for (int i = 1; i <= metaData.getColumnCount(); i++) {
                String columnName = metaData.getColumnName(i);
                String columnClassName = metaData.getColumnClassName(i);
                ColumnMeta columnMeta = columnMap.get(columnName);
                if (columnMeta == null) { continue; }
                columnMeta.setClassName(columnClassName);
            }
        }
        finally {
            CloseUtils.closeQuietly(resultSet);
            CloseUtils.closeQuietly(statement);
        }
    }

    private void fillColumnMeta(DatabaseMetaData dbMetaData, TableMeta tableMeta, Map<String, ColumnMeta> columnMap) throws SQLException {
        String tableName = tableMeta.getName();
        ResultSet columnResultSet = null;
        try {
            String primaryKey = findPrimaryKey(tableName, dbMetaData);
            tableMeta.setPrimaryKey(primaryKey);
            columnResultSet = dbMetaData.getColumns(null, null, tableName, null);
            tableMeta.setColumnList(new ArrayList<ColumnMeta>());
            while (columnResultSet.next()) {
                ColumnMeta columnMeta = createColumnMeta(primaryKey, columnResultSet);
                tableMeta.getColumnList().add(columnMeta);
                columnMap.put(columnMeta.getName(), columnMeta);
            }
        }
        finally {
            CloseUtils.closeQuietly(columnResultSet);
        }
    }

}
