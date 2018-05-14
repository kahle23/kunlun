package com.github.kahlkn.artoria.logging;

/**
 * Super logger.
 * @author Kahle
 */
public interface Logger {

    /**
     * Logger trace
     * @param msg The log message
     */
    void trace(String msg);

    /**
     * Logger trace
     * @param e exception message
     */
    void trace(Throwable e);

    /**
     * Logger trace
     * @param msg The log message
     * @param e exception message
     */
    void trace(String msg, Throwable e);

    /**
     * Logger debug
     * @param msg The log message
     */
    void debug(String msg);

    /**
     * Logger debug
     * @param e exception message
     */
    void debug(Throwable e);

    /**
     * Logger debug
     * @param msg The log message
     * @param e exception message
     */
    void debug(String msg, Throwable e);

    /**
     * Logger information
     * @param msg The log message
     */
    void info(String msg);

    /**
     * Logger information
     * @param e The log message
     */
    void info(Throwable e);

    /**
     * Logger information
     * @param msg The log message
     * @param e exception message
     */
    void info(String msg, Throwable e);

    /**
     * Logger warning
     * @param msg The log message
     */
    void warn(String msg);

    /**
     * Logger warning
     * @param e exception message
     */
    void warn(Throwable e);

    /**
     * Logger warning
     * @param msg The log message
     * @param e exception message
     */
    void warn(String msg, Throwable e);

    /**
     * Logger error
     * @param msg The log message
     */
    void error(String msg);

    /**
     * Logger error
     * @param e exception message
     */
    void error(Throwable e);

    /**
     * Logger error
     * @param msg The log message
     * @param e exception message
     */
    void error(String msg, Throwable e);

    /**
     * Is trace enabled
     * @return Enable trace or not
     */
    boolean isTraceEnabled();

    /**
     * Is debug enabled
     * @return Enable debug or not
     */
    boolean isDebugEnabled();

    /**
     * Is info enabled
     * @return Enable info or not
     */
    boolean isInfoEnabled();

    /**
     * Is warn enabled
     * @return Enable warn or not
     */
    boolean isWarnEnabled();

    /**
     * Is error enabled
     * @return Enable error or not
     */
    boolean isErrorEnabled();

}
