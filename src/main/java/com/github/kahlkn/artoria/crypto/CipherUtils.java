package com.github.kahlkn.artoria.crypto;

import com.github.kahlkn.artoria.logging.Logger;
import com.github.kahlkn.artoria.logging.LoggerFactory;
import com.github.kahlkn.artoria.util.Assert;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.SecretKeySpec;
import java.security.*;
import java.security.cert.Certificate;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * Cipher tools.
 * @author Kahle
 */
public class CipherUtils {
    private static Logger log = LoggerFactory.getLogger(CipherUtils.class);

    public static byte[] fill(byte[] data, int multiple) {
        Assert.notEmpty(data, "Parameter \"data\" must not empty. ");
        Assert.state(multiple > 0, "Parameter \"multiple\" must greater than 0. ");
        int len = data.length;
        int fill = len % multiple;
        fill = fill != 0 ? multiple - fill : 0;
        byte[] result = new byte[len + fill];
        System.arraycopy(data, 0, result, 0, len);
        return result;
    }

    public static KeyPair generateKeyPair(String algorithmName, int generatorKeySize)
            throws GeneralSecurityException {
        KeyPairGenerator generator = KeyPairGenerator.getInstance(algorithmName);
        generator.initialize(generatorKeySize);
        return generator.generateKeyPair();
    }

    public static SecretKey parseSecretKey(String algorithmName, byte[] key)
            throws GeneralSecurityException {
        return new SecretKeySpec(key, algorithmName);
    }

    public static SecretKey parseSecretKey(String algorithmName, KeySpec key)
            throws GeneralSecurityException {
        SecretKeyFactory factory = SecretKeyFactory.getInstance(algorithmName);
        return factory.generateSecret(key);
    }

    public static PublicKey parsePublicKey(String algorithmName, byte[] key)
            throws GeneralSecurityException {
        Assert.notEmpty(key, "Parameter \"key\" must not empty. ");
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(key);
        KeyFactory factory = KeyFactory.getInstance(algorithmName);
        return factory.generatePublic(x509EncodedKeySpec);
    }

    public static PrivateKey parsePrivateKey(String algorithmName, byte[] key)
            throws GeneralSecurityException {
        Assert.notEmpty(key, "Parameter \"key\" must not empty. ");
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(key);
        KeyFactory factory = KeyFactory.getInstance(algorithmName);
        return factory.generatePrivate(pkcs8EncodedKeySpec);
    }

    public static Cipher getEncrypter(String transformation, Key key)
            throws GeneralSecurityException {
        Cipher cipher = Cipher.getInstance(transformation);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        return cipher;
    }

    public static Cipher getEncrypter(String transformation, Certificate certificate)
            throws GeneralSecurityException {
        Cipher cipher = Cipher.getInstance(transformation);
        cipher.init(Cipher.ENCRYPT_MODE, certificate);
        return cipher;
    }

    public static Cipher getEncrypter(String transformation, Key key, AlgorithmParameterSpec params)
            throws GeneralSecurityException {
        Cipher cipher = Cipher.getInstance(transformation);
        cipher.init(Cipher.ENCRYPT_MODE, key, params);
        return cipher;
    }

    public static Cipher getEncrypter(String transformation, Key key, AlgorithmParameters params)
            throws GeneralSecurityException {
        Cipher cipher = Cipher.getInstance(transformation);
        cipher.init(Cipher.ENCRYPT_MODE, key, params);
        return cipher;
    }

    public static Cipher getDecrypter(String transformation, Key key)
            throws GeneralSecurityException {
        Cipher cipher = Cipher.getInstance(transformation);
        cipher.init(Cipher.DECRYPT_MODE, key);
        return cipher;
    }

    public static Cipher getDecrypter(String transformation, Certificate key)
            throws GeneralSecurityException {
        Cipher cipher = Cipher.getInstance(transformation);
        cipher.init(Cipher.DECRYPT_MODE, key);
        return cipher;
    }

    public static Cipher getDecrypter(String transformation, Key key, AlgorithmParameterSpec params)
            throws GeneralSecurityException {
        Cipher cipher = Cipher.getInstance(transformation);
        cipher.init(Cipher.DECRYPT_MODE, key, params);
        return cipher;
    }

    public static Cipher getDecrypter(String transformation, Key key, AlgorithmParameters params)
            throws GeneralSecurityException {
        Cipher cipher = Cipher.getInstance(transformation);
        cipher.init(Cipher.DECRYPT_MODE, key, params);
        return cipher;
    }

}
