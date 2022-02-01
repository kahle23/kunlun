package artoria.message.handler;

import artoria.lang.Handler;

import java.util.List;
import java.util.Map;

/**
 * The message handler used to send and query messages.
 * @author Kahle
 */
public interface MessageHandler extends Handler {

    /**
     *
     * @param listener
     */
    void registerListener(Object listener);

    /**
     * Sends a message.
     * @param properties Some of the properties
     * @param message The message to be sent
     * @param clazz The type of the return value
     * @return The result of the invoke
     */
    <T> T send(Map<?, ?> properties, Object message, Class<T> clazz);

    /**
     * Batch sends multiple message.
     * @param properties Some of the properties
     * @param messages The list of messages to be sent
     * @param clazz The type of the return value
     * @return The result of the invoke
     */
    <T> T batchSend(Map<?, ?> properties, List<?> messages, Class<T> clazz);

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
