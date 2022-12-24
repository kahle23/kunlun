package artoria.core.codec;

/**
 * Defines common encoding methods for String encoders.
 * @author Kahle
 */
public interface StringEncoder extends Encoder {

    /**
     * Encodes a string and returns a string.
     * @param source The string to encode
     * @return The encoded string
     */
    String encode(String source);

}
