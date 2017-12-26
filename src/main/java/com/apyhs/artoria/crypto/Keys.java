package com.apyhs.artoria.crypto;

import com.apyhs.artoria.util.Assert;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.SecretKeySpec;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class Keys {

    public static PublicKey parsePublicKey(String algorithmName, byte[] key) throws NoSuchAlgorithmException, InvalidKeySpecException {
        Assert.notEmpty(key, "Key must is not empty. ");
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(key);
        KeyFactory keyFactory = KeyFactory.getInstance(algorithmName);
        return keyFactory.generatePublic(x509EncodedKeySpec);
    }

    public static PrivateKey parsePrivateKey(String algorithmName, byte[] key) throws NoSuchAlgorithmException, InvalidKeySpecException {
        Assert.notEmpty(key, "Key must is not empty. ");
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(key);
        KeyFactory keyFactory = KeyFactory.getInstance(algorithmName);
        return keyFactory.generatePrivate(pkcs8EncodedKeySpec);
    }

    public static SecretKeySpec parseSecretKey(String algorithmName, byte[] key) {
        return new SecretKeySpec(key, algorithmName);
    }

    public static SecretKey parseSecretKey(String algorithmName, KeySpec key) throws NoSuchAlgorithmException, InvalidKeySpecException {
        return SecretKeyFactory.getInstance(algorithmName).generateSecret(key);
    }

    public static KeyPair generateKeyPair(String algorithmName, int generatorKeySize) throws NoSuchAlgorithmException {
        KeyPairGenerator generator = KeyPairGenerator.getInstance(algorithmName);
        generator.initialize(generatorKeySize);
        return generator.generateKeyPair();
    }

}
