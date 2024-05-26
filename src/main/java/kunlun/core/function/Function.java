/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.core.function;

/**
 * Represents a function that accepts one parameter and produces a result.
 * @author Kahle
 */
public interface Function<Param, Result> {

    /**
     * Applies this function to the given parameter.
     * @param param The function parameter
     * @return The function result
     */
    Result apply(Param param);

}
