package artoria.codec;

import artoria.util.ArrayUtils;
import artoria.util.Assert;
import artoria.util.ClassUtils;
import artoria.util.StringUtils;

import javax.xml.bind.DatatypeConverter;
import java.io.Serializable;
import java.nio.charset.Charset;

import static artoria.common.Constants.*;

/**
 * Base64 encode and decode tools.
 * @author Kahle
 */
public class Base64 implements BinaryEncoder, BinaryDecoder, Serializable {
    /**
     * The line separator when encoded to MIME.
     */
    private byte[] lineSeparator;
    /**
     * The line length when encoded to MIME.
     */
    private Integer lineLength;
    /**
     * Encoded as URL safe.
     */
    private boolean urlSafe = false;
    /**
     * Encoded to support MIME.
     */
    private boolean mime = false;

    private Base64Delegate delegate;

    private Base64Delegate getDelegate() {
        if (delegate != null) { return delegate; }
        synchronized (this) {
            if (delegate != null) { return delegate; }
            ClassLoader classLoader =
                    Base64.class.getClassLoader();
            // If have JDK 8's java.util.Base64, to use it.
            String java8Base64Class = "java.util.Base64";
            boolean isPresent = ClassUtils.isPresent(
                    java8Base64Class, classLoader
            );
            delegate = isPresent
                    ? new Java8Base64Delegate()
                    : new Java7Base64Delegate();
            return delegate;
        }
    }

    public Base64() {
    }

    public Base64(boolean isUrlSafe) {
        this.setMime(false);
        this.setUrlSafe(isUrlSafe);
    }

    public Base64(boolean isMime, Integer lineLength, byte[] lineSeparator) {
        this.setMime(isMime);
        this.setUrlSafe(false);
        this.setLineLength(lineLength);
        this.setLineSeparator(lineSeparator);
    }

    public boolean isUrlSafe() {

        return this.urlSafe;
    }

    public void setUrlSafe(boolean urlSafe) {

        this.urlSafe = urlSafe;
    }

    public boolean isMime() {

        return this.mime;
    }

    public void setMime(boolean mime) {

        this.mime = mime;
    }

    public byte[] getLineSeparator() {

        return this.lineSeparator;
    }

    public void setLineSeparator(byte[] lineSeparator) {

        this.lineSeparator = lineSeparator;
    }

    public Integer getLineLength() {

        return this.lineLength;
    }

    public void setLineLength(Integer lineLength) {

        this.lineLength = lineLength;
    }

    @Override
    public Object encode(Object source) throws EncodeException {

        return this.encode((byte[]) source);
    }

    @Override
    public Object decode(Object source) throws DecodeException {

        return this.decode((byte[]) source);
    }

    @Override
    public byte[] encode(byte[] source) throws EncodeException {

        return this.getDelegate().encode(source);
    }

    @Override
    public byte[] decode(byte[] source) throws DecodeException {

        return this.getDelegate().decode(source);
    }

    public String encodeToString(byte[] source) {
        byte[] encode = this.encode(source);
        Charset charset = Charset.forName(DEFAULT_CHARSET_NAME);
        return new String(encode, charset);
    }

    public String encodeToString(byte[] source, String charset) {
        Assert.notBlank(charset
                , "Parameter \"charset\" must not blank. ");
        Charset encoding = Charset.forName(charset);
        byte[] encode = this.encode(source);
        return new String(encode, encoding);
    }

    public byte[] decodeFromString(String source) {
        Assert.notNull(source
                , "Parameter \"source\" must not null. ");
        Charset charset = Charset.forName(DEFAULT_CHARSET_NAME);
        byte[] sourceBytes = source.getBytes(charset);
        return this.decode(sourceBytes);
    }

    public byte[] decodeFromString(String source, String charset) {
        Assert.notNull(source
                , "Parameter \"source\" must not null. ");
        Assert.notBlank(charset
                , "Parameter \"charset\" must not blank. ");
        Charset encoding = Charset.forName(charset);
        byte[] sourceBytes = source.getBytes(encoding);
        return this.decode(sourceBytes);
    }

    /**
     * Base64 delegate.
     */
    private interface Base64Delegate extends BinaryEncoder, BinaryDecoder {
    }

