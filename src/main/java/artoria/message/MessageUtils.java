package artoria.message;

import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import artoria.message.handler.MessageHandler;
import artoria.util.Assert;

import java.util.List;

import static artoria.common.Constants.EMPTY_STRING;

/**
 * The message tools.
 * @author Kahle
 */
public class MessageUtils {
    private static final Logger log = LoggerFactory.getLogger(MessageUtils.class);
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

    public static void registerHandler(String handlerName, MessageHandler messageHandler) {

        getMessageProvider().registerHandler(handlerName, messageHandler);
    }

    public static void registerHandler(Class<?> type, MessageHandler messageHandler) {
        Assert.notNull(type, "Parameter \"type\" must not null. ");
        getMessageProvider().registerHandler(type.getName(), messageHandler);
    }

    public static void deregisterHandler(String handlerName) {

        getMessageProvider().deregisterHandler(handlerName);
    }

    public static void deregisterHandler(Class<?> type) {
        Assert.notNull(type, "Parameter \"type\" must not null. ");
        getMessageProvider().deregisterHandler(type.getName());
    }

    public static <T> T send(Object message, String handlerName, Class<T> clazz) {

        return getMessageProvider().send(message, handlerName, clazz);
    }

    public static <T> T send(Object message, Class<T> clazz) {

        return getMessageProvider().send(message, EMPTY_STRING, clazz);
    }

    public static <T> T batchSend(List<?> messages, String handlerName, Class<T> clazz) {

        return getMessageProvider().batchSend(messages, handlerName, clazz);
    }

    public static <T> T batchSend(List<?> messages, Class<T> clazz) {

        return getMessageProvider().batchSend(messages, EMPTY_STRING, clazz);
    }

    public static <T> T info(Object input, String handlerName, Class<T> clazz) {

        return getMessageProvider().info(input, handlerName, clazz);
    }

    public static <T> T info(Object input, Class<T> clazz) {

        return getMessageProvider().info(input, EMPTY_STRING, clazz);
    }

    public static <T> List<T> search(Object input, String handlerName, Class<T> clazz) {

        return getMessageProvider().search(input, handlerName, clazz);
    }

    public static <T> List<T> search(Object input, Class<T> clazz) {

        return getMessageProvider().search(input, EMPTY_STRING, clazz);
    }

}
