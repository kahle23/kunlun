package artoria.crypto;

import javax.crypto.Cipher;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.PrivateKey;
import java.security.PublicKey;

import static artoria.crypto.KeyType.PUBLIC_KEY;

/**
 * Simple implementation of asymmetric encryption and decryption tools.
 * @author Kahle
 */
public class SimpleAsymmetricCrypto extends AbstractCrypto implements AsymmetricCrypto {
    private PrivateKey privateKey;
    private PublicKey publicKey;

    @Override
    public PublicKey getPublicKey() {

        return publicKey;
    }

    @Override
    public void setPublicKey(PublicKey publicKey) {

        this.publicKey = publicKey;
    }

    @Override
    public PrivateKey getPrivateKey() {

        return privateKey;
    }

    @Override
    public void setPrivateKey(PrivateKey privateKey) {

        this.privateKey = privateKey;
    }

    @Override
    public byte[] encrypt(byte[] data, KeyType keyType) throws GeneralSecurityException {
        Key key = keyType == PUBLIC_KEY ? getPublicKey() : getPrivateKey();
        Cipher cipher = createCipher(Cipher.ENCRYPT_MODE, key, null);
        return cipher.doFinal(data);
    }

    @Override
    public byte[] decrypt(byte[] data, KeyType keyType) throws GeneralSecurityException {
        Key key = keyType == PUBLIC_KEY ? getPublicKey() : getPrivateKey();
        Cipher cipher = createCipher(Cipher.DECRYPT_MODE, key, null);
        return cipher.doFinal(data);
    }

}
