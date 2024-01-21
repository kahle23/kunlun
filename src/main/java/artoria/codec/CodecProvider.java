package artoria.codec;

import artoria.core.Decoder;
import artoria.core.Encoder;

import java.util.Map;

/**
 * The codec tools provider.
 * @see artoria.core.Encoder
 * @see artoria.core.Decoder
 * @author Kahle
 */
public interface CodecProvider {

    /**
     * Register common properties' information.
     * @param commonProperties The common properties
     */
    void registerCommonProperties(Map<?, ?> commonProperties);

    /**
     * Clears common properties' information.
     */
    void clearCommonProperties();

    /**
     * Get common properties' information.
     * @return The common properties
     */
    Map<String, Object> getCommonProperties();

    /**
     * Register the encoder.
     * @param name The encoder name
     * @param encoder The encoder
     */
    void registerEncoder(String name, Encoder<?> encoder);

    /**
     * Deregister the encoder.
     * @param name The encoder name
     */
    void deregisterEncoder(String name);

    /**
     * Register the decoder.
     * @param name The decoder name
     * @param decoder The decoder
     */
    void registerDecoder(String name, Decoder<?> decoder);

    /**
     * Deregister the decoder.
     * @param name The decoder name
     */
    void deregisterDecoder(String name);

    /**
     * Get the encoder by name.
     * @param name The encoder name
     * @return The encoder
     */
    Encoder<Object> getEncoder(String name);

    /**
     * Get the decoder by name.
     * @param name The decoder name
     * @return The decoder
     */
    Decoder<Object> getDecoder(String name);

    /**
     * Encode a source data and return the encoded data.
     * @param name The encoder name
     * @param source The data to be encoded
     * @return The encoded data
     */
    Object encode(String name, Object source);

    /**
     * Decode a source data and return the result.
     * @param name The decoder name
     * @param source The data to be decoded
     * @return The decoded content
     */
    Object decode(String name, Object source);

}
