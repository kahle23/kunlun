package artoria.action.handler;

import artoria.action.AbstractActionHandler;
import artoria.util.Assert;
import artoria.util.ObjectUtils;

import java.lang.reflect.Type;

import static artoria.common.Constants.*;

/**
 * The abstract classic action handler.
 * @author Kahle
 */
public abstract class AbstractClassicActionHandler extends AbstractActionHandler {

    @Override
    public Object execute(Object[] arguments) {
        // Check parameters.
        Assert.notNull(arguments, "Parameter \"arguments\" must not null. ");
        Assert.isTrue(arguments.length >= TWO
            , "The length of parameter \"arguments\" must be at least 2. ");
        // Check return value type.
        Assert.notNull(arguments[ONE], "Parameter \"arguments[1]\" must not null. ");
        Assert.isInstanceOf(Type.class, arguments[ONE]
            , "Parameter \"arguments[1]\" must instance of Type. ");
        // Extract parameters.
        Type type = (Type) arguments[ONE];
        Object input = arguments[ZERO];
        // Method call.
        return execute(input, type);
    }

    public <T> T execute(Object input, Type type) {
        Assert.isInstanceOf(Class.class, type, "Parameter \"type\" must instance of class. ");
        Class<T> clazz = ObjectUtils.cast(type);
        return execute(input, clazz);
    }

    /**
     * Execute a specific logic.
     * @param input The input parameters to be executed
     * @param clazz The class of the return value
     * @param <T> The generic type of the return value
     * @return The return value
     */
    public abstract <T> T execute(Object input, Class<T> clazz);

}
