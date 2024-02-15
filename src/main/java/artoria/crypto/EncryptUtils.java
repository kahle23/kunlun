package artoria.crypto;

import artoria.exception.ExceptionUtils;
import artoria.util.Assert;

import javax.crypto.SecretKey;

import static artoria.common.constant.Algorithms.*;
import static artoria.common.constant.Numbers.*;

/**
 * Universal encryption tools.
 * @author Kahle
 */
public class EncryptUtils {
    private static SymmetricCrypto aesCrypto = new SimpleSymmetricCrypto();
    private static SymmetricCrypto desCrypto = new SimpleSymmetricCrypto();
    private static Hmac hmacSha512 = new Hmac(HMAC_SHA512);
    private static Hmac hmacSha256 = new Hmac(HMAC_SHA256);
    private static Hmac hmacSha1 = new Hmac(HMAC_SHA1);

    private static byte[] addSalt(byte[] data) {
        int dataLen = data.length, arrLen = dataLen + SIXTEEN;
        int destPos = dataLen + EIGHT;
        byte[] newData = new byte[arrLen];
        System.arraycopy(PI_KEY_8, ZERO, newData, ZERO, EIGHT);
        System.arraycopy(data, ZERO, newData, EIGHT, dataLen);
        System.arraycopy(PI_KEY_8, ZERO, newData, destPos, EIGHT);
        return newData;
    }

    private static byte[] removeSalt(byte[] data) {
        int arrLen = data.length - SIXTEEN;
        byte[] newData = new byte[arrLen];
        System.arraycopy(data, EIGHT, newData, ZERO, arrLen);
        return newData;
    }

    private static byte[] doDigest(Hmac hmac, byte[] data) {
        try {
            Assert.notEmpty(data, "Parameter \"data\" must not empty. ");
            byte[] encrypt = desCrypto.encrypt(data);
            encrypt = addSalt(encrypt);
            byte[] digest = hmacSha512.digest(encrypt);
            digest = addSalt(digest);
            digest = hmac.digest(digest);
            return digest;
        }
        catch (Exception e) {
            throw ExceptionUtils.wrap(e);
        }
    }

    public static byte[] encrypt(byte[] data) {
        try {
            Assert.notEmpty(data, "Parameter \"data\" must not empty. ");
            byte[] encrypt = aesCrypto.encrypt(data);
            encrypt = addSalt(encrypt);
            encrypt = desCrypto.encrypt(encrypt);
            encrypt = aesCrypto.encrypt(encrypt);
            return encrypt;
        }
        catch (Exception e) {
            throw ExceptionUtils.wrap(e);
        }
    }

    public static byte[] decrypt(byte[] data) {
        try {
            Assert.notEmpty(data, "Parameter \"data\" must not empty. ");
            byte[] decrypt = aesCrypto.decrypt(data);
            decrypt = desCrypto.decrypt(decrypt);
            decrypt = removeSalt(decrypt);
            decrypt = aesCrypto.decrypt(decrypt);
            return decrypt;
        }
        catch (Exception e) {
            throw ExceptionUtils.wrap(e);
        }
    }

    public static byte[] digest(byte[] data) {

        return doDigest(hmacSha1, data);
    }

    public static byte[] digest256(byte[] data) {

        return doDigest(hmacSha256, data);
    }

    public static byte[] digest512(byte[] data) {

        return doDigest(hmacSha512, data);
    }

    private static final byte[] PI_KEY_64 = new byte[]{51,46,49,52,49,53,57,50,54,53,51,53,56,57,55,57,51,50,51,56,52,54,50,54,52,51,51,56,51,50,55,57,53,48,50,56,56,52,49,57,55,49,54,57,51,57,57,51,55,53,49,48,53,56,50,48,57,55,52,57,52,52,53,57};
    private static final byte[] PI_KEY_32 = new byte[]{51,46,49,52,49,53,57,50,54,53,51,53,56,57,55,57,51,50,51,56,52,54,50,54,52,51,51,56,51,50,55,57};
    private static final byte[] PI_KEY_16 = new byte[]{51,46,49,52,49,53,57,50,54,53,51,53,56,57,55,57};
    private static final byte[] PI_KEY_8 = new byte[]{51,46,49,52,49,53,57,50};

    static {
        SecretKey secretKey = KeyUtils.parseSecretKey(AES, PI_KEY_16);
        aesCrypto.setSecretKey(secretKey);
        aesCrypto.setAlgorithm(AES);
        aesCrypto.setMode(Mode.ECB);
        aesCrypto.setPadding(Padding.PKCS5_PADDING);
        secretKey = KeyUtils.parseSecretKey(DES, PI_KEY_8);
        desCrypto.setSecretKey(secretKey);
        desCrypto.setAlgorithm(DES);
        desCrypto.setMode(Mode.ECB);
        desCrypto.setPadding(Padding.PKCS5_PADDING);
        secretKey = KeyUtils.parseSecretKey(HMAC_SHA512, PI_KEY_64);
        hmacSha512.setSecretKey(secretKey);
        secretKey = KeyUtils.parseSecretKey(HMAC_SHA256, PI_KEY_32);
        hmacSha256.setSecretKey(secretKey);
        secretKey = KeyUtils.parseSecretKey(HMAC_SHA1, PI_KEY_16);
        hmacSha1.setSecretKey(secretKey);
    }

}
