package artoria.message;

import artoria.lang.Router;
import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import artoria.message.handler.MessageHandler;
import artoria.util.ArrayUtils;
import artoria.util.Assert;
import artoria.util.MapUtils;
import artoria.util.ObjectUtils;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static artoria.common.Constants.*;

/**
 * The abstract message provider.
 * @author Kahle
 */
public abstract class AbstractMessageProvider implements MessageProvider {
    private static final Logger log = LoggerFactory.getLogger(AbstractMessageProvider.class);
    protected final Map<String, MessageHandler> messageHandlers;
    protected final Map<String, Object> commonProperties;
    private Router router;

    protected AbstractMessageProvider(Map<String, Object> commonProperties,
                                      Map<String, MessageHandler> messageHandlers) {
        Assert.notNull(commonProperties, "Parameter \"commonProperties\" must not null. ");
        Assert.notNull(messageHandlers, "Parameter \"messageHandlers\" must not null. ");
        this.commonProperties = commonProperties;
        this.messageHandlers = messageHandlers;
        setRouter(new SimpleMessageRouter());
    }

    public AbstractMessageProvider() {
        this(new ConcurrentHashMap<String, Object>(),
                new ConcurrentHashMap<String, MessageHandler>());
    }

    protected MessageHandler getMessageHandlerInner(Object... arguments) {
        Assert.notNull(arguments, "Parameter \"arguments\" must not null. ");
        // Parameter "arguments" usually is:
        // 0 handler name, 1 operation name, 2 input or message, 3 type
        Object routeObj = getRouter().route(arguments);
        String route = routeObj != null ? String.valueOf(routeObj) : null;
        Assert.notBlank(route,
                "The route calculated according to the arguments is blank. ");
        MessageHandler messageHandler = messageHandlers.get(route);
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
        log.info("Register the handler \"{}\" to \"{}\". ", className, handlerName);
        messageHandlers.put(handlerName, messageHandler);
        messageHandler.attrs(getCommonProperties());
    }

    @Override
    public void deregisterHandler(String handlerName) {
        Assert.notBlank(handlerName, "Parameter \"handlerName\" must not blank. ");
        MessageHandler remove = messageHandlers.remove(handlerName);
        if (remove != null) {
            String className = remove.getClass().getName();
            log.info("Deregister the handler \"{}\" from \"{}\". ", className, handlerName);
        }
    }

    @Override
    public MessageHandler getMessageHandler(String handlerName) {
        Assert.notBlank(handlerName, "Parameter \"handlerName\" must not blank. ");
        return messageHandlers.get(handlerName);
    }

    @Override
    public void setRouter(Router router) {
        Assert.notNull(router, "Parameter \"router\" must not null. ");
        this.router = router;
    }

    @Override
    public Router getRouter() {

        return router;
    }

    @Override
    public <T> T send(Object message, String handlerName, Type type) {
        Assert.notNull(message, "Parameter \"message\" must not null. ");
        Assert.notNull(type, "Parameter \"type\" must not null. ");
        MessageHandler handler = getMessageHandlerInner(handlerName, null, message, type);
        return ObjectUtils.cast(handler.send(message, type));
    }

    @Override
    public <T> T operate(String operation, String handlerName, Object[] arguments) {
        Assert.notBlank(operation, "Parameter \"operation\" must not blank. ");
        // Parameter "arguments" usually is: 0 input or message, 1 type
        Object[] newArgs;
        if (ArrayUtils.isNotEmpty(arguments)) {
            newArgs = new Object[arguments.length + TWO];
            newArgs[ZERO] = handlerName;
            newArgs[ONE] = operation;
            System.arraycopy(arguments, ZERO, newArgs, TWO, arguments.length);
        }
        else { newArgs = new Object[]{handlerName, operation, null, null}; }
        MessageHandler handler = getMessageHandlerInner(newArgs);
        return ObjectUtils.cast(handler.operate(operation, arguments));
    }

}
