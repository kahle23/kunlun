/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.crypto.cipher;

import kunlun.codec.CodecUtils;
import kunlun.crypto.util.BouncyCastleSupport;
import kunlun.crypto.util.KeyUtils;
import kunlun.exception.ExceptionUtils;
import kunlun.logging.Logger;
import kunlun.logging.LoggerFactory;
import org.junit.Test;

import java.security.KeyPair;

import static kunlun.codec.CodecUtils.BASE64;
import static kunlun.common.constant.Algorithms.RSA;

/**
 * RSA/None/NoPadding
 * RSA/None/PKCS1Padding
 * RSA/ECB/NoPadding
 * RSA/ECB/PKCS1Padding
 */
public class RSATest extends BouncyCastleSupport {
    private static final Logger log = LoggerFactory.getLogger(RSATest.class);
    private static final AsymmetricCipher cipher = new AsymmetricCipher();
    private static final byte[] data = "Hello, Java!".getBytes();
    private static final KeyPair key;

    static {
        try {
            // RSA keys must be at least 512 bits long
            key = KeyUtils.generateKeyPair(RSA, 2048);
            log.info("Public key: {}", CodecUtils.encodeToString(BASE64, key.getPublic().getEncoded()));
            log.info("Private key: {}", CodecUtils.encodeToString(BASE64, key.getPrivate().getEncoded()));
        } catch (Exception e) { throw ExceptionUtils.wrap(e); }
    }

    private void testEncryptAndDecrypt(String algorithm) {
        log.info("Start test {}", algorithm);
        AsymmetricCipher.Cfg cfg = AsymmetricCipher.Cfg.of(algorithm, key);

        byte[] bytes = cipher.encrypt(cfg.usePublicKey(), data);
        log.info("Encrypt public key: {}", CodecUtils.encodeToString(BASE64, bytes));
        log.info("Decrypt private key: {}", cipher.decryptToString(cfg.usePrivateKey(), bytes));
        //log.info("Decrypt public key: {}", cipher.decryptToString(bytes, PUBLIC_KEY));

        bytes = cipher.encrypt(cfg.usePrivateKey(), data);
        log.info("Encrypt private key: {}", CodecUtils.encodeToString(BASE64, bytes));
        log.info("Decrypt public key: {}", cipher.decryptToString(cfg.usePublicKey(), bytes));
        //log.info("Decrypt private key: {}", cipher.decryptToString(bytes, PRIVATE_KEY));

        log.info("End test {}", algorithm);
    }

    @Test
    public void testNoneNoPadding() {

        testEncryptAndDecrypt("RSA/None/NoPadding");
    }

    @Test
    public void testNonePKCS1Padding() {

        testEncryptAndDecrypt("RSA/None/PKCS1Padding");
    }

    @Test
    public void testEcbNoPadding() {

        testEncryptAndDecrypt("RSA/ECB/NoPadding");
    }

    @Test
    public void testEcbPKCS1Padding() {

        testEncryptAndDecrypt("RSA/ECB/PKCS1Padding");
    }

}
