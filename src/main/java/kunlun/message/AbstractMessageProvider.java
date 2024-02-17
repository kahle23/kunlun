/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.message;

import kunlun.logging.Logger;
import kunlun.logging.LoggerFactory;
import kunlun.util.Assert;
import kunlun.util.MapUtils;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static kunlun.util.ObjectUtils.cast;

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

    protected MessageHandler getMessageHandlerInner(String handlerName) {
        Assert.notBlank(handlerName, "Parameter \"handlerName\" must not blank. ");
        MessageHandler messageHandler = messageHandlers.get(handlerName);
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
        return cast(getMessageHandlerInner(handlerName).send(message, type));
    }

    @Override
    public <T> T receive(Object condition, String handlerName, Type type) {
        Assert.notBlank(handlerName, "Parameter \"handlerName\" must not blank. ");
        Assert.notNull(condition, "Parameter \"condition\" must not null. ");
        Assert.notNull(type, "Parameter \"type\" must not null. ");
        return cast(getMessageHandlerInner(handlerName).receive(condition, type));
    }

    @Override
    public Object subscribe(String handlerName, Object condition, Object messageListener) {
        Assert.notBlank(handlerName, "Parameter \"handlerName\" must not blank. ");
        Assert.notNull(messageListener, "Parameter \"messageListener\" must not null. ");
        return getMessageHandlerInner(handlerName).subscribe(condition, messageListener);
    }

    @Override
    public Object operate(String handlerName, String operation, Object[] arguments) {
        Assert.notBlank(handlerName, "Parameter \"handlerName\" must not blank. ");
        Assert.notBlank(operation, "Parameter \"operation\" must not blank. ");
        return getMessageHandlerInner(handlerName).operate(operation, arguments);
    }

}
