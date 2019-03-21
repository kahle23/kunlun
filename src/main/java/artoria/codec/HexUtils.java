package artoria.codec;

import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import artoria.util.Assert;

/**
 * Hex tools.
 * @author Kahle
 */
public class HexUtils {
    private static final Hex DEFAULT_HEX = new Hex();
    private static Logger log = LoggerFactory.getLogger(HexUtils.class);
    private static Hex hex;

    public static Hex getHex() {

        return hex != null ? hex : DEFAULT_HEX;
    }

    public static void setHex(Hex hex) {
        Assert.notNull(hex, "Parameter \"hex\" must not null. ");
        log.info("Set hex: {}", hex.getClass().getName());
        HexUtils.hex = hex;
    }

    public static Object encode(Object source) {

        return getHex().encode(source);
    }

    public static Object decode(Object source) {

        return getHex().decode(source);
    }

    public static byte[] encode(byte[] source) {

        return getHex().encode(source);
    }

    public static byte[] decode(byte[] source) {

        return getHex().decode(source);
    }

    public static String encodeToString(byte[] source) {

        return getHex().encodeToString(source);
    }

    public static byte[] decodeFromString(String source) {

        return getHex().decodeFromString(source);
    }

}
