/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.crypto;

import kunlun.core.Cipher;
import kunlun.core.Digester;
import kunlun.crypto.cipher.AsymmetricCipher;
import kunlun.crypto.cipher.SymmetricCipher;
import kunlun.crypto.cipher.support.CustomCipher;
import kunlun.crypto.digest.Hash;
import kunlun.crypto.digest.Hmac;
import kunlun.crypto.digest.support.CustomDigester;
import kunlun.logging.Logger;
import kunlun.logging.LoggerFactory;
import kunlun.util.Assert;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;

import static kunlun.common.constant.Words.DEFAULT;

/**
 * The crypto tools.
 * @author Kahle
 */
public class CryptoUtils {
    private static final Logger log = LoggerFactory.getLogger(CryptoUtils.class);
    private static volatile CryptoProvider cryptoProvider;

    public static CryptoProvider getCryptoProvider() {
        if (cryptoProvider != null) { return cryptoProvider; }
        synchronized (CryptoUtils.class) {
            if (cryptoProvider != null) { return cryptoProvider; }
            CryptoUtils.setCryptoProvider(new SimpleCryptoProvider());
            registerDigester(DEFAULT, new CustomDigester());
            registerCipher(DEFAULT, new CustomCipher());
            return cryptoProvider;
        }
    }

    public static void setCryptoProvider(CryptoProvider cryptoProvider) {
        Assert.notNull(cryptoProvider, "Parameter \"cryptoProvider\" must not null. ");
        log.info("Set crypto provider: {}", cryptoProvider.getClass().getName());
        CryptoUtils.cryptoProvider = cryptoProvider;
    }

    // ====

    public static SymmetricCipher getSymmetricCipher() {

        return getCryptoProvider().getSymmetricCipher();
    }

    public static AsymmetricCipher getAsymmetricCipher() {

        return getCryptoProvider().getAsymmetricCipher();
    }

    public static Hash getHash() {

        return getCryptoProvider().getHash();
    }

    public static Hmac getHmac() {

        return getCryptoProvider().getHmac();
    }

    // ====

    public static void registerCipher(String transformation, Cipher cipher) {

        getCryptoProvider().registerCipher(transformation, cipher);
    }

    public static void deregisterCipher(String transformation) {

        getCryptoProvider().deregisterCipher(transformation);
    }

    public static Cipher getCipher(String transformation) {

        return getCryptoProvider().getCipher(transformation);
    }

    public static void registerDigester(String algorithm, Digester digester) {

        getCryptoProvider().registerDigester(algorithm, digester);
    }

    public static void deregisterDigester(String algorithm) {

        getCryptoProvider().deregisterDigester(algorithm);
    }

    public static Digester getDigester(String algorithm) {

        return getCryptoProvider().getDigester(algorithm);
    }

    // ====

    public static byte[] encrypt(Cipher.Config config, byte[] data) {

        return getCryptoProvider().encrypt(config, data);
    }

    public static byte[] decrypt(Cipher.Config config, byte[] data) {

        return getCryptoProvider().decrypt(config, data);
    }

    public static void encrypt(Cipher.Config config, InputStream data, OutputStream out) {

        getCryptoProvider().encrypt(config, data, out);
    }

    public static void decrypt(Cipher.Config config, InputStream data, OutputStream out) {

        getCryptoProvider().decrypt(config, data, out);
    }

    public static byte[] digest(Digester.Config config, byte[] data) {

        return getCryptoProvider().digest(config, data);
    }

    public static byte[] digest(Digester.Config config, InputStream data) {

        return getCryptoProvider().digest(config, data);
    }

    // ====

    public static String encryptToHex(Cipher.Config config, byte[] data) {

        return ((AbstractCryptoProvider) getCryptoProvider()).encryptToHex(config, data);
    }

    public static String encryptToBase64(Cipher.Config config, byte[] data) {

        return ((AbstractCryptoProvider) getCryptoProvider()).encryptToBase64(config, data);
    }

    public static String decryptToString(Cipher.Config config, byte[] data, Charset charset) {

        return ((AbstractCryptoProvider) getCryptoProvider()).decryptToString(config, data, charset);
    }

    public static String decryptToString(Cipher.Config config, byte[] data) {

        return ((AbstractCryptoProvider) getCryptoProvider()).decryptToString(config, data);
    }

    // ====

    public static byte[] encrypt(Cipher.Config config, String data, Charset charset) {

        return ((AbstractCryptoProvider) getCryptoProvider()).encrypt(config, data, charset);
    }

    public static byte[] encrypt(Cipher.Config config, String data) {

        return ((AbstractCryptoProvider) getCryptoProvider()).encrypt(config, data);
    }

    public static String encryptToHex(Cipher.Config config, String data, Charset charset) {

        return ((AbstractCryptoProvider) getCryptoProvider()).encryptToHex(config, data, charset);
    }

