package artoria.codec;

/**
 * Defines common decoding methods for byte array decoders.
 * @author Kahle
 */
public interface BinaryDecoder extends Decoder {

    /**
     * Decodes a byte array and returns the results as a byte array.
     * @param source Data to be decoded
     * @return A byte array that contains decoded content
     * @throws DecodeException Thrown if decode error
     */
    byte[] decode(byte[] source) throws DecodeException;

}
