package artoria.codec;

import artoria.codec.support.Base64;
import artoria.codec.support.Hex;
import artoria.codec.support.Unicode;
import artoria.core.Decoder;
import artoria.core.Encoder;
import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import artoria.util.Assert;
import artoria.util.MapUtils;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static artoria.codec.CodecUtils.*;

/**
 * The simple codec provider.
 * @author Kahle
 */
public class SimpleCodecProvider implements CodecProvider {
    private static final Logger log = LoggerFactory.getLogger(SimpleCodecProvider.class);
    protected final Map<String, Object>  commonProperties;
    protected final Map<String, Encoder<Object>> encoders;
    protected final Map<String, Decoder<Object>> decoders;

    protected SimpleCodecProvider(Map<String, Object> commonProperties,
                                  Map<String, Encoder<Object>> encoders,
                                  Map<String, Decoder<Object>> decoders) {
        Assert.notNull(commonProperties, "Parameter \"commonProperties\" must not null. ");
        Assert.notNull(encoders, "Parameter \"encoders\" must not null. ");
        Assert.notNull(decoders, "Parameter \"decoders\" must not null. ");
        this.commonProperties = commonProperties;
        this.encoders = encoders;
        this.decoders = decoders;
        // Register the default codec.
        // The unicode.
        Unicode unicode = new Unicode();
        registerEncoder(UNICODE, unicode);
        registerDecoder(UNICODE, unicode);
        // The base64.
        Base64 base64 = new Base64();
        registerEncoder(BASE64, base64);
        registerDecoder(BASE64, base64);
        // The hex.
        Hex hex = new Hex();
        registerEncoder(HEX, hex);
        registerDecoder(HEX, hex);
    }

    public SimpleCodecProvider() {
        this(new ConcurrentHashMap<String, Object>(),
                new ConcurrentHashMap<String, Encoder<Object>>(),
                new ConcurrentHashMap<String, Decoder<Object>>()
        );
    }

    @Override
    public void registerCommonProperties(Map<?, ?> commonProperties) {
        if (MapUtils.isEmpty(commonProperties)) { return; }
        for (Map.Entry<?, ?> entry : commonProperties.entrySet()) {
            String keyStr = String.valueOf(entry.getKey());
            this.commonProperties.put(keyStr, entry.getValue());
        }
    }

    @Override
    public void clearCommonProperties() {

        this.commonProperties.clear();
    }

    @Override
    public Map<String, Object> getCommonProperties() {

        return Collections.unmodifiableMap(commonProperties);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void registerEncoder(String name, Encoder<?> encoder) {
        Assert.notNull(encoder, "Parameter \"encoder\" must not null. ");
        Assert.notBlank(name, "Parameter \"name\" must not blank. ");
        String className = encoder.getClass().getName();
        //encoder.setCommonProperties(getCommonProperties())
        encoders.put(name, (Encoder<Object>) encoder);
        log.debug("Register the encoder \"{}\" to \"{}\". ", className, name);
    }

    @Override
    public void deregisterEncoder(String name) {
        Assert.notBlank(name, "Parameter \"name\" must not blank. ");
        Encoder<?> remove = encoders.remove(name);
        if (remove != null) {
            String className = remove.getClass().getName();
            log.debug("Deregister the encoder \"{}\" from \"{}\". ", className, name);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void registerDecoder(String name, Decoder<?> decoder) {
        Assert.notNull(decoder, "Parameter \"decoder\" must not null. ");
        Assert.notBlank(name, "Parameter \"name\" must not blank. ");
        String className = decoder.getClass().getName();
        decoders.put(name, (Decoder<Object>) decoder);
        //decoder.setCommonProperties(getCommonProperties())
        log.debug("Register the decoder \"{}\" to \"{}\". ", className, name);
    }

    @Override
    public void deregisterDecoder(String name) {
        Assert.notBlank(name, "Parameter \"name\" must not blank. ");
        Decoder<?> remove = decoders.remove(name);
        if (remove != null) {
            String className = remove.getClass().getName();
            log.debug("Deregister the decoder \"{}\" from \"{}\". ", className, name);
        }
    }

    @Override
    public Encoder<Object> getEncoder(String name) {
        Assert.notBlank(name, "Parameter \"name\" must not blank. ");
        Encoder<Object> encoder = encoders.get(name);
        Assert.notNull(encoder, "The corresponding encoder could not be found by name. ");
        return encoder;
    }

    @Override
    public Decoder<Object> getDecoder(String name) {
        Assert.notBlank(name, "Parameter \"name\" must not blank. ");
        Decoder<Object> decoder = decoders.get(name);
        Assert.notNull(decoder, "The corresponding decoder could not be found by name. ");
        return decoder;
    }

    @Override
    public Object encode(String name, Object source) {

        return getEncoder(name).encode(source);
    }

    @Override
    public Object decode(String name, Object source) {

        return getDecoder(name).decode(source);
    }

}
