package artoria.crypto;

import java.security.GeneralSecurityException;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * Abstract asymmetric encryption and decryption tools.
 * @author Kahle
 */
public interface AsymmetricCrypto extends Crypto {

    /**
     * Get public key.
     * @return Public key
     */
    PublicKey getPublicKey();

    /**
     * Set public key.
     * @param publicKey Public key
     */
    void setPublicKey(PublicKey publicKey);

    /**
     * Get private key.
     * @return Private key
     */
    PrivateKey getPrivateKey();

    /**
     * Set private key.
     * @param privateKey Private key
     */
    void setPrivateKey(PrivateKey privateKey);

    /**
     * Encrypt the data.
     * @param data Data that will be encrypted
     * @param keyType The type of key to use
     * @return The encrypted result
     * @throws GeneralSecurityException An error occurred while encrypting
     */
    byte[] encrypt(byte[] data, KeyType keyType) throws GeneralSecurityException;

    /**
     * Decrypt the data.
     * @param data Data to be decrypted
     * @param keyType The type of key to use
     * @return The result of decryption
     * @throws GeneralSecurityException An error occurred while decrypting
     */
    byte[] decrypt(byte[] data, KeyType keyType) throws GeneralSecurityException;

}
