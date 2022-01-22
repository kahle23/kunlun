package artoria.message;

import artoria.lang.Code;
import artoria.lang.callback.FailureCallback;
import artoria.lang.callback.SuccessCallback;

import java.util.List;

/**
 * The asynchronous message provider.
 * @author Kahle
 */
public interface AsyncMessageProvider extends MessageProvider {

    /**
     * Sends a message(perhaps more than one) of the specified multiple types asynchronously.
     * @param message The message to be sent
     * @param types The type or channel of message (abstract and custom)
     * @param success The callback object when successfully invoked
     * @param failure The callback object when unsuccessfully invoked
     */
    void sendAsync(Object message, SuccessCallback<?> success, FailureCallback failure, Code<?>... types);

    /**
     * Batch sends multiple messages of the specified multiple types asynchronously.
     * @param messages The list of messages to be sent
     * @param types The type or channel of message (abstract and custom)
     * @param success The callback object when successfully invoked
     * @param failure The callback object when unsuccessfully invoked
     */
    void batchSendAsync(List<?> messages, SuccessCallback<?> success, FailureCallback failure, Code<?>... types);

}
