package artoria.crypto;

import artoria.util.Assert;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import java.security.GeneralSecurityException;

import static artoria.common.Constants.*;
import static artoria.crypto.Padding.NO_PADDING;
import static artoria.crypto.Padding.ZERO_PADDING;

/**
 * Simple implementation of symmetric encryption and decryption tools.
 * @author Kahle
 */
public class SimpleSymmetricCrypto extends AbstractCrypto implements SymmetricCrypto {
    private final boolean handleZeroPadding;
    private SecretKey secretKey;

    public SimpleSymmetricCrypto() {

        this(true);
    }

    public SimpleSymmetricCrypto(boolean handleZeroPadding) {

        this.handleZeroPadding = handleZeroPadding;
    }

    protected byte[] fillZeroToNoPadding(byte[] data) {
        Assert.notEmpty(data, "Parameter \"data\" must not empty. ");
        String algorithm = getAlgorithm();
        int multiple;
        if (AES.equalsIgnoreCase(algorithm)) {
            multiple = SIXTEEN;
        }
        else if (DES.equalsIgnoreCase(algorithm)
                || DESEDE.equalsIgnoreCase(algorithm)
                || BLOWFISH.equalsIgnoreCase(algorithm)) {
            multiple = EIGHT;
        }
        else {
            return data;
        }
        int len = data.length, fill = len % multiple;
        fill = fill != ZERO ? multiple - fill : ZERO;
        byte[] result = new byte[len + fill];
        System.arraycopy(data, ZERO, result, ZERO, len);
        return result;
    }

    @Override
    protected String getTransformation() {
        String algorithm = getAlgorithm();
        Assert.notBlank(algorithm, "Parameter \"algorithm\" must not blank. ");
        String mode = getMode();
        Assert.notBlank(mode, "Parameter \"mode\" must not blank. ");
        String padding = getPadding();
        Assert.notBlank(padding, "Parameter \"padding\" must not blank. ");
        if (handleZeroPadding && ZERO_PADDING.getName().equalsIgnoreCase(padding)) {
            padding = NO_PADDING.getName();
        }
        return algorithm + SLASH + mode + SLASH + padding;
    }

    @Override
    public SecretKey getSecretKey() {

        return secretKey;
    }

    @Override
    public void setSecretKey(SecretKey secretKey) {
        Assert.notNull(secretKey, "Parameter \"secretKey\" must not null. ");
        this.secretKey = secretKey;
    }

    @Override
    public byte[] encrypt(byte[] data) throws GeneralSecurityException {
        SecretKey secretKey = getSecretKey();
        Assert.notNull(secretKey, "Parameter \"secretKey\" must not null. ");
        String padding = getPadding();
        if (handleZeroPadding && ZERO_PADDING.getName().equalsIgnoreCase(padding)) {
            data = fillZeroToNoPadding(data);
        }
        Cipher cipher = createCipher(Cipher.ENCRYPT_MODE, secretKey, null);
        return cipher.doFinal(data);
    }

    @Override
    public byte[] decrypt(byte[] data) throws GeneralSecurityException {
        SecretKey secretKey = getSecretKey();
        Assert.notNull(secretKey, "Parameter \"secretKey\" must not null. ");
        Cipher cipher = createCipher(Cipher.DECRYPT_MODE, secretKey, null);
        return cipher.doFinal(data);
    }

}
