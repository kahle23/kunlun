package artoria.message;

import artoria.lang.Code;
import artoria.lang.callback.FailureCallback;
import artoria.lang.callback.SuccessCallback;
import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import artoria.util.Assert;

import java.util.List;

/**
 * The message tools.
 * @author Kahle
 */
public class MessageUtils {
    private static Logger log = LoggerFactory.getLogger(MessageUtils.class);
    private static volatile AsyncMessageProvider messageProvider;

    public static AsyncMessageProvider getMessageProvider() {
        if (messageProvider != null) { return messageProvider; }
        synchronized (MessageUtils.class) {
            if (messageProvider != null) { return messageProvider; }
            MessageUtils.setMessageProvider(new SimpleMessageProvider());
            return messageProvider;
        }
    }

    public static void setMessageProvider(AsyncMessageProvider messageProvider) {
        Assert.notNull(messageProvider, "Parameter \"messageProvider\" must not null. ");
        log.info("Set message provider: {}", messageProvider.getClass().getName());
        MessageUtils.messageProvider = messageProvider;
    }

    public static Object send(Object message, Code<?>... types) {

        return getMessageProvider().send(message, types);
    }

    public static Object batchSend(List<?> messages, Code<?>... types) {

        return getMessageProvider().batchSend(messages, types);
    }

    public static <T> T info(Object input, Class<T> clazz) {

        return getMessageProvider().info(input, clazz);
    }

    public static <T> List<T> search(Object input, Class<T> clazz) {

        return getMessageProvider().search(input, clazz);
    }

    public static void sendAsync(Object message, SuccessCallback<?> fine, FailureCallback fail, Code<?>... types) {

        getMessageProvider().sendAsync(message, fine, fail, types);
    }

    public static void batchSendAsync(List<?> list, SuccessCallback<?> fine, FailureCallback fail, Code<?>... types) {

        getMessageProvider().batchSendAsync(list, fine, fail, types);
    }

}
