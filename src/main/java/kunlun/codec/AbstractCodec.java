/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.codec;

import kunlun.core.Codec;
import kunlun.exception.ExceptionUtils;
import kunlun.io.util.IOUtils;
import kunlun.util.Assert;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * The abstract codec.
 * @author Kahle
 */
public abstract class AbstractCodec implements Codec {

    @Override
    public void encode(Config config, InputStream source, OutputStream out) {
        Assert.notNull(source, "Parameter \"source\" must not null. ");
        Assert.notNull(out, "Parameter \"out\" must not null. ");
        try {
            out.write(encode(config, IOUtils.toByteArray(source)));
        } catch (Exception e) { throw ExceptionUtils.wrap(e); }
    }

    @Override
    public void decode(Config config, InputStream source, OutputStream out) {
        Assert.notNull(source, "Parameter \"source\" must not null. ");
        Assert.notNull(out, "Parameter \"out\" must not null. ");
        try {
            out.write(decode(config, IOUtils.toByteArray(source)));
        } catch (Exception e) { throw ExceptionUtils.wrap(e); }
    }

    /**
     * Encode a source data and return the encoded data.
     * @param source The data to be encoded
     * @return The encoded data
     */
    public byte[] encode(byte[] source) {

        return encode(null, source);
    }

    /**
     * Decode a source data and return the result.
     * @param source The data to be decoded
     * @return The decoded content
     */
    public byte[] decode(byte[] source) {

        return decode(null, source);
    }

    /**
     * Encode a source data and return the encoded data.
     * @param source The data to be encoded
     * @param out The encoded data
     */
    public void encode(InputStream source, OutputStream out) {

        encode(null, source, out);
    }

    /**
     * Decode a source data and return the result.
     * @param source The data to be decoded
     * @param out The decoded content
     */
    public void decode(InputStream source, OutputStream out) {

        decode(null, source, out);
    }

}
