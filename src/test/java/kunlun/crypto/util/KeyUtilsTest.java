/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.crypto.util;

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

public class KeyUtilsTest {
    private static Logger log = LoggerFactory.getLogger(KeyUtilsTest.class);

    private void testGenerateKey(String algorithm, int keySize) throws Exception {
        SecretKey secretKey = KeyUtils.generateKey(algorithm, keySize);
        algorithm = secretKey.getAlgorithm();
        String format = secretKey.getFormat();
        String content = KeyUtils.toHexString(secretKey);
        String message = "algorithm: {}, format: {}, key size: {}, content for hex: {}";
        log.info(message, algorithm, format, keySize, content);
    }

    private void testGenerateKeyPair(String algorithm, int keySize) throws Exception {
        String publicKeyMessage = "publicKey >> algorithm: {}, format: {}, key size: {}, content for base64: {}";
        String privateKeyMessage = "privateKey >> algorithm: {}, format: {}, key size: {}, content for base64: {}";
        String format, content;
        KeyPair keyPair = KeyUtils.generateKeyPair(algorithm, keySize);

        PublicKey publicKey = keyPair.getPublic();
        algorithm = publicKey.getAlgorithm();
        format = publicKey.getFormat();
        content = KeyUtils.toBase64String(publicKey);
        log.info(publicKeyMessage, algorithm, format, keySize, content);

        PrivateKey privateKey = keyPair.getPrivate();
        algorithm = privateKey.getAlgorithm();
        format = privateKey.getFormat();
        content = KeyUtils.toBase64String(privateKey);
        log.info(privateKeyMessage, algorithm, format, keySize, content);
    }

    @Test
    public void testParseSecretKey() throws Exception {
        SecretKey secretKey = KeyUtils.generateKey(AES, 128);
        log.info("Secret key array: {}", Arrays.toString(secretKey.getEncoded()));
        SecretKey secretKey1 = KeyUtils.parseSecretKey(AES, secretKey.getEncoded());
        log.info("Parse secret key array: {}", Arrays.toString(secretKey1.getEncoded()));
        assertArrayEquals(secretKey.getEncoded(), secretKey1.getEncoded());
    }

    @Test
    public void testParsePublicKeyAndParsePrivateKey() throws Exception {
        KeyPair keyPair = KeyUtils.generateKeyPair(RSA, 512);
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
    public void testGenerateKeyForAES() throws Exception {
        // Wrong keySize: must be equal to 128, 192 or 256
        this.testGenerateKey(AES, 128);
        this.testGenerateKey(AES, 192);
        this.testGenerateKey(AES, 256);
    }

    @Test
    public void testGenerateKeyForBlowfish() throws Exception {
        // KeySize must be multiple of 8, and can only range from 32 to 448 (inclusive)
        this.testGenerateKey(BLOWFISH, 32);
        this.testGenerateKey(BLOWFISH, 192);
        this.testGenerateKey(BLOWFISH, 448);
    }

    @Test
    public void testGenerateKeyForDES() throws Exception {
        // Wrong keySize: must be equal to 56
        this.testGenerateKey(DES, 56);
    }

    @Test
    public void testGenerateKeyForDESede() throws Exception {
        // Wrong keySize: must be equal to 112 or 168
        this.testGenerateKey(DESEDE, 112);
        this.testGenerateKey(DESEDE, 168);
    }

    @Test
    public void testGenerateKeyByKeySizeForRSA() throws Exception {
        // RSA keys must be at least 512 bits long
        this.testGenerateKeyPair(RSA, 512);
        this.testGenerateKeyPair(RSA, 888);
    }

    @Test
    public void testToHexString() throws Exception {
        SecretKey secretKey = KeyUtils.generateKey(AES, 128);
        log.info("Hex string: {}", KeyUtils.toHexString(secretKey));
        secretKey = KeyUtils.generateKey(AES, 256);
        log.info("Hex string: {}", KeyUtils.toHexString(secretKey));
    }

    @Test
    public void testToBase64String() throws Exception {
        SecretKey secretKey = KeyUtils.generateKey(AES, 128);
        log.info("Base64 string: {}", KeyUtils.toBase64String(secretKey));
        secretKey = KeyUtils.generateKey(AES, 256);
        log.info("Base64 string: {}", KeyUtils.toBase64String(secretKey));
    }

}
