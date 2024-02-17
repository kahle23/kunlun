/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.crypto;

import javax.crypto.SecretKey;
import java.security.GeneralSecurityException;

/**
 * Abstract symmetric encryption and decryption tools.
 * @author Kahle
 */
public interface SymmetricCrypto extends Crypto {

    /**
     * Get secret key.
     * @return Secret key
     */
    SecretKey getSecretKey();

    /**
     * Set secret key.
     * @param secretKey Secret key
     */
    void setSecretKey(SecretKey secretKey);

    /**
     * Encrypt the data.
     * @param data Data that will be encrypted
     * @return The encrypted result
     * @throws GeneralSecurityException An error occurred while encrypting
     */
    byte[] encrypt(byte[] data) throws GeneralSecurityException;

    /**
     * Decrypt the data.
     * @param data Data to be decrypted
     * @return The result of decryption
     * @throws GeneralSecurityException An error occurred while decrypting
     */
    byte[] decrypt(byte[] data) throws GeneralSecurityException;

}
