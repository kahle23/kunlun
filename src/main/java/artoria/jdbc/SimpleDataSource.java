package artoria.jdbc;

import artoria.aop.Interceptor;
import artoria.aop.ProxyUtils;
import artoria.exception.ExceptionUtils;
import artoria.util.Assert;
import artoria.util.ClassLoaderUtils;
import artoria.util.StringUtils;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
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

import static artoria.common.constant.Numbers.*;

/**
 * Simple data source.
 * @author Kahle
 */
@Deprecated
public class SimpleDataSource implements DataSource {
    private static final String DEFAULT_CONFIG_NAME = "jdbc.properties";
    private BlockingQueue<Connection> queue;
    private String driverClass;
    private String jdbcUrl;
    private String user;
    private String password;
    private int maxPoolSize;
    private int minPoolSize;

    private static Properties readProperties() {
        try {
            InputStream inputStream =
                    ClassLoaderUtils.getResourceAsStream(DEFAULT_CONFIG_NAME, SimpleDataSource.class);
            Assert.notNull(inputStream,
                    "The file \"" + DEFAULT_CONFIG_NAME + "\" cannot be found in the classpath. "
            );
            Properties properties = new Properties();
            properties.load(inputStream);
            return properties;
        }
        catch (IOException e) {
            throw ExceptionUtils.wrap(e);
        }
    }

    private class ConnectionInterceptor implements Interceptor {
        private static final String PROXY_METHOD = "close";
        private BlockingQueue<Connection> queue;
        private Connection connection;

        ConnectionInterceptor(BlockingQueue<Connection> queue, Connection connection) {
            this.connection = connection;
            this.queue = queue;
        }

        @Override
        public Object intercept(Object proxyObject, Method method, Object[] args) throws Throwable {
            boolean offer = false;
            if (PROXY_METHOD.equals(method.getName())) {
                offer = queue.offer(connection);
            }
            if (!offer) {
                return method.invoke(connection, args);
            }
            return null;
        }

    }

    private Connection createConnection() throws SQLException {
        Connection conn = DriverManager.getConnection(jdbcUrl, user, password);
        ConnectionInterceptor interceptor = new ConnectionInterceptor(queue, conn);
        return (Connection) ProxyUtils.proxy(Connection.class, interceptor);
    }

    public SimpleDataSource() {

        this(readProperties());
    }

    public SimpleDataSource(Properties properties) {
        this(
                properties.getProperty("driverClass"),
                properties.getProperty("jdbcUrl"),
                properties.getProperty("user"),
                properties.getProperty("password")
        );
    }

    public SimpleDataSource(String driverClass, String jdbcUrl, String user, String password) {

        this(driverClass, jdbcUrl, user, password, MINUS_ONE, MINUS_ONE);
    }

    public SimpleDataSource(String driverClass, String jdbcUrl, String user, String password, int maxPoolSize, int minPoolSize) {
        Assert.notBlank(jdbcUrl, "Parameter \"jdbcUrl\" must not blank. ");
        Assert.notNull(user, "Parameter \"user\" must not null. ");
        Assert.notNull(password, "Parameter \"password\" must not null. ");
        if (minPoolSize > maxPoolSize) {
            throw new IllegalArgumentException(
                    "Parameter \"minPoolSize\" must less than or equal to \"maxPoolSize\". "
            );
        }
        this.driverClass = StringUtils.isNotBlank(driverClass) ? driverClass : "com.mysql.jdbc.Driver";
        this.jdbcUrl = jdbcUrl;
        this.user = user;
        this.password = password;
        this.maxPoolSize = maxPoolSize > ZERO ? maxPoolSize : EIGHT;
        this.minPoolSize = minPoolSize > ZERO ? minPoolSize : TWO;
        this.queue = new ArrayBlockingQueue<Connection>(this.maxPoolSize);
        try {
            Class.forName(this.driverClass);
            for (int i = ZERO; i < this.minPoolSize; i++) {
                queue.offer(createConnection());
            }
        }
        catch (Exception e) {
            throw ExceptionUtils.wrap(e);
        }
    }

    @Override
    public Connection getConnection() throws SQLException {
        Connection conn = queue.poll();
        if (conn == null) {
            conn = createConnection();
        }
        else {
            if (!conn.isValid(TEN)) {
                conn.close();
                conn = createConnection();
            }
        }
        return conn;
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {

        throw new UnsupportedOperationException();
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {

        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {

        throw new UnsupportedOperationException();
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException {

        throw new UnsupportedOperationException();
    }

    @Override
    public void setLogWriter(PrintWriter out) throws SQLException {

        throw new UnsupportedOperationException();
    }

    @Override
    public void setLoginTimeout(int seconds) throws SQLException {

        throw new UnsupportedOperationException();
    }

    @Override
    public int getLoginTimeout() throws SQLException {

        throw new UnsupportedOperationException();
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {

        throw new UnsupportedOperationException();
    }

}
