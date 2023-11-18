package artoria.db.jdbc;

import artoria.db.DbHandler;

import java.util.List;
import java.util.Map;

/**
 * The jdbc database handler.
 * @author Kahle
 */
public interface JdbcDbHandler extends DbHandler {

    /**
     * The operation execute update.
     */
    String EXECUTE_UPDATE = "executeUpdate";

    /**
     * The operation execute query.
     */
    String EXECUTE_QUERY = "executeQuery";

    /**
     * The operation transaction.
     */
    String TRANSACTION = "transaction";

    /**
     * The operation callback.
     */
    String CALLBACK = "callback";

    /**
     * transaction
     * @param jdbcTx
     * @return
     */
    Boolean transaction(JdbcTx jdbcTx);

    /**
     * callback
     * @param jdbcCallback
     * @param <T>
     * @return
     */
    <T>  T  callback(JdbcCallback<T> jdbcCallback);

    /**
     * executeUpdate
     * @param jdbcUpdate
     * @return
     */
    Integer executeUpdate(JdbcUpdate jdbcUpdate);

    /**
     * executeQuery
     * @param jdbcQuery
     * @return
     */
    List<Map<String, Object>> executeQuery(JdbcQuery jdbcQuery);

}
