/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.core.function;

/**
 * Represents a predicate (boolean-valued function) of two arguments.
 * @author Kahle
 */
public interface BiPredicate<Param1, Param2> {

    /**
     * Evaluates this predicate on the given arguments.
     *
     * @param param1 The first input argument
     * @param param2 The second input argument
     * @return True if the input arguments match the predicate, otherwise false
     */
    boolean test(Param1 param1, Param2 param2);

}
