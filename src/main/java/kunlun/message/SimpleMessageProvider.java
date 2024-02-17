/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.message;

import kunlun.logging.Logger;
import kunlun.logging.LoggerFactory;
import kunlun.message.support.ConsoleHandler;
import kunlun.message.support.LogHandler;

import java.util.Map;

/**
 * The simple message provider.
 * @author Kahle
 */
public class SimpleMessageProvider extends AbstractMessageProvider {
    private static final Logger log = LoggerFactory.getLogger(SimpleMessageProvider.class);

    protected SimpleMessageProvider(Map<String, Object> commonProperties,
                                    Map<String, MessageHandler> messageHandlers) {
        super(commonProperties, messageHandlers);
    }

    public SimpleMessageProvider() {
        registerHandler("console", new ConsoleHandler());
        registerHandler("log", new LogHandler());
    }

}
