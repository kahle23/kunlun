package com.github.kahlkn.artoria.serialize;

import java.io.IOException;
import java.io.InputStream;

/**
 * Deserializer.
 * @author Kahle
 */
public interface Deserializer<T> {

    /**
     * Do deserialize
     * @param inputStream Input stream have data
     * @return Data object
     * @throws IOException IO exception
     */
    T deserialize(InputStream inputStream) throws IOException;

}
