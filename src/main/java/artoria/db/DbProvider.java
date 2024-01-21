package artoria.db;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * The database tools provider.
 * @author Kahle
 */
public interface DbProvider {

    /**
     * Register common properties' information.
     * @param commonProperties The common properties
     */
    void registerCommonProperties(Map<?, ?> commonProperties);

    /**
     * Clears common properties' information.
     */
    void clearCommonProperties();

    /**
     * Get common properties' information.
     * @return The common properties
     */
    Map<String, Object> getCommonProperties();

    /**
     * Register the database handler.
     * @param handlerName The database handler name
     * @param dbHandler The database handler
     */
    void registerHandler(String handlerName, DbHandler dbHandler);

    /**
     * Deregister the database handler.
     * @param handlerName The database handler name
     */
    void deregisterHandler(String handlerName);

    /**
     * Get the database handler by name.
     * @param handlerName The database handler name
     * @return The database handler
     */
    DbHandler getDbHandler(String handlerName);

    /**
     * Perform database related operations.
     * The arguments mean (most of the scenes):
     *      0 strategy or operation or null,
     *      1 input object,
     *      2 return value type
     * @param handlerName The database handler name
     * @param arguments The arguments required for the operation
     * @return The result of the operation
     */
    Object execute(String handlerName, Object[] arguments);

    /**
     * Perform database related operations.
     * @param handlerName The database handler name
     * @param input The input when the database operation is executed
     * @param operation The name of the database operation to be performed
     * @param type The type of the return value
     * @return The execution result of the database operation
     */
    <T> T execute(String handlerName, Object input, String operation, Type type);

}
