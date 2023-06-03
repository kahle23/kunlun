package artoria.crypto;

import artoria.codec.CodecUtils;
import artoria.core.Encoder;
import artoria.util.ArrayUtils;
import artoria.util.Assert;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.*;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import static artoria.codec.CodecUtils.BASE64;
import static artoria.codec.CodecUtils.HEX;
import static artoria.common.Constants.EMPTY_STRING;
import static artoria.common.Constants.NULL;
import static artoria.util.ObjectUtils.cast;

/**
 * Key tools.
 * @author Kahle
 */
public class KeyUtils {
    private static final SecureRandom RANDOM = new SecureRandom();
    private static Encoder<byte[]> base64Encoder = cast(CodecUtils.getEncoder(BASE64));
    private static Encoder<byte[]> hexEncoder = cast(CodecUtils.getEncoder(HEX));

    public static void setBase64Encoder(Encoder<byte[]> base64Encoder) {
        Assert.notNull(base64Encoder, "Parameter \"base64Encoder\" must not null. ");
        KeyUtils.base64Encoder = base64Encoder;
    }

    public static void setHexEncoder(Encoder<byte[]> hexEncoder) {
        Assert.notNull(hexEncoder, "Parameter \"hexEncoder\" must not null. ");
        KeyUtils.hexEncoder = hexEncoder;
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

    private static String toEncoderString(Encoder<byte[]> encoder, Key key) {
        if (key == null) { return NULL; }
        byte[] encoded = key.getEncoded();
        if (ArrayUtils.isEmpty(encoded)) {
            return EMPTY_STRING;
        }
        byte[] bytes = encoder.encode(encoded);
        return new String(bytes);
    }

    public static String toHexString(Key key) {

        return KeyUtils.toEncoderString(hexEncoder, key);
    }

    public static String toBase64String(Key key) {

        return KeyUtils.toEncoderString(base64Encoder, key);
    }

}
