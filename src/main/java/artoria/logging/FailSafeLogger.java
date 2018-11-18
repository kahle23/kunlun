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
    public void trace(String message) {

        logger.trace(message);
    }

    @Override
    public void trace(Throwable t) {

        logger.trace(t);
    }

    @Override
    public void trace(String message, Throwable t) {

        logger.trace(message, t);
    }

    @Override
    public void debug(String message) {

        logger.debug(message);
    }

    @Override
    public void debug(Throwable t) {

        logger.debug(t);
    }

    @Override
    public void debug(String message, Throwable t) {

        logger.debug(message, t);
    }

    @Override
    public void info(String message) {

        logger.info(message);
    }

    @Override
    public void info(Throwable t) {

        logger.info(t);
    }

    @Override
    public void info(String message, Throwable t) {

        logger.info(message, t);
    }

    @Override
    public void warn(String message) {

        logger.warn(message);
    }

    @Override
    public void warn(Throwable t) {

        logger.warn(t);
    }

    @Override
    public void warn(String message, Throwable t) {

        logger.warn(message, t);
    }

    @Override
    public void error(String message) {

        logger.error(message);
    }

    @Override
    public void error(Throwable t) {

        logger.error(t);
    }

    @Override
    public void error(String message, Throwable t) {

        logger.error(message, t);
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
