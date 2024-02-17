/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.core.handler;

import kunlun.core.Handler;

/**
 * The property supported handler.
 * @author Kahle
 */
public interface PropertySupportHandler extends Handler {

    /**
     * Return whether the given property name is available.
     * @param name The given property name
     * @return The available or not
     */
    boolean containsProperty(String name);

    /**
     * Set the property name and property value.
     * @param name The property name
     * @param value The property value
     * @return The previous value associated with name, or null
     */
    Object setProperty(String name, Object value);

    /**
     * Return the property value associated with the given name.
     * @param name The property name
     * @return The property value
     */
    Object getProperty(String name);

    /**
     * Remove the property value based on the property name.
     * @param name The property name
     * @return The property value or null
     */
    Object removeProperty(String name);

}
