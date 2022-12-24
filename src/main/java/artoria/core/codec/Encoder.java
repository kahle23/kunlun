package artoria.core.codec;

/**
 * Provide the highest level of abstraction for encoder.
 * @author Kahle
 */
public interface Encoder {

    /**
     * Encode a source data and return the encoded data.
     * @param source The data to be encoded
     * @return The encoded data
     */
    Object encode(Object source);

}
