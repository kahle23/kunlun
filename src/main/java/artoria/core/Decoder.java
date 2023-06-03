package artoria.core;

/**
 * Provide the highest level of abstraction for decoder.
 * @author Kahle
 */
public interface Decoder<T> {

    /**
     * Decode a source data and return the result.
     * @param source The data to be decoded
     * @return The decoded content
     */
    T decode(T source);

}
