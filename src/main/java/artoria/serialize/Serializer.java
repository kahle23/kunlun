package artoria.serialize;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Serializer for streaming an object to an OutputStream.
 * @author Kahle
 */
public interface Serializer<T> {

    /**
     * Write an object of type T to the given OutputStream.
     * @param object The object to serialize
     * @param outputStream The output stream
     * @throws IOException In case of errors writing to the stream
     */
    void serialize(T object, OutputStream outputStream) throws IOException;

}
