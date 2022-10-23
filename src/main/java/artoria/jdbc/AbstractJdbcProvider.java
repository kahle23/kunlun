package artoria.jdbc;

import artoria.data.bean.BeanUtils;
import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import artoria.util.Assert;
import artoria.util.CloseUtils;
import artoria.util.StringUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static artoria.common.Constants.ONE;
import static artoria.common.Constants.ZERO;

public abstract class AbstractJdbcProvider implements JdbcProvider {
    private static Logger log = LoggerFactory.getLogger(AbstractJdbcProvider.class);

    Connection getConnection() throws SQLException {

        throw new UnsupportedOperationException();
    }

    void closeConnection(Connection connection) throws SQLException {

        throw new UnsupportedOperationException();
    }

    void fillStatement(PreparedStatement prepStmt, Object... params) throws SQLException {
        for (int i = ZERO; i < params.length; i++) {
            prepStmt.setObject(i + ONE, params[i]);
        }
    }

    Object getColumnValue(ResultSet resultSet, int columnType, int columnIndex) throws SQLException {
        Object value;
        if (columnType < Types.BLOB) {
            value = resultSet.getObject(columnIndex);
        }
        else {
            /*if (columnType == Types.CLOB) {
                value = handleClob(resultSet.getClob(columnIndex));
            }
            else if (columnType == Types.NCLOB) {
                value = handleClob(resultSet.getNClob(columnIndex));
            }
            else if (columnType == Types.BLOB) {
                value = handleBlob(resultSet.getBlob(columnIndex));
            }
            else {
                value = resultSet.getObject(columnIndex);
            }*/
            value = resultSet.getObject(columnIndex);
        }
        return value;
    }

    @Override
    public <T> T execute(JdbcCallback<T> callback) throws SQLException {
        Assert.notNull(callback, "Parameter \"callback\" must not null. ");
        Connection connection = null;
        try {
            connection = getConnection();
            return callback.call(connection);
        }
        finally {
            closeConnection(connection);
        }
    }

    @Override
    public int executeUpdate(String sql, Object... params) throws SQLException {
        Assert.notBlank(sql, "Parameter \"sql\" must not blank. ");
        Connection connection = null;
        PreparedStatement prepStmt = null;
        try {
            connection = getConnection();
            prepStmt = connection.prepareStatement(sql);
            fillStatement(prepStmt, params);
            return prepStmt.executeUpdate();
        }
        finally {
            CloseUtils.closeQuietly(prepStmt);
            closeConnection(connection);
        }
    }

    @Override
    public <T> List<T> executeQuery(String sql, Class<T> clazz, Object... params) throws SQLException {
        Assert.notNull(clazz, "Parameter \"clazz\" must not null. ");
        return BeanUtils.beanToBeanInList(executeQuery(sql, params), clazz);
    }

    @Override
    public List<Map<String, Object>> executeQuery(String sql, Object... params) throws SQLException {

        return executeRealQuery(sql, Boolean.TRUE, params);
    }

    @Override
    public List<Map<String, Object>> executePureQuery(String sql, Object... params) throws SQLException {

        return executeRealQuery(sql, Boolean.FALSE, params);
    }

    private List<Map<String, Object>> executeRealQuery(String sql, Boolean toCamel, Object... params) throws SQLException {
        Assert.notBlank(sql, "Parameter \"sql\" must not blank. ");
        Connection connection = null;
        PreparedStatement prepStmt = null;
        ResultSet resSet = null;
        try {
            connection = getConnection();
            prepStmt = connection.prepareStatement(sql);
            fillStatement(prepStmt, params);
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
                Map<String, Object> data = new HashMap<String, Object>(columnCount);
                for (int i = ZERO; i < columnCount; i++) {
                    int type = columnTypes[i], index = i + ONE;
                    Object value = getColumnValue(resSet, type, index);
                    data.put(columnLabels[i], value);
                }
                result.add(data);
            }
            return result;
        }
        finally {
            CloseUtils.closeQuietly(resSet);
            CloseUtils.closeQuietly(prepStmt);
            closeConnection(connection);
        }
    }

}
