/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.core;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * Provide the highest level of abstraction for codec.
 *
 * @see <a href="https://en.wikipedia.org/wiki/Codec">Codec</a>
 * @author Kahle
 */
public interface Codec {

    /**
     * Encode a source data and return the encoded data.
     * @param config The configuration when encoding
     * @param source The data to be encoded
     * @return The encoded data
     */
    byte[] encode(Config config, byte[] source);

    /**
     * Decode a source data and return the result.
     * @param config The configuration when decoding
     * @param source The data to be decoded
     * @return The decoded content
     */
    byte[] decode(Config config, byte[] source);

    /**
     * Encode a source data and return the encoded data.
     * @param config The configuration when encoding
     * @param source The data to be encoded
     * @param out The encoded data
     */
    void encode(Config config, InputStream source, OutputStream out);

    /**
     * Decode a source data and return the result.
     * @param config The configuration when decoding
     * @param source The data to be decoded
     * @param out The decoded content
     */
    void decode(Config config, InputStream source, OutputStream out);

    /**
     * The configuration of the codec.
     * @author Kahle
     */
    interface Config {

    }

}
