package artoria.core.codec;

/**
 * Defines common decoding methods for byte array decoders.
 * @author Kahle
 */
public interface BinaryDecoder extends Decoder {

    /**
     * Decodes a byte array and returns the results as a byte array.
     * @param source The data to be decoded
     * @return A byte array that contains decoded content
     */
    byte[] decode(byte[] source);

}
