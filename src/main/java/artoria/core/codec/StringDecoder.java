package artoria.core.codec;

/**
 * Defines common decoding methods for String decoders.
 * @author Kahle
 */
public interface StringDecoder extends Decoder {

    /**
     * Decodes a string and returns a string.
     * @param source The string to decode
     * @return The decoded string
     */
    String decode(String source);

}
