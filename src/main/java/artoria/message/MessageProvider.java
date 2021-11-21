package artoria.message;

import artoria.lang.Code;

import java.util.List;

/**
 * The message send provider.
 * @author Kahle
 */
public interface MessageProvider {

    /**
     * Sends a message(perhaps more than one) of the specified multiple types.
     * @param message The message to be sent
     * @param types The type or channel of message (abstract and custom)
     */
    void send(Object message, Code<?>... types);

    /**
     * Sends a message(perhaps more than one) of the specified multiple types asynchronously.
     * @param message The message to be sent
     * @param types The type or channel of message (abstract and custom)
     */
    void sendAsync(Object message, Code<?>... types);

    /**
     * Batch sends multiple messages of the specified multiple types.
     * @param messages The list of messages to be sent
     * @param types The type or channel of message (abstract and custom)
     */
    void batchSend(List<?> messages, Code<?>... types);

    /**
     * Batch sends multiple messages of the specified multiple types asynchronously.
     * @param messages The list of messages to be sent
     * @param types The type or channel of message (abstract and custom)
     */
    void batchSendAsync(List<?> messages, Code<?>... types);

}
