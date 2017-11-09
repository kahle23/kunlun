package saber.crypto;

import org.apache.commons.lang3.ArrayUtils;
import saber.util.Reflect;

import javax.crypto.Cipher;
import java.nio.charset.Charset;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * @author Kahle
 */
public class RSA {
    public static final String ALGORITHM_NAME = "RSA";
    public static final String DEFAULT_TRANSFORMATION = "RSA";
    public static final int DEFAULT_GENERATOR_KEY_SIZE = 2048;

    public static final String NONE_NO_PADDING = "RSA/None/NoPadding";
    public static final String NONE_PKCS_1_PADDING = "RSA/None/PKCS1Padding";
    public static final String ECB_NO_PADDING = "RSA/ECB/NoPadding";
    public static final String ECB_PKCS_1_PADDING = "RSA/ECB/PKCS1Padding";

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

    public static RSA on(String transformation) {
        return new RSA().setTransformation(transformation);
    }

    public static RSA on(int keySize) throws NoSuchAlgorithmException {
        return new RSA().setKeyPairGenerator(keySize);
    }

    public static RSA on(PublicKey publicKey) {
        return new RSA().setPublicKey(publicKey);
    }

    public static RSA on(PrivateKey privateKey) {
        return new RSA().setPrivateKey(privateKey);
    }

    public static RSA on(PublicKey publicKey, PrivateKey privateKey) {
        return new RSA().setPublicKey(publicKey).setPrivateKey(privateKey);
    }

    public static RSA on(byte[] publicKey, byte[] privateKey) throws GeneralSecurityException {
        RSA rsa = new RSA();
        if (ArrayUtils.isNotEmpty(publicKey)) {
            rsa.parsePublicKey(publicKey);
        }
        if (ArrayUtils.isNotEmpty(privateKey)) {
            rsa.parsePrivateKey(privateKey);
        }
        return rsa;
    }

    private KeyPairGenerator keyPairGenerator;
    private KeyFactory keyFactory;
    private String charset = Charset.defaultCharset().name();
    private String transformation = DEFAULT_TRANSFORMATION;
    private PublicKey publicKey;
    private PrivateKey privateKey;

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

    public RSA setKeyPairGenerator(int keySize) throws NoSuchAlgorithmException {
        this.keyPairGenerator = KeyPairGenerator.getInstance(ALGORITHM_NAME);
        keyPairGenerator.initialize(keySize);
        return this;
    }

    public KeyFactory getKeyFactory() {
        return keyFactory;
    }

    public RSA setKeyFactory(KeyFactory keyFactory) {
        this.keyFactory = keyFactory;
        return this;
    }

    public String getCharset() {
        return charset;
    }

    public RSA setCharset(String charset) {
        this.charset = charset;
        return this;
    }

    public String getTransformation() {
        return transformation;
    }

    public RSA setTransformation(String transformation) {
        this.transformation = transformation;
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

    public byte[] calc(byte[] data, int opmode) throws GeneralSecurityException {
        Cipher cipher = Cipher.getInstance(transformation);
        cipher.init(opmode, opmode == Cipher.ENCRYPT_MODE
                ? publicKey : opmode == Cipher.DECRYPT_MODE
                ? privateKey : null);
        return cipher.doFinal(data);
    }

    public byte[] encrypt(byte[] data) throws GeneralSecurityException {
        return calc(data, Cipher.ENCRYPT_MODE);
    }

    public byte[] encrypt(String data) throws GeneralSecurityException {
        return calc(data.getBytes(Charset.forName(charset)), Cipher.ENCRYPT_MODE);
    }

    public byte[] decrypt(byte[] data) throws GeneralSecurityException {
        return calc(data, Cipher.DECRYPT_MODE);
    }

}
