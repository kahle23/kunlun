/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.data.xml;

import kunlun.core.Handler;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * The xml conversion handler.
 * @author Kahle
 */
public interface XmlHandler extends Handler {

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
     * Serialize java object to xml string.
     * @param object The java object
     * @param arguments The arguments (maybe is configuration or feature)
     * @return The xml string
     */
    String toXmlString(Object object, Object... arguments);

    /**
     * Parse xml string to java object.
     * @param xmlString The xml string
     * @param type The java object type
     * @param arguments The arguments (maybe is configuration or feature)
     * @param <T> The java object type
     * @return The java object
     */
    <T> T parseObject(String xmlString, Type type, Object... arguments);

}
