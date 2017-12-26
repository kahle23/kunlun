package com.apyhs.artoria.logging;

import com.apyhs.artoria.util.DateUtils;
import com.apyhs.artoria.util.StringUtils;
import com.apyhs.artoria.util.ThrowableUtils;

import static com.apyhs.artoria.util.StringConstant.EMPTY_STRING;
import static com.apyhs.artoria.util.StringConstant.ENDL;

/**
 * Failsafe logger
 * @author Kahle
 */
public class FailsafeLogger implements Logger {

	private static final String LOGGER_ERROR_MESSAGE = "The logger component has a error. ";
	private static final int STACK_TRACE_INDEX = 3;

	private Logger logger;

	public FailsafeLogger(Logger logger) {
		this.logger = logger;
	}

	public Logger getLogger() {
		return logger;
	}

	public void setLogger(Logger logger) {
		this.logger = logger;
	}

	private void logp(Level level, String message, Throwable t) {
		Thread thread = Thread.currentThread();
		StackTraceElement[] stackTrace = thread.getStackTrace();
		System.err.println(">> [" + DateUtils.format() + "] [" + level + "] ["
				+ thread.getName() + "] ["
				+ stackTrace[STACK_TRACE_INDEX].getClassName() + "."
				+ stackTrace[STACK_TRACE_INDEX].getMethodName() + "()]"
				+ (StringUtils.isNotBlank(message) ? " [" + message + "]" : EMPTY_STRING)
				+ (t != null ? " [" + ENDL + ThrowableUtils.toString(t) + "]" : EMPTY_STRING)
		);
	}

	@Override
	public void trace(String msg) {
		try {
			logger.trace(msg);
		}
		catch (Throwable t) {
			this.logp(Level.ERROR, LOGGER_ERROR_MESSAGE, t);
			this.logp(Level.TRACE, msg, null);
		}
	}

	@Override
	public void trace(Throwable e) {
		try {
			logger.trace(e);
		}
		catch (Throwable t) {
			this.logp(Level.ERROR, LOGGER_ERROR_MESSAGE, t);
			this.logp(Level.TRACE, e.getMessage(), e);
		}
	}

	@Override
	public void trace(String msg, Throwable e) {
		try {
			logger.trace(msg, e);
		}
		catch (Throwable t) {
			this.logp(Level.ERROR, LOGGER_ERROR_MESSAGE, t);
			this.logp(Level.TRACE, msg, e);
		}
	}

	@Override
	public void debug(String msg) {
		try {
			logger.debug(msg);
		}
		catch (Throwable t) {
			this.logp(Level.ERROR, LOGGER_ERROR_MESSAGE, t);
			this.logp(Level.DEBUG, msg, null);
		}
	}

	@Override
	public void debug(Throwable e) {
		try {
			logger.debug(e);
		}
		catch (Throwable t) {
			this.logp(Level.ERROR, LOGGER_ERROR_MESSAGE, t);
			this.logp(Level.DEBUG, e.getMessage(), e);
		}
	}

	@Override
	public void debug(String msg, Throwable e) {
		try {
			logger.debug(msg, e);
		}
		catch (Throwable t) {
			this.logp(Level.ERROR, LOGGER_ERROR_MESSAGE, t);
			this.logp(Level.DEBUG, msg, e);
		}
	}

	@Override
	public void info(String msg) {
		try {
			logger.info(msg);
		}
		catch (Throwable t) {
			this.logp(Level.ERROR, LOGGER_ERROR_MESSAGE, t);
			this.logp(Level.INFO, msg, null);
		}
	}

	@Override
	public void info(Throwable e) {
		try {
			logger.info(e);
		}
		catch (Throwable t) {
			this.logp(Level.ERROR, LOGGER_ERROR_MESSAGE, t);
			this.logp(Level.INFO, e.getMessage(), e);
		}
	}

	@Override
	public void info(String msg, Throwable e) {
		try {
			logger.info(msg, e);
		}
		catch (Throwable t) {
			this.logp(Level.ERROR, LOGGER_ERROR_MESSAGE, t);
			this.logp(Level.INFO, msg, e);
		}
	}

	@Override
	public void warn(String msg) {
		try {
			logger.warn(msg);
		}
		catch (Throwable t) {
			this.logp(Level.ERROR, LOGGER_ERROR_MESSAGE, t);
			this.logp(Level.WARN, msg, null);
		}
	}

	@Override
	public void warn(Throwable e) {
		try {
			logger.warn(e);
		}
		catch (Throwable t) {
			this.logp(Level.ERROR, LOGGER_ERROR_MESSAGE, t);
			this.logp(Level.WARN, e.getMessage(), e);
		}
	}

	@Override
	public void warn(String msg, Throwable e) {
		try {
			logger.warn(msg, e);
		}
		catch (Throwable t) {
			this.logp(Level.ERROR, LOGGER_ERROR_MESSAGE, t);
			this.logp(Level.WARN, msg, e);
		}
	}

	@Override
	public void error(String msg) {
		try {
			logger.error(msg);
		}
		catch (Throwable t) {
			this.logp(Level.ERROR, LOGGER_ERROR_MESSAGE, t);
			this.logp(Level.ERROR, msg, null);
		}
	}

	@Override
	public void error(Throwable e) {
		try {
			logger.error(e);
		}
		catch (Throwable t) {
			this.logp(Level.ERROR, LOGGER_ERROR_MESSAGE, t);
			this.logp(Level.ERROR, e.getMessage(), e);
		}
	}

	@Override
	public void error(String msg, Throwable e) {
		try {
			logger.error(msg, e);
		}
		catch (Throwable t) {
			this.logp(Level.ERROR, LOGGER_ERROR_MESSAGE, t);
			this.logp(Level.ERROR, msg, e);
		}
	}

	@Override
    public boolean isTraceEnabled() {
		try {
			return logger.isTraceEnabled();
		}
		catch (Throwable t) {
			this.logp(Level.ERROR, LOGGER_ERROR_MESSAGE, t);
			return false;
		}
    }

	@Override
	public boolean isDebugEnabled() {
		try {
			return logger.isDebugEnabled();
		}
		catch (Throwable t) {
			this.logp(Level.ERROR, LOGGER_ERROR_MESSAGE, t);
			return false;
		}
	}

	@Override
	public boolean isInfoEnabled() {
		try {
			return logger.isInfoEnabled();
		}
		catch (Throwable t) {
			this.logp(Level.ERROR, LOGGER_ERROR_MESSAGE, t);
			return false;
		}
	}

	@Override
	public boolean isWarnEnabled() {
		try {
			return logger.isWarnEnabled();
		}
		catch (Throwable t) {
			this.logp(Level.ERROR, LOGGER_ERROR_MESSAGE, t);
			return false;
		}
	}

	@Override
	public boolean isErrorEnabled() {
		try {
			return logger.isErrorEnabled();
		}
		catch (Throwable t) {
			this.logp(Level.ERROR, LOGGER_ERROR_MESSAGE, t);
			return false;
		}
	}

}
