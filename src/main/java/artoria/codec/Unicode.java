package artoria.codec;

import artoria.util.Assert;

import java.io.Serializable;

import static artoria.io.IOUtils.EOF;

/**
 * Unicode encode and decode tools.
 * @author Kahle
 */
public class Unicode implements StringEncoder, StringDecoder, Serializable {
    private static final String BACKLASH_U = "\\u";
    private static final int UNICODE_LENGTH = 6;
    private static final int RADIX = 16;

    @Override
    public Object encode(Object source) throws EncodeException {

        return this.encode((String) source);
    }

    @Override
    public Object decode(Object source) throws DecodeException {

        return this.decode((String) source);
    }

    @Override
    public String encode(String source) throws EncodeException {
        Assert.notBlank(source, "Parameter \"source\" must not blank. ");
        StringBuilder unicode = new StringBuilder();
        char[] chars = source.toCharArray();
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

    @Override
    public String decode(String source) throws DecodeException {
        Assert.notBlank(source, "Parameter \"source\" must not blank. ");
        int index, pos = 0;
        StringBuilder result = new StringBuilder();
        while ((index = source.indexOf(BACKLASH_U, pos)) != EOF) {
            result.append(source.substring(pos, index));
            if (index + 5 < source.length()) {
                pos = index + UNICODE_LENGTH;
                String hex = source.substring(index + 2, pos);
                char ch = (char) Integer.parseInt(hex, RADIX);
                result.append(ch);
            }
        }
        if (source.length() > pos + 1) {
            String tmp = source.substring(pos, source.length());
            result.append(tmp);
        }
        return result.toString();
    }

}
