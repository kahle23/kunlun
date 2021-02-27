package artoria.codec;

import artoria.util.Assert;

import java.io.Serializable;

import static artoria.common.Constants.*;

/**
 * Hex encode and decode tools.
 * @author Kahle
 */
public class Hex implements BinaryEncoder, BinaryDecoder, Serializable {
    protected static final byte[] LOWER_CASE_DIGITS =
            {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    protected static final byte[] UPPER_CASE_DIGITS =
            {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
    protected static final int HEX_01 = 0x01;
    protected static final int RADIX = 16;
    private boolean lowerCase;

    public Hex() {

        this(true);
    }

    public Hex(boolean lowerCase) {

        this.lowerCase = lowerCase;
    }

    public boolean isLowerCase() {

        return lowerCase;
    }

    public void setLowerCase(boolean lowerCase) {

        this.lowerCase = lowerCase;
    }

    protected byte[] getDigits() {

        return lowerCase ? LOWER_CASE_DIGITS : UPPER_CASE_DIGITS;
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
            out[j++] = getDigits()[(0xF0 & source[i]) >>> FOUR];
            out[j++] = getDigits()[0x0F & source[i]];
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
