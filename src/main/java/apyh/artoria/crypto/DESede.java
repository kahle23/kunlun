package apyh.artoria.crypto;

import apyh.artoria.util.Assert;

import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.security.spec.AlgorithmParameterSpec;

/**
 * @author Kahle
 */
public class DESede extends apyh.artoria.crypto.Cipher {
    private static final int MAX_KEY_LENGTH = 24;
    private static final int MAX_IV_LENGTH = 8;
    private static final int MULTIPLE = 8;

    public static final String ALGORITHM_NAME = "DESede";
    public static final String ECB_NO_PADDING = "DESede/ECB/NoPadding";
    public static final String ECB_PKCS_5_PADDING = "DESede/ECB/PKCS5Padding";
    public static final String CBC_NO_PADDING = "DESede/CBC/NoPadding";
    public static final String CBC_PKCS_5_PADDING = "DESede/CBC/PKCS5Padding";

    public static Cipher create() {
        return new DESede()
                .setTransformation(ECB_PKCS_5_PADDING);
    }

    public static Cipher create(byte[] key) {
        return new DESede().setKey(key)
                .setTransformation(ECB_PKCS_5_PADDING);
    }

    public static Cipher create(String transformation) {
        return new DESede()
                .setTransformation(transformation);
    }

    public static Cipher create(String transformation, byte[] key) {
        return new DESede().setKey(key)
                .setTransformation(transformation);
    }

    private DESede() {
        this.setMultiple(MULTIPLE);
    }

    @Override
    protected Key handleKey(byte[] key) {
        return new SecretKeySpec(key, ALGORITHM_NAME);
    }

    @Override
    protected AlgorithmParameterSpec handleIv(byte[] iv) {
        return new IvParameterSpec(iv);
    }

    @Override
    protected void assertKey(byte[] key) {
        boolean isWrongKey = key == null || key.length != MAX_KEY_LENGTH;
        Assert.state(!isWrongKey, "Wrong key size. Key length must is 24. ");
    }

    @Override
    protected void assertIv(boolean needIv, byte[] iv) {
        boolean isWrongIv = needIv && (iv == null || iv.length != MAX_IV_LENGTH);
        Assert.state(!isWrongIv, "Wrong Iv length: must be 8 bytes long. ");
    }

}
