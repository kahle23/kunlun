/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.crypto.cipher;

import kunlun.codec.CodecUtils;
import kunlun.common.constant.Charsets;
import kunlun.core.Cipher;
import kunlun.exception.ExceptionUtils;
import kunlun.io.util.IOUtils;
import kunlun.util.Assert;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;

import static kunlun.codec.CodecUtils.BASE64;
import static kunlun.codec.CodecUtils.HEX;

public abstract class AbstractCipher implements Cipher {
    private final Config config;

    protected AbstractCipher(Config config) {

        this.config = config;
    }

    public Config getConfig() {

        return config;
    }

    // ====

    @Override
    public void encrypt(Config config, InputStream data, OutputStream out) {
        Assert.notNull(data, "Parameter \"data\" must not null. ");
        Assert.notNull(out, "Parameter \"out\" must not null. ");
        try {
            out.write(encrypt(config, IOUtils.toByteArray(data)));
        } catch (Exception e) { throw ExceptionUtils.wrap(e); }
    }

    @Override
    public void decrypt(Config config, InputStream data, OutputStream out) {
        Assert.notNull(data, "Parameter \"data\" must not null. ");
        Assert.notNull(out, "Parameter \"out\" must not null. ");
        try {
            out.write(decrypt(config, IOUtils.toByteArray(data)));
        } catch (Exception e) { throw ExceptionUtils.wrap(e); }
    }

    // ====

    public String encryptToHex(Config config, byte[] data) {

        return CodecUtils.encodeToString(HEX, encrypt(config, data));
    }

    public String encryptToBase64(Config config, byte[] data) {

        return CodecUtils.encodeToString(BASE64, encrypt(config, data));
    }

    public String decryptToString(Config config, byte[] data, Charset charset) {
        charset = charset != null ? charset : Charsets.UTF_8;
        byte[] decrypt = decrypt(config, data);
        return new String(decrypt, charset);
    }

    public String decryptToString(Config config, byte[] data) {

        return decryptToString(config, data, null);
    }

    // ====

    public byte[] encrypt(Config config, String data, Charset charset) {
        Assert.notBlank(data, "Parameter \"data\" must not blank. ");
        charset = charset != null ? charset : Charsets.UTF_8;
        return encrypt(config, data.getBytes(charset));
    }

    public byte[] encrypt(Config config, String data) {

        return encrypt(config, data, null);
    }

    public String encryptToHex(Config config, String data, Charset charset) {

        return CodecUtils.encodeToString(HEX, encrypt(config, data, charset));
    }

    public String encryptToHex(Config config, String data) {

        return encryptToHex(config, data, null);
    }

    public String encryptToBase64(Config config, String data, Charset charset) {

        return CodecUtils.encodeToString(BASE64, encrypt(config, data, charset));
    }

    public String encryptToBase64(Config config, String data) {

        return encryptToBase64(config, data, null);
    }

    // ====

    /**
     * Create "Cipher" object based on existing conditions.
     * @param cipherMode Cipher mode (encrypt or decrypt)
     * @param key The key for encryption or decryption
     * @return The "Cipher" object that was successfully created
     * @throws GeneralSecurityException Some Java encryption and decryption exceptions
     * @see javax.crypto.Cipher#ENCRYPT_MODE Encryption mode
     * @see javax.crypto.Cipher#DECRYPT_MODE Decryption mode
     */
    public static javax.crypto.Cipher createCipher(String transformation, int cipherMode,
                                                   java.security.Key key,
                                                   AlgorithmParameterSpec paramSpec,
                                                   SecureRandom random) throws GeneralSecurityException {
        Assert.notBlank(transformation, "Parameter \"transformation\" must not blank. ");
        Assert.notNull(key, "Parameter \"key\" must not null. ");
        javax.crypto.Cipher cipher = javax.crypto.Cipher.getInstance(transformation);
        if (paramSpec != null) {
            if (random != null) {
                cipher.init(cipherMode, key, paramSpec, random);
            } else {
                cipher.init(cipherMode, key, paramSpec);
            }
        } else {
            if (random != null) {
                cipher.init(cipherMode, key, random);
            } else {
                cipher.init(cipherMode, key);
            }
        }
        return cipher;
    }

    /**
     * The abstract configuration of the cipher.
     * @author Kahle
     */
    public static abstract class AbstractConfig implements Config {
        private final String transformation;

        public AbstractConfig(String transformation) {
            Assert.notBlank(transformation, "Parameter \"transformation\" must not blank. ");
            this.transformation = transformation;
        }

        @Override
        public String getTransformation() {

            return transformation;
        }
    }

}
