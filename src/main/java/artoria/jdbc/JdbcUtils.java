package artoria.jdbc;

import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import artoria.util.Assert;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class JdbcUtils {
    private static Logger log = LoggerFactory.getLogger(JdbcUtils.class);
    private static JdbcProvider jdbcProvider;

    public static JdbcProvider getJdbcProvider() {
        if (jdbcProvider == null) {
            throw new JdbcException("Please set the jdbc provider first. ");
        }
        return jdbcProvider;
    }

    public static void setJdbcProvider(JdbcProvider jdbcProvider) {
        Assert.notNull(jdbcProvider, "Parameter \"jdbcProvider\" must not null. ");
        log.info("Set jdbc provider: {}", jdbcProvider.getClass().getName());
        JdbcUtils.jdbcProvider = jdbcProvider;
    }

    public static boolean transaction(JdbcAtom atom) {
        try {
            return getJdbcProvider().transaction(atom, null);
        }
        catch (SQLException e) {
            throw new JdbcException(e);
        }
    }

    public static boolean transaction(JdbcAtom atom, Integer level) {
        try {
            return getJdbcProvider().transaction(atom, level);
        }
        catch (SQLException e) {
            throw new JdbcException(e);
        }
    }

    public static <T> T execute(JdbcCallback<T> callback) {
        try {
            return getJdbcProvider().execute(callback);
        }
        catch (SQLException e) {
            throw new JdbcException(e);
        }
    }

    public static int executeUpdate(String sql, Object... params) {
        try {
            return getJdbcProvider().executeUpdate(sql, params);
        }
        catch (SQLException e) {
            throw new JdbcException(e);
        }
    }

    public static <T> List<T> executeQuery(String sql, Class<T> clazz, Object... params) {
        try {
            return getJdbcProvider().executeQuery(sql, clazz, params);
        }
        catch (SQLException e) {
            throw new JdbcException(e);
        }
    }

    public static List<Map<String, Object>> executeQuery(String sql, Object... params) {
        try {
            return getJdbcProvider().executeQuery(sql, params);
        }
        catch (SQLException e) {
            throw new JdbcException(e);
        }
    }

    public static List<Map<String, Object>> executePureQuery(String sql, Object... params) {
        try {
            return getJdbcProvider().executePureQuery(sql, params);
        }
        catch (SQLException e) {
            throw new JdbcException(e);
        }
    }

}
