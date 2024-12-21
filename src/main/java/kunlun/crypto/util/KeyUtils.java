/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.crypto.util;

import kunlun.exception.ExceptionUtils;
import kunlun.util.Assert;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.*;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * The crypto key tools.
 * @author Kahle
 */
public class KeyUtils {

    public static SecretKey parseSecretKey(String algorithm, byte[] keyBytes) {
        Assert.notEmpty(keyBytes, "Parameter \"keyBytes\" must not empty. ");
        return new SecretKeySpec(keyBytes, algorithm);
    }

    public static IvParameterSpec parseIv(byte[] ivBytes) {
        Assert.notEmpty(ivBytes, "Parameter \"ivBytes\" must not empty. ");
        return new IvParameterSpec(ivBytes);
    }

    public static PublicKey parsePublicKey(String algorithm, byte[] keyBytes) {
        try {
            Assert.notEmpty(keyBytes, "Parameter \"keyBytes\" must not empty. ");
            X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(keyBytes);
            KeyFactory factory = KeyFactory.getInstance(algorithm);
            return factory.generatePublic(x509EncodedKeySpec);
        } catch (Exception e) { throw ExceptionUtils.wrap(e); }
    }

    public static PrivateKey parsePrivateKey(String algorithm, byte[] keyBytes) {
        try {
            Assert.notEmpty(keyBytes, "Parameter \"keyBytes\" must not empty. ");
            PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory factory = KeyFactory.getInstance(algorithm);
            return factory.generatePrivate(pkcs8EncodedKeySpec);
        } catch (Exception e) { throw ExceptionUtils.wrap(e); }
    }

    // ====

    private static final SecureRandom RANDOM = new SecureRandom();

    public static SecretKey generateKey(String algorithm, int keySize) throws GeneralSecurityException {

        return KeyUtils.generateKey(algorithm, keySize, RANDOM);
    }

    public static SecretKey generateKey(String algorithm, AlgorithmParameterSpec params) throws GeneralSecurityException {

        return KeyUtils.generateKey(algorithm, params, RANDOM);
    }

    public static SecretKey generateKey(String algorithm, int keySize, SecureRandom random) throws GeneralSecurityException {
        KeyGenerator generator = KeyGenerator.getInstance(algorithm);
        generator.init(keySize, random != null ? random : RANDOM);
        return generator.generateKey();
    }

    /**
     * Generate SecretKey. List of supported algorithms: AES, ARCFOUR, Blowfish, DES
     *      , DESede, RC2, HmacMD5, HmacSHA1, HmacSHA256, HmacSHA384, HmacSHA512
     * @see <a href="https://docs.oracle.com/javase/7/docs/technotes/guides/security/StandardNames.html#KeyGenerator">KeyGenerator Algorithms</a>
     */
    public static SecretKey generateKey(String algorithm, AlgorithmParameterSpec params, SecureRandom random) throws GeneralSecurityException {
        KeyGenerator generator = KeyGenerator.getInstance(algorithm);
        generator.init(params, random != null ? random : RANDOM);
        return generator.generateKey();
    }

    public static KeyPair generateKeyPair(String algorithm, int keySize) throws GeneralSecurityException {

        return KeyUtils.generateKeyPair(algorithm, keySize, RANDOM);
    }

    public static KeyPair generateKeyPair(String algorithm, AlgorithmParameterSpec params) throws GeneralSecurityException {

        return KeyUtils.generateKeyPair(algorithm, params, RANDOM);
    }

    public static KeyPair generateKeyPair(String algorithm, int keySize, SecureRandom random) throws GeneralSecurityException {
        KeyPairGenerator generator = KeyPairGenerator.getInstance(algorithm);
        generator.initialize(keySize, random != null ? random : RANDOM);
        return generator.generateKeyPair();
    }

    /**
     * Generate PublicKey and PrivateKey.
     * List of supported algorithms: DiffieHellman, DSA, RSA, EC
     * @see <a href="https://docs.oracle.com/javase/7/docs/technotes/guides/security/StandardNames.html#KeyPairGenerator">KeyPairGenerator Algorithms</a>
     */
    public static KeyPair generateKeyPair(String algorithm, AlgorithmParameterSpec params, SecureRandom random) throws GeneralSecurityException {
        KeyPairGenerator generator = KeyPairGenerator.getInstance(algorithm);
        generator.initialize(params, random != null ? random : RANDOM);
        return generator.generateKeyPair();
    }

}
