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
 *
 * @see <a href="https://docs.oracle.com/javase/7/docs/technotes/guides/security/StandardNames.html#KeyGenerator">
 *     KeyGenerator Algorithms</a>
 * @see <a href="https://docs.oracle.com/javase/7/docs/technotes/guides/security/StandardNames.html#KeyPairGenerator">
 *     KeyPairGenerator Algorithms</a>
 * @author Kahle
 */
public class KeyUtils {

    public static PrivateKey parsePrivateKey(String algorithm, byte[] keyBytes) {
        Assert.notEmpty(keyBytes, "Parameter \"keyBytes\" must not empty. ");
        try {
            PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory factory = KeyFactory.getInstance(algorithm);
            return factory.generatePrivate(pkcs8EncodedKeySpec);
        } catch (Exception e) { throw ExceptionUtils.wrap(e); }
    }

    public static PublicKey  parsePublicKey(String algorithm, byte[] keyBytes) {
        Assert.notEmpty(keyBytes, "Parameter \"keyBytes\" must not empty. ");
        try {
            X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(keyBytes);
            KeyFactory factory = KeyFactory.getInstance(algorithm);
            return factory.generatePublic(x509EncodedKeySpec);
        } catch (Exception e) { throw ExceptionUtils.wrap(e); }
    }

    public static SecretKey  parseSecretKey(String algorithm, byte[] keyBytes) {
        Assert.notEmpty(keyBytes, "Parameter \"keyBytes\" must not empty. ");
        return new SecretKeySpec(keyBytes, algorithm);
    }

    public static IvParameterSpec parseIvParamSpec(byte[] ivBytes) {
        Assert.notEmpty(ivBytes, "Parameter \"ivBytes\" must not empty. ");
        return new IvParameterSpec(ivBytes);
    }

    // ==== Generate the secret key ====
    // Supported algorithms: AES, ARCFOUR, Blowfish, DES, DESede, RC2, HmacMD5, HmacSHA1, HmacSHA256, HmacSHA384, HmacSHA512.

    public static SecretKey genKey(String algorithm, int keySize) {

        return genKey(algorithm, keySize, null);
    }

    public static SecretKey genKey(String algorithm, int keySize, SecureRandom random) {
        try {
            KeyGenerator gen = KeyGenerator.getInstance(algorithm);
            if (random != null) {
                gen.init(keySize, random);
            } else {
                gen.init(keySize);
            }
            return gen.generateKey();
        } catch (Exception e) { throw ExceptionUtils.wrap(e); }
    }

    public static SecretKey genKey(String algorithm, AlgorithmParameterSpec paramSpec) {

        return genKey(algorithm, paramSpec, null);
    }

    public static SecretKey genKey(String algorithm, AlgorithmParameterSpec paramSpec, SecureRandom random) {
        try {
            KeyGenerator gen = KeyGenerator.getInstance(algorithm);
            if (random != null) {
                gen.init(paramSpec, random);
            } else {
                gen.init(paramSpec);
            }
            return gen.generateKey();
        } catch (Exception e) { throw ExceptionUtils.wrap(e); }
    }

    // ==== Generate the public key and the private key ====
    // Supported algorithms: DiffieHellman, DSA, RSA, EC.

    public static KeyPair genKeyPair(String algorithm, int keySize) {

        return genKeyPair(algorithm, keySize, null);
    }

    public static KeyPair genKeyPair(String algorithm, int keySize, SecureRandom random) {
        try {
            KeyPairGenerator gen = KeyPairGenerator.getInstance(algorithm);
            if (random != null) {
                gen.initialize(keySize, random);
            } else {
                gen.initialize(keySize);
            }
            return gen.generateKeyPair();
        } catch (Exception e) { throw ExceptionUtils.wrap(e); }
    }

    public static KeyPair genKeyPair(String algorithm, AlgorithmParameterSpec paramSpec) {

        return genKeyPair(algorithm, paramSpec, null);
    }

    public static KeyPair genKeyPair(String algorithm, AlgorithmParameterSpec paramSpec, SecureRandom random) {
        try {
            KeyPairGenerator gen = KeyPairGenerator.getInstance(algorithm);
            if (random != null) {
                gen.initialize(paramSpec, random);
            } else {
                gen.initialize(paramSpec);
            }
            return gen.generateKeyPair();
        } catch (Exception e) { throw ExceptionUtils.wrap(e); }
    }

}
