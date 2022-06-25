package artoria.codec;

import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import artoria.util.Assert;

import static artoria.common.Constants.MINUS_ONE;
import static java.lang.Boolean.TRUE;

/**
 * The base64 tools.
 * @author Kahle
 */
public class Base64Utils {
    private static Logger log = LoggerFactory.getLogger(Base64Utils.class);
    private static volatile Base64 mimeBase64;
    private static volatile Base64 urlBase64;
    private static volatile Base64 base64;

    public static Base64 getBase64() {
        if (base64 != null) { return base64; }
        synchronized (Base64Utils.class) {
            if (base64 != null) { return base64; }
            setBase64(new Base64());
            return base64;
        }
    }

    public static void setBase64(Base64 base64) {
        Assert.notNull(base64, "Parameter \"base64\" must not null. ");
        log.info("Set base64: {}", base64.getClass().getName());
        Base64Utils.base64 = base64;
    }

    public static Base64 getUrlBase64() {
        if (urlBase64 != null) { return urlBase64; }
        synchronized (Base64Utils.class) {
            if (urlBase64 != null) { return urlBase64; }
            setUrlBase64(new Base64(TRUE));
            return urlBase64;
        }
    }

    public static void setUrlBase64(Base64 urlBase64) {
        Assert.notNull(urlBase64, "Parameter \"urlBase64\" must not null. ");
        log.info("Set url base64: {}", urlBase64.getClass().getName());
        Base64Utils.urlBase64 = urlBase64;
    }

    public static Base64 getMimeBase64() {
        if (mimeBase64 != null) { return mimeBase64; }
        synchronized (Base64Utils.class) {
            if (mimeBase64 != null) { return mimeBase64; }
            setMimeBase64(new Base64(TRUE, MINUS_ONE, null));
            return mimeBase64;
        }
    }

    public static void setMimeBase64(Base64 mimeBase64) {
        Assert.notNull(mimeBase64, "Parameter \"mimeBase64\" must not null. ");
        log.info("Set mime base64: {}", mimeBase64.getClass().getName());
        Base64Utils.mimeBase64 = mimeBase64;
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

    public static byte[] decodeFromString(String source) {

        return getBase64().decodeFromString(source);
    }

    public static byte[] encodeUrlSafe(byte[] source) {

        return getUrlBase64().encode(source);
    }

    public static byte[] decodeUrlSafe(byte[] source) {

        return getUrlBase64().decode(source);
    }

    public static String encodeToUrlSafeString(byte[] source) {

        return getUrlBase64().encodeToString(source);
    }

    public static byte[] decodeFromUrlSafeString(String source) {

        return getUrlBase64().decodeFromString(source);
    }

    public static byte[] encodeMime(byte[] source) {

        return getMimeBase64().encode(source);
    }

    public static byte[] decodeMime(byte[] source) {

        return getMimeBase64().decode(source);
    }

    public static String encodeToMimeString(byte[] source) {

        return getMimeBase64().encodeToString(source);
    }

    public static byte[] decodeFromMimeString(String source) {

        return getMimeBase64().decodeFromString(source);
    }

}
