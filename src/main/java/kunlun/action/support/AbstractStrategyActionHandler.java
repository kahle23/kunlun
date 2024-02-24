/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.action.support;

import kunlun.action.AbstractActionHandler;
import kunlun.core.handler.StrategySupportHandler;
import kunlun.data.tuple.Pair;
import kunlun.util.ArgumentUtils;
import kunlun.util.Assert;

import static kunlun.common.constant.Numbers.*;

/**
 * The abstract strategy action handler.
 * @author Kahle
 */
public abstract class AbstractStrategyActionHandler
        extends AbstractActionHandler implements StrategySupportHandler {

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
        Object strategy = arguments[ZERO];
        // Method call.
        return execute(strategy, newArgs);
    }

    @Override
    public Object execute(Object strategy, Object[] arguments) {
        Assert.notNull(strategy, "Parameter \"strategy\" must not null. ");
        Pair<Object, Class<?>> pair = ArgumentUtils.parseToObjCls(arguments);
        return execute(pair.getLeft(), String.valueOf(strategy), pair.getRight());
    }

    /**
     * The action related implementation.
     * @param input The input parameters to the strategy
     * @param strategy The name of strategy
     * @param clazz The return value class of the strategy
     * @return The result of the strategy or null
     */
    public abstract Object execute(Object input, String strategy, Class<?> clazz);

}
