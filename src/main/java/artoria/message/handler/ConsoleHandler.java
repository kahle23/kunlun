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
public class ConsoleHandler extends AbstractClassicMessageHandler {

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
    public Object operate(Object input, String name, Class<?> clazz) {
        if (SEND.equals(name)) {
            Assert.notNull(input, "Parameter \"input\" must not null. ");
            Assert.notNull(clazz, "Parameter \"clazz\" must not null. ");
            if (input instanceof List) {
                return operate(input, "batchSend", clazz);
            }
            isSupport(new Class[]{Boolean.class}, clazz);
            System.out.println(convert(input, attrs()));
            return ObjectUtils.cast(Boolean.TRUE, clazz);
        }
        else if (BATCH_SEND.equals(name)) {
            Assert.isInstanceOf(List.class, input
                    , "Parameter \"input\" must instance of list. ");
            List<?> messages = (List<?>) input;
            Assert.notEmpty(messages, "Parameter \"input\" must not empty. ");
            Assert.notNull(clazz, "Parameter \"clazz\" must not null. ");
            isSupport(new Class[]{Boolean.class}, clazz);
            for (Object message : messages) {
                System.out.println(convert(message, attrs()));
            }
            return ObjectUtils.cast(Boolean.TRUE, clazz);
        }
        else {
            throw new UnsupportedOperationException(
                    "Unsupported operation name \"" + name + "\"! "
            );
        }
    }

}
