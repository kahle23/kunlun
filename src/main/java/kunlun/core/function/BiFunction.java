/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.core.function;

/**
 * Represents a function that accepts two arguments and produces a result.
 * @see kunlun.core.function.Function
 * @author Kahle
 */
public interface BiFunction<Param1, Param2, Result> {

    /**
     * Applies this function to the given arguments.
     * @param param1 The first function argument
     * @param param2 The second function argument
     * @return The function result
     */
    Result apply(Param1 param1, Param2 param2);

}
