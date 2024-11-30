/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.core;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * Provide the highest level of abstraction for cipher (encipher, decipher).
 * @author Kahle
 */
public interface Cipher {

    /**
     * Encrypt data.
     * @param config The cipher config
     * @param data The data
     * @return The output
     */
    byte[] encrypt(Config config, byte[] data);

    /**
     * Decrypt data.
     * @param config The cipher config
     * @param data The data
     * @return The output
     */
    byte[] decrypt(Config config, byte[] data);

    /**
     * Encrypt data.
     * @param config The cipher config
     * @param data The data
     * @param out The output
     */
    void encrypt(Config config, InputStream data, OutputStream out);

    /**
     * Decrypt data.
     * @param config The cipher config
     * @param data The data
     * @param out The output
     */
    void decrypt(Config config, InputStream data, OutputStream out);

    /**
     * The cipher config.
     * @author Kahle
     */
    interface Config {

        /**
         * Get the transformation.
         * @return The transformation
         */
        String getTransformation();

        /**
         * Get the key's data.
         * @return The key's data
         */
        Object getKey();

    }

}
