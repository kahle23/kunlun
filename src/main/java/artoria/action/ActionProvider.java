package artoria.action;

import artoria.core.Router;
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
     * @param actionName The action handler name
     * @param actionHandler The action handler
     */
    void registerHandler(String actionName, ActionHandler actionHandler);

    /**
     * Deregister the action handler.
     * @param actionName The action handler name
     */
    void deregisterHandler(String actionName);

    /**
     * Get the action handler (the action name is fixed).
     * @param actionName The action handler name
     * @return The action handler
     */
    ActionHandler getActionHandler(String actionName);

    /**
     * Set the action router.
     * @param router The action router
     */
    void setRouter(Router router);

    /**
     * Get the action router.
     * The action name is fixed, but where to get the action name is uncertain, so need a router.
     * @return The action router
     */
    Router getRouter();

    /**
     * Execute a specific logic.
     * The arguments mean (most of the scenes):
     *      0 strategy or operation or null,
     *      1 input object,
     *      2 return value type
     * @param actionName The name of action
     * @param arguments The arguments to the execution of a specific logic
     * @return The execution result of a specific logic
     */
    Object execute(String actionName, Object[] arguments);

    /**
     * Execute a specific logic.
     * @param input The input parameters to be handled
     * @param actionName The name of action
     * @param operation The name of the operation to be performed (strategy or operation or null)
     * @param type The type of the return value
     * @param <T> The generic type of the return value
     * @return The return value corresponding to the handler
     */
    <T> T execute(Object input, String actionName, String operation, Type type);

}
