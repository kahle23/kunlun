package com.apyhs.artoria.serialize;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Serializer.
 * @author Kahle
 */
public interface Serializer<T> {

    /**
     * Do serialize
     * @param object The object will serialize
     * @param outputStream Serialize to output stream
     * @throws IOException IO exception
     */
    void serialize(T object, OutputStream outputStream) throws IOException;

}
