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
public class DESede {
    private static final int MAX_KEY_LENGTH = 24;
    private static final int MAX_IV_LENGTH = 8;

    public static final String ALGORITHM_NAME = "DESede";
    public static final String DEFAULT_TRANSFORMATION = "DESede/ECB/PKCS5Padding";

    public static final String ECB_NO_PADDING = "DESede/ECB/NoPadding";
    public static final String ECB_PKCS_5_PADDING = "DESede/ECB/PKCS5Padding";
    public static final String CBC_NO_PADDING = "DESede/CBC/NoPadding";
    public static final String CBC_PKCS_5_PADDING = "DESede/CBC/PKCS5Padding";

    private static final int FILL_LENGTH = 8;

    public static DESede create() {
        return new DESede()
                .setTransformation(DEFAULT_TRANSFORMATION);
    }

    public static DESede create(byte[] key) {
        return new DESede().setKey(key)
                .setTransformation(DEFAULT_TRANSFORMATION);
    }

    public static DESede create(String transformation) {
        return new DESede()
                .setTransformation(transformation);
    }

    public static DESede create(String transformation, byte[] key) {
        return new DESede().setKey(key)
                .setTransformation(transformation);
    }

    private String charset = Charset.defaultCharset().name();
    private String transformation;
    private boolean needFill;
    private boolean needIv;
    private byte[] key;
    private byte[] iv;

    private DESede() {}

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

    public DESede setCharset(String charset) {
        this.charset = charset;
        return this;
    }

    public String getTransformation() {
        return transformation;
    }

    public DESede setTransformation(String transformation) {
        this.transformation = transformation;
        needFill = checkNeedFill(transformation);
        needIv = checkNeedIv(transformation);
        return this;
    }

    public byte[] getKey() {
        return key;
    }

    public DESede setKey(byte[] key) {
        this.key = key;
        return this;
    }

    public DESede setKey(String key) {
        this.key = key.getBytes(Charset.forName(charset));
        return this;
    }

    public byte[] getIv() {
        return iv;
    }

    public DESede setIv(byte[] iv) {
        this.iv = iv;
        return this;
    }

    public DESede setIv(String iv) {
        this.iv = iv.getBytes(Charset.forName(charset));
        return this;
    }

    public byte[] calc(byte[] data, int opmode) throws GeneralSecurityException {
        boolean isWrongKey = key == null || key.length != MAX_KEY_LENGTH;
        boolean isWrongIv = needIv && (iv == null || iv.length != MAX_IV_LENGTH);
        if (isWrongKey) {
            throw new IllegalArgumentException("Wrong key size. Key length must is 24. ");
        }
        if (isWrongIv) {
            throw new IllegalArgumentException("Wrong Iv length: must be 8 bytes long. ");
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
