package artoria.codec;

import artoria.core.Decoder;
import artoria.core.Encoder;
import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import artoria.util.Assert;

public class CodecUtils {
    private static final Logger log = LoggerFactory.getLogger(CodecUtils.class);
    private static volatile CodecProvider codecProvider;
    public static final String UNICODE = "unicode";
    public static final String BASE64  = "base64";
    public static final String HEX     = "hex";

    public static CodecProvider getCodecProvider() {
        if (codecProvider != null) { return codecProvider; }
        synchronized (CodecUtils.class) {
            if (codecProvider != null) { return codecProvider; }
            CodecUtils.setCodecProvider(new SimpleCodecProvider());
            return codecProvider;
        }
    }

    public static void setCodecProvider(CodecProvider codecProvider) {
        Assert.notNull(codecProvider, "Parameter \"codecProvider\" must not null. ");
        log.info("Set codec provider: {}", codecProvider.getClass().getName());
        CodecUtils.codecProvider = codecProvider;
    }

    public static void registerEncoder(String name, Encoder<?> encoder) {

        getCodecProvider().registerEncoder(name, encoder);
    }

    public static void deregisterEncoder(String name) {

        getCodecProvider().deregisterEncoder(name);
    }

    public static void registerDecoder(String name, Decoder<?> decoder) {

        getCodecProvider().registerDecoder(name, decoder);
    }

    public static void deregisterDecoder(String name) {

        getCodecProvider().deregisterEncoder(name);
    }

    public static Encoder<?> getEncoder(String name) {

        return getCodecProvider().getEncoder(name);
    }

    public static Decoder<?> getDecoder(String name) {

        return getCodecProvider().getDecoder(name);
    }

    public static Object encode(String name, Object source) {

        return getCodecProvider().encode(name, source);
    }

    public static Object decode(String name, Object source) {

        return getCodecProvider().decode(name, source);
    }

    // ----

    public static byte[] encode(String name, byte[] source) {

        return (byte[]) getCodecProvider().encode(name, source);
    }

    public static byte[] decode(String name, byte[] source) {

        return (byte[]) getCodecProvider().decode(name, source);
    }

    public static String encodeToString(String name, byte[] source) {
        byte[] encode = encode(name, source);
        return new String(encode);
    }

    public static byte[] decodeFromString(String name, String source) {
        Assert.notNull(source, "Parameter \"source\" must not null. ");
        byte[] sourceBytes = source.getBytes();
        return decode(name, sourceBytes);
    }

    // ----

    public static String encode(String name, String source) {

        return (String) getCodecProvider().encode(name, source);
    }

    public static String decode(String name, String source) {

        return (String) getCodecProvider().decode(name, source);
    }

}
