package artoria.logging;

import artoria.io.StringBuilderWriter;
import artoria.template.TemplateEngine;

import java.util.logging.Level;

import static artoria.common.Constants.THREE;

/**
 * Jdk logger wrapper.
 * @author Kahle
 */
public class JdkLogger implements Logger {
    private final java.util.logging.Logger logger;
    private final TemplateEngine templateEngine;

    private void logp(Level level, String format, Object[] arguments, Throwable throwable) {
        if (!logger.isLoggable(level)) { return; }
        StackTraceElement element = new Throwable().getStackTrace()[THREE];
        String clazzName = element.getClassName();
        String methodName = element.getMethodName();
        StringBuilderWriter writer = new StringBuilderWriter();
        templateEngine.render(arguments, writer, null, format);
        String message = writer.toString();
        logger.logp(level, clazzName, methodName, message, throwable);
    }

    public JdkLogger(java.util.logging.Logger logger, TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
        this.logger = logger;
    }

    @Override
    public void trace(String format, Object... arguments) {

        logp(Level.FINER, format, arguments, null);
    }

    @Override
    public void trace(String message, Throwable throwable) {

        logp(Level.FINER, message, null, throwable);
    }

    @Override
    public void debug(String format, Object... arguments) {

        logp(Level.FINE, format, arguments, null);
    }

    @Override
    public void debug(String message, Throwable throwable) {

        logp(Level.FINE, message, null, throwable);
    }

    @Override
    public void info(String format, Object... arguments) {

        logp(Level.INFO, format, arguments, null);
    }

    @Override
    public void info(String message, Throwable throwable) {

        logp(Level.INFO, message, null, throwable);
    }

    @Override
    public void warn(String format, Object... arguments) {

        logp(Level.WARNING, format, arguments, null);
    }

    @Override
    public void warn(String message, Throwable throwable) {

        logp(Level.WARNING, message, null, throwable);
    }

    @Override
    public void error(String format, Object... arguments) {

        logp(Level.SEVERE, format, arguments, null);
    }

    @Override
    public void error(String message, Throwable throwable) {

        logp(Level.SEVERE, message, null, throwable);
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
