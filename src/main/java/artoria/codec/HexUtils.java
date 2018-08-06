package artoria.codec;

import artoria.util.Assert;

/**
 * Hex encode and decode tools.
 * @author Kahle
 */
public class HexUtils {
    private static final char[] DIGITS_LOWER = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    private static final char[] DIGITS_UPPER = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
    private static final int HEX_01 = 0x01;
    private static Boolean defaultToUpperCase = false;

    private static int toDigit(final char ch, int index) {
        final int digit = Character.digit(ch, 16);
        if (digit == -1) {
            throw new IllegalArgumentException("Illegal hexadecimal character " + ch + " at index " + index);
        }
        return digit;
    }

    public static Boolean getDefaultToUpperCase() {
        return defaultToUpperCase;
    }

    public static void setDefaultToUpperCase(Boolean toUpperCase) {
        Assert.notNull(toUpperCase, "Parameter \"toUpperCase\" must not null. ");
        HexUtils.defaultToUpperCase = toUpperCase;
    }

    public static char[] encode(byte[] data) {
        // Use default to uppercase.
        return HexUtils.encode(data, defaultToUpperCase);
    }

    public static char[] encode(byte[] data, boolean toUpperCase) {
        Assert.notNull(data, "Parameter \"data\" must not null. ");
        char[] digits = toUpperCase ? DIGITS_UPPER : DIGITS_LOWER;
        int len = data.length;
        char[] out = new char[len << 1];
        // two characters form the hex value.
        for (int i = 0, j = 0; i < len; i++) {
            out[j++] = digits[(0xF0 & data[i]) >>> 4];
            out[j++] = digits[0x0F & data[i]];
        }
        return out;
    }

    public static byte[] decode(char[] data) {
        Assert.notNull(data, "Parameter \"data\" must not null. ");
        int len = data.length;
        if ((len & HEX_01) != 0) {
            throw new IllegalArgumentException("Odd number of characters.");
        }
        byte[] out = new byte[len >> 1];
        // two characters form the hex value.
        for (int i = 0, j = 0; j < len; i++) {
            int f = HexUtils.toDigit(data[j], j) << 4;
            j++;
            f = f | HexUtils.toDigit(data[j], j);
            j++;
            out[i] = (byte) (f & 0xFF);
        }
        return out;
    }

    public static String encodeToString(byte[] data) {
        char[] encode = HexUtils.encode(data);
        return String.valueOf(encode);
    }

    public static String encodeToString(byte[] data, boolean toUpperCase) {
        char[] encode = HexUtils.encode(data, toUpperCase);
        return String.valueOf(encode);
    }

    public static byte[] decodeFromString(String data) {
        Assert.notNull(data, "Parameter \"data\" must not null. ");
        char[] chars = data.toCharArray();
        return HexUtils.decode(chars);
    }

}
