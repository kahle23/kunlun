package artoria.logging;

/**
 * Fail safe logger.
 * @author Kahle
 */
public class FailSafeLogger implements Logger {
    private Logger logger;

    public FailSafeLogger(Logger logger) {

        this.logger = logger;
    }

    public Logger getLogger() {

        return this.logger;
    }

    public void setLogger(Logger logger) {

        this.logger = logger;
    }

    @Override
    public void trace(String format, Object... arguments) {

        logger.trace(format, arguments);
    }

    @Override
    public void trace(String message, Throwable throwable) {

        logger.trace(message, throwable);
    }

    @Override
    public void debug(String format, Object... arguments) {

        logger.debug(format, arguments);
    }

    @Override
    public void debug(String message, Throwable throwable) {

        logger.debug(message, throwable);
    }

    @Override
    public void info(String format, Object... arguments) {

        logger.info(format, arguments);
    }

    @Override
    public void info(String message, Throwable throwable) {

        logger.info(message, throwable);
    }

    @Override
    public void warn(String format, Object... arguments) {

        logger.warn(format, arguments);
    }

    @Override
    public void warn(String message, Throwable throwable) {

        logger.warn(message, throwable);
    }

    @Override
    public void error(String format, Object... arguments) {

        logger.error(format, arguments);
    }

    @Override
    public void error(String message, Throwable throwable) {

        logger.error(message, throwable);
    }

    @Override
    public boolean isTraceEnabled() {

        return logger.isTraceEnabled();
    }

    @Override
    public boolean isDebugEnabled() {

        return logger.isDebugEnabled();
    }

    @Override
    public boolean isInfoEnabled() {

        return logger.isInfoEnabled();
    }

    @Override
    public boolean isWarnEnabled() {

        return logger.isWarnEnabled();
    }

    @Override
    public boolean isErrorEnabled() {

        return logger.isErrorEnabled();
    }

}
