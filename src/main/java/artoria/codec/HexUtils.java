package artoria.codec;

import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import artoria.util.Assert;

/**
 * Hex tools.
 * @author Kahle
 */
public class HexUtils {
    private static Logger log = LoggerFactory.getLogger(HexUtils.class);
    private static HexFactory hexFactory;

    public static HexFactory getHexFactory() {
        if (hexFactory != null) { return hexFactory; }
        synchronized (HexUtils.class) {
            if (hexFactory != null) { return hexFactory; }
            HexUtils.setHexFactory(new SimpleHexFactory());
            return hexFactory;
        }
    }

    public static void setHexFactory(HexFactory hexFactory) {
        Assert.notNull(hexFactory, "Parameter \"hexFactory\" must not null. ");
        log.info("Set hex factory: {}", hexFactory.getClass().getName());
        HexUtils.hexFactory = hexFactory;
    }

    public static byte[] encode(byte[] source) {

        return getHexFactory().getInstance().encode(source);
    }

    public static byte[] encode(byte[] source, boolean lowerCase) {

        return getHexFactory().getInstance(lowerCase).encode(source);
    }

    public static byte[] decode(byte[] source) {

        return getHexFactory().getInstance().decode(source);
    }

    public static String encodeToString(byte[] source) {

        return getHexFactory().getInstance().encodeToString(source);
    }

    public static String encodeToString(byte[] source, boolean lowerCase) {

        return getHexFactory().getInstance(lowerCase).encodeToString(source);
    }

    public static byte[] decodeFromString(String source) {

        return getHexFactory().getInstance().decodeFromString(source);
    }

}
