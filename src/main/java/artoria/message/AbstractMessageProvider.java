package artoria.message;

import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import artoria.util.Assert;
import artoria.util.MapUtils;
import artoria.util.ObjectUtils;
import artoria.util.StringUtils;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The abstract message provider.
 * @author Kahle
 */
public abstract class AbstractMessageProvider implements MessageProvider {
    private static final Logger log = LoggerFactory.getLogger(AbstractMessageProvider.class);
    protected final Map<String, MessageHandler> messageHandlers;
    protected final Map<String, Object> commonProperties;

    protected AbstractMessageProvider(Map<String, Object> commonProperties,
                                      Map<String, MessageHandler> messageHandlers) {
        Assert.notNull(commonProperties, "Parameter \"commonProperties\" must not null. ");
        Assert.notNull(messageHandlers, "Parameter \"messageHandlers\" must not null. ");
        this.commonProperties = commonProperties;
        this.messageHandlers = messageHandlers;
    }

    public AbstractMessageProvider() {
        this(new ConcurrentHashMap<String, Object>(),
                new ConcurrentHashMap<String, MessageHandler>());
    }

    protected MessageHandler getMessageHandlerInner(String handlerName, Object input) {
        String name = null;
        // TODO: 2023/5/5 The handler name is required in the future, no longer handle "class:"
        if (StringUtils.isNotBlank(handlerName)) { name = handlerName; }
        else {
            // The input parameter should not be null.
            if (input != null) {
                if (input instanceof List) {
                    List<?> messages = (List<?>) input;
                    Class<?> prevType = null;
                    for (Object message : messages) {
                        Assert.notNull(message, "Parameter \"input\"'s elements all must not null. ");
                        Class<?> nowType = message.getClass();
                        if (prevType == null) { prevType = nowType; }
                        Assert.isTrue(nowType.equals(prevType)
                                , "Parameter \"input\"'s elements type all must be equal. ");
                    }
                    name = prevType != null ? "class:" + prevType.getName() : null;
                }
                else {
                    // Other scenarios concatenate class names directly.
                    name = "class:" + input.getClass().getName();
                }
            }
        }
        Assert.notBlank(name, "Parameter \"handlerName\" must not blank. ");
        MessageHandler messageHandler = messageHandlers.get(name);
        Assert.notNull(messageHandler,
            "The corresponding message handler could not be found by name. ");
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
    public void registerHandler(String handlerName, MessageHandler messageHandler) {
        Assert.notNull(messageHandler, "Parameter \"messageHandler\" must not null. ");
        Assert.notBlank(handlerName, "Parameter \"handlerName\" must not blank. ");
        String className = messageHandler.getClass().getName();
        log.info("Register the message handler \"{}\" to \"{}\". ", className, handlerName);
        messageHandlers.put(handlerName, messageHandler);
        messageHandler.setCommonProperties(getCommonProperties());
    }

    @Override
    public void deregisterHandler(String handlerName) {
        Assert.notBlank(handlerName, "Parameter \"handlerName\" must not blank. ");
        MessageHandler remove = messageHandlers.remove(handlerName);
        if (remove != null) {
            String className = remove.getClass().getName();
            log.info("Deregister the message handler \"{}\" from \"{}\". ", className, handlerName);
        }
    }

    @Override
    public MessageHandler getMessageHandler(String handlerName) {
        Assert.notBlank(handlerName, "Parameter \"handlerName\" must not blank. ");
        return messageHandlers.get(handlerName);
    }

    @Override
    public <T> T send(Object message, String handlerName, Type type) {
        Assert.notNull(message, "Parameter \"message\" must not null. ");
        Assert.notNull(type, "Parameter \"type\" must not null. ");
        MessageHandler handler = getMessageHandlerInner(handlerName, message);
        return ObjectUtils.cast(handler.send(message, type));
    }

    @Override
    public <T> T receive(Object condition, String handlerName, Type type) {
        Assert.notBlank(handlerName, "Parameter \"handlerName\" must not blank. ");
        Assert.notNull(condition, "Parameter \"condition\" must not null. ");
        Assert.notNull(type, "Parameter \"type\" must not null. ");
        MessageHandler handler = getMessageHandlerInner(handlerName, null);
        return ObjectUtils.cast(handler.receive(condition, type));
    }

    @Override
    public Object subscribe(String handlerName, Object condition, Object messageListener) {
        Assert.notBlank(handlerName, "Parameter \"handlerName\" must not blank. ");
        Assert.notNull(messageListener, "Parameter \"messageListener\" must not null. ");
        MessageHandler handler = getMessageHandlerInner(handlerName, null);
        return handler.subscribe(condition, messageListener);
    }

    @Override
    public Object operate(String handlerName, String operation, Object[] arguments) {
        Assert.notBlank(handlerName, "Parameter \"handlerName\" must not blank. ");
        Assert.notBlank(operation, "Parameter \"operation\" must not blank. ");
        MessageHandler handler = getMessageHandlerInner(handlerName, null);
        return handler.operate(operation, arguments);
    }

}
