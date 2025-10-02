/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.core.function;

/**
 * Represents a predicate (boolean-valued function) of one argument.
 * @author Kahle
 */
public interface Predicate<Param> {

    /**
     * Evaluates this predicate on the given argument.
     *
     * @param param The input argument
     * @return True if the input argument matches the predicate, otherwise false
     */
    boolean test(Param param);

    /**
     * 空的 Predicate.
     * @author Kahle
     */
    class Empty<Param> implements Predicate<Param> {
        @Override
        public boolean test(Param param) { return false; }
    }

}
