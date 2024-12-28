/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.crypto.util;

import kunlun.codec.CodecUtils;
import kunlun.logging.Logger;
import kunlun.logging.LoggerFactory;
import org.junit.Test;

import javax.crypto.SecretKey;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Arrays;

import static kunlun.common.constant.Algorithms.*;
import static org.junit.Assert.assertArrayEquals;

/**
 * The crypto key tools Test.
 * @author Kahle
 */
public class KeyUtilsTest {
    private static final Logger log = LoggerFactory.getLogger(KeyUtilsTest.class);

    private void testGenKey(String algorithm, int keySize) {
        SecretKey secretKey = KeyUtils.genKey(algorithm, keySize);
        algorithm = secretKey.getAlgorithm();
        String format = secretKey.getFormat();
        String content = CodecUtils.encodeToHex(secretKey.getEncoded());
        String message = "algorithm: {}, format: {}, key size: {}, content for hex: {}";
        log.info(message, algorithm, format, keySize, content);
    }

    private void testGenKeyPair(String algorithm, int keySize) {
        String publicKeyMessage = "publicKey >> algorithm: {}, format: {}, key size: {}, content for base64: {}";
        String privateKeyMessage = "privateKey >> algorithm: {}, format: {}, key size: {}, content for base64: {}";
        String format, content;
        KeyPair keyPair = KeyUtils.genKeyPair(algorithm, keySize);

        PublicKey publicKey = keyPair.getPublic();
        algorithm = publicKey.getAlgorithm();
        format = publicKey.getFormat();
        content = CodecUtils.encodeToBase64(publicKey.getEncoded());
        log.info(publicKeyMessage, algorithm, format, keySize, content);

        PrivateKey privateKey = keyPair.getPrivate();
        algorithm = privateKey.getAlgorithm();
        format = privateKey.getFormat();
        content = CodecUtils.encodeToBase64(privateKey.getEncoded());
        log.info(privateKeyMessage, algorithm, format, keySize, content);
    }

    @Test
    public void testParseSecretKey() {
        SecretKey secretKey = KeyUtils.genKey(AES, 128);
        log.info("Secret key array: {}", Arrays.toString(secretKey.getEncoded()));
        SecretKey secretKey1 = KeyUtils.parseSecretKey(AES, secretKey.getEncoded());
        log.info("Parse secret key array: {}", Arrays.toString(secretKey1.getEncoded()));
        assertArrayEquals(secretKey.getEncoded(), secretKey1.getEncoded());
    }

    @Test
    public void testParsePublicKeyAndParsePrivateKey() {
        KeyPair keyPair = KeyUtils.genKeyPair(RSA, 512);
        PrivateKey privateKey = keyPair.getPrivate();
        PublicKey publicKey = keyPair.getPublic();
        log.info("Public key array: {}", Arrays.toString(publicKey.getEncoded()));
        PublicKey publicKey1 = KeyUtils.parsePublicKey(RSA, publicKey.getEncoded());
        log.info("Parse public key array: {}", Arrays.toString(publicKey1.getEncoded()));
        assertArrayEquals(publicKey.getEncoded(), publicKey1.getEncoded());
        log.info("Private key array: {}", Arrays.toString(privateKey.getEncoded()));
        PrivateKey privateKey1 = KeyUtils.parsePrivateKey(RSA, privateKey.getEncoded());
        log.info("Parse private key array: {}", Arrays.toString(privateKey1.getEncoded()));
        assertArrayEquals(privateKey.getEncoded(), privateKey1.getEncoded());
    }

    @Test
    public void testGenerateKeyForAES() {
        // Wrong keySize: must be equal to 128, 192 or 256
        testGenKey(AES, 128);
        testGenKey(AES, 192);
        testGenKey(AES, 256);
    }

    @Test
    public void testGenerateKeyForBlowfish() {
        // KeySize must be multiple of 8, and can only range from 32 to 448 (inclusive)
        testGenKey(BLOWFISH, 32);
        testGenKey(BLOWFISH, 192);
        testGenKey(BLOWFISH, 448);
    }

    @Test
    public void testGenerateKeyForDES() {
        // Wrong keySize: must be equal to 56
        testGenKey(DES, 56);
    }

    @Test
    public void testGenerateKeyForDESede() {
        // Wrong keySize: must be equal to 112 or 168
        testGenKey(DESEDE, 112);
        testGenKey(DESEDE, 168);
    }

    @Test
    public void testGenerateKeyByKeySizeForRSA() {
        // RSA keys must be at least 512 bits long
        testGenKeyPair(RSA, 512);
        testGenKeyPair(RSA, 888);
    }

    @Test
    public void testToHexString() {
        SecretKey secretKey = KeyUtils.genKey(AES, 128);
        log.info("Hex string: {}", CodecUtils.encodeToHex(secretKey.getEncoded()));
        secretKey = KeyUtils.genKey(AES, 256);
        log.info("Hex string: {}", CodecUtils.encodeToHex(secretKey.getEncoded()));
    }

    @Test
    public void testToBase64String() {
        SecretKey secretKey = KeyUtils.genKey(AES, 128);
        log.info("Base64 string: {}", CodecUtils.encodeToBase64(secretKey.getEncoded()));
        secretKey = KeyUtils.genKey(AES, 256);
        log.info("Base64 string: {}", CodecUtils.encodeToBase64(secretKey.getEncoded()));
    }

}
