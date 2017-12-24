package apyh.artoria.crypto;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import java.nio.charset.Charset;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;

/**
 * @author Kahle
 */
public class DES {
    private static final int MAX_KEY_LENGTH = 8;
    private static final int MAX_IV_LENGTH = 8;
    private static final int FILL_LENGTH = 8;

    public static final String ALGORITHM_NAME = "DES";
    public static final String ECB_NO_PADDING = "DES/ECB/NoPadding";
    public static final String ECB_PKCS_5_PADDING = "DES/ECB/PKCS5Padding";
    public static final String CBC_NO_PADDING = "DES/CBC/NoPadding";
    public static final String CBC_PKCS_5_PADDING = "DES/CBC/PKCS5Padding";

    public static DES create() {
        return new DES()
                .setTransformation(ECB_PKCS_5_PADDING);
    }

    public static DES create(byte[] key) {
        return new DES().setKey(key)
                .setTransformation(ECB_PKCS_5_PADDING);
    }

    public static DES create(String transformation) {
        return new DES()
                .setTransformation(transformation);
    }

    public static DES create(String transformation, byte[] key) {
        return new DES().setKey(key)
                .setTransformation(transformation);
    }

    private String charset = Charset.defaultCharset().name();
    private SecretKeyFactory keyFactory;
    private String transformation;
    private boolean needFill;
    private boolean needIv;
    private byte[] key;
    private byte[] iv;

    private DES() {}

    private boolean checkNeedFill(String transformation) {
        String lowerCase = transformation.toLowerCase();
        return lowerCase.contains("nopadding");
    }

    private boolean checkNeedIv(String transformation) {
        String lowerCase = transformation.toLowerCase();
        return lowerCase.contains("cbc");
    }

    private SecretKeyFactory keyFactory() throws NoSuchAlgorithmException {
        if (keyFactory == null) {
            keyFactory = SecretKeyFactory.getInstance(ALGORITHM_NAME);
        }
        return keyFactory;
    }

    public String getCharset() {
        return charset;
    }

    public DES setCharset(String charset) {
        this.charset = charset;
        return this;
    }

    public SecretKeyFactory getKeyFactory() {
        return keyFactory;
    }

    public DES setKeyFactory(SecretKeyFactory keyFactory) {
        this.keyFactory = keyFactory;
        return this;
    }

    public String getTransformation() {
        return transformation;
    }

    public DES setTransformation(String transformation) {
        this.transformation = transformation;
        needFill = checkNeedFill(transformation);
        needIv = checkNeedIv(transformation);
        return this;
    }

    public byte[] getKey() {
        return key;
    }

    public DES setKey(byte[] key) {
        this.key = key;
        return this;
    }

    public DES setKey(String key) {
        this.key = key.getBytes(Charset.forName(charset));
        return this;
    }

    public byte[] getIv() {
        return iv;
    }

    public DES setIv(byte[] iv) {
        this.iv = iv;
        return this;
    }

    public DES setIv(String iv) {
        this.iv = iv.getBytes(Charset.forName(charset));
        return this;
    }

    public byte[] calc(byte[] data, int opmode) throws GeneralSecurityException {
        boolean isWrongKey = key == null || key.length < MAX_KEY_LENGTH;
        boolean isWrongIv = needIv && (iv == null || iv.length != MAX_IV_LENGTH);
        if (isWrongKey) {
            throw new IllegalArgumentException("Wrong key size. Key length >= 8. ");
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
        SecretKey secretKey = keyFactory().generateSecret(new DESKeySpec(key));
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
        return this.calc(data, Cipher.ENCRYPT_MODE);
    }

    public byte[] encrypt(String data) throws GeneralSecurityException {
        return this.calc(data.getBytes(Charset.forName(charset)), Cipher.ENCRYPT_MODE);
    }

    public byte[] decrypt(byte[] data) throws GeneralSecurityException {
        return this.calc(data, Cipher.DECRYPT_MODE);
    }

}
