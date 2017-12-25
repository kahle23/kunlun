package apyh.artoria.crypto;

import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.spec.AlgorithmParameterSpec;

/**
 * @author Kahle
 */
public class AES extends Cipher {
    private static final int MAX_KEY_LENGTH_1 = 16;
    private static final int MAX_KEY_LENGTH_2 = 24;
    private static final int MAX_KEY_LENGTH_3 = 32;
    private static final int MAX_IV_LENGTH = 16;

    public static final String ECB_NO_PADDING = "AES/ECB/NoPadding";
    public static final String ECB_PKCS_5_PADDING = "AES/ECB/PKCS5Padding";
    public static final String CBC_NO_PADDING = "AES/CBC/NoPadding";
    public static final String CBC_PKCS_5_PADDING = "AES/CBC/PKCS5Padding";

    protected AES() {
        this.setMultiple(16);
    }

    @Override
    public String getAlgorithmName() {
        return "AES";
    }

    @Override
    protected void assertKey(byte[] key) {
        boolean isWrongKey = key == null || (key.length != MAX_KEY_LENGTH_1 && key.length != MAX_KEY_LENGTH_2 && key.length != MAX_KEY_LENGTH_3);
        if (isWrongKey) {
            throw new IllegalArgumentException("Wrong key size. Key length only is 16 or 24 or 32. ");
        }
    }

    @Override
    protected void assertIv(boolean needIv, byte[] iv) {
        boolean isWrongIv = needIv && (iv == null || iv.length != MAX_IV_LENGTH);
        if (isWrongIv) {
            throw new IllegalArgumentException("Wrong IV length: must be 16 bytes long. ");
        }
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
