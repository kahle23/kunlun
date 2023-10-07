package artoria.db;

import java.lang.reflect.Type;
import java.util.Map;

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
     * Register the ocr handler.
     * @param handlerName The ocr handler name
     * @param ocrHandler The ocr handler
     */
    void registerHandler(String handlerName, DbHandler ocrHandler);

    /**
     * Deregister the ocr handler.
     * @param handlerName The ocr handler name
     */
    void deregisterHandler(String handlerName);

    /**
     * Get the ocr handler by name.
     * @param handlerName The ocr handler name
     * @return The ocr handler
     */
    DbHandler getDbHandler(String handlerName);

    /**
     * Perform database related operations.
     * The arguments mean (most of the scenes):
     *      0 strategy or operation or null,
     *      1 input object,
     *      2 return value type
     * @param arguments The arguments required for the operation
     * @return The result of the operation
     */
    Object execute(String handlerName, Object[] arguments);

    <T> T execute(String handlerName, Object input, String operation, Type type);

}
