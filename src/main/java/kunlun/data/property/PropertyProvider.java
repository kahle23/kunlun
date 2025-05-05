/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.data.property;

import java.util.Map;
import java.util.Set;

/**
 * The property provider.
 * @author Kahle
 */
public interface PropertyProvider {

    // region ======== source manage ========

    /**
     * Get the default source name.
     * @return The default source name
     */
    String getDefaultSourceName();

    /**
     * Set the default source name.
     * @param defaultSourceName The default source name
     */
    void setDefaultSourceName(String defaultSourceName);

    /**
     * Register the property source.
     * @param sourceName The property source name
     * @param propertySource The property source
     */
    void registerSource(String sourceName, PropertySource propertySource);

    /**
     * Deregister the property source.
     * @param sourceName The property source name
     */
    void deregisterSource(String sourceName);

    /**
     * Get the property source.
     * @param sourceName The property source name
     * @return The property source
     */
    PropertySource getPropertySource(String sourceName);

    // endregion


    // region ======== source methods ========

    /**
     * Return whether the given property name is available at the specified source.
     * @param source The property source name
     * @param name The given property name
     * @return The available or not
     */
    boolean containsProperty(String source, String name);

    /**
     * Return the property value associated with the given name.
     * @param name The property name
     * @return The property value
     */
    Object getProperty(String source, String name);

    /**
     * Set the property name and property value at the specified source.
     * @param source The property source name
     * @param name The property name
     * @param value The property value
     * @return The previous value associated with name, or null
     */
    Object setProperty(String source, String name, Object value);

    /**
     * Remove the property value based on the property name at the specified source.
     * @param source The property source name
     * @param name The property name
     * @return The property value or null
     */
    Object removeProperty(String source, String name);

    /**
     * Return the set of all property names in this property accessor.
     * @param source The property source name
     * @return The set of all property names in this property accessor
     */
    Set<String> getPropertyNames(String source);

    /**
     * Return all the properties at the specified source.
     * @param source The property source name
     * @return The properties
     */
    Map<String, Object> getProperties(String source);

    /**
     * Set property data in batches at the specified source.
     * @param source The property source name
     * @param properties The property data to be set
     */
    void setProperties(String source, Map<?, ?> properties);

    // endregion


    // region ======== extension methods ========

    /**
     * Return the property value associated with the given name, converted to
     * the given target type (never null).
     * @param source The property source name
     * @param name The given property name
     * @param targetType The given target type
     * @return The property value
     */
    <T> T getRequiredProperty(String source, String name, Class<T> targetType);

    /**
     * Return the property value associated with the given name, or default value.
     * @param source The property source name
     * @param name The given property name
     * @param targetType The given target type
     * @param defaultValue The default value
     * @return The property value
     */
    <T> T getProperty(String source, String name, Class<T> targetType, T defaultValue);

    /**
     * Return the property value associated with the given name at the specified source.
     * @param source The property source name
     * @param name The property name
     * @param defaultValue The default value
     * @return The property value
     */
    Object getProperty(String source, String name, Object defaultValue);

    // endregion

}
