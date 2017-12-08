package artoria.logging;

import artoria.util.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.LogManager;

/**
 * @author Kahle
 */
public class Logger {
	private static final String LOGGER_CONFIG_FILENAME = "logging.properties";

	private static final LogManager LOG_MANAGER;

	static {
		LOG_MANAGER = LogManager.getLogManager();
		ClassLoader loader = Logger.class.getClassLoader();
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
			catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			LOG_MANAGER.readConfiguration(in);
		}
		catch (IOException | SecurityException e) {
			e.printStackTrace();
		}
		finally { IOUtils.closeQuietly(in); }
	}

	private static java.util.logging.Logger getLogger(String clazzName) {
		java.util.logging.Logger log = LOG_MANAGER.getLogger(clazzName);
		if(log == null) {
			log = java.util.logging.Logger.getLogger(clazzName);
			LOG_MANAGER.addLogger(log);
		}
		return log;
	}

	private static boolean isLoggable(Level level) {
		String clazzName = Thread.currentThread().getStackTrace()[3].getClassName();
		return getLogger(clazzName).isLoggable(level);
	}

	private static void logp(Level level, String message, Throwable t) {
		String clazzName = Thread.currentThread().getStackTrace()[3].getClassName();
		String methodName = Thread.currentThread().getStackTrace()[3].getMethodName();
		getLogger(clazzName).logp(level, clazzName, methodName, message, t);
	}

	public static boolean isFinestEnabled() {
		return isLoggable(Level.FINEST);
	}

	public static boolean isFinerEnabled() {
		return isLoggable(Level.FINER);
	}

	public static boolean isFineEnabled() {
		return isLoggable(Level.FINE);
	}

	public static boolean isConfigEnabled() {
		return isLoggable(Level.CONFIG);
	}

	public static boolean isInfoEnabled() {
		return isLoggable(Level.INFO);
	}

	public static boolean isWarningEnabled() {
		return isLoggable(Level.WARNING);
	}

	public static boolean isSevereEnabled() {
		return isLoggable(Level.SEVERE);
	}

	public static void finest(String message) {
		logp(Level.FINEST, message, null);
	}

	public static void finest(String message,  Throwable t) {
		logp(Level.FINEST, message, t);
	}

	public static void finer(String message) {
		logp(Level.FINER, message, null);
	}

	public static void finer(String message,  Throwable t) {
		logp(Level.FINER, message, t);
	}

	public static void fine(String message) {
		logp(Level.FINE, message, null);
	}

	public static void fine(String message,  Throwable t) {
		logp(Level.FINE, message, t);
	}

	public static void config(String message) {
		logp(Level.CONFIG, message, null);
	}

	public static void config(String message,  Throwable t) {
		logp(Level.CONFIG, message, t);
	}

	public static void info(String message) {
		logp(Level.INFO, message, null);
	}

	public static void info(String message, Throwable t) {
		logp(Level.INFO, message, t);
	}

	public static void warning(String message) {
		logp(Level.WARNING, message, null);
	}

	public static void warning(String message, Throwable t) {
		logp(Level.WARNING, message, t);
	}

	public static void severe(String message) {
		logp(Level.SEVERE, message, null);
	}

	public static void severe(String message, Throwable t) {
		logp(Level.SEVERE, message, t);
	}

}
