package artoria.crypto;

import artoria.util.Assert;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import java.security.GeneralSecurityException;

import static artoria.common.Constants.*;
import static artoria.crypto.Padding.NO_PADDING;

/**
 * Default implementation of symmetric encryption and decryption tools.
 * @author Kahle
 */
public class DefaultSymmetricCrypto extends AbstractCrypto implements SymmetricCrypto {
    private SecretKey secretKey;

    protected byte[] fillNoPadding(byte[] data) {
        String algorithm = this.getAlgorithm();
        Assert.notBlank(algorithm, "Parameter \"algorithm\" must not blank. ");
        String padding = this.getPadding();
        Assert.notBlank(padding, "Parameter \"padding\" must not blank. ");
        boolean isEquals = NO_PADDING.getName().equalsIgnoreCase(padding);
        if (!isEquals) { return data; }
        Assert.notEmpty(data, "Parameter \"data\" must not empty. ");
        int multiple;
        if (AES.equalsIgnoreCase(algorithm)) {
            multiple = 16;
        }
        else if (DES.equalsIgnoreCase(algorithm)
                || DESEDE.equalsIgnoreCase(algorithm)
                || BLOWFISH.equalsIgnoreCase(algorithm)) {
            multiple = 8;
        }
        else {
            return data;
        }
        int len = data.length, fill = len % multiple;
        fill = fill != 0 ? multiple - fill : 0;
        byte[] result = new byte[len + fill];
        System.arraycopy(data, 0, result, 0, len);
        return result;
    }

    @Override
    public SecretKey getSecretKey() {

        return this.secretKey;
    }

    @Override
    public void setSecretKey(SecretKey secretKey) {
        Assert.notNull(secretKey, "Parameter \"secretKey\" must not null. ");
        this.secretKey = secretKey;
    }

    @Override
    public byte[] encrypt(byte[] data) throws GeneralSecurityException {
        SecretKey secretKey = this.getSecretKey();
        Assert.notNull(secretKey, "Parameter \"secretKey\" must not null. ");
        data = this.fillNoPadding(data);
        Cipher cipher = this.createCipher(Cipher.ENCRYPT_MODE, secretKey, null);
        return cipher.doFinal(data);
    }

    @Override
    public byte[] decrypt(byte[] data) throws GeneralSecurityException {
        SecretKey secretKey = this.getSecretKey();
        Assert.notNull(secretKey, "Parameter \"secretKey\" must not null. ");
        Cipher cipher = this.createCipher(Cipher.DECRYPT_MODE, secretKey, null);
        return cipher.doFinal(data);
    }

}
