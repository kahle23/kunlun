package artoria.jdbc;

import artoria.exception.ExceptionUtils;
import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import artoria.util.Assert;
import artoria.util.CloseUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class SimpleJdbcProvider extends AbstractJdbcProvider {
    private static Logger log = LoggerFactory.getLogger(SimpleJdbcProvider.class);
    private final ThreadLocal<Connection> threadConnection = new ThreadLocal<Connection>();
    private DataSource dataSource;

    public SimpleJdbcProvider(DataSource dataSource) {
        Assert.notNull(dataSource, "Parameter \"dataSource\" must not null. ");
        this.dataSource = dataSource;
    }

    public DataSource getDataSource() {

        return dataSource;
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

    @Override
    Connection getConnection() throws SQLException {
        Connection connection = threadConnection.get();
        if (connection == null) {
            connection = dataSource.getConnection();
        }
        return connection;
    }

    @Override
    void closeConnection(Connection connection) throws SQLException {
        if (threadConnection.get() == null) {
            // Indicates that no transaction was executed.
            CloseUtils.closeQuietly(connection);
        }
        // Ignore close if there is a transaction going on.
    }

    @Override
    public boolean transaction(JdbcAtom atom, Integer level) throws SQLException {
        Assert.notNull(atom, "Parameter \"atom\" must not null. ");
        if (level == null) {
            level = Connection.TRANSACTION_REPEATABLE_READ;
        }
        Connection connection = threadConnection.get();
        // Nested transaction support.
        if (connection != null) {
            if (connection.getTransactionIsolation() < level) {
                connection.setTransactionIsolation(level);
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
            connection = getConnection();
            autoCommit = connection.getAutoCommit();
            threadConnection.set(connection);
            connection.setTransactionIsolation(level);
            connection.setAutoCommit(false);
            boolean result = atom.run();
            if (result) {
                connection.commit();
            }
            else {
                connection.rollback();
            }
            return result;
        }
        catch (Exception e) {
            rollbackTransaction(connection);
            throw ExceptionUtils.wrap(e);
        }
        finally {
            closeTransaction(connection, autoCommit);
        }
    }

}
