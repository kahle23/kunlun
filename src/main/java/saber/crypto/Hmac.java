package saber.crypto;

import saber.codec.Hex;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.Charset;
import java.security.GeneralSecurityException;

/**
 * @author Kahle
 */
public class Hmac {

    public enum Algorithm {
        HMAC_MD5("HmacMD5"),
        HMAC_SHA1("HmacSHA1"),
        HMAC_SHA256("HmacSHA256"),
        HMAC_SHA384("HmacSHA384"),
        HMAC_SHA512("HmacSHA512")
        ;

        public static Algorithm search(String algorithm) {
            Algorithm[] values = Algorithm.values();
            for (Algorithm value : values) {
                if (value.getAlgorithm().equalsIgnoreCase(algorithm)) {
                    return value;
                }
            }
            return null;
        }

        private String algorithm;

        Algorithm(String algorithm) {
            this.algorithm = algorithm;
        }

        public String getAlgorithm() {
            return algorithm;
        }

        @Override
        public String toString() {
            return algorithm;
        }

    }

    public static final Algorithm DEFAULT_ALGORITHM = Algorithm.HMAC_SHA1;

    public static Hmac md5(byte[] key) {
        return new Hmac(Algorithm.HMAC_MD5.toString()).setKey(key);
    }

    public static Hmac md5(String key) {
        return new Hmac(Algorithm.HMAC_MD5.toString()).setKey(key);
    }

    public static Hmac sha1(byte[] key) {
        return new Hmac(Algorithm.HMAC_SHA1.toString()).setKey(key);
    }

    public static Hmac sha1(String key) {
        return new Hmac(Algorithm.HMAC_SHA1.toString()).setKey(key);
    }

    public static Hmac sha256(byte[] key) {
        return new Hmac(Algorithm.HMAC_SHA256.toString()).setKey(key);
    }

    public static Hmac sha256(String key) {
        return new Hmac(Algorithm.HMAC_SHA256.toString()).setKey(key);
    }

    public static Hmac sha384(byte[] key) {
        return new Hmac(Algorithm.HMAC_SHA384.toString()).setKey(key);
    }

    public static Hmac sha384(String key) {
        return new Hmac(Algorithm.HMAC_SHA384.toString()).setKey(key);
    }

    public static Hmac sha512(byte[] key) {
        return new Hmac(Algorithm.HMAC_SHA512.toString()).setKey(key);
    }

    public static Hmac sha512(String key) {
        return new Hmac(Algorithm.HMAC_SHA512.toString()).setKey(key);
    }

    public static Hmac on() {
        return new Hmac(DEFAULT_ALGORITHM.toString());
    }

    public static Hmac on(String algorithm) {
        return new Hmac(algorithm);
    }

    public static Hmac on(Algorithm algorithm) {
        return new Hmac(algorithm.toString());
    }

    public static Hmac on(String algorithm, byte[] key) {
        return new Hmac(algorithm).setKey(key);
    }

    public static Hmac on(String algorithm, String key) {
        return new Hmac(algorithm).setKey(key);
    }

    public static Hmac on(Algorithm algorithm, byte[] key) {
        return new Hmac(algorithm.toString()).setKey(key);
    }

    public static Hmac on(Algorithm algorithm, String key) {
        return new Hmac(algorithm.toString()).setKey(key);
    }

    private String charset = Charset.defaultCharset().name();
    private Hex hex = Hex.on();
    private String algorithm;
    private byte[] key;

    private Hmac(String algorithm) {
        this.algorithm = algorithm;
    }

    public String getCharset() {
        return charset;
    }

    public Hmac setCharset(String charset) {
        this.charset = charset;
        return this;
    }

    public Hex getHex() {
        return hex;
    }

    public Hmac setHex(Hex hex) {
        this.hex = hex;
        return this;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public Hmac setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
        return this;
    }

    public byte[] getKey() {
        return key;
    }

    public Hmac setKey(byte[] key) {
        this.key = key;
        return this;
    }

    public Hmac setKey(String key) {
        this.key = key.getBytes(Charset.forName(charset));
        return this;
    }

    public byte[] calc(byte[] data) throws GeneralSecurityException {
        SecretKey secretKey = new SecretKeySpec(key, algorithm);
        Mac mac = Mac.getInstance(secretKey.getAlgorithm());
        mac.init(secretKey);
        return mac.doFinal(data);
    }

    public byte[] calc(String data) throws GeneralSecurityException {
        return calc(data.getBytes(Charset.forName(charset)));
    }

    public String calcToHex(byte[] data) throws GeneralSecurityException {
        return hex.encodeToString(calc(data));
    }

    public String calcToHex(String data) throws GeneralSecurityException {
        return hex.encodeToString(calc(data));
    }

}
