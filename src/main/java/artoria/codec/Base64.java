package artoria.codec;

import artoria.util.ArrayUtils;
import artoria.util.StringUtils;

import javax.xml.bind.DatatypeConverter;
import java.io.Serializable;

import static artoria.common.Constants.*;

/**
 * Base64 encode and decode tools.
 * @author Kahle
 */
public class Base64 implements BinaryEncoder, BinaryDecoder, Serializable {
    /**
     * Default MIME line separator.
     */
    protected static final byte[] DEFAULT_MIME_LINE_SEPARATOR = new byte[] {'\r', '\n'};
    /**
     * Default MIME line length.
     */
    protected static final int DEFAULT_MIME_LINE_LENGTH = 76;
    /**
     * The line separator when encoded to MIME.
     */
    private final byte[] lineSeparator;
    /**
     * The line length when encoded to MIME.
     */
    private final int lineLength;
    /**
     * Encoded to support MIME.
     */
    private final boolean mime;
    /**
     * Encoded as URL safe.
     */
    private final boolean urlSafe;

    public Base64() {

        this(false, false, MINUS_ONE, null);
    }

    public Base64(boolean urlSafe) {

        this(urlSafe, false, MINUS_ONE, null);
    }

    public Base64(boolean mime, int lineLength, byte[] lineSeparator) {

        this(false, mime, lineLength, lineSeparator);
    }

    protected Base64(boolean urlSafe, boolean mime, int lineLength, byte[] lineSeparator) {
        this.lineSeparator = lineSeparator;
        this.lineLength = lineLength;
        this.mime = mime;
        this.urlSafe = urlSafe;
    }

    public boolean isUrlSafe() {

        return urlSafe;
    }

    public boolean isMime() {

        return mime;
    }

    public int getLineLength() {

        return lineLength;
    }

    public byte[] getLineSeparator() {

        return lineSeparator;
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
        if (ArrayUtils.isEmpty(source)) { return source; }
        String encode = DatatypeConverter.printBase64Binary(source);
        if (isUrlSafe()) {
            encode = StringUtils.replace(encode, PLUS, MINUS);
            encode = StringUtils.replace(encode, SLASH, UNDERLINE);
        }
        else if (isMime()) {
            byte[] lineSeparatorBytes = getLineSeparator();
            String lineSeparator = new String(
                    ArrayUtils.isNotEmpty(lineSeparatorBytes)
                            ? lineSeparatorBytes : DEFAULT_MIME_LINE_SEPARATOR
            );
            int lineLength = getLineLength();
            lineLength = lineLength > ZERO ? lineLength : DEFAULT_MIME_LINE_LENGTH;
            StringBuilder builder = new StringBuilder();
            int beginIndex = ZERO, endIndex = lineLength;
            int dataLength = encode.length();
            while (beginIndex < dataLength) {
                if (endIndex > dataLength) { endIndex = dataLength; }
                String subData = encode.substring(beginIndex, endIndex);
                builder.append(subData).append(lineSeparator);
                beginIndex = endIndex;
                endIndex += lineLength;
            }
            int builderEnd = builder.length();
            builderEnd -= lineSeparator.length();
            encode = builder.substring(ZERO, builderEnd);
        }
        return encode.getBytes();
    }

    @Override
    public byte[] decode(byte[] source) throws DecodeException {
        if (ArrayUtils.isEmpty(source)) { return source; }
        String decode = new String(source);
        if (isUrlSafe()) {
            decode = StringUtils.replace(decode, MINUS, PLUS);
            decode = StringUtils.replace(decode, UNDERLINE, SLASH);
        }
        return DatatypeConverter.parseBase64Binary(decode);
    }

    public String encodeToString(byte[] source) {

        return new String(encode(source));
    }

    public byte[] decodeFromString(String source) {

        return decode(source != null ? source.getBytes() : new byte[ZERO]);
    }

}
