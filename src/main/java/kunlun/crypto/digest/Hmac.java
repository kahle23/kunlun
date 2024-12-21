/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.crypto.digest;

import kunlun.exception.ExceptionUtils;
import kunlun.util.Assert;

import javax.crypto.Mac;
import java.io.InputStream;
import java.security.Key;

import static kunlun.common.constant.Numbers.ZERO;
import static kunlun.io.util.IOUtils.DEFAULT_BUFFER_SIZE;
import static kunlun.io.util.IOUtils.EOF;

/**
 * The hmac tools.
 * (Support list: HMAC_MD5, HMAC_SHA1, HMAC_SHA256, HMAC_SHA384, HMAC_SHA512)
 *
 * @see <a href="https://docs.oracle.com/javase/7/docs/technotes/guides/security/StandardNames.html#Mac">Mac Algorithms</a>
 * @author Kahle
 */
public class Hmac extends AbstractDigester {

    @Override
    public byte[] digest(Config config, byte[] data) {
        Assert.notNull(config, "Parameter \"config\" must not null. ");
        Assert.notNull(data, "Parameter \"data\" must not null. ");
        Cfg cfg = (Cfg) config;
        Assert.notNull(cfg.getKey(), "Parameter \"config.key\" must not null. ");
        try {
            Mac mac = Mac.getInstance(cfg.getAlgorithm());
            mac.init(cfg.getKey());
            return mac.doFinal(data);
        } catch (Exception e) { throw ExceptionUtils.wrap(e); }
    }

    @Override
    public byte[] digest(Config config, InputStream data) {
        Assert.notNull(config, "Parameter \"config\" must not null. ");
        Assert.notNull(data, "Parameter \"data\" must not null. ");
        Cfg cfg = (Cfg) config;
        Assert.notNull(cfg.getKey(), "Parameter \"config.key\" must not null. ");
        try {
            Mac mac = Mac.getInstance(cfg.getAlgorithm());
            mac.init(cfg.getKey());
            byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
            for (int len; (len = data.read(buffer)) != EOF;) {
                mac.update(buffer, ZERO, len);
            }
            return mac.doFinal();
        } catch (Exception e) { throw ExceptionUtils.wrap(e); }
    }

    /**
     * The configuration of the hmac.
     * @author Kahle
     */
    public static class Cfg extends AbstractConfig {

        public static Cfg of(String algorithm, Key key) {

            return of(algorithm).setKey(key);
        }

        public static Cfg of(String algorithm) {

            return new Cfg(algorithm);
        }

        private Key key;

        public Cfg(String algorithm) {

            super(algorithm);
        }

        public Key getKey() {

            return key;
        }

        public Cfg setKey(Key key) {
            Assert.notNull(key, "Parameter \"key\" must not null. ");
            this.key = key;
            return this;
        }

    }

}
