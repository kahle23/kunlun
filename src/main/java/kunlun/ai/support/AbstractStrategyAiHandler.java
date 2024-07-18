/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.ai.support;

import kunlun.ai.AbstractAiHandler;
import kunlun.data.tuple.Triple;
import kunlun.util.ArgumentUtils;

/**
 * The abstract strategy AI handler.
 * @author Kahle
 */
public abstract class AbstractStrategyAiHandler extends AbstractAiHandler {

    @Override
    public Object execute(Object[] arguments) {
        Triple<Object, String, Class<?>> triple = ArgumentUtils.parseToObjStrCls(arguments);
        return execute(triple.getLeft(), triple.getMiddle(), triple.getRight());
    }

    /**
     * Get the AI handler configuration according to the arguments.
     * @param input The input parameters for inference calculations
     * @param strategy The strategy or operation or null for AI handler execution
     * @param clazz The class of the return value
     * @return The AI handler configuration
     */
    protected abstract AbstractConfig getConfig(Object input, String strategy, Class<?> clazz);

    /**
     * The artificial intelligence performs reasoning operations.
     * @param input The input parameters for inference calculations
     * @param strategy The strategy or operation or null for AI handler execution
     * @param clazz The class of the return value
     * @return The result of the inference calculation
     */
    public abstract Object execute(Object input, String strategy, Class<?> clazz);

}
