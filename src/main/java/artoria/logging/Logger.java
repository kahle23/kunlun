package artoria.logging;

/**
 * Provide the highest level of abstraction for Logger.
 * @author Kahle
 */
public interface Logger {

    /**
     * Logger trace.
     * @param message The log message
     */
    void trace(String message);

    /**
     * Logger trace.
     * @param t Exception object
     */
    void trace(Throwable t);

    /**
     * Logger trace.
     * @param message The log message
     * @param t Exception object
     */
    void trace(String message, Throwable t);

    /**
     * Logger debug.
     * @param message The log message
     */
    void debug(String message);

    /**
     * Logger debug.
     * @param t Exception object
     */
    void debug(Throwable t);

    /**
     * Logger debug.
     * @param message The log message
     * @param t Exception object
     */
    void debug(String message, Throwable t);

    /**
     * Logger information.
     * @param message The log message
     */
    void info(String message);

    /**
     * Logger information.
     * @param t Exception object
     */
    void info(Throwable t);

    /**
     * Logger information.
     * @param message The log message
     * @param t Exception object
     */
    void info(String message, Throwable t);

    /**
     * Logger warning.
     * @param message The log message
     */
    void warn(String message);

    /**
     * Logger warning.
     * @param t Exception object
     */
    void warn(Throwable t);

    /**
     * Logger warning.
     * @param message The log message
     * @param t Exception object
     */
    void warn(String message, Throwable t);

    /**
     * Logger error.
     * @param message The log message
     */
    void error(String message);

    /**
     * Logger error.
     * @param t Exception object
     */
    void error(Throwable t);

    /**
     * Logger error.
     * @param message The log message
     * @param t Exception object
     */
    void error(String message, Throwable t);

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
