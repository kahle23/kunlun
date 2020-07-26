package artoria.codec;

import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import artoria.util.Assert;

/**
 * Unicode tools.
 * @author Kahle
 */
public class UnicodeUtils {
    private static Logger log = LoggerFactory.getLogger(UnicodeUtils.class);
    private static Unicode unicode;

    public static Unicode getUnicode() {
        if (unicode != null) { return unicode; }
        synchronized (UnicodeUtils.class) {
            if (unicode != null) { return unicode; }
            UnicodeUtils.setUnicode(new Unicode());
            return unicode;
        }
    }

    public static void setUnicode(Unicode unicode) {
        Assert.notNull(unicode, "Parameter \"unicode\" must not null. ");
        log.info("Set unicode: {}", unicode.getClass().getName());
        UnicodeUtils.unicode = unicode;
    }

    public static Object encode(Object source) {

        return getUnicode().encode(source);
    }

    public static Object decode(Object source) {

        return getUnicode().decode(source);
    }

    public static String encode(String source) {

        return getUnicode().encode(source);
    }

    public static String decode(String source) {

        return getUnicode().decode(source);
    }

}
