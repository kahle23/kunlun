/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.message;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * The message provider. If the message fails to be sent and needs to be retried,
 *  retrying means invoking the send method again.
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
     * Send a message (perhaps more than one) (from: most message sending scenarios).
     * If you want to be asynchronous, the message object can contain "SuccessCallback" and "FailureCallback".
     * @param message The message to be sent
     * @param handlerName The name of the message handler
     * @param type The type of the return value
     * @param <T> The generic type of the return value
     * @return The result of the handler invoke
     */
    <T> T send(Object message, String handlerName, Type type);

    /**
     * Receive a message (from: amqp and delay message).
     * @param condition Maybe is topic or topic + other parameters
     * @param handlerName The name of the message handler
     * @param type The type of the return value
     * @param <T> The generic type of the return value
     * @return The result received
     */
    <T> T receive(Object condition, String handlerName, Type type);

    /**
     * Subscribe to a topic (from: message queue).
     * @param handlerName The name of the message handler
     * @param condition Maybe is topic or topic + "subExpression" or null
     * @param messageListener The message listener (maybe the condition is also contained in the listener)
     * @return The subscription result or null (most scenarios are null)
     */
    Object subscribe(String handlerName, Object condition, Object messageListener);

    /**
     * Perform an operation and return the corresponding result.
     * @param handlerName The name of the message handler
     * @param operation The name of the operation to be performed
     * @param arguments The arguments for the operation to be performed
     * @return The result of the operation
     */
    Object operate(String handlerName, String operation, Object[] arguments);

}
