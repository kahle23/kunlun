package artoria.crypto;

import artoria.util.Assert;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.Charset;
import java.security.GeneralSecurityException;

import static artoria.common.Constants.DEFAULT_CHARSET_NAME;

/**
 * Hmac tools, in JDK, support list: HMAC_MD5
 * , HMAC_SHA1, HMAC_SHA256, HMAC_SHA384, HMAC_SHA512.
 * @author Kahle
 */
public class Hmac {
    public static final String HMAC_MD5 = "HmacMD5";
    public static final String HMAC_SHA1 = "HmacSHA1";
    public static final String HMAC_SHA256 = "HmacSHA256";
    public static final String HMAC_SHA384 = "HmacSHA384";
    public static final String HMAC_SHA512 = "HmacSHA512";
    private String charset = DEFAULT_CHARSET_NAME;
    private String algorithm;
    private byte[] key;

    public static Hmac getInstance(String algorithm) {
        Hmac hmac = new Hmac();
        hmac.setAlgorithm(algorithm);
        return hmac;
    }

    private Hmac() {
    }

    public String getCharset() {

        return this.charset;
    }

    public void setCharset(String charset) {
        Assert.notBlank(charset, "Parameter \"charset\" must not blank. ");
        this.charset = charset;
    }

    public String getAlgorithm() {

        return this.algorithm;
    }

    public void setAlgorithm(String algorithm) {
        Assert.notBlank(algorithm, "Parameter \"algorithm\" must not blank. ");
        this.algorithm = algorithm;
    }

    public byte[] getKey() {

        return this.key;
    }

    public void setKey(byte[] key) {
        Assert.notNull(key, "Parameter \"key\" must not null. ");
        this.key = key;
    }

    public void setKey(String key) {
        Assert.notNull(key, "Parameter \"key\" must not null. ");
        Charset charset = Charset.forName(this.charset);
        this.key = key.getBytes(charset);
    }

    public byte[] digest(String data) throws GeneralSecurityException {
        Assert.notNull(data, "Parameter \"data\" must not null. ");
        Charset charset = Charset.forName(this.charset);
        byte[] bytes = data.getBytes(charset);
        return this.digest(bytes);
    }

    public byte[] digest(byte[] data) throws GeneralSecurityException {
        Assert.notNull(data, "Parameter \"data\" must not null. ");
        Assert.notNull(this.key, "Parameter \"key\" must not null. ");
        SecretKey secretKey = new SecretKeySpec(this.key, this.algorithm);
        Mac mac = Mac.getInstance(this.algorithm);
        mac.init(secretKey);
        return mac.doFinal(data);
    }

}
