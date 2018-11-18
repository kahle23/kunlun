package artoria.codec;

/**
 * Provide the highest level of abstraction for Decoder.
 * @param <T> Will decode type
 * @author Kahle
 */
public interface Decoder<T> {

    /**
     * Decode a source data and return the result as source data type.
     * @param source Data to be decoded
     * @return Decoded content
     * @throws DecodeException Thrown if decode error
     */
    T decode(T source) throws DecodeException;

}
