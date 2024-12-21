/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.crypto.digest;

import kunlun.codec.CodecUtils;
import kunlun.common.constant.Charsets;
import kunlun.core.Digester;
import kunlun.exception.ExceptionUtils;
import kunlun.io.util.IOUtils;
import kunlun.util.Assert;

import java.io.InputStream;
import java.nio.charset.Charset;

import static kunlun.codec.CodecUtils.BASE64;
import static kunlun.codec.CodecUtils.HEX;

/**
 * The abstract message digest tools.
 * @author Kahle
 */
public abstract class AbstractDigester implements Digester {

    @Override
    public byte[] digest(Config config, InputStream data) {
        Assert.notNull(data, "Parameter \"data\" must not null. ");
        try {
            return digest(config, IOUtils.toByteArray(data));
        } catch (Exception e) { throw ExceptionUtils.wrap(e); }
    }

    // ====

    public String digestToHex(Config config, byte[] data) {

        return CodecUtils.encodeToString(HEX, digest(config, data));
    }

    public String digestToBase64(Config config, byte[] data) {

        return CodecUtils.encodeToString(BASE64, digest(config, data));
    }

    public String digestToHex(Config config, InputStream data) {

        return CodecUtils.encodeToString(HEX, digest(config, data));
    }

    public String digestToBase64(Config config, InputStream data) {

        return CodecUtils.encodeToString(BASE64, digest(config, data));
    }

    // ====

    public byte[] digest(Config config, String data, Charset charset) {
        Assert.notBlank(data, "Parameter \"data\" must not blank. ");
        charset = charset != null ? charset : Charsets.UTF_8;
        return digest(config, data.getBytes(charset));
    }

    public byte[] digest(Config config, String data) {

        return digest(config, data, null);
    }

    public String digestToHex(Config config, String data, Charset charset) {

        return CodecUtils.encodeToString(HEX, digest(config, data, charset));
    }

    public String digestToHex(Config config, String data) {

        return digestToHex(config, data, null);
    }

    public String digestToBase64(Config config, String data, Charset charset) {

        return CodecUtils.encodeToString(BASE64, digest(config, data, charset));
    }

    public String digestToBase64(Config config, String data) {

        return digestToBase64(config, data, null);
    }

    // ====

    /**
     * The abstract configuration of the digester.
     * @author Kahle
     */
    public static abstract class AbstractConfig implements Config {
        private final String algorithm;

        public AbstractConfig(String algorithm) {
            Assert.notBlank(algorithm, "Parameter \"algorithm\" must not blank. ");
            this.algorithm = algorithm;
        }

        @Override
        public String getAlgorithm() {

            return algorithm;
        }
    }

}
