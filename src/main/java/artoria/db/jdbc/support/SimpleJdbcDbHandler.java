package artoria.db.jdbc.support;

import artoria.data.tuple.Triple;
import artoria.db.jdbc.*;
import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import artoria.util.ArgumentUtils;
import artoria.util.Assert;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.Collection;
import java.util.List;

/**
 * The simple jdbc database handler.
 * @author Kahle
 */
public class SimpleJdbcDbHandler extends AbstractJdbcDbHandler {
    private static final Logger log = LoggerFactory.getLogger(SimpleJdbcDbHandler.class);
    private final JdbcConfig jdbcConfig;

    public SimpleJdbcDbHandler(DataSource dataSource) {
        Assert.notNull(dataSource, "Parameter \"dataSource\" must not null. ");
        this.jdbcConfig = new ConfigImpl(dataSource);
    }

    @Override
    protected JdbcConfig getJdbcConfig(String configCode) {

        return jdbcConfig;
    }

    @Override
    public Object execute(Object[] arguments) {
        Triple<Object, String, Class<?>> triple = ArgumentUtils.parseToObjStrCls(arguments);
        String   operation = triple.getMiddle();
        Object   input = triple.getLeft();
        Class<?> type = triple.getRight();
        if (TRANSACTION.equals(operation)) {
            Assert.isSupport(type, false, Boolean.class, Object.class);
            return transaction((JdbcTx) input);
        }
        else if (CALLBACK.equals(operation)) {
            return callback((JdbcCallback<?>) input);
        }
        else if (EXECUTE_UPDATE.equals(operation)) {
            Assert.isSupport(type, false, Integer.class, Number.class, Object.class);
            return executeUpdate((JdbcUpdate) input);
        }
        else if (EXECUTE_QUERY.equals(operation)) {
            Assert.isSupport(type, false, List.class, Collection.class, Object.class);
            return executeQuery((JdbcQuery) input);
        }
        else {
            throw new UnsupportedOperationException(
                    "The method is unsupported. \n\n" +
                            "\n" +
                            "Supported method:\n" +
                            " - transaction\n" +
                            " - callback\n" +
                            " - executeUpdate\n" +
                            " - executeQuery\n"
            );
        }
    }

    public static class ConfigImpl extends AbstractConfig {
        private final ThreadLocal<Connection> threadLocalConnection = new ThreadLocal<Connection>();
        private final DataSource dataSource;

        public ConfigImpl(DataSource dataSource) {

            this.dataSource = dataSource;
        }

        @Override
        public DataSource getDataSource() {

            return dataSource;
        }

        @Override
        public Connection getThreadLocalConnection() {

            return threadLocalConnection.get();
        }

        @Override
        public void setThreadLocalConnection(Connection connection) {

            threadLocalConnection.set(connection);
        }

        @Override
        public void removeThreadLocalConnection() {

            threadLocalConnection.remove();
        }
    }

}
