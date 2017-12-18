package artoria.logging;

import artoria.util.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.LogManager;

import static artoria.util.StringConstant.EMPTY_STRING;

/**
 * Jdk logger adapter
 * @author Kahle
 */
public class JdkLoggerAdapter implements LoggerAdapter {

	private static final String LOGGER_CONFIG_FILENAME = "logging.properties";

	private java.util.logging.Logger logger;

	public JdkLoggerAdapter() {
		Class<JdkLoggerAdapter> clazz = JdkLoggerAdapter.class;
		logger = java.util.logging.Logger.getLogger(clazz.getName());
		ClassLoader loader = clazz.getClassLoader();
		InputStream in = loader != null
				? loader.getResourceAsStream(LOGGER_CONFIG_FILENAME)
				: ClassLoader.getSystemResourceAsStream(LOGGER_CONFIG_FILENAME);
		if (in == null) {
			try {
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				Properties prop = new Properties();
				prop.setProperty("java.util.logging.ConsoleHandler.level", "INFO");
				prop.setProperty("java.util.logging.ConsoleHandler.formatter", "artoria.logging.SimpleFormatter");
				prop.setProperty("java.util.logging.SocketHandler.formatter", "artoria.logging.SimpleFormatter");
				prop.setProperty("java.util.logging.FileHandler.formatter", "artoria.logging.SimpleFormatter");
				prop.setProperty("handlers", "java.util.logging.ConsoleHandler");
				prop.store(out, null);
				in = new ByteArrayInputStream(out.toByteArray());
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (in != null) {
			try {
				LogManager.getLogManager().readConfiguration(in);
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			finally {
				IOUtils.closeQuietly(in);
			}
		}
	}

	@Override
	public Logger getLogger(Class<?> clazz) {
		String name = clazz == null ? EMPTY_STRING : clazz.getName();
		return new JdkLogger(java.util.logging.Logger.getLogger(name));
	}

	@Override
	public Logger getLogger(String clazz) {
		java.util.logging.Logger logger = java.util.logging.Logger.getLogger(clazz);
		return new JdkLogger(logger);
	}

	@Override
	public Level getLevel() {
		java.util.logging.Level level = logger.getLevel();
		if (level == java.util.logging.Level.FINER) {
			return Level.TRACE;
		}
		if (level == java.util.logging.Level.FINE) {
			return Level.DEBUG;
		}
		if (level == java.util.logging.Level.INFO) {
			return Level.INFO;
		}
		if (level == java.util.logging.Level.WARNING) {
			return Level.WARN;
		}
		if (level == java.util.logging.Level.SEVERE) {
			return Level.ERROR;
		}
		return Level.INFO;
	}

	@Override
	public void setLevel(Level level) {
		if (level == Level.TRACE) {
			logger.setLevel(java.util.logging.Level.FINER);
		}
		if (level == Level.DEBUG) {
			logger.setLevel(java.util.logging.Level.FINE);
		}
		if (level == Level.INFO) {
			logger.setLevel(java.util.logging.Level.INFO);
		}
		if (level == Level.WARN) {
			logger.setLevel(java.util.logging.Level.WARNING);
		}
		if (level == Level.ERROR) {
			logger.setLevel(java.util.logging.Level.SEVERE);
		}
	}

}
