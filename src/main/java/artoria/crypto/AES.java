package artoria.crypto;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.Charset;
import java.security.GeneralSecurityException;

/**
 * @author Kahle
 */
public class AES {
    private static final int MAX_KEY_LENGTH_1 = 16;
    private static final int MAX_KEY_LENGTH_2 = 24;
    private static final int MAX_KEY_LENGTH_3 = 32;
    private static final int MAX_IV_LENGTH = 16;

    public static final String ALGORITHM_NAME = "AES";
    public static final String DEFAULT_TRANSFORMATION = "AES/ECB/PKCS5Padding";

    public static final String ECB_NO_PADDING = "AES/ECB/NoPadding";
    public static final String ECB_PKCS_5_PADDING = "AES/ECB/PKCS5Padding";
    public static final String CBC_NO_PADDING = "AES/CBC/NoPadding";
    public static final String CBC_PKCS_5_PADDING = "AES/CBC/PKCS5Padding";

    private static final int FILL_LENGTH = 16;

    public static AES create() {
        return new AES()
                .setTransformation(DEFAULT_TRANSFORMATION);
    }

    public static AES create(byte[] key) {
        return new AES().setKey(key)
                .setTransformation(DEFAULT_TRANSFORMATION);
    }

    public static AES create(String transformation) {
        return new AES()
                .setTransformation(transformation);
    }

    public static AES create(String transformation, byte[] key) {
        return new AES().setKey(key)
                .setTransformation(transformation);
    }

    private String charset = Charset.defaultCharset().name();
    private String transformation;
    private boolean needFill;
    private boolean needIv;
    private byte[] key;
    private byte[] iv;

    private AES() {}

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

    public AES setCharset(String charset) {
        this.charset = charset;
        return this;
    }

    public String getTransformation() {
        return transformation;
    }

    public AES setTransformation(String transformation) {
        this.transformation = transformation;
        needFill = checkNeedFill(transformation);
        needIv = checkNeedIv(transformation);
        return this;
    }

    public byte[] getKey() {
        return key;
    }

    public AES setKey(byte[] key) {
        this.key = key;
        return this;
    }

    public AES setKey(String key) {
        this.key = key.getBytes(Charset.forName(charset));
        return this;
    }

    public byte[] getIv() {
        return iv;
    }

    public AES setIv(byte[] iv) {
        this.iv = iv;
        return this;
    }

    public AES setIv(String iv) {
        this.iv = iv.getBytes(Charset.forName(charset));
        return this;
    }

    public byte[] calc(byte[] data, int opmode) throws GeneralSecurityException {
        boolean isWrongKey = key == null || (key.length != MAX_KEY_LENGTH_1 && key.length != MAX_KEY_LENGTH_2 && key.length != MAX_KEY_LENGTH_3);
        boolean isWrongIv = needIv && (iv == null || iv.length != MAX_IV_LENGTH);
        if (isWrongKey) {
            throw new IllegalArgumentException("Wrong key size. Key length only is 16 or 24 or 32. ");
        }
        if (isWrongIv) {
            throw new IllegalArgumentException("Wrong IV length: must be 16 bytes long. ");
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
