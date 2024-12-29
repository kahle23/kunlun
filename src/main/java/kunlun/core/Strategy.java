/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.core;

/**
 * Provide the highest level of abstraction for strategy.
 * @author Kahle
 */
public interface Strategy {

    /**
     * The logic of executing specific strategies.
     * @param strategy The name of strategy
     * @param input The primary input parameter to the strategy
     * @param arguments The others related arguments or empty
     * @return The result of the strategy or null
     */
    Object execute(String strategy, Object input, Object[] arguments);

}
