package artoria.message;

import artoria.lang.Code;
import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import artoria.message.sender.MessageSender;
import artoria.util.ArrayUtils;
import artoria.util.Assert;
import artoria.util.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * The class based message provider.
 * @author Kahle
 */
public class ClassBasedMessageProvider extends AbstractMessageProvider {
    private static Logger log = LoggerFactory.getLogger(ClassBasedMessageProvider.class);

    protected ClassBasedMessageProvider(Map<String, Object> commonProperties,
                                        Map<String, MessageSender> messageSenders) {

        super(commonProperties, messageSenders);
    }

    public ClassBasedMessageProvider() {
    }

    public void registerSender(Class<?> type, MessageSender messageSender) {
        Assert.notNull(messageSender, "Parameter \"messageSender\" must not null. ");
        Assert.notNull(type, "Parameter \"type\" must not null. ");
        String className = messageSender.getClass().getName();
        String typeName = type.getName();
        log.info("Register the sender \"{}\" to \"{}\". ", className, typeName);
        messageSenders.put(typeName, messageSender);
    }

    public void deregisterSender(Class<?>... types) {
        Assert.notEmpty(types, "Parameter \"types\" must not empty. ");
        for (Class<?> type : types) {
            if (type == null) { continue; }
            String typeName = type.getName();
            MessageSender remove = messageSenders.remove(typeName);
            if (remove != null) {
                String className = remove.getClass().getName();
                log.info("Deregister the sender \"{}\" from \"{}\". ", className, typeName);
            }
        }
    }

    @Override
    public Object send(Object message, Code<?>... types) {
        Assert.notNull(message, "Parameter \"message\" must not null. ");
        if (message instanceof List) { return batchSend((List<?>) message, types); }
        if (ArrayUtils.isNotEmpty(types)) {
            return super.send(message, types);
        }
        Map<String, Object> properties = getCommonProperties();
        String typeName = message.getClass().getName();
        return getMessageSender(typeName).send(properties, message);
    }

    @Override
    public Object batchSend(List<?> messages, Code<?>... types) {
        Assert.notEmpty(messages, "Parameter \"messages\" must not null. ");
        if (ArrayUtils.isNotEmpty(types)) {
            return super.batchSend(messages, types);
        }
        Class<?> prevType = null;
        for (Object message : messages) {
            Assert.notNull(message, "Parameter \"messages\"'s elements all must not null. ");
            Class<?> type = message.getClass();
            if (prevType == null) { prevType = type; }
            Assert.isTrue(type.equals(prevType)
                    , "Parameter \"messages\"'s elements type all must be equal. ");
        }
        String typeName = prevType != null ? prevType.getName() : null;
        Map<String, Object> properties = getCommonProperties();
        return getMessageSender(typeName).batchSend(properties, messages);
    }

    @Override
    public <T> T info(Object input, Class<T> clazz) {
        Assert.notNull(input, "Parameter \"input\" must not null. ");
        Assert.notNull(clazz, "Parameter \"clazz\" must not null. ");
        String msgType = getMessageType(input);
        if (StringUtils.isNotBlank(msgType)) {
            return super.info(input, clazz);
        }
        Map<String, Object> properties = getCommonProperties();
        String typeName = input.getClass().getName();
        return getMessageSender(typeName).info(properties, input, clazz);
    }

    @Override
    public <T> List<T> search(Object input, Class<T> clazz) {
        Assert.notNull(input, "Parameter \"input\" must not null. ");
        Assert.notNull(clazz, "Parameter \"clazz\" must not null. ");
        String msgType = getMessageType(input);
        if (StringUtils.isNotBlank(msgType)) {
            return super.search(input, clazz);
        }
        Map<String, Object> properties = getCommonProperties();
        String typeName = input.getClass().getName();
        return getMessageSender(typeName).search(properties, input, clazz);
    }

}
