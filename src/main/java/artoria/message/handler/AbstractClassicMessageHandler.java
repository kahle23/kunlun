package artoria.message.handler;

import artoria.message.MessageHandler;
import artoria.util.Assert;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Map;

import static artoria.common.Constants.*;

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

        return operate(SEND, new Object[]{ message, type });
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
    public Object operate(Object operation, Object[] arguments) {
        Assert.notNull(operation, "Parameter \"operation\" must not null. ");
        Assert.notNull(arguments, "Parameter \"arguments\" must not null. ");
        Assert.isTrue(arguments.length >= TWO
                , "Parameter \"arguments\" length must >= 2. ");
        Assert.notNull(arguments[ZERO], "Parameter \"arguments[0]\" must not null. ");
        Assert.notNull(arguments[ONE], "Parameter \"arguments[1]\" must not null. ");
        Assert.isInstanceOf(Class.class, arguments[ONE]
                , "Parameter \"arguments[1]\" must instance of class. ");
        Class<?> clazz = (Class<?>) arguments[ONE];
        String name = String.valueOf(operation);
        Object input = arguments[ZERO];
        return operate(input, name, clazz);
    }

    /**
     * The message related operations.
     * The standard operation: send, batchSend, findOne, findMultiple, receive, subscribe, unsubscribe
     * @param input The input parameters to the operation
     * @param name The name of operation
     * @param clazz The return value class of the operation
     * @return The result of the operation or null
     */
    public abstract Object operate(Object input, String name, Class<?> clazz);

}
