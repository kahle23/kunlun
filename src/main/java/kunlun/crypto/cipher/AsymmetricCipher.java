/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.crypto.cipher;

import kunlun.exception.ExceptionUtils;
import kunlun.util.Assert;

import javax.crypto.Cipher;
import java.security.Key;
import java.security.KeyPair;

/**
 * The asymmetric encryption and decryption tools.
 * @author Kahle
 */
public class AsymmetricCipher extends AbstractCipher {

    public AsymmetricCipher(Config config) {

        super(config);
    }

    public AsymmetricCipher() {

        this(null);
    }

    protected Cfg convert(Config config) {
        Cfg cfg = config != null ? (Cfg) config : (Cfg) getConfig();
        Assert.notNull(cfg, "Parameter \"config\" must not null. ");
        Assert.notNull(cfg.getKey(), "Parameter \"config.key\" must not null. ");
        return cfg;
    }

    @Override
    public byte[] encrypt(Config config, byte[] data) {
        Assert.notEmpty(data, "Parameter \"data\" must not empty. ");
        Cfg cfg = convert(config);
        Key key = cfg.getKeyType() ? cfg.getKey().getPublic() : cfg.getKey().getPrivate();
        try {
            Cipher cipher = createCipher(cfg.getTransformation()
                    , Cipher.ENCRYPT_MODE, key, null, null);
            return cipher.doFinal(data);
        } catch (Exception e) { throw ExceptionUtils.wrap(e); }
    }

    @Override
    public byte[] decrypt(Config config, byte[] data) {
        Assert.notEmpty(data, "Parameter \"data\" must not empty. ");
        Cfg cfg = convert(config);
        Key key = cfg.getKeyType() ? cfg.getKey().getPublic() : cfg.getKey().getPrivate();
        try {
            Cipher cipher = createCipher(cfg.getTransformation()
                    , Cipher.DECRYPT_MODE, key, null, null);
            return cipher.doFinal(data);
        } catch (Exception e) { throw ExceptionUtils.wrap(e); }
    }

    /**
     * The configuration of the asymmetric cipher.
     * @author Kahle
     */
    public static class Cfg extends AbstractConfig {

        public static Cfg of(String transformation, KeyPair key) {

            return of(transformation).setKey(key);
        }

        public static Cfg of(String transformation) {

            return new Cfg(transformation);
        }

        /**
         * The key's data.
         */
        private KeyPair key;
        /**
         * The public key is true, the private key is false.
         */
        private boolean keyType = true;

        public Cfg(String transformation) {

            super(transformation);
        }

        @Override
        public KeyPair getKey() {

            return key;
        }

        public Cfg setKey(KeyPair key) {
            Assert.notNull(key, "Parameter \"key\" must not null. ");
            this.key = key;
            return this;
        }

        public boolean getKeyType() {

            return keyType;
        }

        public Cfg setKeyType(boolean keyType) {
            this.keyType = keyType;
            return this;
        }

        public Cfg usePublicKey() {

            return setKeyType(true);
        }

        public Cfg usePrivateKey() {

            return setKeyType(false);
        }
    }

}
