/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.codec;

import kunlun.core.Codec;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

/**
 * The codec manager.
 *
 * @see kunlun.core.Codec
 * @author Kahle
 */
public interface CodecManager {

    /**
     * Register common properties' information.
     * @param commonProperties The common properties
     */
    void registerCommonProperties(Map<?, ?> commonProperties);

    /**
     * Clears common properties' information.
     */
    void clearCommonProperties();

    /**
     * Get common properties' information.
     * @return The common properties
     */
    Map<String, Object> getCommonProperties();

    /**
     * Register the codec.
     * @param name The codec name
     * @param codec The codec
     */
    void registerCodec(String name, Codec codec);

    /**
     * Deregister the codec.
     * @param name The codec name
     */
    void deregisterCodec(String name);

    /**
     * Get the codec by name.
     * @param name The codec name
     * @return The codec
     */
    Codec getCodec(String name);

    /**
     * Encode a source data and return the encoded data.
     * @param name The codec name
     * @param config The configuration when encoding
     * @param source The data to be encoded
     * @return The encoded data
     */
    byte[] encode(String name, Codec.Config config, byte[] source);

    /**
     * Decode a source data and return the result.
     * @param name The codec name
     * @param config The configuration when decoding
     * @param source The data to be decoded
     * @return The decoded content
     */
    byte[] decode(String name, Codec.Config config, byte[] source);

    /**
     * Encode a source data and return the encoded data.
     * @param name The codec name
     * @param config The configuration when encoding
     * @param source The data to be encoded
     * @param out The encoded data
     */
    void encode(String name, Codec.Config config, InputStream source, OutputStream out);

    /**
     * Decode a source data and return the result.
     * @param name The codec name
     * @param config The configuration when decoding
     * @param source The data to be decoded
     * @param out The decoded content
     */
    void decode(String name, Codec.Config config, InputStream source, OutputStream out);

    // ----

    /**
     * Encode a source data and return the encoded string.
     * @param name The codec name
     * @param config The configuration when encoding
     * @param source The data to be encoded
     * @return The encoded string
     */
    String encodeToString(String name, Codec.Config config, byte[] source);

    /**
     * Decode a source string and return the result.
     * @param name The codec name
     * @param config The configuration when decoding
     * @param source The string to be decoded
     * @return The decoded content
     */
    byte[] decodeFromString(String name, Codec.Config config, String source);

    // ----

    /**
     * Encode a source string and return the encoded string.
     * @param name The codec name
     * @param config The configuration when encoding
     * @param source The string to be encoded
     * @return The encoded string
     */
    String encode(String name, Codec.Config config, String source);

    /**
     * Decode a source string and return the result.
     * @param name The codec name
     * @param config The configuration when decoding
     * @param source The string to be decoded
     * @return The decoded content
     */
    String decode(String name, Codec.Config config, String source);

}
