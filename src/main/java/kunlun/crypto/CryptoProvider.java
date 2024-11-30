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

public interface CryptoProvider {

    SymmetricCipher getSymmetricCipher();

    AsymmetricCipher getAsymmetricCipher();

    Hash getHash();

    Hmac getHmac();

    /**
     * Register the cipher.
     * @param transformation The transformation
     * @param cipher The cipher
     */
    void registerCipher(String transformation, Cipher cipher);

    /**
     * Deregister the cipher.
     * @param transformation The transformation
     */
    void deregisterCipher(String transformation);

    /**
     * Get the cipher.
     * @param transformation The transformation
     * @return The cipher or null
     */
    Cipher getCipher(String transformation);

    /**
     * Register the digester.
     * @param algorithm The digest algorithm
     * @param digester The digester
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
     * Encrypt data.
     * @param config The key
     * @param data The data
     * @return The output
     */
    byte[] encrypt(Cipher.Config config, byte[] data);

    /**
     * Decrypt data.
     * @param config The key
     * @param data The data
     * @return The output
     */
    byte[] decrypt(Cipher.Config config, byte[] data);

    /**
     * Encrypt data.
     * @param config The key
     * @param data The data
     * @param out The output
     */
    void encrypt(Cipher.Config config, InputStream data, OutputStream out);

    /**
     * Decrypt data.
     * @param config The key
     * @param data The data
     * @param out The output
     */
    void decrypt(Cipher.Config config, InputStream data, OutputStream out);

    /**
     * Perform digest operations on the data.
     * @param config The digest config
     * @param data Data to be digested
     * @return Result after digest
     */
    byte[] digest(Digester.Config config, byte[] data);

    /**
     * Perform digest operations on the input stream.
     * @param config The digest config
     * @param data Input stream to be digested
     * @return Result after digest
     */
    byte[] digest(Digester.Config config, InputStream data);

}
