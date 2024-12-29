/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.core.handler;

import kunlun.core.Handler;

/**
 * The strategy supported handler.
 * @author Kahle
 */
@Deprecated
public interface StrategySupportedHandler extends Handler {

    /**
     * Perform a strategy and return the corresponding result.
     * The arguments mean (most of the scenes):
     *      0 input object,
     *      1 return value type
     * @param strategy The name of the strategy to be performed (strategy or operation or null)
     * @param arguments The arguments for the strategy to be performed
     * @return The result of the strategy
     */
    Object execute(Object strategy, Object[] arguments);

}
