/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.message.support;

import kunlun.data.tuple.Pair;
import kunlun.message.MessageHandler;
import kunlun.util.ArgumentUtils;
import kunlun.util.Assert;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Map;

/**
 * The abstract message handler.
 * @author Kahle
 */
public abstract class AbstractClassicMessageHandler implements MessageHandler {
    /**
     * The standard operation name: send.
     */
    protected static final String SEND = "send";
    /**
     * The standard operation name: batch send.
     */
    protected static final String BATCH_SEND = "batchSend";
    /**
     * The standard operation name: find one.
     */
    protected static final String FIND_ONE = "findOne";
    /**
     * The standard operation name: find multiple.
     */
    protected static final String FIND_MULTIPLE = "findMultiple";
    /**
     * The standard operation name: receive.
     */
    protected static final String RECEIVE = "receive";
    /**
     * The standard operation name: subscribe.
     */
    protected static final String SUBSCRIBE = "subscribe";
    /**
     * The standard operation name: unsubscribe.
     */
    protected static final String UNSUBSCRIBE = "unsubscribe";
    /**
     * The common properties.
     */
    private Map<Object, Object> commonProperties = Collections.emptyMap();

    protected void isSupport(Class<?>[] supportClasses, Class<?> clazz) {
        if (Object.class.equals(clazz)) { return; }
        for (Class<?> supportClass : supportClasses) {
            if (supportClass.equals(clazz)) { return; }
        }
        throw new IllegalArgumentException("Parameter \"clazz\" is not supported. ");
    }

    @Override
    public Map<Object, Object> getCommonProperties() {

        return commonProperties;
    }

    @Override
    public void setCommonProperties(Map<?, ?> properties) {
        Assert.notNull(properties, "Parameter \"properties\" must not null. ");
        this.commonProperties = Collections.unmodifiableMap(properties);
    }

    @Override
    public Object send(Object message, Type type) {

        return execute(SEND, new Object[]{ message, type });
    }

    @Override
    public Object receive(Object condition, Type type) {

        throw new UnsupportedOperationException("This method is not supported! ");
    }

    @Override
    public Object subscribe(Object condition, Object messageListener) {

        throw new UnsupportedOperationException("This method is not supported! ");
    }

    @Override
    public Object execute(Object operation, Object[] arguments) {
        Assert.notNull(operation, "Parameter \"operation\" must not null. ");
        Pair<Object, Class<?>> pair = ArgumentUtils.parseToObjCls(arguments);
        return execute(pair.getLeft(), String.valueOf(operation), pair.getRight());
    }

    /**
     * The message related operations.
     * The standard operation: send, batchSend, findOne, findMultiple, receive, subscribe, unsubscribe
     * @param input The input parameters to the operation
     * @param name The name of operation
     * @param clazz The return value class of the operation
     * @return The result of the operation or null
     */
    public abstract Object execute(Object input, String name, Class<?> clazz);

}
