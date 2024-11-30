/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.core;

import java.io.InputStream;

/**
 * Provide a high level abstract digest tools.
 * @author Kahle
 */
public interface Digester {

    /**
     * Perform digest operations on the data.
     * @param config The digest config
     * @param data Data to be digested
     * @return Result after digest
     */
    byte[] digest(Config config, byte[] data);

    /**
     * Perform digest operations on the input stream.
     * @param config The digest config
     * @param data Input stream to be digested
     * @return Result after digest
     */
    byte[] digest(Config config, InputStream data);

    /**
     * The digest config.
     * @author Kahle
     */
    interface Config {

        /**
         * Get digest algorithm.
         * @return Digest algorithm
         */
        String getAlgorithm();

    }

}
