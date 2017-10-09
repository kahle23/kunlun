package saber;

import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class LogUtils {

	public static void debug(String message) {
		logp(Level.FINE, message, null);
	}

	public static void debug(String message,  Throwable t) {
		logp(Level.FINE, message, t);
	}

	public static void info(String message) {
		logp(Level.INFO, message, null);
	}

	public static void info(String message, Throwable t) {
		logp(Level.INFO, message, t);
	}

	public static void warn(String message) {
		logp(Level.WARNING, message, null);
	}

	public static void warn(String message, Throwable t) {
		logp(Level.WARNING, message, t);
	}

	public static void error(String message) {
		logp(Level.SEVERE, message, null);
	}

	public static void error(String message, Throwable t) {
		logp(Level.SEVERE, message, t);
	}

	public static void fatal(String message) {
		logp(Level.SEVERE, message, null);
	}

	public static void fatal(String message, Throwable t) {
		logp(Level.SEVERE, message, t);
	}

	public static boolean isDebugEnabled() {
		return isLoggable(Level.FINE);
	}

	public static boolean isInfoEnabled() {
		return isLoggable(Level.INFO);
	}

	public static boolean isWarnEnabled() {
		return isLoggable(Level.WARNING);
	}

	public static boolean isErrorEnabled() {
		return isLoggable(Level.SEVERE);
	}

	public static boolean isFatalEnabled() {
		return isLoggable(Level.SEVERE);
	}

	private static void logp(Level level, String message, Throwable t) {
		String clazzName = Thread.currentThread().getStackTrace()[3].getClassName();
		String methodName = Thread.currentThread().getStackTrace()[3].getMethodName();
		getLogger(clazzName).logp(level, clazzName, methodName, message, t);
	}

	private static boolean isLoggable(Level level) {
		String clazzName = Thread.currentThread().getStackTrace()[3].getClassName();
		return getLogger(clazzName).isLoggable(level);
	}

	private static Logger getLogger(String clazzName) {
		Logger log = LOG_MANAGER.getLogger(clazzName);
		if(log == null) {
			log = Logger.getLogger(clazzName);
			LOG_MANAGER.addLogger(log);
		}
		return log;
	}

	private static final LogManager LOG_MANAGER = LogManager.getLogManager();

}
