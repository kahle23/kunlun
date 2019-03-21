package artoria.codec;

/**
 * Defines common encoding methods for String encoders.
 * @author Kahle
 */
public interface StringEncoder extends Encoder {

    /**
     * Encodes a String and returns a String.
     * @param source The String to encode
     * @return The encoded String
     * @throws EncodeException Thrown if encode error
     */
    String encode(String source) throws EncodeException;

}
