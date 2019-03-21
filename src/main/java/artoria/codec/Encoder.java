package artoria.codec;

/**
 * Provide the highest level of abstraction for Encoder.
 * @author Kahle
 */
public interface Encoder {

    /**
     * Encode a source data and return the encoded data.
     * @param source Data to be encoded
     * @return Encoded data
     * @throws EncodeException Thrown if encode error
     */
    Object encode(Object source) throws EncodeException;

}
