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
    private static UnicodeFactory unicodeFactory;

    public static UnicodeFactory getUnicodeFactory() {
        if (unicodeFactory != null) { return unicodeFactory; }
        synchronized (UnicodeUtils.class) {
            if (unicodeFactory != null) { return unicodeFactory; }
            UnicodeUtils.setUnicodeFactory(new SimpleUnicodeFactory());
            return unicodeFactory;
        }
    }

    public static void setUnicodeFactory(UnicodeFactory unicodeFactory) {
        Assert.notNull(unicodeFactory, "Parameter \"unicodeFactory\" must not null. ");
        log.info("Set unicode factory: {}", unicodeFactory.getClass().getName());
        UnicodeUtils.unicodeFactory = unicodeFactory;
    }

    public static String encode(String source) {

        return getUnicodeFactory().getInstance().encode(source);
    }

    public static String decode(String source) {

        return getUnicodeFactory().getInstance().decode(source);
    }

}
