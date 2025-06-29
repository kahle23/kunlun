/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.message.support;

import kunlun.action.message.AbstractMessageBus;
import kunlun.data.Dict;
import kunlun.logging.Logger;
import kunlun.logging.LoggerFactory;
import kunlun.message.MessageListener;
import kunlun.message.model.Message;
import kunlun.message.model.MessageRt;
import kunlun.message.model.Subscribe;
import kunlun.message.model.SubscribeRt;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static kunlun.util.Assert.*;
import static kunlun.util.CollUtil.isNotEmpty;

/**
 * The simple (sync no queue) message handler.
 * @author Kahle
 */
public class SimpleMessageBus extends AbstractMessageBus {
    private static final Logger log = LoggerFactory.getLogger(SimpleMessageBus.class);
    private final Map<String, List<MessageListener>> listeners;

    protected SimpleMessageBus(Map<String, List<MessageListener>> listeners) {

        this.listeners = notNull(listeners);
    }

    public SimpleMessageBus() {

        this(new ConcurrentHashMap<String, List<MessageListener>>());
    }

    @Override
    public <T extends Message> MessageRt send(Collection<T> messages) {
        List<Object> onMessages = new ArrayList<Object>();
        for (Message message : notEmpty(messages)) {
            String topic = notNull(message).getTopic();
            List<MessageListener> list = listeners.get(topic);
            state(isNotEmpty(list), "Please register the message listener first! ");
            for (MessageListener listener : list) {
                // Do not handle the exception, as it is executed synchronously.
                onMessages.add(listener.onMessage(message));
            }
        }
        return new MessageRt(Dict.of("onMessages", onMessages));
    }

    @Override
    public SubscribeRt subscribe(Subscribe subscribe) {
        // Process parameters.
        Object msgListener = notNull(notNull(subscribe).getMessageListener());
        String topic = notBlank(subscribe.getTopic());
        isInstanceOf(MessageListener.class, msgListener);
        MessageListener listener = (MessageListener) msgListener;
        // Subscribe.
        List<MessageListener> list = listeners.get(topic);
        if (list == null) {
            listeners.put(topic, list = new ArrayList<MessageListener>());
        }
        if (!list.contains(listener)) { list.add(listener); }
        // Result.
        return new SubscribeRt();
    }

}
