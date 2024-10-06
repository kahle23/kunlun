/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.codec;

/**
 * The bytes-based codec.
 * @author Kahle
 */
public abstract class ByteCodec extends AbstractCodec {

    /**
     * Encode a source data and return the encoded string.
     * @param config The configuration when encoding
     * @param source The data to be encoded
     * @return The encoded string
     */
    public String encodeToString(Config config, byte[] source) {
        byte[] encode = encode(config, source);
        return encode != null ? new String(encode) : null;
    }

    /**
     * Decode a source string and return the result.
     * @param config The configuration when decoding
     * @param source The string to be decoded
     * @return The decoded content
     */
    public byte[] decodeFromString(Config config, String source) {

        return decode(config, source != null ? source.getBytes() : null);
    }

    /**
     * Encode a source data and return the encoded string.
     * @param source The data to be encoded
     * @return The encoded string
     */
    public String encodeToString(byte[] source) {

        return encodeToString(null, source);
    }

    /**
     * Decode a source string and return the result.
     * @param source The string to be decoded
     * @return The decoded content
     */
    public byte[] decodeFromString(String source) {

        return decodeFromString(null, source);
    }

}
