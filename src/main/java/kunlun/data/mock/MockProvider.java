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
     * Get the default handler name.
     * @return The default handler name
     */
    String getDefaultHandlerName();

    /**
     * Set the default handler name.
     * Depending on the implementation class, this method may throw an error
     *  (i.e. it does not allow the modification of the default handler name).
     * @param defaultHandlerName The default handler name
     */
    void setDefaultHandlerName(String defaultHandlerName);

    /**
     * Register the mock handler.
     * @param name The mock handler name
     * @param mockHandler The mock handler
     */
    void registerHandler(String name, MockHandler mockHandler);

    /**
     * Deregister the mock handler.
     * @param name The mock handler name
     */
    void deregisterHandler(String name);

    /**
     * Get the mock handler by name.
     * @param name The mock handler name
     * @return The mock handler
     */
    MockHandler getMockHandler(String name);

    /**
     * Mock the data.
     * @param name The mock handler name
     * @param type The type of the mock data
     * @param arguments The arguments (maybe is configuration or feature)
     * @return The mock data
     */
    Object mock(String name, Type type, Object... arguments);

}
