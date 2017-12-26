package com.apyhs.artoria.crypto;

import com.apyhs.artoria.codec.Hex;
import com.apyhs.artoria.util.Assert;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.Charset;
import java.security.GeneralSecurityException;

import static com.apyhs.artoria.util.StringConstant.DEFAULT_CHARSET_NAME;

/**
 * Hmac tools.
 * In JDK, support list:
 *  HMAC MD5
 *  HMAC SHA1
 *  HMAC SHA256
 *  HMAC SHA384
 *  HMAC SHA512
 *
 * @author Kahle
 */
public class Hmac {
    public static final String HMAC_MD5 = "HmacMD5";
    public static final String HMAC_SHA1 = "HmacSHA1";
    public static final String HMAC_SHA256 = "HmacSHA256";
    public static final String HMAC_SHA384 = "HmacSHA384";
    public static final String HMAC_SHA512 = "HmacSHA512";

    public static Hmac create() {
        return new Hmac(Hmac.HMAC_MD5);
    }

    public static Hmac create(String algorithm) {
        return new Hmac(algorithm);
    }

    private String charset = DEFAULT_CHARSET_NAME;
    private Hex hex = Hex.ME;
    private String algorithm;
    private byte[] key;

    private Hmac(String algorithm) {
        this.setAlgorithm(algorithm);
    }

    public String getCharset() {
        return charset;
    }

    public Hmac setCharset(String charset) {
        Assert.notBlank(charset, "Charset must is not blank. ");
        this.charset = charset;
        return this;
    }

    public Hex getHex() {
        return hex;
    }

    public Hmac setHex(Hex hex) {
        Assert.notNull(hex, "Hex must is not null. ");
        this.hex = hex;
        return this;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public Hmac setAlgorithm(String algorithm) {
        Assert.notBlank(algorithm, "Algorithm must is not blank. ");
        this.algorithm = algorithm;
        return this;
    }

    public byte[] getKey() {
        return key;
    }

    public Hmac setKey(byte[] key) {
        Assert.notNull(key, "Key must is not null. ");
        this.key = key;
        return this;
    }

    public Hmac setKey(String key) {
        Assert.notNull(key, "Key must is not null. ");
        Charset charset = Charset.forName(this.charset);
        this.key = key.getBytes(charset);
        return this;
    }

    public byte[] calc(String data) throws GeneralSecurityException {
        Assert.notNull(data, "Data must is not null. ");
        Charset charset = Charset.forName(this.charset);
        byte[] bytes = data.getBytes(charset);
        return this.calc(bytes);
    }

    public byte[] calc(byte[] data) throws GeneralSecurityException {
        Assert.notNull(data, "Data must is not null. ");
        SecretKey secretKey = new SecretKeySpec(key, algorithm);
        Mac mac = Mac.getInstance(algorithm);
        mac.init(secretKey);
        return mac.doFinal(data);
    }

    public String calcToHexString(String data) throws GeneralSecurityException {
        byte[] calc = this.calc(data);
        return hex.encodeToString(calc);
    }

    public String calcToHexString(byte[] data) throws GeneralSecurityException {
        byte[] calc = this.calc(data);
        return hex.encodeToString(calc);
    }

}
