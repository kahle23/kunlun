package artoria.data.json;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * The json conversion provider.
 * (If you want to use provider directly, you must know which handlers are registered.)
 * @author Kahle
 */
public interface JsonProvider {

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
     * Register the json handler.
     * @param name The json handler name
     * @param jsonHandler The json handler
     */
    void registerHandler(String name, JsonHandler jsonHandler);

    /**
     * Deregister the json handler.
     * @param name The json handler name
     */
    void deregisterHandler(String name);

    /**
     * Get the json handler by name.
     * @param name The json handler name
     * @return The json handler
     */
    JsonHandler getJsonHandler(String name);

    /**
     * Determine whether the string is a json object.
     * @param name The json handler name
     * @param jsonString The json string
     * @return Json object or not
     */
    boolean isJsonObject(String name, String jsonString);

    /**
     * Determine whether the string is a json array.
     * @param name The json handler name
     * @param jsonString The json string
     * @return Json array or not
     */
    boolean isJsonArray(String name, String jsonString);

    /**
     * Serialize java object to json string.
     * @param name The json handler name
     * @param object The java object
     * @param arguments The arguments (maybe is configuration or feature)
     * @return The json string
     */
    String toJsonString(String name, Object object, Object... arguments);

    /**
     * Parse json string to java object.
     * @param name The json handler name
     * @param jsonString The json string
     * @param type The java object type
     * @param arguments The arguments (maybe is configuration or feature)
     * @param <T> The java object type
     * @return The java object
     */
    <T> T parseObject(String name, String jsonString, Type type, Object... arguments);

}
