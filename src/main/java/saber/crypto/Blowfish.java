package saber.crypto;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.Charset;
import java.security.GeneralSecurityException;

/**
 * @author Kahle
 */
public class Blowfish {
    private static final int MAX_KEY_LENGTH = 56;
    private static final int MAX_IV_LENGTH = 8;

    public static final String ALGORITHM_NAME = "Blowfish";
    public static final String DEFAULT_TRANSFORMATION = "Blowfish/ECB/PKCS5Padding";

    public static final String ECB_NO_PADDING = "Blowfish/ECB/NoPadding";
    public static final String ECB_PKCS_5_PADDING = "Blowfish/ECB/PKCS5Padding";
    public static final String CBC_NO_PADDING = "Blowfish/CBC/NoPadding";
    public static final String CBC_PKCS_5_PADDING = "Blowfish/CBC/PKCS5Padding";

    private static final int FILL_LENGTH = 8;

    public static Blowfish on() {
        return new Blowfish()
                .setTransformation(DEFAULT_TRANSFORMATION);
    }

    public static Blowfish on(byte[] key) {
        return new Blowfish().setKey(key)
                .setTransformation(DEFAULT_TRANSFORMATION);
    }

    public static Blowfish on(String transformation) {
        return new Blowfish()
                .setTransformation(transformation);
    }

    public static Blowfish on(String transformation, byte[] key) {
        return new Blowfish().setKey(key)
                .setTransformation(transformation);
    }

    private String charset = Charset.defaultCharset().name();
    private String transformation;
    private boolean needFill;
    private boolean needIv;
    private byte[] key;
    private byte[] iv;

    private Blowfish() {}

    private boolean checkNeedFill(String transformation) {
        String lowerCase = transformation.toLowerCase();
        return lowerCase.contains("nopadding");
    }

    private boolean checkNeedIv(String transformation) {
        String lowerCase = transformation.toLowerCase();
        return lowerCase.contains("cbc");
    }

    public String getCharset() {
        return charset;
    }

    public Blowfish setCharset(String charset) {
        this.charset = charset;
        return this;
    }

    public String getTransformation() {
        return transformation;
    }

    public Blowfish setTransformation(String transformation) {
        this.transformation = transformation;
        needFill = checkNeedFill(transformation);
        needIv = checkNeedIv(transformation);
        return this;
    }

    public byte[] getKey() {
        return key;
    }

    public Blowfish setKey(byte[] key) {
        this.key = key;
        return this;
    }

    public Blowfish setKey(String key) {
        this.key = key.getBytes(Charset.forName(charset));
        return this;
    }

    public byte[] getIv() {
        return iv;
    }

    public Blowfish setIv(byte[] iv) {
        this.iv = iv;
        return this;
    }

    public Blowfish setIv(String iv) {
        this.iv = iv.getBytes(Charset.forName(charset));
        return this;
    }

    public byte[] calc(byte[] data, int opmode) throws GeneralSecurityException {
        if (key == null || key.length > MAX_KEY_LENGTH) {
            throw new IllegalArgumentException("Key too long (> 448 bits (56 bytes)). ");
        }
        if (needIv && iv.length != MAX_IV_LENGTH) {
            throw new IllegalArgumentException("Wrong IV length: must be 8 bytes long. ");
        }
        byte[] finalData = data;
        if (opmode == Cipher.ENCRYPT_MODE && needFill) {
            // Transformation have no padding
            // Input length multiple of [fill] bytes
            int len = data.length;
            finalData = new byte[len + FILL_LENGTH - len % FILL_LENGTH];
            System.arraycopy(data, 0, finalData, 0, len);
        }
        SecretKey secretKey = new SecretKeySpec(key, ALGORITHM_NAME);
        Cipher cipher = Cipher.getInstance(transformation);
        if (needIv) {
            cipher.init(opmode, secretKey, new IvParameterSpec(iv));
        }
        else {
            cipher.init(opmode, secretKey);
        }
        return cipher.doFinal(finalData);
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
