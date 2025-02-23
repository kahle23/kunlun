/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.action.message.support;

import kunlun.action.message.AbstractMessageHandler;
import kunlun.data.bean.BeanUtils;
import kunlun.data.json.JsonUtils;
import kunlun.logging.Logger;
import kunlun.logging.LoggerFactory;
import kunlun.message.model.Message;
import kunlun.message.model.Result;
import kunlun.util.Assert;
import kunlun.util.ClassUtil;
import kunlun.util.MapUtil;
import kunlun.util.StrUtil;

import java.util.Collection;
import java.util.Map;

import static kunlun.common.constant.Numbers.*;
import static kunlun.common.constant.Symbols.*;

/**
 * The simple log-based message handler.
 * @author Kahle
 */
public class SimpleMessageHandler extends AbstractMessageHandler {
    private static final Logger log = LoggerFactory.getLogger(SimpleMessageHandler.class);

    protected void append(StringBuilder builder, Map<?, ?> map) {
        if (MapUtil.isEmpty(map)) { return; }
        if (builder == null) { return; }
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            Object entryValue = entry.getValue();
            Object entryKey = entry.getKey();
            if (entryKey == null) { continue; }
            String propertyName = String.valueOf(entryKey);
            if (StrUtil.isBlank(propertyName)) { continue; }
            propertyName = StrUtil.capitalize(propertyName);
            builder.append(propertyName).append(COLON);
            int length = TWENTY - propertyName.length() - ONE;
            if (length <= ZERO) { length = ONE; }
            for (int i = ZERO; i < length; i++) {
                builder.append(BLANK_SPACE);
            }
            if (entryValue == null || ClassUtil.isSimpleValueType(entryValue.getClass())) {
                builder.append(entryValue).append(NEWLINE);
            }
            else {
                builder.append(JsonUtils.toJsonString(entryValue)).append(NEWLINE);
            }
        }
    }

    protected String convert(Message message) {
        StringBuilder builder = new StringBuilder(NEWLINE);
        builder.append("---- Begin Message ----").append(NEWLINE);
        // Begin building message.
        builder.append("Provider:           ").append(getClass().getName()).append(NEWLINE);
        // Fill the builder with message.
        append(builder, BeanUtils.beanToMap(message));
        // End building message
        builder.append("---- End Message ----").append(NEWLINE);
        return builder.toString();
    }

    @Override
    public <T extends Message> Result send(Collection<T> messages) {
        for (Message message : Assert.notEmpty(messages)) {
            log.info(convert(message));
        }
        return new Result();
    }

}
