/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.db.jdbc;

import kunlun.db.DbHandler;

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
     * @param jdbcTx jdbcTx
     * @return return
     */
    Boolean transaction(JdbcTx jdbcTx);

    /**
     * callback
     * @param jdbcCallback jdbcCallback
     * @return return
     */
    <T>  T  callback(JdbcCallback<T> jdbcCallback);

    /**
     * executeUpdate
     * @param jdbcUpdate jdbcUpdate
     * @return return
     */
    Integer executeUpdate(JdbcUpdate jdbcUpdate);

    /**
     * executeQuery
     * @param jdbcQuery jdbcQuery
     * @return return
     */
    List<Map<String, Object>> executeQuery(JdbcQuery jdbcQuery);

}
