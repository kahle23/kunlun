package saber.crypto;

import saber.codec.HexUtils;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public abstract class HmacUtils {
    private static final String DEFAULT_CHARSET_NAME = Charset.defaultCharset().name();
    public static final String ALGORITHM_HMACMD5 = "HmacMD5";
    public static final String ALGORITHM_HMACSHA1 = "HmacSHA1";
    public static final String ALGORITHM_HMACSHA256 = "HmacSHA256";
    public static final String ALGORITHM_HMACSHA384 = "HmacSHA384";
    public static final String ALGORITHM_HMACSHA512 = "HmacSHA512";

    public static byte[] hmac(byte[] data, byte[] key, String algorithm)
            throws NoSuchAlgorithmException, InvalidKeyException {
        SecretKey secretKey = new SecretKeySpec(key, algorithm);
        Mac mac = Mac.getInstance(secretKey.getAlgorithm());
        mac.init(secretKey);
        return mac.doFinal(data);
    }

    public static String hmd5(String data, String key)
            throws NoSuchAlgorithmException, InvalidKeyException {
        return hmd5(data, key, DEFAULT_CHARSET_NAME);
    }

    public static String hmd5(String data, String key, String charset)
            throws NoSuchAlgorithmException, InvalidKeyException {
        byte[] dataBytes = data.getBytes(Charset.forName(charset));
        byte[] keyBytes = key.getBytes(Charset.forName(charset));
        return HexUtils.encode(hmd5(dataBytes, keyBytes));
    }

    public static byte[] hmd5(byte[] data, byte[] key)
            throws NoSuchAlgorithmException, InvalidKeyException {
        return hmac(data, key, ALGORITHM_HMACMD5);
    }

    public static String hsha1(String data, String key)
            throws NoSuchAlgorithmException, InvalidKeyException {
        return hsha1(data, key, DEFAULT_CHARSET_NAME);
    }

    public static String hsha1(String data, String key, String charset)
            throws NoSuchAlgorithmException, InvalidKeyException {
        byte[] dataBytes = data.getBytes(Charset.forName(charset));
        byte[] keyBytes = key.getBytes(Charset.forName(charset));
        return HexUtils.encode(hsha1(dataBytes, keyBytes));
    }

    public static byte[] hsha1(byte[] data, byte[] key)
            throws NoSuchAlgorithmException, InvalidKeyException {
        return hmac(data, key, ALGORITHM_HMACSHA1);
    }

    public static String hsha256(String data, String key)
            throws NoSuchAlgorithmException, InvalidKeyException {
        return hsha256(data, key, DEFAULT_CHARSET_NAME);
    }

    public static String hsha256(String data, String key, String charset)
            throws NoSuchAlgorithmException, InvalidKeyException {
        byte[] dataBytes = data.getBytes(Charset.forName(charset));
        byte[] keyBytes = key.getBytes(Charset.forName(charset));
        return HexUtils.encode(hsha256(dataBytes, keyBytes));
    }

    public static byte[] hsha256(byte[] data, byte[] key)
            throws NoSuchAlgorithmException, InvalidKeyException {
        return hmac(data, key, ALGORITHM_HMACSHA256);
    }

    public static String hsha384(String data, String key)
            throws NoSuchAlgorithmException, InvalidKeyException {
        return hsha384(data, key, DEFAULT_CHARSET_NAME);
    }

    public static String hsha384(String data, String key, String charset)
            throws NoSuchAlgorithmException, InvalidKeyException {
        byte[] dataBytes = data.getBytes(Charset.forName(charset));
        byte[] keyBytes = key.getBytes(Charset.forName(charset));
        return HexUtils.encode(hsha384(dataBytes, keyBytes));
    }

    public static byte[] hsha384(byte[] data, byte[] key)
            throws NoSuchAlgorithmException, InvalidKeyException {
        return hmac(data, key, ALGORITHM_HMACSHA384);
    }

    public static String hsha512(String data, String key)
            throws NoSuchAlgorithmException, InvalidKeyException {
        return hsha512(data, key, DEFAULT_CHARSET_NAME);
    }

    public static String hsha512(String data, String key, String charset)
            throws NoSuchAlgorithmException, InvalidKeyException {
        byte[] dataBytes = data.getBytes(Charset.forName(charset));
        byte[] keyBytes = key.getBytes(Charset.forName(charset));
        return HexUtils.encode(hsha512(dataBytes, keyBytes));
    }

    public static byte[] hsha512(byte[] data, byte[] key)
            throws NoSuchAlgorithmException, InvalidKeyException {
        return hmac(data, key, ALGORITHM_HMACSHA512);
    }

}
