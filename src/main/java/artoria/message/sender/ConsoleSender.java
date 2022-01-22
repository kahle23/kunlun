package artoria.message.sender;

import artoria.beans.BeanUtils;
import artoria.exchange.JsonUtils;
import artoria.lang.Code;
import artoria.lang.Dict;
import artoria.message.MessageType;
import artoria.util.ClassUtils;
import artoria.util.MapUtils;
import artoria.util.StringUtils;

import java.util.Map;

import static artoria.common.Constants.*;

/**
 * The sender of the message sent to the console.
 * @author Kahle
 */
public class ConsoleSender extends AbstractMessageSender {

    public ConsoleSender() {

        this(MessageType.CONSOLE);
    }

    public ConsoleSender(Code<?> type) {

        super(type);
    }

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
        String typeInfo = getType().getCode() + " (" + getType().getDescription() + ")";
        builder.append("Type:               ").append(typeInfo).append(NEWLINE);
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
    public Object send(Map<?, ?> properties, Object message) {
        System.out.println(convert(message, properties));
        return Boolean.TRUE;
    }

}
