/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.data.mock;

import kunlun.core.Handler;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * The data mock handler.
 * @author Kahle
 */
public interface MockHandler extends Handler {

    /**
     * Get the common properties of the settings.
     * @return The common properties that is set
     */
    Map<Object, Object> getCommonProperties();

    /**
     * Set common properties for the mock handler.
     * @param properties The properties to be set
     */
    void setCommonProperties(Map<?, ?> properties);

    /**
     * Mock the data.
     * @param type The type of the mock data
     * @param arguments The arguments (maybe is configuration or feature)
     * @return The mock data
     */
    Object mock(Type type, Object... arguments);

}
