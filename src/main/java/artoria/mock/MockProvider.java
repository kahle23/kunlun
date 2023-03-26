package artoria.mock;

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
