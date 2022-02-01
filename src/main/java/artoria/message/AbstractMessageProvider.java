package artoria.message;

import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import artoria.message.handler.MessageHandler;
import artoria.util.Assert;
import artoria.util.MapUtils;
import artoria.util.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static artoria.common.Constants.EMPTY_STRING;

/**
 * The abstract message provider.
 * @author Kahle
 */
public abstract class AbstractMessageProvider implements MessageProvider {
    private static Logger log = LoggerFactory.getLogger(AbstractMessageProvider.class);
    protected final Map<String, MessageHandler> messageHandlers;
    protected final Map<String, Object> commonProperties;

    protected AbstractMessageProvider(Map<String, Object> commonProperties,
                                      Map<String, MessageHandler> messageHandlers) {
        Assert.notNull(commonProperties, "Parameter \"commonProperties\" must not null. ");
        Assert.notNull(messageHandlers, "Parameter \"messageHandlers\" must not null. ");
        this.commonProperties = commonProperties;
        this.messageHandlers = messageHandlers;
    }

    protected String getIdentifier(Class<?> type, String handlerName) {
        if (StringUtils.isBlank(handlerName)) { handlerName = EMPTY_STRING; }
        return String.format("%s:%s", type.getName(), handlerName);
    }

    public AbstractMessageProvider() {
        this(new ConcurrentHashMap<String, Object>(),
                new ConcurrentHashMap<String, MessageHandler>());
    }

    public MessageHandler getMessageHandler(String identifier) {
        Assert.notBlank(identifier, "Parameter \"identifier\" must not blank. ");
        MessageHandler messageHandler = messageHandlers.get(identifier);
        Assert.notNull(messageHandler,
            "The corresponding message handler could not be found by input identifier. ");
        return messageHandler;
    }

    @Override
    public void registerCommonProperties(Map<?, ?> commonProperties) {
        if (MapUtils.isEmpty(commonProperties)) { return; }
        for (Map.Entry<?, ?> entry : commonProperties.entrySet()) {
            String keyStr = String.valueOf(entry.getKey());
            this.commonProperties.put(keyStr, entry.getValue());
        }
    }

    @Override
    public void clearCommonProperties() {

        this.commonProperties.clear();
    }

    @Override
    public Map<String, Object> getCommonProperties() {

        return Collections.unmodifiableMap(commonProperties);
    }

    @Override
    public void registerHandler(Class<?> type, String handlerName, MessageHandler messageHandler) {
        Assert.notNull(messageHandler, "Parameter \"messageHandler\" must not null. ");
        Assert.notNull(type, "Parameter \"type\" must not null. ");
        String className = messageHandler.getClass().getName();
        String identifier = getIdentifier(type, handlerName);
        log.info("Register the handler \"{}\" to \"{}\". ", className, identifier);
        messageHandlers.put(identifier, messageHandler);
    }

    @Override
    public void deregisterHandler(Class<?> type, String handlerName) {
        Assert.notNull(type, "Parameter \"type\" must not null. ");
        String identifier = getIdentifier(type, handlerName);
        MessageHandler remove = messageHandlers.remove(identifier);
        if (remove != null) {
            String className = remove.getClass().getName();
            log.info("Deregister the handler \"{}\" from \"{}\". ", className, identifier);
        }
    }

    @Override
    public <T> T send(Object message, String handlerName, Class<T> clazz) {
        Assert.notNull(message, "Parameter \"message\" must not null. ");
        Assert.notNull(clazz, "Parameter \"clazz\" must not null. ");
        if (message instanceof List) {
            return batchSend((List<?>) message, handlerName, clazz);
        }
        Map<String, Object> properties = getCommonProperties();
        String identifier = getIdentifier(message.getClass(), handlerName);
        MessageHandler messageHandler = getMessageHandler(identifier);
        return messageHandler.send(properties, message, clazz);
    }

    @Override
    public <T> T batchSend(List<?> messages, String handlerName, Class<T> clazz) {
        Assert.notEmpty(messages, "Parameter \"messages\" must not null. ");
        Assert.notNull(clazz, "Parameter \"clazz\" must not null. ");
        Class<?> prevType = null;
        for (Object message : messages) {
            Assert.notNull(message, "Parameter \"messages\"'s elements all must not null. ");
            Class<?> type = message.getClass();
            if (prevType == null) { prevType = type; }
            Assert.isTrue(type.equals(prevType)
                    , "Parameter \"messages\"'s elements type all must be equal. ");
        }
        Map<String, Object> properties = getCommonProperties();
        String identifier = getIdentifier(prevType, handlerName);
        MessageHandler messageHandler = getMessageHandler(identifier);
        return messageHandler.batchSend(properties, messages, clazz);
    }

    @Override
    public <T> T info(Object input, String handlerName, Class<T> clazz) {
        Assert.notNull(input, "Parameter \"input\" must not null. ");
        Assert.notNull(clazz, "Parameter \"clazz\" must not null. ");
        Map<String, Object> properties = getCommonProperties();
        String identifier = getIdentifier(input.getClass(), handlerName);
        MessageHandler messageHandler = getMessageHandler(identifier);
        return messageHandler.info(properties, input, clazz);
    }

    @Override
    public <T> List<T> search(Object input, String handlerName, Class<T> clazz) {
        Assert.notNull(input, "Parameter \"input\" must not null. ");
        Assert.notNull(clazz, "Parameter \"clazz\" must not null. ");
        Map<String, Object> properties = getCommonProperties();
        String identifier = getIdentifier(input.getClass(), handlerName);
        MessageHandler messageHandler = getMessageHandler(identifier);
        return messageHandler.search(properties, input, clazz);
    }

}
