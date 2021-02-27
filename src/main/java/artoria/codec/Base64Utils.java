package artoria.codec;

import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import artoria.util.Assert;

import static artoria.common.Constants.MINUS_ONE;

/**
 * Base64 tools.
 * @author Kahle
 */
public class Base64Utils {
    private static Logger log = LoggerFactory.getLogger(Base64Utils.class);
    private static Base64Factory base64Factory;

    public static Base64Factory getBase64Factory() {
        if (base64Factory != null) { return base64Factory; }
        synchronized (Base64Utils.class) {
            if (base64Factory != null) { return base64Factory; }
            Base64Utils.setBase64Factory(new SimpleBase64Factory());
            return base64Factory;
        }
    }

    public static void setBase64Factory(Base64Factory base64Factory) {
        Assert.notNull(base64Factory, "Parameter \"base64Factory\" must not null. ");
        log.info("Set base64 factory: {}", base64Factory.getClass().getName());
        Base64Utils.base64Factory = base64Factory;
    }

    public static byte[] encode(byte[] source) {

        return getBase64Factory().getInstance().encode(source);
    }

    public static byte[] decode(byte[] source) {

        return getBase64Factory().getInstance().decode(source);
    }

    public static String encodeToString(byte[] source) {

        return getBase64Factory().getInstance().encodeToString(source);
    }

    public static byte[] decodeFromString(String source) {

        return getBase64Factory().getInstance().decodeFromString(source);
    }

    public static byte[] encodeUrlSafe(byte[] source) {

        return getBase64Factory().getInstance(true).encode(source);
    }

    public static byte[] decodeUrlSafe(byte[] source) {

        return getBase64Factory().getInstance(true).decode(source);
    }

    public static String encodeToUrlSafeString(byte[] source) {

        return getBase64Factory().getInstance(true).encodeToString(source);
    }

    public static byte[] decodeFromUrlSafeString(String source) {

        return getBase64Factory().getInstance(true).decodeFromString(source);
    }

    public static byte[] encodeMime(byte[] source) {

        return getBase64Factory().getInstance(true, MINUS_ONE, null).encode(source);
    }

    public static byte[] decodeMime(byte[] source) {

        return getBase64Factory().getInstance(true, MINUS_ONE, null).decode(source);
    }

    public static String encodeToMimeString(byte[] source) {

        return getBase64Factory().getInstance(true, MINUS_ONE, null).encodeToString(source);
    }

    public static byte[] decodeFromMimeString(String source) {

        return getBase64Factory().getInstance(true, MINUS_ONE, null).decodeFromString(source);
    }

}
