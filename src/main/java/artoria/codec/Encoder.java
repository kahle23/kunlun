package artoria.codec;

/**
 * Provide the highest level of abstraction for Encoder.
 * @param <T> Will encode type
 * @author Kahle
 */
public interface Encoder<T> {

    /**
     * Encode a source data and return the encoded data as source data type.
     * @param source Data to be encoded
     * @return Encoded data
     * @throws EncodeException Thrown if encode error
     */
    T encode(T source) throws EncodeException;

}
