package artoria.codec;

/**
 * Defines common decoding methods for String decoders.
 * @author Kahle
 */
public interface StringDecoder extends Decoder {

    /**
     * Decodes a String and returns a String.
     * @param source The String to decode
     * @return The decoded String
     * @throws DecodeException Thrown if decode error
     */
    String decode(String source) throws DecodeException;

}
