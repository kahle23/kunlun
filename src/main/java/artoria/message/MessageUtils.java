package artoria.message;

import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import artoria.util.Assert;

import java.lang.reflect.Type;

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

    public static void deregisterHandler(String handlerName) {

        getMessageProvider().deregisterHandler(handlerName);
    }

    public static MessageHandler getMessageHandler(String handlerName) {

        return getMessageProvider().getMessageHandler(handlerName);
    }

    public static <T> T send(Object message, Type type) {

        return getMessageProvider().send(message, EMPTY_STRING, type);
    }

    public static <T> T send(Object message, String handlerName, Type type) {

        return getMessageProvider().send(message, handlerName, type);
    }

    public static <T> T operate(String operation, Object input, Type type) {

        return getMessageProvider().operate(operation, EMPTY_STRING, new Object[]{input, type});
    }

    public static <T> T operate(String operation, String handlerName, Object input, Type type) {

        return getMessageProvider().operate(operation, handlerName, new Object[]{input, type});
    }

    public static <T> T operate(String operation, Object[] arguments) {

        return getMessageProvider().operate(operation, EMPTY_STRING, arguments);
    }

    public static <T> T operate(String operation, String handlerName, Object[] arguments) {

        return getMessageProvider().operate(operation, handlerName, arguments);
    }

}
