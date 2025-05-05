/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.data.property;

import kunlun.core.PropertyAccessor;

import java.util.Map;

/**
 * The property source.
 * @author Kahle
 */
public interface PropertySource extends PropertyAccessor {

    /**
     * Return all the properties.
     * @return The properties
     */
    Map<String, Object> getProperties();

    /**
     * Set property data in batches.
     * @param properties The property data to be set
     */
    void setProperties(Map<?, ?> properties);

}
