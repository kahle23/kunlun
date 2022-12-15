package artoria.action;

import artoria.core.Handler;

import java.util.Map;

/**
 * The action handler.
 * @author Kahle
 */
public interface ActionHandler extends Handler {

    /**
     * Set attributes for the handler.
     * @param attrs The attributes to be set
     */
    void attrs(Map<?, ?> attrs);

    /**
     * Get the attributes of the settings.
     * @return The attributes that is set
     */
    Map<Object, Object> attrs();

    /**
     * Execute a specific logic.
     * The arguments mean (most of the scenes):
     *      0 strategy or operation or null,
     *      1 input object,
     *      2 return value type
     * @param arguments The arguments to the execution of a specific logic
     * @return The execution result of a specific logic
     */
    Object execute(Object[] arguments);

}
