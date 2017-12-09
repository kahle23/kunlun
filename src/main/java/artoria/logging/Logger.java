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
 * Jdk logger tools.
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
		return Logger.getLogger(clazzName).isLoggable(level);
	}

	private static void logp(Level level, String message, Throwable t) {
		String clazzName = Thread.currentThread().getStackTrace()[3].getClassName();
		String methodName = Thread.currentThread().getStackTrace()[3].getMethodName();
		Logger.getLogger(clazzName).logp(level, clazzName, methodName, message, t);
	}

	public static boolean isFinestEnabled() {
		return Logger.isLoggable(Level.FINEST);
	}

	public static boolean isFinerEnabled() {
		return Logger.isLoggable(Level.FINER);
	}

	public static boolean isFineEnabled() {
		return Logger.isLoggable(Level.FINE);
	}

	public static boolean isConfigEnabled() {
		return Logger.isLoggable(Level.CONFIG);
	}

	public static boolean isInfoEnabled() {
		return Logger.isLoggable(Level.INFO);
	}

	public static boolean isWarningEnabled() {
		return Logger.isLoggable(Level.WARNING);
	}

	public static boolean isSevereEnabled() {
		return Logger.isLoggable(Level.SEVERE);
	}

	public static void finest(String message) {
		Logger.logp(Level.FINEST, message, null);
	}

	public static void finest(String message,  Throwable t) {
		Logger.logp(Level.FINEST, message, t);
	}

	public static void finer(String message) {
		Logger.logp(Level.FINER, message, null);
	}

	public static void finer(String message,  Throwable t) {
		Logger.logp(Level.FINER, message, t);
	}

	public static void fine(String message) {
		Logger.logp(Level.FINE, message, null);
	}

	public static void fine(String message,  Throwable t) {
		Logger.logp(Level.FINE, message, t);
	}

	public static void config(String message) {
		Logger.logp(Level.CONFIG, message, null);
	}

	public static void config(String message,  Throwable t) {
		Logger.logp(Level.CONFIG, message, t);
	}

	public static void info(String message) {
		Logger.logp(Level.INFO, message, null);
	}

	public static void info(String message, Throwable t) {
		Logger.logp(Level.INFO, message, t);
	}

	public static void warning(String message) {
		Logger.logp(Level.WARNING, message, null);
	}

	public static void warning(String message, Throwable t) {
		Logger.logp(Level.WARNING, message, t);
	}

	public static void severe(String message) {
		Logger.logp(Level.SEVERE, message, null);
	}

	public static void severe(String message, Throwable t) {
		Logger.logp(Level.SEVERE, message, t);
	}

}
