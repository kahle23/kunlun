package artoria.message.sender;

import artoria.lang.Code;

import java.util.List;
import java.util.Map;

/**
 * The message sender.
 * @author Kahle
 */
public interface MessageSender {

    /**
     * The type that the message sender can handle.
     * @return The type of the message sender
     */
    Code<?> getType();

    /**
     * Sends a message.
     * @param properties Some of the properties
     * @param message The message to be sent
     */
    void send(Map<?, ?> properties, Object message);

    /**
     * Batch sends multiple message.
     * @param properties Some of the properties
     * @param messages The list of messages to be sent
     */
    void batchSend(Map<?, ?> properties, List<?> messages);

}
