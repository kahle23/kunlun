/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.codec.support;

import kunlun.codec.CharCodec;
import kunlun.util.Assert;

import static kunlun.common.constant.Numbers.*;
import static kunlun.io.util.IOUtils.EOF;

/**
 * The unicode encode and decode tools.
 * @author Kahle
 */
public class Unicode extends CharCodec {
    private static final String BACKLASH_U = "\\u";
    private static final int UNICODE_LENGTH = 6;
    private static final int RADIX = 16;

    @Override
    public String encode(Config config, String source) {
        Assert.notBlank(source, "Parameter \"source\" must not blank. ");
        StringBuilder unicode = new StringBuilder();
        char[] chars = source.toCharArray();
        for (char c : chars) {
            String hexString = Integer.toHexString(c);
            int len = hexString.length();
            if (len != FOUR) {
                hexString = (len == TWO ? "00" : "0") + hexString;
            }
            unicode.append(BACKLASH_U).append(hexString);
        }
        return unicode.toString();
    }

    @Override
    public String decode(Config config, String source) {
        Assert.notBlank(source, "Parameter \"source\" must not blank. ");
        int index, pos = ZERO;
        StringBuilder result = new StringBuilder();
        while ((index = source.indexOf(BACKLASH_U, pos)) != EOF) {
            result.append(source, pos, index);
            if (index + FIVE < source.length()) {
                pos = index + UNICODE_LENGTH;
                String hex = source.substring(index + TWO, pos);
                char ch = (char) Integer.parseInt(hex, RADIX);
                result.append(ch);
            }
        }
        if (source.length() > pos + ONE) {
            result.append(source, pos, source.length());
        }
        return result.toString();
    }

}
