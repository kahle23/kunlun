package artoria.serialize;

import java.io.IOException;
import java.io.InputStream;

/**
 * Deserializer converting from data in an InputStream to an Object.
 * @author Kahle
 */
public interface Deserializer<T> {

    /**
     * Read (assemble) an object of type T from the given InputStream.
     * @param inputStream The input stream
     * @return The deserialize object
     * @throws IOException In case of errors reading from the stream
     */
    T deserialize(InputStream inputStream) throws IOException;

}
