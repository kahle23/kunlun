/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.codec;

/**
 * The character-based codec.
 * @author Kahle
 */
public abstract class CharCodec extends AbstractCodec {

    @Override
    public byte[] encode(Config config, byte[] source) {
        String encode = encode(config, source != null ? new String(source) : null);
        return encode != null ? encode.getBytes() : null;
    }

    @Override
    public byte[] decode(Config config, byte[] source) {
        String decode = decode(config, source != null ? new String(source) : null);
        return decode != null ? decode.getBytes() : null;
    }

    /**
     * Encode a source string and return the encoded string.
     * @param config The configuration when encoding
     * @param source The string to be encoded
     * @return The encoded string
     */
    public abstract String encode(Config config, String source);

    /**
     * Decode a source string and return the result.
     * @param config The configuration when decoding
     * @param source The string to be decoded
     * @return The decoded content
     */
    public abstract String decode(Config config, String source);

    /**
     * Encode a source string and return the encoded string.
     * @param source The string to be encoded
     * @return The encoded string
     */
    public String encode(String source) {

        return encode(null, source);
    }

    /**
     * Decode a source string and return the result.
     * @param source The string to be decoded
     * @return The decoded content
     */
    public String decode(String source) {

        return decode(null, source);
    }

}
