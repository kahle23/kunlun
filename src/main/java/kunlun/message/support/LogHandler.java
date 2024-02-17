/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.message.support;

import kunlun.logging.Logger;
import kunlun.logging.LoggerFactory;
import kunlun.util.Assert;
import kunlun.util.ObjectUtils;

import java.util.List;

/**
 * The handler of the message sent to the log.
 * @author Kahle
 */
public class LogHandler extends ConsoleHandler {
    private static final Logger log = LoggerFactory.getLogger(LogHandler.class);

    @Override
    public Object operate(Object input, String name, Class<?> clazz) {
        if (SEND.equals(name)) {
            Assert.notNull(input, "Parameter \"input\" must not null. ");
            Assert.notNull(clazz, "Parameter \"clazz\" must not null. ");
            if (input instanceof List) {
                return operate(input, "batchSend", clazz);
            }
            isSupport(new Class[]{Boolean.class}, clazz);
            log.info(convert(input, getCommonProperties()));
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
                log.info(convert(message, getCommonProperties()));
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
