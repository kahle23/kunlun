package artoria.message;

import artoria.message.sender.MessageSender;

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
     * Register the message sender.
     * @param type The message parameters type
     * @param senderName The name of the message sender
     * @param messageSender The message sender
     */
    void registerSender(Class<?> type, String senderName, MessageSender messageSender);

    /**
     * Deregister the message sender.
     * @param type The message parameters type
     * @param senderName The name of the message sender
     */
    void deregisterSender(Class<?> type, String senderName);

    /**
     * Sends a message(perhaps more than one).
     * If you want to be asynchronous, the message object can contain "SuccessCallback" and "FailureCallback".
     * @param message The message to be sent
     * @param senderName The name of the message sender
     * @param clazz The type of the return value
     * @param <T> The generic type of the return value
     * @return The result of the sender invoke
     */
    <T> T send(Object message, String senderName, Class<T> clazz);

    /**
     * Batch sends multiple messages.
     * If you want to be asynchronous, the message object can contain "SuccessCallback" and "FailureCallback".
     * @param messages The list of messages to be sent
     * @param senderName The name of the message sender
     * @param clazz The type of the return value
     * @param <T> The generic type of the return value
     * @return The result of the sender invoke
     */
    <T> T batchSend(List<?> messages, String senderName, Class<T> clazz);

    /**
     * Query message based on entered criteria.
     * @param input The input query criteria
     * @param senderName The name of the message sender
     * @param clazz The type of the return value
     * @param <T> The generic type of the return value
     * @return The queried message record or null
     */
    <T> T info(Object input, String senderName, Class<T> clazz);

    /**
     * Search the list of messages based on entered criteria.
     * @param input The input query criteria
     * @param senderName The name of the message sender
     * @param clazz The type of the return value
     * @param <T> The generic type of the return value
     * @return The queried message list or null
     */
    <T> List<T> search(Object input, String senderName, Class<T> clazz);

}
