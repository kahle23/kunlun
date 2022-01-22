package artoria.message;

import artoria.beans.BeanUtils;
import artoria.lang.Code;
import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import artoria.message.sender.ConsoleSender;
import artoria.message.sender.LogSender;
import artoria.message.sender.MessageSender;
import artoria.util.Assert;
import artoria.util.MapUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static artoria.common.Constants.ONE;
import static artoria.common.Constants.ZERO;
import static artoria.message.MessageType.UNKNOWN;

/**
 * The abstract message provider.
 * @author Kahle
 */
public abstract class AbstractMessageProvider implements MessageProvider {
    private static Logger log = LoggerFactory.getLogger(AbstractMessageProvider.class);
    protected final Map<String, MessageSender> messageSenders;
    protected final Map<String, Object> commonProperties;

    protected AbstractMessageProvider(Map<String, Object> commonProperties,
                                      Map<String, MessageSender> messageSenders) {
        Assert.notNull(commonProperties, "Parameter \"commonProperties\" must not null. ");
        Assert.notNull(messageSenders, "Parameter \"messageSenders\" must not null. ");
        this.commonProperties = commonProperties;
        this.messageSenders = messageSenders;
    }

    protected String getMessageType(Object input) {
        if (input == null) { return null; }
        Map map = input instanceof Map
                ? (Map) input : BeanUtils.createBeanMap(input);
        Object messageType = map.get("messageType");
        return messageType != null ? String.valueOf(messageType) : null;
    }

    public AbstractMessageProvider() {
        this(new ConcurrentHashMap<String, Object>(),
                new ConcurrentHashMap<String, MessageSender>());
        registerSender(new LogSender(MessageType.UNKNOWN));
        registerSender(new ConsoleSender());
        registerSender(new LogSender());
    }

    public void registerSender(MessageSender messageSender) {
        Assert.notNull(messageSender, "Parameter \"messageSender\" must not null. ");
        Assert.notNull(messageSender.getType(),
                "Property \"type\" of parameter \"messageSender\" must not null. ");
        String msgType = String.valueOf(messageSender.getType().getCode());
        String className = messageSender.getClass().getName();
        log.info("Register the sender \"{}\" to \"{}\". ", className, msgType);
        messageSenders.put(msgType, messageSender);
    }

    public void deregisterSender(Code<?>... types) {
        Assert.notEmpty(types, "Parameter \"types\" must not empty. ");
        String unknownType = String.valueOf(UNKNOWN.getCode());
        for (Code<?> type : types) {
            if (type == null) { continue; }
            String msgType = String.valueOf(type.getCode());
            if (unknownType.equals(msgType)) { continue;}
            MessageSender remove = messageSenders.remove(msgType);
            if (remove != null) {
                String className = remove.getClass().getName();
                log.info("Deregister the sender \"{}\" from \"{}\". ", className, msgType);
            }
        }
    }

    public MessageSender getMessageSender(String identifier) {
        Assert.notBlank(identifier, "Parameter \"identifier\" must not blank. ");
        MessageSender messageSender = messageSenders.get(identifier);
        if (messageSender == null) {
            String unknownType = String.valueOf(UNKNOWN.getCode());
            messageSender = messageSenders.get(unknownType);
        }
        Assert.notNull(messageSender,
            "The corresponding message sender could not be found by input identifier. ");
        return messageSender;
    }

    @Override
    public void registerCommonProperties(Map<?, ?> commonProperties) {
        if (MapUtils.isEmpty(commonProperties)) { return; }
        for (Map.Entry<?, ?> entry : commonProperties.entrySet()) {
            Object entryValue = entry.getValue();
            Object entryKey = entry.getKey();
            if (entryKey == null) { continue; }
            String keyStr = String.valueOf(entryKey);
            this.commonProperties.put(keyStr, entryValue);
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
    public Object send(Object message, Code<?>... types) {
        Assert.notNull(message, "Parameter \"message\" must not null. ");
        Assert.notEmpty(types, "Parameter \"types\" must not empty. ");
        if (message instanceof List) { return batchSend((List<?>) message, types); }
        Map<String, Object> properties = getCommonProperties();
        List<Object> result = new ArrayList<Object>();
        for (Code<?> type : types) {
            Assert.notNull(type, "Parameter \"type\" must not null. ");
            String msgType = String.valueOf(type.getCode());
            Object send = getMessageSender(msgType).send(properties, message);
            result.add(send);
        }
        return result.size() == ONE ? result.get(ZERO) : result;
    }

    @Override
    public Object batchSend(List<?> messages, Code<?>... types) {
        Assert.notEmpty(messages, "Parameter \"messages\" must not null. ");
        Assert.notEmpty(types, "Parameter \"types\" must not empty. ");
        Map<String, Object> properties = getCommonProperties();
        List<Object> result = new ArrayList<Object>();
        for (Code<?> type : types) {
            Assert.notNull(type, "Parameter \"type\" must not null. ");
            String msgType = String.valueOf(type.getCode());
            Object batchSend = getMessageSender(msgType).batchSend(properties, messages);
            result.add(batchSend);
        }
        return result.size() == ONE ? result.get(ZERO) : result;
    }

    @Override
    public <T> T info(Object input, Class<T> clazz) {
        Assert.notNull(input, "Parameter \"input\" must not null. ");
        Assert.notNull(clazz, "Parameter \"clazz\" must not null. ");
        String msgType = getMessageType(input);
        Assert.notBlank(msgType, "Parameter \"input\"'s \"messageType\" must not blank. ");
        Map<String, Object> properties = getCommonProperties();
        return getMessageSender(msgType).info(properties, input, clazz);
    }

    @Override
    public <T> List<T> search(Object input, Class<T> clazz) {
        Assert.notNull(input, "Parameter \"input\" must not null. ");
        Assert.notNull(clazz, "Parameter \"clazz\" must not null. ");
        String msgType = getMessageType(input);
        Assert.notBlank(msgType, "Parameter \"input\"'s \"messageType\" must not blank. ");
        Map<String, Object> properties = getCommonProperties();
        return getMessageSender(msgType).search(properties, input, clazz);
    }

}
