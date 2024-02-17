/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.action.support;

import kunlun.action.AbstractActionHandler;
import kunlun.core.handler.OperatingSupportHandler;
import kunlun.util.Assert;

import static kunlun.common.constant.Numbers.*;

/**
 * The abstract operate action handler.
 * @author Kahle
 */
public abstract class AbstractOperateActionHandler
        extends AbstractActionHandler implements OperatingSupportHandler {

    @Override
    public Object execute(Object[] arguments) {
        // Check parameters.
        Assert.notNull(arguments, "Parameter \"arguments\" must not null. ");
        Assert.isTrue(arguments.length >= THREE
            , "The length of parameter \"arguments\" must be at least 3. ");
        // Extract parameters.
        int newLength = arguments.length - ONE;
        Object[] newArgs = new Object[newLength];
        System.arraycopy(arguments, ONE, newArgs, ZERO, newLength);
        Object operation = arguments[ZERO];
        // Method call.
        return operate(operation, newArgs);
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
     * The action related operations.
     * @param input The input parameters to the operation
     * @param name The name of operation
     * @param clazz The return value class of the operation
     * @return The result of the operation or null
     */
    public abstract Object operate(Object input, String name, Class<?> clazz);

}
