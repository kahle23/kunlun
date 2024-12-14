/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.core;

/**
 * Provide the highest level of abstraction for action.
 *
 * What is action?
 * Representing a specific operation, action, or task used to perform a particular function.
 *
 * @author Kahle
 */
public interface Action {

    /**
     * Execute a specific logic.
     *
     * The arguments mean (most of the scenes):
     *      0 strategy or operation or null,
     *      1 input object,
     *
     * @param arguments The arguments to the execution of a specific logic
     * @return The execution result of a specific logic
     */
    Object execute(Object[] arguments);

}
