/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.data.tuple;

/**
 * A triple consisting of three elements.
 * @param <L> The left element type
 * @param <M> The middle element type
 * @param <R> The right element type
 * @author Kahle
 */
public interface Triple<L, M, R> {

    /**
     * Gets the left element from this triple.
     * @return The left element, may be null
     */
    L getLeft();

    /**
     * Gets the middle element from this triple.
     * @return The middle element, may be null
     */
    M getMiddle();

    /**
     * Gets the right element from this triple.
     * @return The right element, may be null
     */
    R getRight();

}
