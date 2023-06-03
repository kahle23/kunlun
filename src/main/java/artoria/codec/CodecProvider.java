package artoria.codec;

import artoria.core.Decoder;
import artoria.core.Encoder;

import java.util.Map;

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
     * Register the proxy handler.
     * @param name The proxy handler name
     * @param encoder The proxy handler
     */
    void registerEncoder(String name, Encoder<?> encoder);

    /**
     * Deregister the proxy handler.
     * @param name The proxy handler name
     */
    void deregisterEncoder(String name);

    /**
     * Register the proxy handler.
     * @param name The proxy handler name
     * @param decoder The proxy handler
     */
    void registerDecoder(String name, Decoder<?> decoder);

    /**
     * Deregister the proxy handler.
     * @param name The proxy handler name
     */
    void deregisterDecoder(String name);

    Encoder<Object> getEncoder(String name);

    Decoder<Object> getDecoder(String name);

    Object encode(String name, Object source);

    Object decode(String name, Object source);

}
