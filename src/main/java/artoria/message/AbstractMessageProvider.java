package artoria.message;

import artoria.lang.Code;
import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import artoria.message.sender.ConsoleSender;
import artoria.message.sender.LogSender;
import artoria.message.sender.MessageSender;
import artoria.util.Assert;
import artoria.util.MapUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static artoria.message.MessageType.UNKNOWN;

/**
 * The abstract message send provider.
 * @author Kahle
 */
public abstract class AbstractMessageProvider implements MessageProvider {
    private static Logger log = LoggerFactory.getLogger(AbstractMessageProvider.class);
    private final Map<String, MessageSender> messageSenderMap;
    private final Map<String, Object> commonProperties;

    public AbstractMessageProvider() {
        messageSenderMap = new ConcurrentHashMap<String, MessageSender>();
        commonProperties = new ConcurrentHashMap<String, Object>();
        registerSender(new LogSender(MessageType.UNKNOWN));
        registerSender(new ConsoleSender());
        registerSender(new LogSender());
    }

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

    public void clearCommonProperties() {

        this.commonProperties.clear();
    }

    public Map<String, Object> getCommonProperties() {

        return Collections.unmodifiableMap(commonProperties);
    }

    public void registerSender(MessageSender messageSender) {
        Assert.notNull(messageSender, "Parameter \"messageSender\" must not null. ");
        Assert.notNull(messageSender.getType(),
                "Property \"type\" of parameter \"messageSender\" must not null. ");
        String nowType = String.valueOf(messageSender.getType().getCode());
        String className = messageSender.getClass().getName();
        log.info("Register sender \"{}\" to \"{}\". ", className, nowType);
        messageSenderMap.put(nowType, messageSender);
    }

    public void deregisterSender(Code<?>... types) {
        Assert.notEmpty(types, "Parameter \"types\" must not empty. ");
        String unknownType = String.valueOf(UNKNOWN.getCode());
        for (Code<?> type : types) {
            if (type == null) { continue; }
            String nowType = String.valueOf(type.getCode());
            if (unknownType.equals(nowType)) { continue;}
            log.info("Deregister the sender of type \"{}\". ", nowType);
            messageSenderMap.remove(nowType);
        }
    }

    public MessageSender getMessageSender(Code<?> type) {
        Assert.notNull(type, "Parameter \"type\" must not null. ");
        String nowType = String.valueOf(type.getCode());
        MessageSender messageSender = messageSenderMap.get(nowType);
        if (messageSender == null) {
            String unknownType = String.valueOf(UNKNOWN.getCode());
            messageSender = messageSenderMap.get(unknownType);
        }
        Assert.notNull(messageSender,
                "The corresponding message sender could not be found by type. ");
        return messageSender;
    }

    @Override
    public void send(Object message, Code<?>... types) {
        Assert.notNull(message, "Parameter \"message\" must not null. ");
        Assert.notEmpty(types, "Parameter \"types\" must not empty. ");
        if (message instanceof List) { batchSend((List<?>) message, types); return; }
        for (Code<?> type : types) {
            if (type == null) { continue; }
            MessageSender messageSender = getMessageSender(type);
            messageSender.send(getCommonProperties(), message);
        }
    }

    @Override
    public void batchSend(List<?> messages, Code<?>... types) {
        Assert.notEmpty(messages, "Parameter \"messages\" must not null. ");
        Assert.notEmpty(types, "Parameter \"types\" must not empty. ");
        for (Code<?> type : types) {
            if (type == null) { continue; }
            MessageSender messageSender = getMessageSender(type);
            messageSender.batchSend(getCommonProperties(), messages);
        }
    }

}
