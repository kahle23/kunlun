package artoria.message;

import artoria.message.handler.MessageHandler;

import java.util.List;
import java.util.Map;

/**
 * The message provider. If the message fails to be sent and needs to be retried,
 * retrying means invoking the send method again.
 * @author Kahle
 */
public interface MessageProvider {

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
     * Register the message handler.
     * @param handlerName The message handler name or the name of the type of input parameter
     * @param messageHandler The message handler
     */
    void registerHandler(String handlerName, MessageHandler messageHandler);

    /**
     * Deregister the message handler.
     * @param handlerName The message handler name or the name of the type of input parameter
     */
    void deregisterHandler(String handlerName);

    /**
     * Sends a message(perhaps more than one).
     * If you want to be asynchronous, the message object can contain "SuccessCallback" and "FailureCallback".
     * @param message The message to be sent
     * @param handlerName The name of the message handler
     * @param clazz The type of the return value
     * @param <T> The generic type of the return value
     * @return The result of the handler invoke
     */
    <T> T send(Object message, String handlerName, Class<T> clazz);

    /**
     * Batch sends multiple messages.
     * If you want to be asynchronous, the message object can contain "SuccessCallback" and "FailureCallback".
     * @param messages The list of messages to be sent
     * @param handlerName The name of the message handler
     * @param clazz The type of the return value
     * @param <T> The generic type of the return value
     * @return The result of the handler invoke
     */
    <T> T batchSend(List<?> messages, String handlerName, Class<T> clazz);

    /**
     * Query message based on entered criteria.
     * @param input The input query criteria
     * @param handlerName The name of the message handler
     * @param clazz The type of the return value
     * @param <T> The generic type of the return value
     * @return The queried message record or null
     */
    <T> T info(Object input, String handlerName, Class<T> clazz);

    /**
     * Search the list of messages based on entered criteria.
     * @param input The input query criteria
     * @param handlerName The name of the message handler
     * @param clazz The type of the return value
     * @param <T> The generic type of the return value
     * @return The queried message list or null
     */
    <T> List<T> search(Object input, String handlerName, Class<T> clazz);

}
