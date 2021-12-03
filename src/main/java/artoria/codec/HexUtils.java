package artoria.codec;

import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import artoria.util.Assert;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

/**
 * The hex tools.
 * @author Kahle
 */
public class HexUtils {
    private static Logger log = LoggerFactory.getLogger(HexUtils.class);
    private static Hex upperCaseHex;
    private static Hex lowerCaseHex;

    public static Hex getUpperCaseHex() {
        if (upperCaseHex != null) { return upperCaseHex; }
        synchronized (HexUtils.class) {
            if (upperCaseHex != null) { return upperCaseHex; }
            setUpperCaseHex(new Hex(TRUE));
            return upperCaseHex;
        }
    }

    public static void setUpperCaseHex(Hex upperCaseHex) {
        Assert.notNull(upperCaseHex, "Parameter \"upperCaseHex\" must not null. ");
        log.info("Set upper case hex: {}", upperCaseHex.getClass().getName());
        HexUtils.upperCaseHex = upperCaseHex;
    }

    public static Hex getLowerCaseHex() {
        if (lowerCaseHex != null) { return lowerCaseHex; }
        synchronized (HexUtils.class) {
            if (lowerCaseHex != null) { return lowerCaseHex; }
            setLowerCaseHex(new Hex(FALSE));
            return lowerCaseHex;
        }
    }

    public static void setLowerCaseHex(Hex lowerCaseHex) {
        Assert.notNull(lowerCaseHex, "Parameter \"lowerCaseHex\" must not null. ");
        log.info("Set lower case hex: {}", lowerCaseHex.getClass().getName());
        HexUtils.lowerCaseHex = lowerCaseHex;
    }

    public static byte[] encode(byte[] source) {

        return getLowerCaseHex().encode(source);
    }

    public static byte[] encode(byte[] source, boolean upperCase) {

        return (upperCase ? getUpperCaseHex() : getLowerCaseHex()).encode(source);
    }

    public static byte[] decode(byte[] source) {

        return getLowerCaseHex().decode(source);
    }

    public static String encodeToString(byte[] source) {

        return getLowerCaseHex().encodeToString(source);
    }

    public static String encodeToString(byte[] source, boolean upperCase) {

        return (upperCase ? getUpperCaseHex() : getLowerCaseHex()).encodeToString(source);
    }

    public static byte[] decodeFromString(String source) {

        return getLowerCaseHex().decodeFromString(source);
    }

}
