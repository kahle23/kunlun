package artoria.message;

import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import artoria.message.sender.MessageSender;
import artoria.util.Assert;
import artoria.util.MapUtils;
import artoria.util.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static artoria.common.Constants.EMPTY_STRING;

/**
 * The class based message provider.
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

    protected String getIdentifier(Class<?> type, String senderName) {
        if (StringUtils.isBlank(senderName)) { senderName = EMPTY_STRING; }
        return String.format("%s:%s", type.getName(), senderName);
    }

    public AbstractMessageProvider() {
        this(new ConcurrentHashMap<String, Object>(),
                new ConcurrentHashMap<String, MessageSender>());
    }

    public MessageSender getMessageSender(String identifier) {
        Assert.notBlank(identifier, "Parameter \"identifier\" must not blank. ");
        MessageSender messageSender = messageSenders.get(identifier);
        Assert.notNull(messageSender,
            "The corresponding message sender could not be found by input identifier. ");
        return messageSender;
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
    public void registerSender(Class<?> type, String senderName, MessageSender messageSender) {
        Assert.notNull(messageSender, "Parameter \"messageSender\" must not null. ");
        Assert.notNull(type, "Parameter \"type\" must not null. ");
        String className = messageSender.getClass().getName();
        String identifier = getIdentifier(type, senderName);
        log.info("Register the sender \"{}\" to \"{}\". ", className, identifier);
        messageSenders.put(identifier, messageSender);
    }

    @Override
    public void deregisterSender(Class<?> type, String senderName) {
        Assert.notNull(type, "Parameter \"type\" must not null. ");
        String identifier = getIdentifier(type, senderName);
        MessageSender remove = messageSenders.remove(identifier);
        if (remove != null) {
            String className = remove.getClass().getName();
            log.info("Deregister the sender \"{}\" from \"{}\". ", className, identifier);
        }
    }

    @Override
    public <T> T send(Object message, String senderName, Class<T> clazz) {
        Assert.notNull(message, "Parameter \"message\" must not null. ");
        Assert.notNull(clazz, "Parameter \"clazz\" must not null. ");
        if (message instanceof List) {
            return batchSend((List<?>) message, senderName, clazz);
        }
        Map<String, Object> properties = getCommonProperties();
        String identifier = getIdentifier(message.getClass(), senderName);
        MessageSender messageSender = getMessageSender(identifier);
        return messageSender.send(properties, message, clazz);
    }

    @Override
    public <T> T batchSend(List<?> messages, String senderName, Class<T> clazz) {
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
        String identifier = getIdentifier(prevType, senderName);
        MessageSender messageSender = getMessageSender(identifier);
        return messageSender.batchSend(properties, messages, clazz);
    }

    @Override
    public <T> T info(Object input, String senderName, Class<T> clazz) {
        Assert.notNull(input, "Parameter \"input\" must not null. ");
        Assert.notNull(clazz, "Parameter \"clazz\" must not null. ");
        Map<String, Object> properties = getCommonProperties();
        String identifier = getIdentifier(input.getClass(), senderName);
        MessageSender messageSender = getMessageSender(identifier);
        return messageSender.info(properties, input, clazz);
    }

    @Override
    public <T> List<T> search(Object input, String senderName, Class<T> clazz) {
        Assert.notNull(input, "Parameter \"input\" must not null. ");
        Assert.notNull(clazz, "Parameter \"clazz\" must not null. ");
        Map<String, Object> properties = getCommonProperties();
        String identifier = getIdentifier(input.getClass(), senderName);
        MessageSender messageSender = getMessageSender(identifier);
        return messageSender.search(properties, input, clazz);
    }

}
