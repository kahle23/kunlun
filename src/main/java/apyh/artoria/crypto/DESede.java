package apyh.artoria.crypto;

import apyh.artoria.util.Assert;

import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.spec.AlgorithmParameterSpec;

/**
 * @author Kahle
 */
public class DESede extends Cipher {
    private static final int MAX_KEY_LENGTH = 24;
    private static final int MAX_IV_LENGTH = 8;

    public static final String ECB_NO_PADDING = "DESede/ECB/NoPadding";
    public static final String ECB_PKCS_5_PADDING = "DESede/ECB/PKCS5Padding";
    public static final String CBC_NO_PADDING = "DESede/CBC/NoPadding";
    public static final String CBC_PKCS_5_PADDING = "DESede/CBC/PKCS5Padding";

    protected DESede() {
        this.setMultiple(8);
    }

    @Override
    public String getAlgorithmName() {
        return "DESede";
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

    @Override
    protected Key handleKey(byte[] key) throws GeneralSecurityException {
        return new SecretKeySpec(key, this.getAlgorithmName());
    }

    @Override
    protected AlgorithmParameterSpec handleIv(byte[] iv) throws GeneralSecurityException {
        return new IvParameterSpec(iv);
    }

}
