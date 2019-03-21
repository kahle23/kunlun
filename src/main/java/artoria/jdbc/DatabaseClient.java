package artoria.jdbc;

import artoria.beans.BeanUtils;
import artoria.exception.ExceptionUtils;
import artoria.io.IOUtils;
import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import artoria.util.Assert;
import artoria.util.CollectionUtils;
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

        this.setDataSource(dataSource);
    }

    public DataSource getDataSource() {

        return this.dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        Assert.notNull(dataSource, "Parameter \"dataSource\" must not null. ");
        this.dataSource = dataSource;
    }

    public List<TableMeta> getTableMetaList() throws SQLException {
        List<TableMeta> tableMetaList = new ArrayList<TableMeta>();
        String[] types = new String[]{"TABLE"};
        ResultSet tableResultSet = null;
        Connection conn = null;
        try {
            conn = this.getConnection();
            DatabaseMetaData databaseMetaData = conn.getMetaData();
            tableResultSet = databaseMetaData.getTables((String) null, (String) null, (String) null, types);
            while (tableResultSet.next()) {
                Map<String, ColumnMeta> columnMap = new HashMap<String, ColumnMeta>((int) 16);
                String tableName = tableResultSet.getString((String) "TABLE_NAME");
                String remarks = tableResultSet.getString((String) "REMARKS");
                TableMeta tableMeta = new TableMeta();
                tableMetaList.add(tableMeta);
                tableMeta.setName(tableName);
                tableMeta.setRemarks(remarks);
                this.handleColumnMeta(databaseMetaData, tableMeta, columnMap);
                this.handleClassName(conn, columnMap, tableName);
            }
            return tableMetaList;
        }
        finally {
            IOUtils.closeQuietly(tableResultSet);
            this.closeConnection(conn);
        }
    }

    public <T> T execute(DatabaseCallback<T> callback) throws SQLException {
        Connection conn = null;
        try {
            conn = this.getConnection();
            return callback.call(conn);
        }
        finally {
            this.closeConnection(conn);
        }
    }

    public boolean transaction(DatabaseAtom atom) throws SQLException {

        return this.transaction(atom, DEFAULT_TRANSACTION_LEVEL);
    }

    public boolean transaction(DatabaseAtom atom, int transactionLevel) throws SQLException {
        Connection conn = threadConnection.get();
        // Nested transaction support.
        if (conn != null) {
            if (conn.getTransactionIsolation() < transactionLevel) {
                conn.setTransactionIsolation(transactionLevel);
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
            conn = this.getConnection();
            autoCommit = conn.getAutoCommit();
            threadConnection.set(conn);
            conn.setTransactionIsolation(transactionLevel);
            conn.setAutoCommit(false);
            boolean result = atom.run();
            if (result) {
                conn.commit();
            }
            else {
                conn.rollback();
            }
            return result;
        }
        catch (Exception e) {
            this.rollbackTransaction(conn);
            throw ExceptionUtils.wrap(e, DatabaseException.class);
        }
        finally {
            this.closeTransaction(conn, autoCommit);
        }
    }

    public int update(String sql, Object... params) throws SQLException {
        PreparedStatement prepStat = null;
        Connection conn = null;
        try {
            conn = this.getConnection();
            prepStat = conn.prepareStatement(sql);
            for (int i = 0; i < params.length; i++) {
                prepStat.setObject(i + 1, params[i]);
            }
            return prepStat.executeUpdate();
        }
        finally {
            IOUtils.closeQuietly(prepStat);
            this.closeConnection(conn);
        }
    }

    public List<Map<String, Object>> query(String sql, Object... params) throws SQLException {
        PreparedStatement prepStat = null;
        ResultSet resSet = null;
        Connection conn = null;
        try {
            conn = this.getConnection();
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
            IOUtils.closeQuietly(resSet);
            IOUtils.closeQuietly(prepStat);
            this.closeConnection(conn);
        }
    }

    public <T> List<T> query(Class<T> clazz, String sql, Object... params) throws SQLException {
        List<Map<String, Object>> list = this.query(sql, params);
        return BeanUtils.mapToBeanInList(list, clazz);
    }

    public Map<String, Object> queryFirst(String sql, Object... params) throws SQLException {
        List<Map<String, Object>> list = this.query(sql, params);
        return CollectionUtils.isNotEmpty(list) ? list.get(0) : null;
    }

    public <T> T queryFirst(Class<T> clazz, String sql, Object... params) throws SQLException {
        List<Map<String, Object>> list = this.query(sql, params);
        return BeanUtils.mapToBean(CollectionUtils.isNotEmpty(list) ? list.get(0) : null, clazz);
    }

    private Connection getConnection() throws SQLException {
        Connection connection = threadConnection.get();
        if (connection == null) {
            connection = this.dataSource.getConnection();
        }
        return connection;
    }

    private void closeConnection(Connection connection) {
        if (threadConnection.get() == null) {
            // Indicates that no transaction was executed.
            IOUtils.closeQuietly(connection);
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

    private ColumnMeta createColumnMeta(ResultSet columnResultSet, String primaryKey) throws SQLException {
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

    private String readPrimaryKey(DatabaseMetaData databaseMetaData, String tableName) throws SQLException {
        StringBuilder primaryKey = new StringBuilder();
        ResultSet primaryKeysResultSet = null;
        try {
            primaryKeysResultSet = databaseMetaData.getPrimaryKeys(null, null, tableName);
            while (primaryKeysResultSet.next()) {
                primaryKey.append(primaryKeysResultSet.getString("COLUMN_NAME")).append(COMMA);
            }
            if (primaryKey.length() > 0) {
                primaryKey.deleteCharAt(primaryKey.length() - 1);
            }
            return primaryKey.toString();
        }
        finally {
            IOUtils.closeQuietly(primaryKeysResultSet);
        }
    }

    private void handleClassName(Connection connection, Map<String, ColumnMeta> columnMap, String tableName) throws SQLException {
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
            IOUtils.closeQuietly(resultSet);
            IOUtils.closeQuietly(statement);
        }
    }

    private void handleColumnMeta(DatabaseMetaData dbMetaData, TableMeta tableMeta, Map<String, ColumnMeta> columnMap) throws SQLException {
        String tableName = tableMeta.getName();
        ResultSet columnResultSet = null;
        try {
            String primaryKey = this.readPrimaryKey(dbMetaData, tableName);
            tableMeta.setPrimaryKey(primaryKey);
            columnResultSet = dbMetaData.getColumns(null, null, tableName, null);
            tableMeta.setColumnMetaList(new ArrayList<ColumnMeta>());
            while (columnResultSet.next()) {
                ColumnMeta columnMeta = this.createColumnMeta(columnResultSet, primaryKey);
                tableMeta.getColumnMetaList().add(columnMeta);
                columnMap.put(columnMeta.getName(), columnMeta);
            }
        }
        finally {
            IOUtils.closeQuietly(columnResultSet);
        }
    }

}
