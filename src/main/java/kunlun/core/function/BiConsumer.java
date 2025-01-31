/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.core.function;

/**
 * Represents an operation that accepts two input arguments and returns no result.
 * @see kunlun.core.function.Consumer
 * @author Kahle
 */
public interface BiConsumer<Param1, Param2> {

    /**
     * Performs this operation on the given arguments.
     *
     * @param param1 The first input argument
     * @param param2 The second input argument
     */
    void accept(Param1 param1, Param2 param2);

}
