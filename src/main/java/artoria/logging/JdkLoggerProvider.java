package artoria.logging;

import artoria.template.LoggerRenderer;
import artoria.template.Renderer;
import artoria.util.ArrayUtils;
import artoria.util.Assert;
import artoria.util.ClassLoaderUtils;
import artoria.util.CloseUtils;

import java.io.InputStream;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.LogManager;

/**
 * Jdk logger provider.
 * @author Kahle
 */
public class JdkLoggerProvider implements LoggerProvider {
    /**
     * Default logger config filename.
     */
    private static final String LOGGER_CONFIG_FILENAME = "logging.properties";
    /**
     * This is JDK root logger name.
     */
    private static final String ROOT_LOGGER_NAME = "";
    /**
     * Root logger object.
     */
    private java.util.logging.Logger logger;
    /**
     * Logger renderer.
     */
    private Renderer loggerRenderer;

    public JdkLoggerProvider() {

        this(new LoggerRenderer());
    }

    public JdkLoggerProvider(Renderer loggerRenderer) {
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
        this.setLoggerRenderer(loggerRenderer);
    }

    public Renderer getLoggerRenderer() {

        return loggerRenderer;
    }

    public void setLoggerRenderer(Renderer loggerRenderer) {
        Assert.notNull(loggerRenderer, "Parameter \"loggerRenderer\" must not null. ");
        this.loggerRenderer = loggerRenderer;
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
        return new JdkLogger(logger, getLoggerRenderer());
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
            default: throw new IllegalStateException("Parameter \"level\" is illegal. ");
        }
    }

}
