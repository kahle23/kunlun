package artoria.core.codec;

/**
 * Defines common encoding methods for byte array encoders.
 * @author Kahle
 */
public interface BinaryEncoder extends Encoder {

    /**
     * Encodes a byte array and return the encoded data as a byte array.
     * @param source The data to be encoded
     * @return A byte array containing the encoded data
     */
    byte[] encode(byte[] source);

}
