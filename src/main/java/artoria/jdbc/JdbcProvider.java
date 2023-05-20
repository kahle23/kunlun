package artoria.jdbc;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Deprecated
public interface JdbcProvider {

    boolean transaction(JdbcAtom atom, Integer level) throws SQLException;

    <T> T execute(JdbcCallback<T> callback) throws SQLException;

    int executeUpdate(String sql, Object... params) throws SQLException;

    <T> List<T> executeQuery(String sql, Class<T> clazz, Object... params) throws SQLException;

    List<Map<String, Object>> executeQuery(String sql, Object... params) throws SQLException;

    List<Map<String, Object>> executePureQuery(String sql, Object... params) throws SQLException;

}
