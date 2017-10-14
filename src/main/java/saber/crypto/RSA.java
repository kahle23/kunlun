package saber.crypto;

import saber.util.Reflect;

import javax.crypto.Cipher;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class RSA {
    public static final String ALGORITHM_NAME = "RSA";
    public static final String DEFAULT_TRANSFORMATION = "RSA";
    public static final int DEFAULT_GENERATOR_KEY_SIZE = 2048;

    static {
        String className = "org.bouncycastle.jce.provider.BouncyCastleProvider";
        ClassLoader classLoader = RSA.class.getClassLoader();
        if (Reflect.isPresent(className, classLoader)) {
            try {
                Provider provider = (Provider) Reflect.on(className).create().getBean();
                Security.addProvider(provider);
            } catch (ReflectiveOperationException e) {
                // ignore
            }
        }
    }

    public static RSA on() {
        return new RSA();
    }

    private KeyPairGenerator keyPairGenerator;
    private KeyFactory keyFactory;
    private PublicKey publicKey;
    private PrivateKey privateKey;
    private String transformation = DEFAULT_TRANSFORMATION;

    private RSA() {}

    private KeyPairGenerator keyPairGenerator() throws NoSuchAlgorithmException {
        if (keyPairGenerator == null) {
            keyPairGenerator = KeyPairGenerator.getInstance(ALGORITHM_NAME);
            keyPairGenerator.initialize(DEFAULT_GENERATOR_KEY_SIZE);
        }
        return keyPairGenerator;
    }

    private KeyFactory keyFactory() throws NoSuchAlgorithmException {
        if (keyFactory == null) {
            keyFactory = KeyFactory.getInstance(ALGORITHM_NAME);
        }
        return keyFactory;
    }

    public KeyPairGenerator getKeyPairGenerator() {
        return keyPairGenerator;
    }

    public RSA setKeyPairGenerator(KeyPairGenerator keyPairGenerator) {
        this.keyPairGenerator = keyPairGenerator;
        return this;
    }

    public KeyFactory getKeyFactory() {
        return keyFactory;
    }

    public RSA setKeyFactory(KeyFactory keyFactory) {
        this.keyFactory = keyFactory;
        return this;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public RSA setPublicKey(PublicKey publicKey) {
        this.publicKey = publicKey;
        return this;
    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    public RSA setPrivateKey(PrivateKey privateKey) {
        this.privateKey = privateKey;
        return this;
    }

    public String getTransformation() {
        return transformation;
    }

    public RSA setTransformation(String transformation) {
        this.transformation = transformation;
        return this;
    }

    public RSA generateKey() throws NoSuchAlgorithmException {
        KeyPair keyPair = keyPairGenerator().generateKeyPair();
        publicKey = keyPair.getPublic();
        privateKey = keyPair.getPrivate();
        return this;
    }

    public RSA parsePublicKey(byte[] array) throws GeneralSecurityException {
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(array);
        publicKey = keyFactory().generatePublic(x509EncodedKeySpec);
        return this;
    }

    public RSA parsePrivateKey(byte[] array) throws GeneralSecurityException {
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(array);
        privateKey = keyFactory().generatePrivate(pkcs8EncodedKeySpec);
        return this;
    }

    public byte[] encrypt(byte[] data) throws GeneralSecurityException {
        Cipher cipher = Cipher.getInstance(transformation);
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return cipher.doFinal(data);
    }

    public byte[] decrypt(byte[] data) throws GeneralSecurityException {
        Cipher cipher = Cipher.getInstance(transformation);
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return cipher.doFinal(data);
    }

}
