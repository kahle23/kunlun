package artoria.message;

import artoria.core.handler.OperatingSupportHandler;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * The message handler used to send and query messages.
 * @author Kahle
 */
public interface MessageHandler extends OperatingSupportHandler {

    /**
     * Get the common properties of the settings.
     * @return The common properties that is set
     */
    Map<Object, Object> getCommonProperties();

    /**
     * Set common properties for the message handler.
     * @param properties The properties to be set
     */
    void setCommonProperties(Map<?, ?> properties);

    /**
     * Send a message (from: most message sending scenarios).
     * @param message The message to be sent
     * @param type The type of the return value
     * @return The result of operation
     */
    Object send(Object message, Type type);

    /**
     * Receive a message (from: amqp and delay message).
     * @param condition Maybe is topic or topic + other parameters
     * @param type The type of the return value
     * @return The result received
     */
    Object receive(Object condition, Type type);

    /**
     * Subscribe to a topic (from: message queue).
     * @param condition Maybe is topic or topic + "subExpression" or null
     * @param messageListener The message listener (maybe the condition is also contained in the listener)
     * @return The subscription result or null (most scenarios are null)
     */
    Object subscribe(Object condition, Object messageListener);

}
