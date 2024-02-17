/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.action.support;

import kunlun.action.AbstractActionHandler;
import kunlun.util.Assert;
import kunlun.util.ObjectUtils;

import java.lang.reflect.Type;

import static kunlun.common.constant.Numbers.*;

/**
 * The abstract classic action handler.
 * @author Kahle
 */
public abstract class AbstractClassicActionHandler extends AbstractActionHandler {

    @Override
    public Object execute(Object[] arguments) {
        // Check parameters.
        Assert.notNull(arguments, "Parameter \"arguments\" must not null. ");
        Assert.isTrue(arguments.length >= THREE
                , "The length of parameter \"arguments\" must be at least 3. ");
        // Check return value type.
        Assert.notNull(arguments[TWO], "Parameter \"arguments[2]\" must not null. ");
        Assert.isInstanceOf(Type.class, arguments[TWO]
            , "Parameter \"arguments[2]\" must instance of Type. ");
        // Extract parameters.
        Type type = (Type) arguments[TWO];
        Object input = arguments[ONE];
        // Method call.
        return execute(input, type);
    }

    /**
     * Execute a specific logic.
     * @param input The input parameters to be executed
     * @param type The type of the return value
     * @param <T> The generic type of the return value
     * @return The return value
     */
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