    /**
     * Java7 or earlier base64 delegate.
     * @see DatatypeConverter#printBase64Binary
     * @see DatatypeConverter#parseBase64Binary
     */
    private class Java7Base64Delegate implements Base64Delegate {
        private static final String DEFAULT_LINE_SEPARATOR = "\r\n";
        private static final int DEFAULT_LINE_LENGTH = 76;
        private String lineSeparator;
        private Integer lineLength;

        Java7Base64Delegate() {
            byte[] separatorBytes = Base64.this.getLineSeparator();
            this.lineSeparator =
                    ArrayUtils.isNotEmpty(separatorBytes)
                            ? new String(separatorBytes)
                            : DEFAULT_LINE_SEPARATOR;
            this.lineLength = Base64.this.getLineLength();
            this.lineLength = lineLength != null
                    ? lineLength : DEFAULT_LINE_LENGTH;
        }

        private String convertToMime(String rawData) {
            if (StringUtils.isBlank(rawData)) { return rawData; }
            StringBuilder builder = new StringBuilder();
            int beginIndex = 0, endIndex = lineLength;
            int dataLength = rawData.length();
            while (beginIndex < dataLength) {
                if (endIndex > dataLength) { endIndex = dataLength; }
                String subData = rawData.substring(beginIndex, endIndex);
                builder.append(subData).append(lineSeparator);
                beginIndex = endIndex; endIndex += lineLength;
            }
            int builderEnd = builder.length();
            builderEnd -= lineSeparator.length();
            return builder.substring(0, builderEnd);
        }

        @Override
        public Object encode(Object source) throws EncodeException {

            return this.encode((byte[]) source);
        }

        @Override
        public Object decode(Object source) throws DecodeException {

            return this.decode((byte[]) source);
        }

        @Override
        public byte[] encode(byte[] source) throws EncodeException {
            if (ArrayUtils.isEmpty(source)) { return source; }
            String encode = DatatypeConverter.printBase64Binary(source);
            if (urlSafe) {
                encode = StringUtils.replace(encode, PLUS, MINUS);
                encode = StringUtils.replace(encode, SLASH, UNDERLINE);
            }
            else if (mime) {
                encode = this.convertToMime(encode);
            }
            return encode.getBytes();
        }

        @Override
        public byte[] decode(byte[] source) throws DecodeException {
            if (ArrayUtils.isEmpty(source)) { return source; }
            String decode = new String(source);
            if (urlSafe) {
                decode = StringUtils.replace(decode, MINUS, PLUS);
                decode = StringUtils.replace(decode, UNDERLINE, SLASH);
            }
            return DatatypeConverter.parseBase64Binary(decode);
        }

    }

    /**
     * Java8 base64 delegate.
     * @see java.util.Base64
     */
    private class Java8Base64Delegate implements Base64Delegate {
        private java.util.Base64.Encoder encoder = java.util.Base64.getEncoder();
        private java.util.Base64.Decoder decoder = java.util.Base64.getDecoder();

        Java8Base64Delegate() {
            if (urlSafe) {
                encoder = java.util.Base64.getUrlEncoder();
                decoder = java.util.Base64.getUrlDecoder();
            }
            else if (mime) {
                encoder = lineLength != null
                        && ArrayUtils.isNotEmpty(lineSeparator)
                        ? java.util.Base64.getMimeEncoder(
                                lineLength, lineSeparator
                            )
                        : java.util.Base64.getMimeEncoder();
                decoder = java.util.Base64.getMimeDecoder();
            }
        }

        @Override
        public Object encode(Object source) throws EncodeException {

            return this.encode((byte[]) source);
        }

        @Override
        public Object decode(Object source) throws DecodeException {

            return this.decode((byte[]) source);
        }

        @Override
        public byte[] encode(byte[] source) throws EncodeException {
            if (ArrayUtils.isEmpty(source)) { return source; }
            return encoder.encode(source);
        }

        @Override
        public byte[] decode(byte[] source) throws DecodeException {
            if (ArrayUtils.isEmpty(source)) { return source; }
            return decoder.decode(source);
        }

    }

}
