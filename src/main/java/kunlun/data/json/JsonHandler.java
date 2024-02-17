/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.data.json;

import kunlun.core.Handler;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * The json conversion handler.
 * @author Kahle
 */
public interface JsonHandler extends Handler {

    /**
     * Get the common properties of the settings.
     * @return The common properties that is set
     */
    Map<Object, Object> getCommonProperties();

    /**
     * Set common properties for the json handler.
     * @param properties The properties to be set
     */
    void setCommonProperties(Map<?, ?> properties);

    /**
     * Determine whether the string is a json object.
     * @param jsonString The json string
     * @return Json object or not
     */
    boolean isJsonObject(String jsonString);

    /**
     * Determine whether the string is a json array.
     * @param jsonString The json string
     * @return Json array or not
     */
    boolean isJsonArray(String jsonString);

    /**
     * Serialize java object to json string.
     * @param object The java object
     * @param arguments The arguments (maybe is configuration or feature)
     * @return The json string
     */
    String toJsonString(Object object, Object... arguments);

    /**
     * Parse json string to java object.
     * @param jsonString The json string
     * @param type The java object type
     * @param arguments The arguments (maybe is configuration or feature)
     * @param <T> The java object type
     * @return The java object
     */
    <T> T parseObject(String jsonString, Type type, Object... arguments);

}
