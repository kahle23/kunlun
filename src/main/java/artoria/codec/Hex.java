package artoria.codec;

/**
 * @author Kahle
 */
public class Hex {
    public static final char[] DIGITS_LOWER = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    public static final char[] DIGITS_UPPER = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
    private static final int HEX_01 = 0x01;

    public static final Hex ME = Hex.create();

    public static Hex create() {
        return new Hex();
    }

    public static Hex create(boolean toUpperCase) {
        return new Hex().setToUpperCase(toUpperCase);
    }

    private static int toDigit(final char ch, int index) {
        final int digit = Character.digit(ch, 16);
        if (digit == -1) {
            throw new IllegalArgumentException("Illegal hexadecimal character " + ch + " at index " + index);
        }
        return digit;
    }

    private boolean toUpperCase = false;

    private Hex() {}

    public boolean getToUpperCase() {
        return toUpperCase;
    }

    public Hex setToUpperCase(boolean toUpperCase) {
        this.toUpperCase = toUpperCase;
        return this;
    }

    public char[] encode(byte[] data) {
        char[] toDigits = toUpperCase ? DIGITS_UPPER : DIGITS_LOWER;
        int len = data.length;
        char[] out = new char[len << 1];
        // two characters form the hex value.
        for (int i = 0, j = 0; i < len; i++) {
            out[j++] = toDigits[(0xF0 & data[i]) >>> 4];
            out[j++] = toDigits[0x0F & data[i]];
        }
        return out;
    }

    public String encodeToString(byte[] data) {
        return new String(encode(data));
    }

    public byte[] decode(char[] data) {
        int len = data.length;
        if ((len & HEX_01) != 0) {
            throw new IllegalArgumentException("Odd number of characters.");
        }
        byte[] out = new byte[len >> 1];
        // two characters form the hex value.
        for (int i = 0, j = 0; j < len; i++) {
            int f = toDigit(data[j], j) << 4;
            j++;
            f = f | toDigit(data[j], j);
            j++;
            out[i] = (byte) (f & 0xFF);
        }
        return out;
    }

    public byte[] decodeFromString(String data) {
        return decode(data.toCharArray());
    }

}
