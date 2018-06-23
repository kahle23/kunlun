package artoria.codec;

import artoria.util.Assert;

/**
 * Unicode encode and decode tools.
 * @author Kahle
 */
public class Unicode {
    private static final String BACKLASH_U = "\\u";
    private static final int RADIX = 16;

    public static String encode(String data) {
        Assert.notBlank(data, "Parameter \"data\" must not blank. ");
        StringBuilder unicode = new StringBuilder();
        char[] chars = data.toCharArray();
        for (char c : chars) {
            String hexString = Integer.toHexString(c);
            int len = hexString.length();
            if (len != 4) {
                hexString = (len == 2 ? "00" : "0") + hexString;
            }
            unicode.append(BACKLASH_U).append(hexString);
        }
        return unicode.toString();
    }

    public static String decode(String unicode) {
        Assert.notBlank(unicode, "Parameter \"unicode\" must not blank. ");
        int index, pos = 0;
        StringBuilder result = new StringBuilder();
        while ((index = unicode.indexOf(BACKLASH_U, pos)) != -1) {
            result.append(unicode.substring(pos, index));
            if (index + 5 < unicode.length()) {
                pos = index + 6;
                String hex = unicode.substring(index + 2, index + 6);
                char ch = (char) Integer.parseInt(hex, RADIX);
                result.append(ch);
            }
        }
        if (unicode.length() > pos + 1) {
            String tmp = unicode.substring(pos, unicode.length());
            result.append(tmp);
        }
        return result.toString();
    }

}