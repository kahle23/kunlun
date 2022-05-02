package artoria.action;

import artoria.lang.Handler;

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
     * The arguments mean (most of the scenes): 0 input object, 1 return value type
     * @param arguments The arguments to the execution of a specific logic
     * @return The execution result of a specific logic
     */
    Object execute(Object[] arguments);

}
