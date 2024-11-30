/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.crypto.cipher;

import kunlun.exception.ExceptionUtils;
import kunlun.util.Assert;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import java.security.Key;

import static kunlun.common.constant.Numbers.*;

/**
 * Simple implementation of symmetric encryption and decryption tools.
 * @author Kahle
 */
public class SymmetricCipher extends AbstractCipher {

    public SymmetricCipher(Config config) {

        super(config);
    }

    public SymmetricCipher() {

        this(null);
    }

    private byte[] fillZeroToNoPadding(String transformation, byte[] data) {
        Assert.notEmpty(data, "Parameter \"data\" must not empty. ");
        int multiple;
        if (transformation.startsWith("AES/")) {
            multiple = SIXTEEN;
        }
        else if (transformation.startsWith("DES/")
                || transformation.startsWith("DESede/")
                || transformation.startsWith("Blowfish/")) {
            multiple = EIGHT;
        }
        else {
            return data;
        }
        int len = data.length, fill = len % multiple;
        fill = fill != ZERO ? multiple - fill : ZERO;
        byte[] result = new byte[len + fill];
        System.arraycopy(data, ZERO, result, ZERO, len);
        return result;
    }

    private String getTransformation(Cfg cfg) {
        if (cfg.getSupportZeroPadding() && cfg.getTransformation().endsWith("/ZeroPadding")) {
            return cfg.getTransformation().replace("/ZeroPadding", "/NoPadding");
        }
        return cfg.getTransformation();
    }

    @Override
    public byte[] encrypt(Config config, byte[] data) {
        try {
            Cfg cfg = config != null ? (Cfg) config : (Cfg) getConfig();
            Assert.notNull(cfg, "Parameter \"config\" must not null. ");
            Assert.notNull(cfg.getKey(), "Parameter \"config.secretKey\" must not null. ");
            // NoSuchAlgorithmException: Cannot find any provider supporting AES/CBC/ZeroPadding
            if (cfg.getSupportZeroPadding() && cfg.getTransformation().endsWith("/ZeroPadding")) {
                data = fillZeroToNoPadding(cfg.getTransformation(), data);
            }
            Cipher cipher = createCipher(getTransformation(cfg)
                    , Cipher.ENCRYPT_MODE, cfg.getKey(), cfg.getIv(), null);
            return cipher.doFinal(data);
        } catch (Exception e) { throw ExceptionUtils.wrap(e); }
    }

    @Override
    public byte[] decrypt(Config config, byte[] data) {
        try {
            Cfg cfg = config != null ? (Cfg) config : (Cfg) getConfig();
            Assert.notNull(cfg, "Parameter \"config\" must not null. ");
            Assert.notNull(cfg.getKey(), "Parameter \"config.secretKey\" must not null. ");
            Cipher cipher = createCipher(getTransformation(cfg)
                    , Cipher.DECRYPT_MODE, cfg.getKey(), cfg.getIv(), null);
            return cipher.doFinal(data);
        } catch (Exception e) { throw ExceptionUtils.wrap(e); }
    }

    /**
     * The configuration of the symmetric cipher.
     * @author Kahle
     */
    public static class Cfg extends AbstractConfig {

        public static Cfg of(String transformation, Key key) {

            return of(transformation).setKey(key);
        }

        public static Cfg of(String transformation) {

            return new Cfg(transformation);
        }

        private Key key;
        private IvParameterSpec iv;

        private boolean supportZeroPadding = true;

        public Cfg(String transformation) {

            super(transformation);
        }

        @Override
        public Key getKey() {

            return key;
        }

        public Cfg setKey(Key key) {
            Assert.notNull(key, "Parameter \"key\" must not null. ");
            this.key = key;
            return this;
        }

        public IvParameterSpec getIv() {

            return iv;
        }

        public Cfg setIv(IvParameterSpec iv) {
            Assert.notNull(iv, "Parameter \"iv\" must not null. ");
            this.iv = iv;
            return this;
        }

        @Deprecated
        public boolean getSupportZeroPadding() {

            return supportZeroPadding;
        }

        @Deprecated // todo need update name
        public Cfg setSupportZeroPadding(boolean supportZeroPadding) {
            this.supportZeroPadding = supportZeroPadding;
            return this;
        }
    }

}
