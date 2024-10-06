/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.core;

/**
 * Provide the highest level of abstraction for decoder.
 * @author Kahle
 */
@Deprecated
public interface Decoder<T> {

    /**
     * Decode a source data and return the result.
     * @param source The data to be decoded
     * @return The decoded content
     */
    T decode(T source);

}
