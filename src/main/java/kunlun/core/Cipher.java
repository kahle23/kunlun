/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.core;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * Provide the highest level of abstraction for cipher (encipher, decipher).
 *
 * @see <a href="https://en.wikipedia.org/wiki/Cipher">Cipher</a>
 * @author Kahle
 */
public interface Cipher {

    /**
     * Encrypt the input data.
     * @param config The config of the encrypt
     * @param data The data to be encrypted
     * @return The result of encryption
     */
    byte[] encrypt(Config config, byte[] data);

    /**
     * Decrypt the input data.
     * @param config The config of the decrypt
     * @param data The data to be decrypted
     * @return The result of decryption
     */
    byte[] decrypt(Config config, byte[] data);

    /**
     * Encrypt the input data.
     * @param config The config of the encrypt
     * @param data The data to be encrypted
     * @param out The encrypted data output
     */
    void encrypt(Config config, InputStream data, OutputStream out);

    /**
     * Decrypt the input data.
     * @param config The config of the decrypt
     * @param data The data to be decrypted
     * @param out The decrypted data output
     */
    void decrypt(Config config, InputStream data, OutputStream out);

    /**
     * The configuration of the cipher.
     * @author Kahle
     */
    interface Config {

        /**
         * Get the cipher transformation.
         * @return The cipher transformation
         */
        String getTransformation();

        /**
         * Get the key's data.
         * @return The key's data
         */
        Object getKey();

    }

}
