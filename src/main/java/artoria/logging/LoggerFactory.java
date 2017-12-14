package artoria.logging;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Logger factory
 * @author Kahle
 */
public class LoggerFactory {

	private static final ConcurrentMap<String, FailsafeLogger> LOGGERS = new ConcurrentHashMap<>();

	private static volatile LoggerAdapter LOGGER_ADAPTER;

	static {
		LoggerFactory.setLoggerAdapter(new JdkLoggerAdapter());
	}

	private LoggerFactory() {}

	/**
	 * Set logger adapter
	 * @param loggerAdapter logger adapter
	 */
	public static void setLoggerAdapter(LoggerAdapter loggerAdapter) {
		if (loggerAdapter != null) {
			LoggerFactory.LOGGER_ADAPTER = loggerAdapter;
			for (Map.Entry<String, FailsafeLogger> entry : LOGGERS.entrySet()) {
				entry.getValue().setLogger(LOGGER_ADAPTER.getLogger(entry.getKey()));
			}
			Logger logger = LoggerFactory.getLogger(LoggerFactory.class.getName());
			logger.info("using logger: " + loggerAdapter.getClass().getName());
		}
	}

	/**
	 * Get logger
	 * @param clazz The class name or other key
	 * @return The logger
	 */
	public static Logger getLogger(Class<?> clazz) {
		FailsafeLogger logger = LOGGERS.get(clazz.getName());
		if (logger == null) {
			Logger inner = LOGGER_ADAPTER.getLogger(clazz);
			LOGGERS.putIfAbsent(clazz.getName(), new FailsafeLogger(inner));
			logger = LOGGERS.get(clazz.getName());
		}
		return logger;
	}

	/**
	 * Get logger
	 * @param clazz The class name or other key
	 * @return The logger
	 */
	public static Logger getLogger(String clazz) {
		FailsafeLogger logger = LOGGERS.get(clazz);
		if (logger == null) {
			Logger inner = LOGGER_ADAPTER.getLogger(clazz);
			LOGGERS.putIfAbsent(clazz, new FailsafeLogger(inner));
			logger = LOGGERS.get(clazz);
		}
		return logger;
	}

	/**
	 * Get log level
	 * @return Current log level
	 */
	public static Level getLevel() {
		return LOGGER_ADAPTER.getLevel();
	}

	/**
	 * Set log level (maybe is invalid)
	 * @param level The log level
	 */
	public static void setLevel(Level level) {
		LOGGER_ADAPTER.setLevel(level);
	}

}
