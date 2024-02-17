/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.core.handler;

import kunlun.core.Context;
import kunlun.core.Handler;

/**
 * The context supported handler.
 * @author Kahle
 */
public interface ContextSupportHandler extends Handler {

    /**
     * Build the context object from the arguments array.
     * Only the implementer knows what the arguments mean.
     * @param arguments The specific arguments array
     * @return The handler context object
     */
    HandlerContext buildContext(Object... arguments);

    /**
     * The handler context.
     * @author Kahle
     */
    interface HandlerContext extends Context {

        /**
         * Get an array of arguments for constructing the context.
         * @return The input arguments array
         */
        Object[] getArguments();

    }

}
