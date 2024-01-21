package artoria.ai;

import artoria.core.ArtificialIntelligence;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * The artificial intelligence provider.
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
     * Register the ai handler.
     * @param handlerName The AI handler name
     * @param aiHandler The AI handler
     */
    void registerHandler(String handlerName, ArtificialIntelligence aiHandler);

    /**
     * Deregister the ai handler.
     * @param handlerName The AI handler name
     */
    void deregisterHandler(String handlerName);

    /**
     * Get the AI handler (the AI handler name is fixed).
     * @param handlerName The AI handler name
     * @return The AI handler
     */
    ArtificialIntelligence getHandler(String handlerName);

    /**
     * The artificial intelligence performs reasoning operations.
     * The arguments mean (most of the scenes):
     *      0 strategy or operation or null,
     *      1 input object,
     *      2 return value type
     * @param handlerName The AI handler name
     * @param arguments The arguments for inference calculations
     * @return The result of the inference calculation
     */
    Object execute(String handlerName, Object[] arguments);

    /**
     * The artificial intelligence performs reasoning operations.
     * @param handlerName The AI handler name
     * @param input The input parameters for inference calculations
     * @param operation The strategy or operation or null for AI handler execution
     * @param type The type of the return value
     * @param <T> The generic type of the return value
     * @return The result of the inference calculation
     */
    <T> T execute(String handlerName, Object input, String operation, Type type);

}
