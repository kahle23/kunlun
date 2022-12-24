package artoria.core.codec;

/**
 * Provide the highest level of abstraction for decoder.
 * @author Kahle
 */
public interface Decoder {

    /**
     * Decode a source data and return the result.
     * @param source The data to be decoded
     * @return The decoded content
     */
    Object decode(Object source);

}
