/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.crypto.digest;

import kunlun.codec.CodecUtil;
import kunlun.common.constant.Charsets;
import kunlun.core.Digester;
import kunlun.exception.ExceptionUtil;
import kunlun.io.util.IoUtil;
import kunlun.util.Assert;

import java.io.InputStream;
import java.nio.charset.Charset;

/**
 * The abstract message digest tools.
 * @author Kahle
 */
public abstract class AbstractDigester implements Digester {

    @Override
    public byte[] digest(Config config, InputStream data) {
        Assert.notNull(data, "Parameter \"data\" must not null. ");
        try {
            return digest(config, IoUtil.readBytes(data));
        } catch (Exception e) { throw ExceptionUtil.wrap(e); }
    }

    // ====

    public String digestToHex(Config config, byte[] data) {

        return CodecUtil.encodeToHex(digest(config, data));
    }

    public String digestToBase64(Config config, byte[] data) {

        return CodecUtil.encodeToBase64(digest(config, data));
    }

    public String digestToHex(Config config, InputStream data) {

        return CodecUtil.encodeToHex(digest(config, data));
    }

    public String digestToBase64(Config config, InputStream data) {

        return CodecUtil.encodeToBase64(digest(config, data));
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

        return CodecUtil.encodeToHex(digest(config, data, charset));
    }

    public String digestToHex(Config config, String data) {

        return digestToHex(config, data, null);
    }

    public String digestToBase64(Config config, String data, Charset charset) {

        return CodecUtil.encodeToBase64(digest(config, data, charset));
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
