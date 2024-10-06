/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.core;

/**
 * Provide the highest level of abstraction for encoder.
 * @author Kahle
 */
@Deprecated
public interface Encoder<T> {

    /**
     * Encode a source data and return the encoded data.
     * @param source The data to be encoded
     * @return The encoded data
     */
    T encode(T source);

}
