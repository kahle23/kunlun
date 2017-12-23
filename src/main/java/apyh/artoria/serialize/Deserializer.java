package apyh.artoria.serialize;

import java.io.IOException;
import java.io.InputStream;

/**
 * Deserializer
 * @author Kahle
 */
public interface Deserializer {

    /**
     * Do deserialize
     * @param inputStream Input stream have data
     * @return Data object
     * @throws IOException IO exception
     * @throws ClassNotFoundException Class not found
     */
    Object deserialize(InputStream inputStream) throws IOException, ClassNotFoundException;

}
