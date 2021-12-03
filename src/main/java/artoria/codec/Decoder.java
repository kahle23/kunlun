package artoria.codec;

/**
 * Provide the highest level of abstraction for decoder.
 * @author Kahle
 */
public interface Decoder {

    /**
     * Decode a source data and return the result.
     * @param source Data to be decoded
     * @return Decoded content
     * @throws DecodeException Thrown if decode error
     */
    Object decode(Object source) throws DecodeException;

}
