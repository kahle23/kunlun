package artoria.message.sender;

import artoria.lang.Code;

import java.util.List;
import java.util.Map;

/**
 * The message sender used to send and query messages.
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
     * @return The result of the invoke
     */
    Object send(Map<?, ?> properties, Object message);

    /**
     * Batch sends multiple message.
     * @param properties Some of the properties
     * @param messages The list of messages to be sent
     * @return The result of the invoke
     */
    Object batchSend(Map<?, ?> properties, List<?> messages);

    /**
     * Query message based on entered criteria.
     * @param properties Some of the properties
     * @param input The input query criteria
     * @param clazz The type of the return value
     * @param <T> The generic type of the return value
     * @return The queried message record or null
     */
    <T> T info(Map<?, ?> properties, Object input, Class<T> clazz);

    /**
     * Search the list of messages based on entered criteria.
     * @param properties Some of the properties
     * @param input The input query criteria
     * @param clazz The type of the return value
     * @param <T> The generic type of the return value
     * @return The queried message list or null
     */
    <T> List<T> search(Map<?, ?> properties, Object input, Class<T> clazz);

}
