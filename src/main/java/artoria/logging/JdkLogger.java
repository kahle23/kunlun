package artoria.logging;

import java.util.logging.Level;

/**
 * Jdk logger wrapper.
 * @author Kahle
 */
public class JdkLogger implements Logger {
    private final java.util.logging.Logger logger;

    public JdkLogger(java.util.logging.Logger logger) {

        this.logger = logger;
    }

    private void logp(Level level, String message, Throwable t) {
        if (!logger.isLoggable(level)) { return; }
        StackTraceElement element = new Throwable().getStackTrace()[3];
        String clazzName = element.getClassName();
        String methodName = element.getMethodName();
        logger.logp(level, clazzName, methodName, message, t);
    }

    @Override
    public void trace(String message) {

        this.logp(Level.FINER, message, null);
    }

    @Override
    public void trace(Throwable t) {

        this.logp(Level.FINER, t.getMessage(), t);
    }

    @Override
    public void trace(String message, Throwable t) {

        this.logp(Level.FINER, message, t);
    }

    @Override
    public void debug(String message) {

        this.logp(Level.FINE, message, null);
    }

    @Override
    public void debug(Throwable t) {

        this.logp(Level.FINE, t.getMessage(), t);
    }

    @Override
    public void debug(String message, Throwable t) {

        this.logp(Level.FINE, message, t);
    }

    @Override
    public void info(String message) {

        this.logp(Level.INFO, message, null);
    }

    @Override
    public void info(Throwable t) {

        this.logp(Level.INFO, t.getMessage(), t);
    }

    @Override
    public void info(String message, Throwable t) {

        this.logp(Level.INFO, message, t);
    }

    @Override
    public void warn(String message) {

        this.logp(Level.WARNING, message, null);
    }

    @Override
    public void warn(Throwable t) {

        this.logp(Level.WARNING, t.getMessage(), t);
    }

    @Override
    public void warn(String message, Throwable t) {

        this.logp(Level.WARNING, message, t);
    }

    @Override
    public void error(String message) {

        this.logp(Level.SEVERE, message, null);
    }

    @Override
    public void error(Throwable t) {

        this.logp(Level.SEVERE, t.getMessage(), t);
    }

    @Override
    public void error(String message, Throwable t) {

        this.logp(Level.SEVERE, message, t);
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
