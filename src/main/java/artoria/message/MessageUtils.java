package artoria.message;

import artoria.lang.Code;
import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import artoria.util.Assert;

import java.util.List;

/**
 * The message send tools.
 * @author Kahle
 */
public class MessageUtils {
    private static Logger log = LoggerFactory.getLogger(MessageUtils.class);
    private static MessageProvider messageProvider;

    public static MessageProvider getMessageProvider() {
        if (messageProvider != null) { return messageProvider; }
        synchronized (MessageUtils.class) {
            if (messageProvider != null) { return messageProvider; }
            MessageUtils.setMessageProvider(new SimpleMessageProvider());
            return messageProvider;
        }
    }

    public static void setMessageProvider(MessageProvider messageProvider) {
        Assert.notNull(messageProvider, "Parameter \"messageProvider\" must not null. ");
        log.info("Set message provider: {}", messageProvider.getClass().getName());
        MessageUtils.messageProvider = messageProvider;
    }

    public static void send(Object message, Code<?>... types) {

        getMessageProvider().send(message, types);
    }

    public static void sendAsync(Object message, Code<?>... types) {

        getMessageProvider().sendAsync(message, types);
    }

    public static void batchSend(List<?> messages, Code<?>... types) {

        getMessageProvider().batchSend(messages, types);
    }

    public static void batchSendAsync(List<?> messages, Code<?>... types) {

        getMessageProvider().batchSendAsync(messages, types);
    }

}
