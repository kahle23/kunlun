/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.crypto.digest;

import kunlun.exception.ExceptionUtils;
import kunlun.util.Assert;

import java.io.InputStream;
import java.security.MessageDigest;

import static kunlun.common.constant.Numbers.ZERO;
import static kunlun.io.util.IOUtils.DEFAULT_BUFFER_SIZE;
import static kunlun.io.util.IOUtils.EOF;

/**
 * The message digest tools.
 * (Support list: MD2, MD5, SHA-1, SHA-256, SHA-384, SHA-512)
 *
 * @see <a href="https://docs.oracle.com/javase/7/docs/technotes/guides/security/StandardNames.html#MessageDigest">MessageDigest Algorithms</a>
 * @author Kahle
 */
public class Hash extends AbstractDigester {

    @Override
    public byte[] digest(Config config, byte[] data) {
        Assert.notNull(config, "Parameter \"config\" must not null. ");
        Assert.notNull(data, "Parameter \"data\" must not null. ");
        try {
            MessageDigest md = MessageDigest.getInstance(config.getAlgorithm());
            return md.digest(data);
        } catch (Exception e) { throw ExceptionUtils.wrap(e); }
    }

    @Override
    public byte[] digest(Config config, InputStream data) {
        Assert.notNull(config, "Parameter \"config\" must not null. ");
        Assert.notNull(data, "Parameter \"data\" must not null. ");
        try {
            MessageDigest md = MessageDigest.getInstance(config.getAlgorithm());
            byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
            for (int len; (len = data.read(buffer)) != EOF;) {
                md.update(buffer, ZERO, len);
            }
            return md.digest();
        } catch (Exception e) { throw ExceptionUtils.wrap(e); }
    }

    /**
     * The configuration of the hash.
     * @author Kahle
     */
    public static class Cfg extends AbstractConfig {

        public static Cfg of(String algorithm) {

            return new Cfg(algorithm);
        }

        public Cfg(String algorithm) {

            super(algorithm);
        }

    }

}
