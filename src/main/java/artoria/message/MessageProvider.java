package artoria.message;

import artoria.lang.Code;

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
     * Sends a message(perhaps more than one) of the specified multiple types.
     * @param message The message to be sent
     * @param types The type or channel of message (abstract and custom)
     * @return The result of the sender invoke
     */
    Object send(Object message, Code<?>... types);

    /**
     * Batch sends multiple messages of the specified multiple types.
     * @param messages The list of messages to be sent
     * @param types The type or channel of message (abstract and custom)
     * @return The result of the sender invoke
     */
    Object batchSend(List<?> messages, Code<?>... types);

    /**
     * Query message based on entered criteria.
     * @param input The input query criteria
     * @param clazz The type of the return value
     * @param <T> The generic type of the return value
     * @return The queried message record or null
     */
    <T> T info(Object input, Class<T> clazz);

    /**
     * Search the list of messages based on entered criteria.
     * @param input The input query criteria
     * @param clazz The type of the return value
     * @param <T> The generic type of the return value
     * @return The queried message list or null
     */
    <T> List<T> search(Object input, Class<T> clazz);

}
