/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.core.function;

/**
 * Represents a supplier of results.
 * @author Zerox
 */
public interface Supplier<Result> {

    /**
     * Gets a result.
     * @return The result
     */
    Result get();

    /**
     * 空的 Supplier.
     * @author Zerox
     */
    class Empty<Result> implements Supplier<Result> {
        @Override
        public Result get() { return null; }
    }

}
