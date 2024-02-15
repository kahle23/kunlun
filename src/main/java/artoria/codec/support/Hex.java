package artoria.codec.support;

import artoria.core.Decoder;
import artoria.core.Encoder;
import artoria.util.Assert;

import java.io.Serializable;

import static artoria.common.constant.Numbers.*;

/**
 * The hex encode and decode tools.
 * @author Kahle
 */
public class Hex implements Encoder<byte[]>, Decoder<byte[]>, Serializable {
    protected static final byte[] LOWER_CASE_DIGITS =
            {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    protected static final byte[] UPPER_CASE_DIGITS =
            {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
    protected static final int HEX_01 = 0x01;
    protected static final int RADIX = 16;
    protected final boolean upperCase;

    public Hex() {

        this(false);
    }

    public Hex(boolean upperCase) {

        this.upperCase = upperCase;
    }

    protected byte[] getDigits() {

        return upperCase ? UPPER_CASE_DIGITS : LOWER_CASE_DIGITS;
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
    public byte[] encode(byte[] source) {
        Assert.notNull(source, "Parameter \"source\" must not null. ");
        int len = source.length;
        byte[] out = new byte[len << ONE];
        // Two characters form the hex value.
        for (int i = ZERO, j = ZERO; i < len; i++) {
            out[j++] = getDigits()[(0xF0 & source[i]) >>> FOUR];
            out[j++] = getDigits()[0x0F & source[i]];
        }
        return out;
    }

    @Override
    public byte[] decode(byte[] source) {
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
