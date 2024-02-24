/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.ai.support;

import kunlun.ai.AbstractAiHandler;
import kunlun.data.tuple.Triple;
import kunlun.util.ArgumentUtils;

/**
 * The abstract classic AI handler.
 * @author Kahle
 */
public abstract class AbstractClassicAiHandler extends AbstractAiHandler {

    @Override
    public Object execute(Object[] arguments) {
        Triple<Object, String, Class<?>> triple = ArgumentUtils.parseToObjStrCls(arguments);
        return execute(triple.getLeft(), triple.getMiddle(), triple.getRight());
    }

    /**
     * The artificial intelligence performs reasoning operations.
     * @param input The input parameters for inference calculations
     * @param operation The strategy or operation or null for AI handler execution
     * @param clazz The class of the return value
     * @return The result of the inference calculation
     */
    public abstract Object execute(Object input, String operation, Class<?> clazz);

}
