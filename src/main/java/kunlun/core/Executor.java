/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.core;

/**
 * Provide the highest level of abstraction for executor.
 * @see java.util.concurrent.Executor
 * @author Kahle
 */
@Deprecated
public interface Executor {

    /**
     * Executes the given task at some time in the future.
     * @param task The task to be performed
     */
    void execute(Object task);

}
