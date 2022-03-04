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

    Object execute(Object[] arguments);

}
