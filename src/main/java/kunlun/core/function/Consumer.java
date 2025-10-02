/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.core.function;

/**
 * Represents an operation that accepts a single input argument and returns no result.
 * @author Kahle
 */
public interface Consumer<Param> {

    /**
     * Performs this operation on the given argument.
     *
     * @param param The input argument
     */
    void accept(Param param);

    /**
     * 空的 Consumer.
     * @author Kahle
     */
    class Empty<Param> implements Consumer<Param> {
        @Override
        public void accept(Param param) { }
    }

}
