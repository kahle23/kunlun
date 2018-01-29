package com.apyhs.artoria.logging;

import com.apyhs.artoria.util.ClassUtils;
import com.apyhs.artoria.io.IOUtils;

import java.io.InputStream;
import java.util.logging.LogManager;

import static com.apyhs.artoria.constant.Const.EMPTY_STRING;

/**
 * Jdk logger adapter.
 * @author Kahle
 */
public class JdkLoggerAdapter implements LoggerAdapter {

	/**
	 * Default logger config filename.
	 */
	private static final String LOGGER_CONFIG_FILENAME = "logging.properties";

	/**
	 * This is JDK root logger name.
	 */
	private static final String ROOT_LOGGER_NAME = "";

	private java.util.logging.Logger log;

	public JdkLoggerAdapter() {
		log = java.util.logging.Logger.getLogger(ROOT_LOGGER_NAME);
		ClassLoader loader = ClassUtils.getDefaultClassLoader();
		InputStream in = loader != null
				? loader.getResourceAsStream(LOGGER_CONFIG_FILENAME)
				: ClassLoader.getSystemResourceAsStream(LOGGER_CONFIG_FILENAME);
		if (in == null) {
			log.info("Logger config file \"" + LOGGER_CONFIG_FILENAME
					+ "\" can not find in classpath, will using default. ");
			in = IOUtils.findJarClasspath(LOGGER_CONFIG_FILENAME);
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
		java.util.logging.Level level = log.getLevel();
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
		return null;
	}

	@Override
	public void setLevel(Level level) {
		if (level == Level.TRACE) {
			log.setLevel(java.util.logging.Level.FINER);
		}
		if (level == Level.DEBUG) {
			log.setLevel(java.util.logging.Level.FINE);
		}
		if (level == Level.INFO) {
			log.setLevel(java.util.logging.Level.INFO);
		}
		if (level == Level.WARN) {
			log.setLevel(java.util.logging.Level.WARNING);
		}
		if (level == Level.ERROR) {
			log.setLevel(java.util.logging.Level.SEVERE);
		}
	}

}
