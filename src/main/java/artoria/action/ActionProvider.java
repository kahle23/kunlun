package artoria.action;

import artoria.action.handler.ActionHandler;

import java.util.List;
import java.util.Map;

/**
 * The action tools provider.
 * @author Kahle
 */
public interface ActionProvider {

    /**
     * Register common properties information.
     * @param commonProperties The common properties
     */
    void registerCommonProperties(Map<?, ?> commonProperties);

    /**
     * Clears common properties information.
     */
    void clearCommonProperties();

    /**
     * Get common properties information.
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
     * Do the execute action.
     * @param input The input parameters to be handled
     * @param actionName The name of action
     * @param clazz The class of the return value
     * @param <T> The generic type of the return value
     * @return The return value corresponding to the handler
     */
    <T> T execute(Object input, String actionName, Class<T> clazz);

    /**
     * Do the information query action.
     * @param input The input parameters to be handled
     * @param actionName The name of action
     * @param clazz The class of the return value
     * @param <T> The generic type of the return value
     * @return The return value corresponding to the handler
     */
    <T> T info(Object input, String actionName, Class<T> clazz);

    /**
     * Do the information search action.
     * @param input The input parameters to be handled
     * @param actionName The name of action
     * @param clazz The class of the return value
     * @param <T> The generic type of the return value
     * @return The return value corresponding to the handler
     */
    <T> List<T> search(Object input, String actionName, Class<T> clazz);

}
