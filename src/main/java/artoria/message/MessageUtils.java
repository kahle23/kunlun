package artoria.message;

import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import artoria.util.Assert;

import java.lang.reflect.Type;

import static artoria.util.ObjectUtils.cast;

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

    public static <T> T send(Object message, String handlerName, Type type) {

        return getMessageProvider().send(message, handlerName, type);
    }

    public static <T> T receive(Object condition, String handlerName, Type type) {

        return getMessageProvider().receive(condition, handlerName, type);
    }

    public static Object subscribe(String handlerName, Object condition, Object messageListener) {

        return getMessageProvider().subscribe(handlerName, condition, messageListener);
    }

    public static <T> T operate(String handlerName, String operation, Object input, Type type) {

        return cast(operate(handlerName, operation, new Object[]{input, type}));
    }

    public static Object operate(String handlerName, String operation, Object[] arguments) {

        return getMessageProvider().operate(handlerName, operation, arguments);
    }

}
