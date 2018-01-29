package com.apyhs.artoria.logging;

import java.util.logging.Level;

/**
 * Jdk logger.
 * @author Kahle
 */
public class JdkLogger implements Logger {

    private final java.util.logging.Logger logger;

    public JdkLogger(java.util.logging.Logger logger) {
        this.logger = logger;
    }

    private void logp(Level level, String message, Throwable t) {
        StackTraceElement element = new Throwable().getStackTrace()[3];
        String clazzName = element.getClassName();
        String methodName = element.getMethodName();
        logger.logp(level, clazzName, methodName, message, t);
    }

    @Override
    public void trace(String msg) {
        if (!this.isTraceEnabled()) {
            return;
        }
        this.logp(Level.FINER, msg, null);
    }

    @Override
    public void trace(Throwable e) {
        if (!this.isTraceEnabled()) {
            return;
        }
        this.logp(Level.FINER, e.getMessage(), e);
    }

    @Override
    public void trace(String msg, Throwable e) {
        if (!this.isTraceEnabled()) {
            return;
        }
        this.logp(Level.FINER, msg, e);
    }

    @Override
    public void debug(String msg) {
        if (!this.isDebugEnabled()) {
            return;
        }
        this.logp(Level.FINE, msg, null);
    }

    @Override
    public void debug(Throwable e) {
        if (!this.isDebugEnabled()) {
            return;
        }
        this.logp(Level.FINE, e.getMessage(), e);
    }

    @Override
    public void debug(String msg, Throwable e) {
        if (!this.isDebugEnabled()) {
            return;
        }
        this.logp(Level.FINE, msg, e);
    }

    @Override
    public void info(String msg) {
        if (!this.isInfoEnabled()) {
            return;
        }
        this.logp(Level.INFO, msg, null);
    }

    @Override
    public void info(Throwable e) {
        if (!this.isInfoEnabled()) {
            return;
        }
        this.logp(Level.INFO, e.getMessage(), e);
    }

    @Override
    public void info(String msg, Throwable e) {
        if (!this.isInfoEnabled()) {
            return;
        }
        this.logp(Level.INFO, msg, e);
    }

    @Override
    public void warn(String msg) {
        if (!this.isWarnEnabled()) {
            return;
        }
        this.logp(Level.WARNING, msg, null);
    }

    @Override
    public void warn(Throwable e) {
        if (!this.isWarnEnabled()) {
            return;
        }
        this.logp(Level.WARNING, e.getMessage(), e);
    }

    @Override
    public void warn(String msg, Throwable e) {
        if (!this.isWarnEnabled()) {
            return;
        }
        this.logp(Level.WARNING, msg, e);
    }

    @Override
    public void error(String msg) {
        if (!this.isErrorEnabled()) {
            return;
        }
        this.logp(Level.SEVERE, msg, null);
    }

    @Override
    public void error(Throwable e) {
        if (!this.isErrorEnabled()) {
            return;
        }
        this.logp(Level.SEVERE, e.getMessage(), e);
    }

    @Override
    public void error(String msg, Throwable e) {
        if (!this.isErrorEnabled()) {
            return;
        }
        this.logp(Level.SEVERE, msg, e);
    }

    @Override
    public boolean isTraceEnabled() {
        return logger.isLoggable(Level.FINER);
    }

    @Override
    public boolean isDebugEnabled() {
        return logger.isLoggable(Level.FINE);
    }

    @Override
    public boolean isInfoEnabled() {
        return logger.isLoggable(Level.INFO);
    }

    @Override
    public boolean isWarnEnabled() {
        return logger.isLoggable(Level.WARNING);
    }

    @Override
    public boolean isErrorEnabled() {
        return logger.isLoggable(Level.SEVERE);
    }

}
