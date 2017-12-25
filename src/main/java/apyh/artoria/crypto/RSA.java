package apyh.artoria.crypto;

import apyh.artoria.exception.UnexpectedException;
import apyh.artoria.util.Assert;

import java.security.*;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * @author Kahle
 */
public class RSA extends Cipher {
    public static final int DEFAULT_GENERATOR_KEY_SIZE = 2048;

    public static final String NONE_NO_PADDING = "RSA/None/NoPadding";
    public static final String NONE_PKCS_1_PADDING = "RSA/None/PKCS1Padding";
    public static final String ECB_NO_PADDING = "RSA/ECB/NoPadding";
    public static final String ECB_PKCS_1_PADDING = "RSA/ECB/PKCS1Padding";

    private KeyPairGenerator keyPairGenerator;
    private KeyFactory keyFactory;
    private Key key;

    protected RSA() {
    }

    private KeyPairGenerator keyPairGenerator() throws NoSuchAlgorithmException {
        if (keyPairGenerator == null) {
            keyPairGenerator = KeyPairGenerator.getInstance(this.getAlgorithmName());
            keyPairGenerator.initialize(DEFAULT_GENERATOR_KEY_SIZE);
        }
        return keyPairGenerator;
    }

    private KeyFactory keyFactory() throws NoSuchAlgorithmException {
        if (keyFactory == null) {
            keyFactory = KeyFactory.getInstance(this.getAlgorithmName());
        }
        return keyFactory;
    }

    public RSA setKeyPairGenerator(int keySize) throws NoSuchAlgorithmException {
        this.keyPairGenerator = KeyPairGenerator.getInstance(this.getAlgorithmName());
        keyPairGenerator.initialize(keySize);
        return this;
    }

    public KeyPair generateKey() throws NoSuchAlgorithmException {
        return keyPairGenerator().generateKeyPair();
    }

    public RSA setPublicKey(byte[] array) throws GeneralSecurityException {
        Assert.isNull(key, "Key have value");
        // publicKey
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(array);
        key = keyFactory().generatePublic(x509EncodedKeySpec);
        return this;
    }

    public RSA setPrivateKey(byte[] array) throws GeneralSecurityException {
        Assert.isNull(key, "Key have value");
        // privateKey
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(array);
        key = keyFactory().generatePrivate(pkcs8EncodedKeySpec);
        return this;
    }

    public byte[] encrypt(byte[] data) throws GeneralSecurityException {
        Assert.isInstanceOf(PublicKey.class, key);
        return this.calc(data);
    }

    public byte[] decrypt(byte[] data) throws GeneralSecurityException {
        Assert.isInstanceOf(PrivateKey.class, key);
        return this.calc(data);
    }

    public byte[] sign(byte[] data) throws GeneralSecurityException {
        Assert.isInstanceOf(PrivateKey.class, key);
        return this.calc(data);
    }

    public byte[] verify(byte[] data) throws GeneralSecurityException {
        Assert.isInstanceOf(PublicKey.class, key);
        return this.calc(data);
    }

    @Override
    protected void judgeDoFill(String transformation) {
        this.setDoFill(false);
    }

    @Override
    protected void judgeNeedIv(String transformation) {
        this.setNeedIv(false);
    }

    @Override
    public Cipher setKey(String key) {
        throw new UnexpectedException("Please using setPublicKey or setPrivateKey. ");
    }

    @Override
    public Cipher setKey(byte[] key) {
        throw new UnexpectedException("Please using setPublicKey or setPrivateKey. ");
    }

    @Override
    public String getAlgorithmName() {
        return "RSA";
    }

    @Override
    protected void assertKey(byte[] key) {
    }

    @Override
    protected void assertIv(boolean needIv, byte[] iv) {
    }

    @Override
    protected Key handleKey(byte[] key) throws GeneralSecurityException {
        return this.key;
    }

    @Override
    protected AlgorithmParameterSpec handleIv(byte[] iv) throws GeneralSecurityException {
        return null;
    }

}
