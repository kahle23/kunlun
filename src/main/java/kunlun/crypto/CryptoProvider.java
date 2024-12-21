/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.crypto;

import kunlun.core.Cipher;
import kunlun.core.Digester;
import kunlun.crypto.cipher.AsymmetricCipher;
import kunlun.crypto.cipher.SymmetricCipher;
import kunlun.crypto.digest.Hash;
import kunlun.crypto.digest.Hmac;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * The crypto tools provider.
 * @author Kahle
 */
public interface CryptoProvider {

    /**
     * Get the symmetric cipher.
     * @return The symmetric cipher
     */
    SymmetricCipher getSymmetricCipher();

    /**
     * Get the asymmetric cipher.
     * @return The asymmetric cipher
     */
    AsymmetricCipher getAsymmetricCipher();

    /**
     * Get the hash.
     * @return The hash.
     */
    Hash getHash();

    /**
     * Get the hmac.
     * @return The hmac.
     */
    Hmac getHmac();

    /**
     * Register the cipher.
     * @param transformation The cipher transformation
     * @param cipher The cipher object
     */
    void registerCipher(String transformation, Cipher cipher);

    /**
     * Deregister the cipher.
     * @param transformation The cipher transformation
     */
    void deregisterCipher(String transformation);

    /**
     * Get the cipher.
     * @param transformation The cipher transformation
     * @return The cipher or null
     */
    Cipher getCipher(String transformation);

    /**
     * Register the digester.
     * @param algorithm The digest algorithm
     * @param digester The digester object
     */
    void registerDigester(String algorithm, Digester digester);

    /**
     * Deregister the digester.
     * @param algorithm The digest algorithm
     */
    void deregisterDigester(String algorithm);

    /**
     * Get the digester.
     * @param algorithm The digest algorithm
     * @return The digester or null
     */
    Digester getDigester(String algorithm);

    /**
     * Encrypt the input data.
     * @param config The config of the encrypt
     * @param data The data to be encrypted
     * @return The result of encryption
     */
    byte[] encrypt(Cipher.Config config, byte[] data);

    /**
     * Decrypt the input data.
     * @param config The config of the decrypt
     * @param data The data to be decrypted
     * @return The result of decryption
     */
    byte[] decrypt(Cipher.Config config, byte[] data);

    /**
     * Encrypt the input data.
     * @param config The config of the encrypt
     * @param data The data to be encrypted
     * @param out The encrypted data output
     */
    void encrypt(Cipher.Config config, InputStream data, OutputStream out);

    /**
     * Decrypt the input data.
     * @param config The config of the decrypt
     * @param data The data to be decrypted
     * @param out The decrypted data output
     */
    void decrypt(Cipher.Config config, InputStream data, OutputStream out);

    /**
     * Perform digest operations on the data.
     * @param config The config of the digest
     * @param data The data to be digested
     * @return The result after digest
     */
    byte[] digest(Digester.Config config, byte[] data);

    /**
     * Perform digest operations on the data.
     * @param config The config of the digest
     * @param data The data to be digested
     * @return The result after digest
     */
    byte[] digest(Digester.Config config, InputStream data);

}
