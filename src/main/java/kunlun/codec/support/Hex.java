/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.codec.support;

import kunlun.codec.ByteCodec;
import kunlun.util.Assert;

import static kunlun.common.constant.Numbers.*;

/**
 * The hex encode and decode tools.
 * @author Kahle
 */
public class Hex extends ByteCodec {
    protected static final byte[] LOWER_CASE_DIGITS =
            {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    protected static final byte[] UPPER_CASE_DIGITS =
            {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
    protected static final int HEX_01 = 0x01;
    protected static final int RADIX = 16;
    private final Cfg config;

    public Hex(Cfg config) {

        this.config = config;
    }

    public Hex() {

        this(Cfg.of());
    }

    public Cfg getConfig() {

        return config;
    }

    protected byte[] getDigits(boolean upperCase) {

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
    public byte[] encode(Config config, byte[] source) {
        Assert.notNull(source, "Parameter \"source\" must not null. ");
        Cfg cfg = config != null ? (Cfg) config : getConfig();
        int len = source.length;
        byte[] out = new byte[len << ONE];
        // Two characters form the hex value.
        for (int i = ZERO, j = ZERO; i < len; i++) {
            out[j++] = getDigits(cfg.isUpperCase())[(0xF0 & source[i]) >>> FOUR];
            out[j++] = getDigits(cfg.isUpperCase())[0x0F & source[i]];
        }
        return out;
    }

    @Override
    public byte[] decode(Config config, byte[] source) {
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

    /**
     * The configuration of the hex.
     * @author Kahle
     */
    public static class Cfg implements Config {

        public static Cfg of() {

            return new Cfg();
        }

        private boolean upperCase;

        public boolean isUpperCase() {

            return upperCase;
        }

        public Cfg setUpperCase(boolean upperCase) {
            this.upperCase = upperCase;
            return this;
        }
    }

}
