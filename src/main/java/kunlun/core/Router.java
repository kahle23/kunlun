/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.core;

/**
 * Provide the highest level of abstraction for router.
 * @author Kahle
 */
public interface Router {

    /**
     * Calculate the destination of the route based on the incoming arguments.
     * @param arguments The arguments to be calculated
     * @return The route calculation result
     */
    Object route(Object[] arguments);

}
