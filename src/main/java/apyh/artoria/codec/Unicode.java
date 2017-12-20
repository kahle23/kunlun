package apyh.artoria.codec;

import apyh.artoria.util.Assert;

/**
 * Unicode encode and decode tools.
 * @author Kahle
 */
public class Unicode {

    private static final String BACKLASH_U = "\\u";
    private static final String REGEX_BACKLASH_U = "\\\\u";
    private static final int RADIX = 16;

    public static String encode(String string) {
        Assert.notNull(string, "String must is not null. ");
        StringBuilder unicode = new StringBuilder();
        char[] chars = string.toCharArray();
        for (char c : chars) {
            String hexString = Integer.toHexString(c);
            unicode.append(BACKLASH_U).append(hexString);
        }
        return unicode.toString();
    }

    public static String decode(String unicode) {
        Assert.notNull(unicode, "Unicode must is not null. ");
        StringBuilder result = new StringBuilder();
        String[] hexs = unicode.split(REGEX_BACKLASH_U);
        for (int i = 1; i < hexs.length; i++) {
            int anInt = Integer.parseInt(hexs[i], RADIX);
            result.append((char) anInt);
        }
        return result.toString();
    }

}
