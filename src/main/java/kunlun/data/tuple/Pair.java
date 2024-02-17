/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.data.tuple;

/**
 * A pair consisting of two elements.
 * @param <L> The left element type
 * @param <R> The right element type
 * @author Kahle
 */
public interface Pair<L, R> {

    /**
     * Gets the left element from this pair.
     * @return The left element, may be null
     */
    L getLeft();

    /**
     * Gets the right element from this pair.
     * @return The right element, may be null
     */
    R getRight();

}
