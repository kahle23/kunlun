package apyh.artoria.crypto;

import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.spec.AlgorithmParameterSpec;

/**
 * @author Kahle
 */
public class Blowfish extends Cipher {
    private static final int MAX_KEY_LENGTH = 56;
    private static final int MAX_IV_LENGTH = 8;

    public static final String ECB_NO_PADDING = "Blowfish/ECB/NoPadding";
    public static final String ECB_PKCS_5_PADDING = "Blowfish/ECB/PKCS5Padding";
    public static final String CBC_NO_PADDING = "Blowfish/CBC/NoPadding";
    public static final String CBC_PKCS_5_PADDING = "Blowfish/CBC/PKCS5Padding";

    protected Blowfish() {
        this.setMultiple(8);
    }

    @Override
    public String getAlgorithmName() {
        return "Blowfish";
    }

    @Override
    protected void assertKey(byte[] key) {
        if (key == null || key.length > MAX_KEY_LENGTH) {
            throw new IllegalArgumentException("Key too long (> 448 bits (56 bytes)). ");
        }
    }

    @Override
    protected void assertIv(boolean needIv, byte[] iv) {
        if (needIv && iv.length != MAX_IV_LENGTH) {
            throw new IllegalArgumentException("Wrong IV length: must be 8 bytes long. ");
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
