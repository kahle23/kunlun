package artoria.message.handler;

import artoria.lang.handler.OperatingSupportHandler;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * The message handler used to send and query messages.
 * @author Kahle
 */
public interface MessageHandler extends OperatingSupportHandler {

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
     * Sends a message.
     * @param message The message to be sent
     * @param type The type of the return value
     * @return The result of the invoked
     */
    Object send(Object message, Type type);

}
