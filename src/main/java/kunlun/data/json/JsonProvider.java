/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.data.json;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * The json conversion provider.
 * (If you want to use provider directly, you must know which processors are registered.)
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
     * Get the default processor name.
     * @return The default processor name
     */
    String getDefaultProcessorName();

    /**
     * Set the default processor name.
     * Depending on the implementation class, this method may throw an error
     *  (i.e. it does not allow the modification of the default processor name).
     * @param defaultProcessorName The default processor name
     */
    void setDefaultProcessorName(String defaultProcessorName);

    /**
     * Register the json processor.
     * @param name The json processor name
     * @param jsonProcessor The json processor
     */
    void registerProcessor(String name, JsonProcessor jsonProcessor);

    /**
     * Deregister the json processor.
     * @param name The json processor name
     */
    void deregisterProcessor(String name);

    /**
     * Get the json processor by name.
     * @param name The json processor name
     * @return The json processor
     */
    JsonProcessor getJsonProcessor(String name);

    /**
     * Determine whether the string is a json object.
     * @param name The json processor name
     * @param jsonString The json string
     * @return Json object or not
     */
    boolean isJsonObject(String name, String jsonString);

    /**
     * Determine whether the string is a json array.
     * @param name The json processor name
     * @param jsonString The json string
     * @return Json array or not
     */
    boolean isJsonArray(String name, String jsonString);

    /**
     * Serialize java object to json string.
     * @param name The json processor name
     * @param object The java object
     * @param arguments The arguments (maybe is configuration or feature)
     * @return The json string
     */
    String toJsonString(String name, Object object, Object... arguments);

    /**
     * Parse json string to java object.
     * @param name The json processor name
     * @param jsonString The json string
     * @param type The java object type
     * @param arguments The arguments (maybe is configuration or feature)
     * @param <T> The java object type
     * @return The java object
     */
    <T> T parseObject(String name, String jsonString, Type type, Object... arguments);

}
