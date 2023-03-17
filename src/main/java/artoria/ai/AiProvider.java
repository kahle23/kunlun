package artoria.ai;

import artoria.core.ArtificialIntelligence;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * The ai engine provider.
 * @author Kahle
 */
public interface AiProvider {

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
     * Register the ai engine.
     * @param engineName The ai engine name
     * @param aiEngine The ai engine
     */
    void registerEngine(String engineName, ArtificialIntelligence aiEngine);

    /**
     * Deregister the ai engine.
     * @param engineName The ai engine name
     */
    void deregisterEngine(String engineName);

    /**
     * Get the ai engine (the ai engine name is fixed).
     * @param engineName The ai engine name
     * @return The ai engine
     */
    ArtificialIntelligence getEngine(String engineName);

    /**
     * The artificial intelligence performs reasoning operations.
     * The arguments mean (most of the scenes):
     *      0 strategy or scene or null,
     *      1 input object,
     *      2 return value type
     * @param engineName The ai engine name
     * @param arguments The arguments for inference calculations
     * @return The result of the inference calculation
     */
    Object execute(String engineName, Object[] arguments);

    /**
     * The artificial intelligence performs reasoning operations.
     * @param input The input parameters for inference calculations
     * @param engineName The ai engine name
     * @param strategy The strategy or scene or null for ai engine execution
     * @param type The type of the return value
     * @param <T> The generic type of the return value
     * @return The result of the inference calculation
     */
    <T> T execute(Object input, String engineName, String strategy, Type type);

}
