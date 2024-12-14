/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.action.support;

import kunlun.action.AbstractAction;
import kunlun.core.handler.StrategySupportedHandler;
import kunlun.data.tuple.Pair;
import kunlun.util.ArgumentUtils;
import kunlun.util.Assert;

/**
 * The abstract strategy action handler.
 * @author Kahle
 */
@Deprecated
public abstract class AbstractStrategyAction
        extends AbstractAction implements StrategySupportedHandler {

    @Override
    public Object execute(Object[] arguments) {
        Pair<Object, Object[]> pair = ArgumentUtils.parseToObjArr(arguments);
        return execute(pair.getLeft(), pair.getRight());
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
