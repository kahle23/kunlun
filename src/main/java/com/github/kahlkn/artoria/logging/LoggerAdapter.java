package com.github.kahlkn.artoria.logging;

/**
 * Super logger adapter.
 * @author Kahle
 */
public interface LoggerAdapter {

    /**
     * Get logger
     * @param clazz The class name or other key
     * @return The logger
     */
    Logger getLogger(Class<?> clazz);

    /**
     * Get logger
     * @param clazz The class name or other key
     * @return The logger
     */
    Logger getLogger(String clazz);

    /**
     * Get log level
     * @return Current log level
     */
    Level getLevel();

    /**
     * Set log level (maybe is invalid)
     * @param level The log level
     */
    void setLevel(Level level);

}
