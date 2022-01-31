package artoria.message;

import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import artoria.message.sender.MessageSender;
import artoria.util.Assert;

import java.util.List;

import static artoria.common.Constants.EMPTY_STRING;

/**
 * The message tools.
 * @author Kahle
 */
public class MessageUtils {
    private static Logger log = LoggerFactory.getLogger(MessageUtils.class);
    private static volatile MessageProvider messageProvider;

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

    public static void registerSender(Class<?> type, String senderName, MessageSender messageSender) {

        getMessageProvider().registerSender(type, senderName, messageSender);
    }

    public static void registerSender(Class<?> type, MessageSender messageSender) {

        getMessageProvider().registerSender(type, EMPTY_STRING, messageSender);
    }

    public static void deregisterSender(Class<?> type, String senderName) {

        getMessageProvider().deregisterSender(type, senderName);
    }

    public static void deregisterSender(Class<?> type) {

        getMessageProvider().deregisterSender(type, EMPTY_STRING);
    }

    public static <T> T send(Object message, String senderName, Class<T> clazz) {

        return getMessageProvider().send(message, senderName, clazz);
    }

    public static <T> T send(Object message, Class<T> clazz) {

        return getMessageProvider().send(message, EMPTY_STRING, clazz);
    }

    public static <T> T batchSend(List<?> messages, String senderName, Class<T> clazz) {

        return getMessageProvider().batchSend(messages, senderName, clazz);
    }

    public static <T> T batchSend(List<?> messages, Class<T> clazz) {

        return getMessageProvider().batchSend(messages, EMPTY_STRING, clazz);
    }

    public static <T> T info(Object input, String senderName, Class<T> clazz) {

        return getMessageProvider().info(input, senderName, clazz);
    }

    public static <T> T info(Object input, Class<T> clazz) {

        return getMessageProvider().info(input, EMPTY_STRING, clazz);
    }

    public static <T> List<T> search(Object input, String senderName, Class<T> clazz) {

        return getMessageProvider().search(input, senderName, clazz);
    }

    public static <T> List<T> search(Object input, Class<T> clazz) {

        return getMessageProvider().search(input, EMPTY_STRING, clazz);
    }

}
