package artoria.core;

/**
 * Provide the highest level of abstraction for encoder.
 * @author Kahle
 */
public interface Encoder<T> {

    /**
     * Encode a source data and return the encoded data.
     * @param source The data to be encoded
     * @return The encoded data
     */
    T encode(T source);

}
