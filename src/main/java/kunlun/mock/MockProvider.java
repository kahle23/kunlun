/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.mock;

import java.lang.reflect.Type;

/**
 * The mock provider.
 * @author Kahle
 */
public interface MockProvider {

    /**
     * Mock the data.
     * @param type The type of the mock data
     * @param arguments The arguments (maybe is configuration or feature)
     * @return The mock data
     */
    Object mock(Type type, Object... arguments);

}
