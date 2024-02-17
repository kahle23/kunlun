/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.core.handler;

import kunlun.core.Handler;

/**
 * The operating supported handler.
 * Very similar to the policy design pattern.
 * But it is mainly to leave an extension port for callers and extenders to handle.
 * @author Kahle
 */
public interface OperatingSupportHandler extends Handler {

    /**
     * Perform an operation and return the corresponding result.
     * The arguments mean (most of the scenes):
     *      0 input object,
     *      1 return value type
     * @param operation The name of the operation to be performed (strategy or operation or null)
     * @param arguments The arguments for the operation to be performed
     * @return The result of the operation
     */
    Object operate(Object operation, Object[] arguments);

}
