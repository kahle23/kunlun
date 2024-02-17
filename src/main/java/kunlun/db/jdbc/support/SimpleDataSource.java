/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.db.jdbc.support;

import kunlun.aop.AbstractInterceptor;
import kunlun.aop.ProxyUtils;
import kunlun.exception.ExceptionUtils;
import kunlun.util.Assert;
import kunlun.util.StringUtils;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Logger;

import static kunlun.common.constant.Numbers.*;

/**
 * Simple data source.
 * @author Kahle
 */
public class SimpleDataSource implements DataSource {
    private final BlockingQueue<Connection> queue;
    private final String driverClass;
    private final String jdbcUrl;
    private final String user;
    private final String password;

    public SimpleDataSource(String driverClass,
                            String jdbcUrl,
                            String user,
                            String password) {

        this(driverClass, jdbcUrl, user, password, MINUS_ONE, MINUS_ONE);
    }

    public SimpleDataSource(String driverClass,
                            String jdbcUrl,
                            String user,
                            String password,
                            int maxPoolSize,
                            int minPoolSize) {
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
        maxPoolSize = maxPoolSize > ZERO ? maxPoolSize : EIGHT;
        minPoolSize = minPoolSize > ZERO ? minPoolSize : TWO;
        this.queue = new ArrayBlockingQueue<Connection>(maxPoolSize);
        try {
            Class.forName(this.driverClass);
            for (int i = ZERO; i < minPoolSize; i++) {
                queue.offer(createConnection());
            }
        }
        catch (Exception e) {
            throw ExceptionUtils.wrap(e);
        }
    }

    protected Connection createConnection() throws SQLException {
        final String proxyMethod = "close";
        Connection conn = DriverManager.getConnection(jdbcUrl, user, password);
        return ProxyUtils.proxy(new AbstractInterceptor<Connection>(conn) {
            @Override
            public Object intercept(Object proxyObject, Method method, Object[] args) throws Throwable {
                boolean offer = false;
                if (proxyMethod.equals(method.getName())) {
                    offer = queue.offer(getOriginalObject());
                }
                if (!offer) {
                    return method.invoke(getOriginalObject(), args);
                }
                return null;
            }
        });
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