    public static String encryptToHex(Cipher.Config config, String data) {

        return ((AbstractCryptoProvider) getCryptoProvider()).encryptToHex(config, data);
    }

    public static String encryptToBase64(Cipher.Config config, String data, Charset charset) {

        return ((AbstractCryptoProvider) getCryptoProvider()).encryptToBase64(config, data, charset);
    }

    public static String encryptToBase64(Cipher.Config config, String data) {

        return ((AbstractCryptoProvider) getCryptoProvider()).encryptToBase64(config, data);
    }

    // ====

    public static String digestToHex(Digester.Config config, byte[] data) {

        return ((AbstractCryptoProvider) getCryptoProvider()).digestToHex(config, data);
    }

    public static String digestToBase64(Digester.Config config, byte[] data) {

        return ((AbstractCryptoProvider) getCryptoProvider()).digestToBase64(config, data);
    }

    public static String digestToHex(Digester.Config config, InputStream data) {

        return ((AbstractCryptoProvider) getCryptoProvider()).digestToHex(config, data);
    }

    public static String digestToBase64(Digester.Config config, InputStream data) {

        return ((AbstractCryptoProvider) getCryptoProvider()).digestToBase64(config, data);
    }

    // ====

    public static byte[] digest(Digester.Config config, String data, Charset charset) {

        return ((AbstractCryptoProvider) getCryptoProvider()).digest(config, data, charset);
    }

    public static byte[] digest(Digester.Config config, String data) {

        return ((AbstractCryptoProvider) getCryptoProvider()).digest(config, data);
    }

    public static String digestToHex(Digester.Config config, String data, Charset charset) {

        return ((AbstractCryptoProvider) getCryptoProvider()).digestToHex(config, data, charset);
    }

    public static String digestToHex(Digester.Config config, String data) {

        return ((AbstractCryptoProvider) getCryptoProvider()).digestToHex(config, data);
    }

    public static String digestToBase64(Digester.Config config, String data, Charset charset) {

        return ((AbstractCryptoProvider) getCryptoProvider()).digestToBase64(config, data, charset);
    }

    public static String digestToBase64(Digester.Config config, String data) {

        return ((AbstractCryptoProvider) getCryptoProvider()).digestToBase64(config, data);
    }

    // ====




    // ====

    public static byte[] encrypt(byte[] data) {

        return encrypt(null, data);
    }

    public static byte[] decrypt(byte[] data) {

        return decrypt(null, data);
    }

    public static void encrypt(InputStream data, OutputStream out) {

        encrypt(null, data, out);
    }

    public static void decrypt(InputStream data, OutputStream out) {

        decrypt(null, data, out);
    }

    public static byte[] digest(byte[] data) {

        return digest(null, data);
    }

    public static byte[] digest(InputStream data) {

        return digest(null, data);
    }

    // ====

    public static String encryptToHex(byte[] data) {

        return encryptToHex(null, data);
    }

    public static String encryptToBase64(byte[] data) {

        return encryptToBase64(null, data);
    }

    public static String decryptToString(byte[] data, Charset charset) {

        return decryptToString(null, data, charset);
    }

    public static String decryptToString(byte[] data) {

        return decryptToString(null, data);
    }

    // ====

    public static byte[] encrypt(String data, Charset charset) {

        return encrypt(null, data, charset);
    }

    public static byte[] encrypt(String data) {

        return encrypt(null, data);
    }

    public static String encryptToHex(String data, Charset charset) {

        return encryptToHex(null, data, charset);
    }

    public static String encryptToHex(String data) {

        return encryptToHex(null, data);
    }

    public static String encryptToBase64(String data, Charset charset) {

        return encryptToBase64(null, data, charset);
    }

    public static String encryptToBase64(String data) {

        return encryptToBase64(null, data);
    }

    // ====

    public static String digestToHex(byte[] data) {

        return digestToHex(null, data);
    }

    public static String digestToBase64(byte[] data) {

        return digestToBase64(null, data);
    }

    public static String digestToHex(InputStream data) {

        return digestToHex(null, data);
    }

    public static String digestToBase64(InputStream data) {

        return digestToBase64(null, data);
    }

    // ====

    public static byte[] digest(String data, Charset charset) {

        return digest(null, data, charset);
    }

    public static byte[] digest(String data) {

        return digest(null, data);
    }

    public static String digestToHex(String data, Charset charset) {

        return digestToHex(null, data, charset);
    }

    public static String digestToHex(String data) {

        return digestToHex(null, data);
    }

    public static String digestToBase64(String data, Charset charset) {

        return digestToBase64(null, data, charset);
    }

    public static String digestToBase64(String data) {

        return digestToBase64(null, data);
    }

    // ====

}
