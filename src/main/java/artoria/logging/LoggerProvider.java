package artoria.logging;

/**
 * The logger provider.
 * @author Kahle
 */
public interface LoggerProvider {

    /**
     * Get logger.
     * @param clazz The class
     * @return The logger
     */
    Logger getLogger(Class<?> clazz);

    /**
     * Get logger.
     * @param name The class name or other name
     * @return The logger
     */
    Logger getLogger(String name);

    /**
     * Get logger level.
     * @return Current logger level
     */
    Level getLevel();

    /**
     * Set logger level.
     * @param level The logger level you want set
     */
    void setLevel(Level level);

}
