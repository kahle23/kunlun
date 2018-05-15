package com.github.kahlkn.artoria.jdbc;

import com.github.kahlkn.artoria.beans.BeanUtils;
import com.github.kahlkn.artoria.exception.ExceptionUtils;
import com.github.kahlkn.artoria.util.Assert;
import com.github.kahlkn.artoria.util.CollectionUtils;
import com.github.kahlkn.artoria.util.StringUtils;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Db tools.
 * @author Kahle
 */
public class DbUtils {
    public static final int DEFAULT_TRANSACTION_LEVEL = Connection.TRANSACTION_REPEATABLE_READ;
    private static Logger log = Logger.getLogger(DbUtils.class.getName());

    public static void close(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            }
            catch (SQLException e) {
                conn = null;
            }
        }
    }

    private final ThreadLocal<Connection> threadConnection = new ThreadLocal<Connection>();
    private DataSource dataSource;

    public DbUtils(DataSource dataSource) {
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

    public <T> T execute(DbCallback<T> callback) throws SQLException {
        Connection conn = null;
        try {
            conn = this.getConnection();
            return callback.call(conn);
        }
        finally {
            DbUtils.close(conn);
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
        Connection conn = null;
        try {
            conn = this.getConnection();
            PreparedStatement prepStat = conn.prepareStatement(sql);
            for (int i = 0; i < params.length; i++) {
                prepStat.setObject(i + 1, params[i]);
            }
            return prepStat.executeUpdate();
        }
        finally {
            DbUtils.close(conn);
        }
    }

    public List<Map<String, Object>> query(String sql, Object... params) throws SQLException {
        Connection conn = null;
        try {
            conn = this.getConnection();
            PreparedStatement prepStat = conn.prepareStatement(sql);
            for (int i = 0; i < params.length; i++) {
                prepStat.setObject(i + 1, params[i]);
            }
            ResultSet resSet = prepStat.executeQuery();
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
            DbUtils.close(conn);
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
