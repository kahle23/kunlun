package artoria.codec;

/**
 * Defines common encoding methods for byte array encoders.
 * @author Kahle
 */
public interface BinaryEncoder extends Encoder {

    /**
     * Encodes a byte array and return the encoded data as a byte array.
     * @param source Data to be encoded
     * @return A byte array containing the encoded data
     * @throws EncodeException Thrown if encode error
     */
    byte[] encode(byte[] source) throws EncodeException;

}
