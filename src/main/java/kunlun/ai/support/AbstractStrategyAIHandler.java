/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.ai.support;

import kunlun.ai.AbstractAIHandler;
import kunlun.core.handler.StrategySupportHandler;
import kunlun.data.tuple.Pair;
import kunlun.util.ArgumentUtils;
import kunlun.util.Assert;

/**
 * The abstract strategy AI handler.
 * @author Kahle
 */
public abstract class AbstractStrategyAIHandler extends AbstractAIHandler implements StrategySupportHandler {

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
     * The artificial intelligence performs reasoning operations.
     * @param input The input parameters for inference calculations
     * @param strategy The strategy or operation or null for AI handler execution
     * @param clazz The class of the return value
     * @return The result of the inference calculation
     */
    public abstract Object execute(Object input, String strategy, Class<?> clazz);

}
