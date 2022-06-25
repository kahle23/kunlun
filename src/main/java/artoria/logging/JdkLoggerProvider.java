package artoria.logging;

import artoria.engine.template.LoggerTemplateEngine;
import artoria.engine.template.PlainTemplateEngine;
import artoria.util.ArrayUtils;
import artoria.util.Assert;
import artoria.util.ClassLoaderUtils;
import artoria.util.CloseUtils;

import java.io.InputStream;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.LogManager;

/**
 * The jdk logger provider.
 * @author Kahle
 */
public class JdkLoggerProvider implements LoggerProvider {
    /**
     * The default logger config filename.
     */
    private static final String LOGGER_CONFIG_FILENAME = "logging.properties";
    /**
     * This is JDK root logger name.
     */
    private static final String ROOT_LOGGER_NAME = "";
    /**
     * The root logger object.
     */
    private java.util.logging.Logger logger;
    /**
     * Logger template engine.
     */
    private PlainTemplateEngine loggerTemplateEngine;

    public JdkLoggerProvider() {

        this(new LoggerTemplateEngine());
    }

    public JdkLoggerProvider(PlainTemplateEngine loggerTemplateEngine) {
        logger = java.util.logging.Logger.getLogger(ROOT_LOGGER_NAME);
        InputStream in = ClassLoaderUtils
                .getResourceAsStream(LOGGER_CONFIG_FILENAME, this.getClass());
        if (in != null) {
            try {
                LogManager.getLogManager().readConfiguration(in);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            finally {
                CloseUtils.closeQuietly(in);
            }
        }
        // Replace the Formatter that comes with the JDK.
        Handler[] handlers = logger.getHandlers();
        if (ArrayUtils.isEmpty(handlers)) { return; }
        SimpleFormatter formatter = new SimpleFormatter();
        for (Handler handler : handlers) {
            if (handler == null) { continue; }
            Formatter fm = handler.getFormatter();
            if (fm == null) { continue; }
            Class<? extends Formatter> cls = fm.getClass();
            if (cls.equals(java.util.logging.SimpleFormatter.class)) {
                handler.setFormatter(formatter);
            }
        }
        setLoggerTemplateEngine(loggerTemplateEngine);
    }

    public PlainTemplateEngine getLoggerTemplateEngine() {

        return loggerTemplateEngine;
    }

    public void setLoggerTemplateEngine(PlainTemplateEngine loggerTemplateEngine) {
        Assert.notNull(loggerTemplateEngine, "Parameter \"loggerTemplateEngine\" must not null. ");
        this.loggerTemplateEngine = loggerTemplateEngine;
    }

    @Override
    public Logger getLogger(Class<?> clazz) {
        Assert.notNull(clazz, "Parameter \"clazz\" must not null. ");
        return getLogger(clazz.getName());
    }

    @Override
    public Logger getLogger(String name) {
        Assert.notNull(name, "Parameter \"name\" must not null. ");
        java.util.logging.Logger logger = java.util.logging.Logger.getLogger(name);
        return new JdkLogger(logger, getLoggerTemplateEngine());
    }

    @Override
    public Level getLevel() {
        java.util.logging.Level level = logger.getLevel();
        if (level == java.util.logging.Level.FINER) {
            return Level.TRACE;
        }
        else if (level == java.util.logging.Level.FINE) {
            return Level.DEBUG;
        }
        else if (level == java.util.logging.Level.INFO) {
            return Level.INFO;
        }
        else if (level == java.util.logging.Level.WARNING) {
            return Level.WARN;
        }
        else if (level == java.util.logging.Level.SEVERE) {
            return Level.ERROR;
        }
        else {
            return null;
        }
    }

    @Override
    public void setLevel(Level level) {
        Assert.notNull(level, "Parameter \"level\" must not null. ");
        switch (level) {
            case TRACE: logger.setLevel(java.util.logging.Level.FINER);   break;
            case DEBUG: logger.setLevel(java.util.logging.Level.FINE);    break;
            case INFO:  logger.setLevel(java.util.logging.Level.INFO);    break;
            case WARN:  logger.setLevel(java.util.logging.Level.WARNING); break;
            case ERROR: logger.setLevel(java.util.logging.Level.SEVERE);  break;
            default: throw new IllegalArgumentException("Parameter \"level\" is illegal. ");
        }
    }

}
