/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.property;

import kunlun.core.handler.PropertySupportedHandler;

import java.util.Map;

/**
 * The property source.
 * @author Kahle
 */
public interface PropertySource extends PropertySupportedHandler {

    /**
     * Get the property source name.
     * @return The source name
     */
    String getName();

    /**
     * Get the common properties of the settings.
     * @return The common properties that is set
     */
    Map<Object, Object> getCommonProperties();

    /**
     * Set common properties for the property source.
     * @param properties The properties to be set
     */
    void setCommonProperties(Map<?, ?> properties);

    /**
     * Set property data in batches.
     * @param properties The property data to be set
     */
    void setProperties(Map<?, ?> properties);

    /**
     * Return all the properties.
     * @return The properties
     */
    Map<String, Object> getProperties();

}
