package artoria.codec;

import artoria.util.Assert;

import java.io.Serializable;

import static artoria.common.Constants.*;

/**
 * Hex encode and decode tools.
 * @author Kahle
 */
public class Hex implements BinaryEncoder, BinaryDecoder, Serializable {
    private static final byte[] LOWER_CASE_DIGITS =
            {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    private static final byte[] UPPER_CASE_DIGITS =
            {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
    private static final int HEX_01 = 0x01;
    private static final int RADIX = 16;
    private byte[] digits;

    public Hex() {

        this(false);
    }

    public Hex(boolean toUpperCase) {
        this(toUpperCase
                ? UPPER_CASE_DIGITS
                : LOWER_CASE_DIGITS
        );
    }

    protected Hex(byte[] digits) {
        Assert.notEmpty(digits
                , "Parameter \"digits\" must not empty. ");
        Assert.state(digits.length == RADIX
                , "Parameter \"digits\" length must equal 16. ");
        this.digits = digits;
    }

    protected int toDigit(int ch, int index) {
        int digit = Character.digit(ch, RADIX);
        if (digit == -1) {
            throw new IllegalArgumentException(
                    "Illegal hexadecimal character " + ch + " at index " + index
            );
        }
        return digit;
    }

    @Override
    public Object encode(Object source) throws EncodeException {

        return encode((byte[]) source);
    }

    @Override
    public Object decode(Object source) throws DecodeException {

        return decode((byte[]) source);
    }

    @Override
    public byte[] encode(byte[] source) throws EncodeException {
        Assert.notNull(source, "Parameter \"source\" must not null. ");
        int len = source.length;
        byte[] out = new byte[len << ONE];
        // Two characters form the hex value.
        for (int i = ZERO, j = ZERO; i < len; i++) {
            out[j++] = digits[(0xF0 & source[i]) >>> FOUR];
            out[j++] = digits[0x0F & source[i]];
        }
        return out;
    }

    @Override
    public byte[] decode(byte[] source) throws DecodeException {
        Assert.notNull(source, "Parameter \"source\" must not null. ");
        int len = source.length;
        if ((len & HEX_01) != ZERO) {
            throw new IllegalArgumentException("Odd number of characters.");
        }
        byte[] out = new byte[len >> ONE];
        // Two characters form the hex value.
        for (int i = ZERO, j = ZERO; j < len; i++) {
            int f = toDigit(source[j], j) << FOUR;
            j++;
            f = f | toDigit(source[j], j);
            j++;
            out[i] = (byte) (f & 0xFF);
        }
        return out;
    }

    public String encodeToString(byte[] source) {
        byte[] encode = encode(source);
        return new String(encode);
    }

    public byte[] decodeFromString(String source) {
        Assert.notNull(source, "Parameter \"source\" must not null. ");
        byte[] sourceBytes = source.getBytes();
        return decode(sourceBytes);
    }

}
