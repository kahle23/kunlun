package artoria.jdbc;

import artoria.beans.BeanUtils;
import artoria.exception.ExceptionUtils;
import artoria.io.IOUtils;
import artoria.util.Assert;
import artoria.util.CollectionUtils;
import artoria.util.StringUtils;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import static artoria.common.Constants.COMMA;

/**
 * Database client.
 * @author Kahle
 */
public class DatabaseClient {
    private static final int DEFAULT_TRANSACTION_LEVEL = Connection.TRANSACTION_REPEATABLE_READ;
    private static Logger log = Logger.getLogger(DatabaseClient.class.getName());
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

    public Connection getConnection() throws SQLException {
        Connection connection = this.threadConnection.get();
        if (connection == null) {
            connection = this.dataSource.getConnection();
        }
        return connection;
    }

    private ColumnMeta createColumnMeta(ResultSet columnResultSet
            , StringBuilder primaryKey) throws SQLException {
        ColumnMeta columnMeta = new ColumnMeta();
        String columnName = columnResultSet.getString("COLUMN_NAME");
        columnMeta.setName(columnName);
        columnMeta.setType(columnResultSet.getString("TYPE_NAME"));
        columnMeta.setSize(columnResultSet.getInt("COLUMN_SIZE"));
        columnMeta.setDecimalDigits(columnResultSet.getInt("DECIMAL_DIGITS"));
        columnMeta.setRemarks(columnResultSet.getString("REMARKS"));
        columnMeta.setNullable(columnResultSet.getString("IS_NULLABLE"));
        columnMeta.setAutoincrement(columnResultSet.getString("IS_AUTOINCREMENT"));
        columnMeta.setPrimaryKey(primaryKey.indexOf(columnName) >= 0);
        return columnMeta;
    }

    private void handleClassName(Map<String, ColumnMeta> columnMap
            , Connection conn, String tableName) throws SQLException {
        String sql = "select * from " + tableName + " where 1 = 2";
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            statement = conn.createStatement();
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

    private void fillPrimaryKey(StringBuilder primaryKey
            , DatabaseMetaData databaseMetaData, String tableName) throws SQLException {
        ResultSet primaryKeysResultSet = null;
        try {
            primaryKeysResultSet = databaseMetaData.getPrimaryKeys(null, null, tableName);
            while (primaryKeysResultSet.next()) {
                primaryKey.append(primaryKeysResultSet.getString("COLUMN_NAME")).append(COMMA);
            }
            if (primaryKey.length() > 0) {
                primaryKey.deleteCharAt(primaryKey.length() - 1);
            }
        }
        finally {
            IOUtils.closeQuietly(primaryKeysResultSet);
        }
    }

    private void handleColumnMeta(TableMeta tableMeta, Map<String, ColumnMeta> columnMap
            , DatabaseMetaData dbMetaData, String tableName, StringBuilder primaryKey) throws SQLException {
        ResultSet columnResultSet = null;
        try {
            columnResultSet = dbMetaData.getColumns(null, null, tableName, null);
            tableMeta.setColumnMetaList(new ArrayList<ColumnMeta>());
            while (columnResultSet.next()) {
                ColumnMeta columnMeta = this.createColumnMeta(columnResultSet, primaryKey);
                tableMeta.getColumnMetaList().add(columnMeta);
                columnMap.put(columnResultSet.getString("COLUMN_NAME"), columnMeta);
            }
        }
        finally {
            IOUtils.closeQuietly(columnResultSet);
        }
    }

    public List<TableMeta> getTableMetaList() throws SQLException {
        List<TableMeta> tableMetaList = new ArrayList<TableMeta>();
        StringBuilder primaryKey = new StringBuilder();
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
                primaryKey.setLength(0);
                this.fillPrimaryKey(primaryKey, databaseMetaData, tableName);
                tableMeta.setPrimaryKey(primaryKey.toString());
                this.handleColumnMeta(tableMeta, columnMap, databaseMetaData, tableName, primaryKey);
                this.handleClassName(columnMap, conn, tableName);
            }
            return tableMetaList;
        }
        finally {
            IOUtils.closeQuietly(tableResultSet);
            IOUtils.closeQuietly(conn);
        }
    }

    public <T> T execute(DatabaseCallback<T> callback) throws SQLException {
        Connection conn = null;
        try {
            conn = this.getConnection();
            return callback.call(conn);
        }
        finally {
            IOUtils.closeQuietly(conn);
        }
    }

    public boolean transaction(DatabaseAtom atom) throws SQLException {

        return this.transaction(atom, DEFAULT_TRANSACTION_LEVEL);
    }

    private void rollback(Connection conn) {
        if (conn == null) {
            return;
        }
        try {
            conn.rollback();
        }
        catch (Exception ex) {
            log.severe(ExceptionUtils.toString(ex));
        }
    }

    private void closing(Connection conn, Boolean autoCommit) {
        try {
            if (conn == null) {
                return;
            }
            if (autoCommit != null) {
                conn.setAutoCommit(autoCommit);
            }
            conn.close();
        }
        catch (Exception e) {
            log.severe(ExceptionUtils.toString(e));
        }
        finally {
            this.threadConnection.remove();
        }
    }

    public boolean transaction(DatabaseAtom atom, int transactionLevel) throws SQLException {
        Connection conn = this.threadConnection.get();
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
            this.threadConnection.set(conn);
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
            this.rollback(conn);
            throw ExceptionUtils.wrap(e, DatabaseException.class);
        }
        finally {
            this.closing(conn, autoCommit);
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
            IOUtils.closeQuietly(conn);
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
            IOUtils.closeQuietly(conn);
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

}
