package artoria.crypto;

import javax.crypto.Cipher;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.PrivateKey;
import java.security.PublicKey;

import static artoria.crypto.KeyType.PUBLIC_KEY;

/**
 * Default implementation of asymmetric encryption and decryption tools.
 * @author Kahle
 */
public class DefaultAsymmetricCrypto extends AbstractCrypto implements AsymmetricCrypto {
    private PrivateKey privateKey;
    private PublicKey publicKey;

    @Override
    public PublicKey getPublicKey() {

        return this.publicKey;
    }

    @Override
    public void setPublicKey(PublicKey publicKey) {

        this.publicKey = publicKey;
    }

    @Override
    public PrivateKey getPrivateKey() {

        return this.privateKey;
    }

    @Override
    public void setPrivateKey(PrivateKey privateKey) {

        this.privateKey = privateKey;
    }

    @Override
    public byte[] encrypt(byte[] data, KeyType keyType) throws GeneralSecurityException {
        Key key = keyType == PUBLIC_KEY ? this.getPublicKey() : this.getPrivateKey();
        Cipher cipher = this.createCipher(Cipher.ENCRYPT_MODE, key, null);
        return cipher.doFinal(data);
    }

    @Override
    public byte[] decrypt(byte[] data, KeyType keyType) throws GeneralSecurityException {
        Key key = keyType == PUBLIC_KEY ? this.getPublicKey() : this.getPrivateKey();
        Cipher cipher = this.createCipher(Cipher.DECRYPT_MODE, key, null);
        return cipher.doFinal(data);
    }

}
