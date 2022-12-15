package artoria.message;

import artoria.core.Router;
import artoria.message.handler.MessageHandler;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * The message provider. If the message fails to be sent and needs to be retried,
 * retrying means invoking the send method again.
 * @author Kahle
 */
public interface MessageProvider {

    /**
     * Register common properties' information.
     * @param commonProperties The common properties
     */
    void registerCommonProperties(Map<?, ?> commonProperties);

    /**
     * Clears common properties' information.
     */
    void clearCommonProperties();

    /**
     * Get common properties' information.
     * @return The common properties
     */
    Map<String, Object> getCommonProperties();

    /**
     * Register the message handler.
     * @param handlerName The message handler name
     * @param messageHandler The message handler
     */
    void registerHandler(String handlerName, MessageHandler messageHandler);

    /**
     * Deregister the message handler.
     * @param handlerName The message handler name
     */
    void deregisterHandler(String handlerName);

    /**
     * Get the message handler.
     * @param handlerName The message handler name
     * @return The message handler
     */
    MessageHandler getMessageHandler(String handlerName);

    /**
     * Set the message router.
     * @param router The message router
     */
    void setRouter(Router router);

    /**
     * Get the message router.
     * @return The message router
     */
    Router getRouter();

    /**
     * Sends a message(perhaps more than one).
     * If you want to be asynchronous, the message object can contain "SuccessCallback" and "FailureCallback".
     * @param message The message to be sent
     * @param handlerName The name of the message handler
     * @param type The type of the return value
     * @param <T> The generic type of the return value
     * @return The result of the handler invoke
     */
    <T> T send(Object message, String handlerName, Type type);

    /**
     * Perform an operation and return the corresponding result.
     * @param operation The name of the operation to be performed
     * @param handlerName The name of the message handler
     * @param arguments The arguments for the operation to be performed
     * @return The result of the operation
     */
    <T> T operate(String operation, String handlerName, Object[] arguments);

}
