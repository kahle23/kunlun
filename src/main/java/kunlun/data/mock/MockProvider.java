/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.data.mock;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * The data mock provider.
 * @author Kahle
 */
public interface MockProvider {

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
     * Get the default generator name.
     * @return The default generator name
     */
    String getDefaultHandlerName();

    /**
     * Set the default generator name.
     * Depending on the implementation class, this method may throw an error
     *  (i.e. it does not allow the modification of the default generator name).
     * @param defaultHandlerName The default generator name
     */
    void setDefaultHandlerName(String defaultHandlerName);

    /**
     * Register the mock generator.
     * @param name The mock generator name
     * @param mockGenerator The mock generator
     */
    void registerGenerator(String name, MockGenerator mockGenerator);

    /**
     * Deregister the mock generator.
     * @param name The mock generator name
     */
    void deregisterGenerator(String name);

    /**
     * Get the mock generator by name.
     * @param name The mock generator name
     * @return The mock generator
     */
    MockGenerator getMockGenerator(String name);

    /**
     * Mock the data.
     * @param name The mock generator name
     * @param type The type of the mock data
     * @param arguments The arguments (maybe is configuration or feature)
     * @return The mock data
     */
    Object mock(String name, Type type, Object... arguments);

}
