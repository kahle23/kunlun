/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.data.xml;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * The xml conversion provider.
 * @author Kahle
 */
public interface XmlProvider {

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
     * @param xmlProcessor The json processor
     */
    void registerProcessor(String name, XmlProcessor xmlProcessor);

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
    XmlProcessor getXmlProcessor(String name);

    /**
     * Serialize java object to xml string.
     * @param name The xml processor name
     * @param object The java object
     * @param arguments The arguments (maybe is configuration or feature)
     * @return The xml string
     */
    String toXmlString(String name, Object object, Object... arguments);

    /**
     * Parse xml string to java object.
     * @param name The xml processor name
     * @param xmlString The xml string
     * @param type The java object type
     * @param arguments The arguments (maybe is configuration or feature)
     * @param <T> The java object type
     * @return The java object
     */
    <T> T parseObject(String name, String xmlString, Type type, Object... arguments);

}
