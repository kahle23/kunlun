package artoria.codec;

import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import artoria.util.Assert;

/**
 * Base64 tools.
 * @author Kahle
 */
public class Base64Utils {
    private static final Base64 DEFAULT_BASE64 = new Base64();
    private static Logger log = LoggerFactory.getLogger(Base64Utils.class);
    private static Base64 base64;

    public static Base64 getBase64() {

        return base64 != null ? base64 : DEFAULT_BASE64;
    }

    public static void setBase64(Base64 base64) {
        Assert.notNull(base64, "Parameter \"base64\" must not null. ");
        log.info("Set base64: {}", base64.getClass().getName());
        Base64Utils.base64 = base64;
    }

    public static Object encode(Object source) {

        return getBase64().encode(source);
    }

    public static Object decode(Object source) {

        return getBase64().decode(source);
    }

    public static byte[] encode(byte[] source) {

        return getBase64().encode(source);
    }

    public static byte[] decode(byte[] source) {

        return getBase64().decode(source);
    }

    public static String encodeToString(byte[] source) {

        return getBase64().encodeToString(source);
    }

    public static String encodeToString(byte[] source, String charset) {

        return getBase64().encodeToString(source, charset);
    }

    public static byte[] decodeFromString(String source) {

        return getBase64().decodeFromString(source);
    }

    public static byte[] decodeFromString(String source, String charset) {

        return getBase64().decodeFromString(source, charset);
    }

}
