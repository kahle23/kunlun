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
public class DbClient {
    private static final int DEFAULT_TRANSACTION_LEVEL = Connection.TRANSACTION_REPEATABLE_READ;
    private static Logger log = Logger.getLogger(DbClient.class.getName());
    private final ThreadLocal<Connection> threadConnection = new ThreadLocal<Connection>();
    private DataSource dataSource;

    public DbClient(DataSource dataSource) {
        this.setDataSource(dataSource);
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        Assert.notNull(dataSource, "Parameter \"dataSource\" must not null. ");
        this.dataSource = dataSource;
    }

    public Connection getConnection() throws SQLException {
        Connection connection = threadConnection.get();
        if (connection == null) {
            connection = dataSource.getConnection();
        }
        return connection;
    }

    public List<TableMeta> getTableMetaList() throws SQLException {
        List<TableMeta> tableMetaList = new ArrayList<TableMeta>();
        ResultSet tableResultSet = null;
        Connection conn = null;
        try {
            conn = this.getConnection();
            DatabaseMetaData databaseMetaData = conn.getMetaData();
            tableResultSet = databaseMetaData.getTables(null, null, null, new String[]{"TABLE"});
            StringBuilder primaryKey = new StringBuilder();
            while (tableResultSet.next()) {
                String tableName = tableResultSet.getString("TABLE_NAME");
                TableMeta tableMeta = new TableMeta();
                tableMetaList.add(tableMeta);
                tableMeta.setName(tableName);
                tableMeta.setRemarks(tableResultSet.getString("REMARKS"));

                primaryKey.setLength(0);
                ResultSet primaryKeysResultSet = null;
                try {
                    primaryKeysResultSet = databaseMetaData.getPrimaryKeys(null, null, tableName);
                    while (primaryKeysResultSet.next()) {
                        primaryKey.append(primaryKeysResultSet.getString("COLUMN_NAME")).append(COMMA);
                    }
                }
                finally {
                    IOUtils.closeQuietly(primaryKeysResultSet);
                }
                if (primaryKey.length() > 0) {
                    primaryKey.deleteCharAt(primaryKey.length() - 1);
                }
                tableMeta.setPrimaryKey(primaryKey.toString());

                Map<String, ColumnMeta> columnMap = new HashMap<String, ColumnMeta>(16);
                ResultSet columnResultSet = null;
                try {
                    columnResultSet = databaseMetaData.getColumns(null, null, tableName, null);
                    tableMeta.setColumnMetaList(new ArrayList<ColumnMeta>());
                    while (columnResultSet.next()) {
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
                        tableMeta.getColumnMetaList().add(columnMeta);
                        columnMap.put(columnName, columnMeta);
                    }
                }
                finally {
                    IOUtils.closeQuietly(columnResultSet);
                }

                Statement statement = null;
                ResultSet resultSet = null;
                try {
                    statement = conn.createStatement();
                    resultSet = statement.executeQuery("select * from " + tableName + " where 1 = 2");
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

            return tableMetaList;
        }
        finally {
            IOUtils.closeQuietly(tableResultSet);
            IOUtils.closeQuietly(conn);
        }
    }

    public <T> T execute(DbCallback<T> callback) throws SQLException {
        Connection conn = null;
        try {
            conn = this.getConnection();
            return callback.call(conn);
        }
        finally {
            IOUtils.closeQuietly(conn);
        }
    }

    public boolean transaction(DbAtom atom) throws SQLException {
        return this.transaction(atom, DEFAULT_TRANSACTION_LEVEL);
    }

    public boolean transaction(DbAtom atom, int transactionLevel) throws SQLException {
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
            throw new JdbcException("Notice the outer transaction that the nested transaction return false");
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
            if (conn != null) {
                try {
                    conn.rollback();
                }
                catch (Exception ex) {
                    log.severe(ExceptionUtils.toString(ex));
                }
            }
            throw new JdbcException(e);
        }
        finally {
            try {
                if (conn != null) {
                    if (autoCommit != null) {
                        conn.setAutoCommit(autoCommit);
                    }
                    conn.close();
                }
            }
            catch (Exception e) {
                log.severe(ExceptionUtils.toString(e));
            }
            finally {
                threadConnection.remove();
            }
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
