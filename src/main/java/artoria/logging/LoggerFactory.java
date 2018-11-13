package artoria.logging;

import artoria.util.Assert;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Logger factory.
 * @author Kahle
 */
public class LoggerFactory {
    private static final ConcurrentMap<String, FailSafeLogger> LOGGERS = new ConcurrentHashMap<String, FailSafeLogger>();
    private static volatile LoggerProvider loggerProvider;

    static {

        LoggerFactory.setLoggerProvider(new JdkLoggerProvider());
    }

    /**
     * Get logger provider.
     * @return Logger provider
     */
    public static LoggerProvider getLoggerProvider() {

        return loggerProvider;
    }

    /**
     * Set logger provider.
     * @param loggerProvider Logger provider
     */
    public static void setLoggerProvider(LoggerProvider loggerProvider) {
        Assert.notNull(loggerProvider, "Parameter \"loggerProvider\" must not null. ");
        LoggerFactory.loggerProvider = loggerProvider;
        for (Map.Entry<String, FailSafeLogger> entry : LOGGERS.entrySet()) {
            Logger logger = loggerProvider.getLogger(entry.getKey());
            entry.getValue().setLogger(logger);
        }
        Logger logger = LoggerFactory.getLogger(LoggerFactory.class);
        logger.info("Set logger provider: " + loggerProvider.getClass().getName());
    }

    /**
     * Get logger.
     * @param clazz The class
     * @return The logger
     */
    public static Logger getLogger(Class<?> clazz) {
        Assert.notNull(clazz, "Parameter \"clazz\" must not null. ");
        String clazzName = clazz.getName();
        return LoggerFactory.getLogger(clazzName);
    }

    /**
     * Get logger.
     * @param name The class name or other name
     * @return The logger
     */
    public static Logger getLogger(String name) {
        Assert.notNull(name, "Parameter \"name\" must not null. ");
        FailSafeLogger logger = LOGGERS.get(name);
        if (logger == null) {
            Logger inner = loggerProvider.getLogger(name);
            LOGGERS.putIfAbsent(name, new FailSafeLogger(inner));
            logger = LOGGERS.get(name);
        }
        return logger;
    }

    /**
     * Get logger level.
     * @return Current logger level
     */
    public static Level getLevel() {

        return loggerProvider.getLevel();
    }

    /**
     * Set logger level.
     * @param level The logger level you want set
     */
    public static void setLevel(Level level) {

        loggerProvider.setLevel(level);
    }

}
