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
        boolean isEquals = NO_PADDING.getName()
                .equalsIgnoreCase(this.getPadding());
        if (!isEquals) { return data; }
        Assert.notEmpty(data, "Parameter \"data\" must not empty. ");
        int multiple;
        if (AES.equalsIgnoreCase(this.getAlgorithm())) {
            multiple = 16;
        }
        else if (DES.equalsIgnoreCase(this.getAlgorithm())
                || DESEDE.equalsIgnoreCase(this.getAlgorithm())
                || BLOWFISH.equalsIgnoreCase(this.getAlgorithm())) {
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
        Assert.notNull(this.secretKey, "Please set the parameter \"secretKey\" first. ");
        return this.secretKey;
    }

    @Override
    public void setSecretKey(SecretKey secretKey) {
        Assert.notNull(secretKey, "Parameter \"secretKey\" must not null. ");
        this.secretKey = secretKey;
    }

    @Override
    public byte[] encrypt(byte[] data) throws GeneralSecurityException {
        data = this.fillNoPadding(data);
        SecretKey secretKey = this.getSecretKey();
        Cipher cipher = this.createCipher(Cipher.ENCRYPT_MODE, secretKey, null);
        return cipher.doFinal(data);
    }

    @Override
    public byte[] decrypt(byte[] data) throws GeneralSecurityException {
        SecretKey secretKey = this.getSecretKey();
        Cipher cipher = this.createCipher(Cipher.DECRYPT_MODE, secretKey, null);
        return cipher.doFinal(data);
    }

}
