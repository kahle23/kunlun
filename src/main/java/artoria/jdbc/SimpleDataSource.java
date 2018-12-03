package artoria.jdbc;

import artoria.aop.Enhancer;
import artoria.aop.Interceptor;
import artoria.exception.ExceptionUtils;
import artoria.util.Assert;
import artoria.util.PropertiesUtils;
import artoria.util.StringUtils;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Properties;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Logger;

/**
 * Simple data source.
 * @author Kahle
 */
public class SimpleDataSource implements DataSource {
    private static final String CLASS_NAME = SimpleDataSource.class.getName();
    private static final String UNSUPPORTED_OPERATION = "In " + CLASS_NAME + " this operation is unsupported. ";
    private static final String DEFAULT_CONFIG_NAME = "jdbc.properties";
    private BlockingQueue<Connection> queue;
    private String driverClass = "com.mysql.jdbc.Driver";
    private String jdbcUrl;
    private String user;
    private String password;
    private int maxPoolSize = 8;
    private int minPoolSize = 2;

    public SimpleDataSource() {

        this(PropertiesUtils.create(DEFAULT_CONFIG_NAME));
    }

    public SimpleDataSource(Properties prop) {
        this(
                prop.getProperty("driverClass"),
                prop.getProperty("jdbcUrl"),
                prop.getProperty("user"),
                prop.getProperty("password")
        );
    }

    public SimpleDataSource(String driverClass, String jdbcUrl, String user, String password) {

        this(driverClass, jdbcUrl, user, password, -1, -1);
    }

    public SimpleDataSource(String driverClass, String jdbcUrl, String user, String password, int maxPoolSize, int minPoolSize) {
        if (StringUtils.isNotBlank(driverClass)) { this.driverClass = driverClass; }
        Assert.notBlank(jdbcUrl, "Parameter \"jdbcUrl\" must not blank. ");
        this.jdbcUrl = jdbcUrl;
        Assert.notNull(user, "Parameter \"user\" must not null. ");
        this.user = user;
        Assert.notNull(password, "Parameter \"password\" must not null. ");
        this.password = password;
        if (maxPoolSize > 0) { this.maxPoolSize = maxPoolSize; }
        Assert.state(minPoolSize <= this.maxPoolSize
                , "Parameter \"minPoolSize\" must less than or equal to \"maxPoolSize\" " + this.maxPoolSize + ". ");
        if (minPoolSize > 0) { this.minPoolSize = minPoolSize; }
        this.queue = new ArrayBlockingQueue<Connection>(this.maxPoolSize);
        try {
            Class.forName(this.driverClass);
            for (int i = 0; i < this.minPoolSize; i++) {
                this.queue.offer(this.createConnection());
            }
        }
        catch (Exception e) {
            throw ExceptionUtils.wrap(e);
        }
    }

    public Connection createConnection() throws SQLException {
        Connection conn = DriverManager.getConnection(this.jdbcUrl, this.user, this.password);
        ConnectionInterceptor intp = new ConnectionInterceptor(this.queue, conn);
        return (Connection) Enhancer.enhance(Connection.class, intp);
    }

    @Override
    public Connection getConnection() throws SQLException {
        Connection conn = this.queue.poll();
        if (conn == null) {
            conn = this.createConnection();
        }
        return conn;
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {

        throw new UnsupportedOperationException(UNSUPPORTED_OPERATION);
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {

        throw new UnsupportedOperationException(UNSUPPORTED_OPERATION);
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {

        throw new UnsupportedOperationException(UNSUPPORTED_OPERATION);
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException {

        throw new UnsupportedOperationException(UNSUPPORTED_OPERATION);
    }

    @Override
    public void setLogWriter(PrintWriter out) throws SQLException {

        throw new UnsupportedOperationException(UNSUPPORTED_OPERATION);
    }

    @Override
    public void setLoginTimeout(int seconds) throws SQLException {

        throw new UnsupportedOperationException(UNSUPPORTED_OPERATION);
    }

    @Override
    public int getLoginTimeout() throws SQLException {

        throw new UnsupportedOperationException(UNSUPPORTED_OPERATION);
    }

//     TODO: 1.7
//    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {

        throw new UnsupportedOperationException(UNSUPPORTED_OPERATION);
    }

    private static class ConnectionInterceptor implements Interceptor {
        private static final String PROXY_METHOD = "close";
        private BlockingQueue<Connection> queue;
        private Connection connection;

        public ConnectionInterceptor(BlockingQueue<Connection> queue, Connection connection) {
            this.queue = queue;
            this.connection = connection;
        }

        @Override
        public Object intercept(Object proxyObject, Method method, Object[] args) throws Throwable {
            boolean offer = false;
            if (PROXY_METHOD.equals(method.getName())) {
                offer = this.queue.offer(connection);
            }
            if (!offer) {
                return method.invoke(connection, args);
            }
            return null;
        }

    }

}
