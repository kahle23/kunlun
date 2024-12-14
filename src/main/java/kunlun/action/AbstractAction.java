/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.action;

import kunlun.core.Action;
import kunlun.data.tuple.Triple;
import kunlun.util.ArgumentUtils;

/**
 * The abstract action.
 * @author Kahle
 */
public abstract class AbstractAction implements Action {

    @Override
    public Object execute(Object[] arguments) {
        Triple<String, Object, Object[]> triple = ArgumentUtils.parseToStrObjArr(arguments);
        return execute(triple.getLeft(), triple.getMiddle(), triple.getRight());
    }

    /**
     * The action related implementation.
     * @param strategy The name of strategy
     * @param input The input parameters to the strategy
     * @param arguments The others related arguments or empty
     * @return The result of the strategy or null
     */
    public abstract Object execute(String strategy, Object input, Object[] arguments);

}
