package artoria.action;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * The action tools provider.
 * @author Kahle
 */
public interface ActionProvider {

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
     * Register the action handler.
     * @param actionName The action handler name or the name of the type of input parameter
     * @param actionHandler The action handler
     */
    void registerHandler(String actionName, ActionHandler actionHandler);

    /**
     * Deregister the action handler.
     * @param actionName The action handler name or the name of the type of input parameter
     */
    void deregisterHandler(String actionName);

    /**
     * Execute a specific logic.
     * The arguments mean (most of the scenes): 0 input object, 1 return value type
     * @param actionName The name of action
     * @param arguments The arguments to the execution of a specific logic
     * @return The execution result of a specific logic
     */
    Object execute(String actionName, Object[] arguments);

    /**
     * Execute a specific logic.
     * @param input The input parameters to be handled
     * @param actionName The name of action
     * @param type The type of the return value
     * @param <T> The generic type of the return value
     * @return The return value corresponding to the handler
     */
    <T> T execute(Object input, String actionName, Type type);

}
