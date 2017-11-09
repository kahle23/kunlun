package saber.codec;

/**
 * @author Kahle
 */
public class Unicode {

    public static String encode(String string) {
        StringBuilder unicode = new StringBuilder();
        char[] chars = string.toCharArray();
        for (char c : chars) {
            unicode.append("\\u")
                    .append(Integer.toHexString(c));
        }
        return unicode.toString();
    }

    public static String decode(String unicode) {
        StringBuilder result = new StringBuilder();
        String[] hexs = unicode.split("\\\\u");
        for (int i = 1; i < hexs.length; i++) {
            result.append((char) Integer.parseInt(hexs[i], 16));
        }
        return result.toString();
    }

}
