/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.logging;

/**
 * Provide the highest level of abstraction for Logger.
 * @author Kahle
 */
public interface Logger {

    /**
     * The logger trace.
     * @param format The format string
     * @param arguments The arguments
     */
    void trace(String format, Object... arguments);

    /**
     * The logger trace.
     * @param message The log message
     * @param throwable Exception object
     */
    void trace(String message, Throwable throwable);

    /**
     * The logger debug.
     * @param format The format string
     * @param arguments The arguments
     */
    void debug(String format, Object... arguments);

    /**
     * The logger debug.
     * @param message The log message
     * @param throwable Exception object
     */
    void debug(String message, Throwable throwable);

    /**
     * The logger information.
     * @param format The format string
     * @param arguments The arguments
     */
    void info(String format, Object... arguments);

    /**
     * The logger information.
     * @param message The log message
     * @param throwable Exception object
     */
    void info(String message, Throwable throwable);

    /**
     * The logger warning.
     * @param format The format string
     * @param arguments The arguments
     */
    void warn(String format, Object... arguments);

    /**
     * The logger warning.
     * @param message The log message
     * @param throwable Exception object
     */
    void warn(String message, Throwable throwable);

    /**
     * The logger error.
     * @param format The format string
     * @param arguments The arguments
     */
    void error(String format, Object... arguments);

    /**
     * The logger error.
     * @param message The log message
     * @param throwable Exception object
     */
    void error(String message, Throwable throwable);

    /**
     * Is trace enabled.
     * @return Enable trace or not
     */
    boolean isTraceEnabled();

    /**
     * Is debug enabled.
     * @return Enable debug or not
     */
    boolean isDebugEnabled();

    /**
     * Is info enabled.
     * @return Enable info or not
     */
    boolean isInfoEnabled();

    /**
     * Is warn enabled.
     * @return Enable warn or not
     */
    boolean isWarnEnabled();

    /**
     * Is error enabled.
     * @return Enable error or not
     */
    boolean isErrorEnabled();

}
