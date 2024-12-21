/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.core;

import java.io.InputStream;

/**
 * Provide the highest level of abstraction for digest tools.
 *
 * @see <a href="https://en.wikipedia.org/wiki/MD5">MD5</a>
 * @see <a href="https://en.wikipedia.org/wiki/HMAC">HMAC</a>
 * @author Kahle
 */
public interface Digester {

    /**
     * Perform digest operations on the data.
     * @param config The config of the digest
     * @param data The data to be digested
     * @return The result after digest
     */
    byte[] digest(Config config, byte[] data);

    /**
     * Perform digest operations on the data.
     * @param config The config of the digest
     * @param data The data to be digested
     * @return The result after digest
     */
    byte[] digest(Config config, InputStream data);

    /**
     * The configuration of the digest.
     * @author Kahle
     */
    interface Config {

        /**
         * Get the digest algorithm.
         * @return The digest algorithm
         */
        String getAlgorithm();

    }

}
