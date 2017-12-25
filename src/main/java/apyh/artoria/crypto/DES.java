package apyh.artoria.crypto;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;

/**
 * @author Kahle
 */
public class DES extends Cipher {
    private static final int MAX_KEY_LENGTH = 8;
    private static final int MAX_IV_LENGTH = 8;

    public static final String ECB_NO_PADDING = "DES/ECB/NoPadding";
    public static final String ECB_PKCS_5_PADDING = "DES/ECB/PKCS5Padding";
    public static final String CBC_NO_PADDING = "DES/CBC/NoPadding";
    public static final String CBC_PKCS_5_PADDING = "DES/CBC/PKCS5Padding";

    private SecretKeyFactory keyFactory;

    private SecretKeyFactory keyFactory() throws NoSuchAlgorithmException {
        if (keyFactory == null) {
            synchronized (this) {
                if (keyFactory == null) {
                    String name = this.getAlgorithmName();
                    keyFactory = SecretKeyFactory.getInstance(name);
                }
            }
        }
        return keyFactory;
    }

    protected DES() {
        this.setMultiple(8);
    }

    @Override
    public String getAlgorithmName() {
        return "DES";
    }

    @Override
    protected void assertKey(byte[] key) {
        boolean isWrongKey = key == null || key.length < MAX_KEY_LENGTH;
        if (isWrongKey) {
            throw new IllegalArgumentException("Wrong key size. Key length >= 8. ");
        }
    }

    @Override
    protected void assertIv(boolean needIv, byte[] iv) {
        boolean isWrongIv = needIv && (iv == null || iv.length != MAX_IV_LENGTH);
        if (isWrongIv) {
            throw new IllegalArgumentException("Wrong Iv length: must be 8 bytes long. ");
        }
    }

    @Override
    protected Key handleKey(byte[] key) throws GeneralSecurityException {
        return keyFactory().generateSecret(new DESKeySpec(key));
    }

    @Override
    protected AlgorithmParameterSpec handleIv(byte[] iv) throws GeneralSecurityException {
        return new IvParameterSpec(iv);
    }

}
