/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.crypto;

import kunlun.codec.ByteCodec;
import kunlun.codec.CodecUtils;
import kunlun.util.Assert;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.*;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import static kunlun.codec.CodecUtils.BASE64;
import static kunlun.codec.CodecUtils.HEX;

/**
 * Key tools.
 * @author Kahle
 */
public class KeyUtils {
    private static final SecureRandom RANDOM = new SecureRandom();
    private static ByteCodec base64 = (ByteCodec) CodecUtils.getCodec(BASE64);
    private static ByteCodec hex = (ByteCodec) CodecUtils.getCodec(HEX);

    public static void setBase64Encoder(ByteCodec base64) {
        Assert.notNull(base64, "Parameter \"base64\" must not null. ");
        KeyUtils.base64 = base64;
    }

    public static void setHexEncoder(ByteCodec hex) {
        Assert.notNull(hex, "Parameter \"hex\" must not null. ");
        KeyUtils.hex = hex;
    }

    public static SecretKey parseSecretKey(String algorithm, byte[] keyBytes) {
        Assert.notEmpty(keyBytes, "Parameter \"keyBytes\" must not empty. ");
        return new SecretKeySpec(keyBytes, algorithm);
    }

    public static PublicKey parsePublicKey(String algorithm, byte[] keyBytes) throws GeneralSecurityException {
        Assert.notEmpty(keyBytes, "Parameter \"keyBytes\" must not empty. ");
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory factory = KeyFactory.getInstance(algorithm);
        return factory.generatePublic(x509EncodedKeySpec);
    }

    public static PrivateKey parsePrivateKey(String algorithm, byte[] keyBytes) throws GeneralSecurityException {
        Assert.notEmpty(keyBytes, "Parameter \"keyBytes\" must not empty. ");
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory factory = KeyFactory.getInstance(algorithm);
        return factory.generatePrivate(pkcs8EncodedKeySpec);
    }

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
     * @param algorithm
     * @param params
     * @param random
     * @return
     * @throws GeneralSecurityException
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
     * @param algorithm
     * @param params
     * @param random
     * @return
     * @throws GeneralSecurityException
     * @see <a href="https://docs.oracle.com/javase/7/docs/technotes/guides/security/StandardNames.html#KeyPairGenerator">KeyPairGenerator Algorithms</a>
     */
    public static KeyPair generateKeyPair(String algorithm, AlgorithmParameterSpec params, SecureRandom random) throws GeneralSecurityException {
        KeyPairGenerator generator = KeyPairGenerator.getInstance(algorithm);
        generator.initialize(params, random != null ? random : RANDOM);
        return generator.generateKeyPair();
    }

    public static String toHexString(Key key) {

        return hex.encodeToString(key.getEncoded());
    }

    public static String toBase64String(Key key) {

        return base64.encodeToString(key.getEncoded());
    }

}
