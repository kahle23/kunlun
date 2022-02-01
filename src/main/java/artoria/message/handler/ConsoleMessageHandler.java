package artoria.message.handler;

import artoria.beans.BeanUtils;
import artoria.exchange.JsonUtils;
import artoria.lang.Dict;
import artoria.util.*;

import java.util.List;
import java.util.Map;

import static artoria.common.Constants.*;

/**
 * The handler of the message sent to the console.
 * @author Kahle
 */
public class ConsoleMessageHandler extends AbstractMessageHandler {

    protected void append(StringBuilder builder, Map<?, ?> map) {
        if (MapUtils.isEmpty(map)) { return; }
        if (builder == null) { return; }
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            Object entryValue = entry.getValue();
            Object entryKey = entry.getKey();
            if (entryKey == null) { continue; }
            String propertyName = String.valueOf(entryKey);
            if (StringUtils.isBlank(propertyName)) { continue; }
            propertyName = StringUtils.capitalize(propertyName);
            builder.append(propertyName).append(COLON);
            int length = TWENTY - propertyName.length() - ONE;
            if (length <= ZERO) { length = ONE; }
            for (int i = ZERO; i < length; i++) {
                builder.append(BLANK_SPACE);
            }
            if (entryValue == null || ClassUtils.isSimpleValueType(entryValue.getClass())) {
                builder.append(entryValue).append(NEWLINE);
            }
            else {
                builder.append(JsonUtils.toJsonString(entryValue)).append(NEWLINE);
            }
        }
    }

    protected String convert(Object message, Map<?, ?> properties) {
        StringBuilder builder = new StringBuilder(NEWLINE);
        builder.append("---- Begin Message ----").append(NEWLINE);
        // Begin building message.
        builder.append("Provider:           ").append(getClass().getName()).append(NEWLINE);
        // Fill the builder with properties.
        append(builder, properties);
        // Fill the builder with message.
        if (message == null || ClassUtils.isSimpleValueType(message.getClass())) {
            append(builder, Dict.of("message", String.valueOf(message)));
        }
        else {
            append(builder, BeanUtils.beanToMap(message));
        }
        // End building message
        builder.append("---- End Message ----").append(NEWLINE);
        return builder.toString();
    }

    @Override
    public <T> T send(Map<?, ?> properties, Object message, Class<T> clazz) {
        Assert.notNull(message, "Parameter \"message\" must not null. ");
        Assert.notNull(clazz, "Parameter \"clazz\" must not null. ");
        isSupport(new Class[]{Boolean.class}, clazz);
        System.out.println(convert(message, properties));
        return ObjectUtils.cast(Boolean.TRUE, clazz);
    }

    @Override
    public <T> T batchSend(Map<?, ?> properties, List<?> messages, Class<T> clazz) {
        Assert.notEmpty(messages, "Parameter \"messages\" must not empty. ");
        Assert.notNull(clazz, "Parameter \"clazz\" must not null. ");
        isSupport(new Class[]{Boolean.class}, clazz);
        for (Object message : messages) {
            System.out.println(convert(message, properties));
        }
        return ObjectUtils.cast(Boolean.TRUE, clazz);
    }

}
