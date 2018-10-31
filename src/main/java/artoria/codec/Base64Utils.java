package artoria.codec;

import artoria.util.ArrayUtils;
import artoria.util.Assert;
import artoria.util.ClassUtils;
import artoria.util.StringUtils;

import javax.xml.bind.DatatypeConverter;
import java.nio.charset.Charset;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static artoria.common.Constants.*;

/**
 * Base64 encode and decode tools.
 * @author Kahle
 * @see java.util.Base64
 * @see DatatypeConverter#printBase64Binary
 * @see DatatypeConverter#parseBase64Binary
 */
public class Base64Utils {
    private static final String JAVAX_XML_DATATYPE_CONVERTER = "javax.xml.bind.DatatypeConverter";
    private static final String JAVA_UTIL_BASE64 = "java.util.Base64";
    private static final Pattern BASE64_URL_SAFE = Pattern.compile("^[a-zA-Z0-9-_]+={0,2}$");
    private static final Pattern BASE64_URL_UNSAFE = Pattern.compile("^[a-zA-Z0-9+/]+={0,2}$");
    private static Logger log = Logger.getLogger(Base64Utils.class.getName());
    private static Base64Delegate delegate;

    public static Base64Delegate getDelegate() {
        if (delegate != null) {
            return delegate;
        }
        synchronized (Base64Delegate.class) {
            if (delegate != null) {
                return delegate;
            }
            ClassLoader classLoader = Base64Utils.class.getClassLoader();
            // If have JDK 8's java.util.Base64, to use it.
            if (ClassUtils.isPresent(JAVA_UTIL_BASE64, classLoader)) {
                setDelegate(new Java8Base64Delegate());
            }
            // Maybe all jdk is ok.
            else if (ClassUtils.isPresent(JAVAX_XML_DATATYPE_CONVERTER, classLoader)) {
                setDelegate(new Java7Base64Delegate());
            }
            return delegate;
        }
    }

    public static void setDelegate(Base64Delegate delegate) {
        Assert.notNull(delegate, "Parameter \"delegate\" must not null. ");
        synchronized (Base64Delegate.class) {
            log.info("Set base64 delegate: " + delegate.getClass().getName());
            Base64Utils.delegate = delegate;
        }
    }

    public static byte[] encode(byte[] source) {

        return getDelegate().encode(source);
    }

    public static byte[] decode(byte[] source) {

        return getDelegate().decode(source);
    }

    public static byte[] encodeUrlSafe(byte[] source) {

        return getDelegate().encodeUrlSafe(source);
    }

    public static byte[] decodeUrlSafe(byte[] source) {

        return getDelegate().decodeUrlSafe(source);
    }

    public static boolean isUrlSafeString(String base64) {
        Assert.notNull(base64, "Parameter \"base64\" must not null. ");
        Matcher matcher = BASE64_URL_SAFE.matcher(base64);
        return matcher.matches();
    }

    public static boolean isUrlUnsafeString(String base64) {
        Assert.notNull(base64, "Parameter \"base64\" must not null. ");
        Matcher matcher = BASE64_URL_UNSAFE.matcher(base64);
        return matcher.matches();
    }

    public static String encodeToString(byte[] source) {
        byte[] encode = Base64Utils.encode(source);
        Charset charset = Charset.forName(DEFAULT_CHARSET_NAME);
        return new String(encode, charset);
    }

    public static String encodeToString(byte[] source, String charset) {
        Assert.notBlank(charset, "Parameter \"charset\" must not blank. ");
        Charset encoding = Charset.forName(charset);
        byte[] encode = Base64Utils.encode(source);
        return new String(encode, encoding);
    }

    public static byte[] decodeFromString(String source) {
        Assert.notNull(source, "Parameter \"source\" must not null. ");
        Charset charset = Charset.forName(DEFAULT_CHARSET_NAME);
        byte[] sourceBytes = source.getBytes(charset);
        return Base64Utils.decode(sourceBytes);
    }

    public static byte[] decodeFromString(String source, String charset) {
        Assert.notNull(source, "Parameter \"source\" must not null. ");
        Assert.notBlank(charset, "Parameter \"charset\" must not blank. ");
        Charset encoding = Charset.forName(charset);
        byte[] sourceBytes = source.getBytes(encoding);
        return Base64Utils.decode(sourceBytes);
    }

    public static String encodeToUrlSafeString(byte[] source) {
        byte[] urlSafe = Base64Utils.encodeUrlSafe(source);
        Charset charset = Charset.forName(DEFAULT_CHARSET_NAME);
        return new String(urlSafe, charset);
    }

    public static String encodeToUrlSafeString(byte[] source, String charset) {
        Assert.notBlank(charset, "Parameter \"charset\" must not blank. ");
        Charset encoding = Charset.forName(charset);
        byte[] urlSafe = Base64Utils.encodeUrlSafe(source);
        return new String(urlSafe, encoding);
    }

    public static byte[] decodeFromUrlSafeString(String source) {
        Assert.notNull(source, "Parameter \"source\" must not null. ");
        Charset charset = Charset.forName(DEFAULT_CHARSET_NAME);
        byte[] sourceBytes = source.getBytes(charset);
        return Base64Utils.decodeUrlSafe(sourceBytes);
    }

    public static byte[] decodeFromUrlSafeString(String source, String charset) {
        Assert.notNull(source, "Parameter \"source\" must not null. ");
        Assert.notBlank(charset, "Parameter \"charset\" must not blank. ");
        Charset encoding = Charset.forName(charset);
        byte[] sourceBytes = source.getBytes(encoding);
        return Base64Utils.decodeUrlSafe(sourceBytes);
    }

    public interface Base64Delegate extends Encoder<byte[]>, Decoder<byte[]> {

        /**
         * Base64 url safe encode
         * @param source something will url safe encode
         * @return something encoded
         */
        byte[] encodeUrlSafe(byte[] source);

        /**
         * Base64 url safe decode
         * @param source something will url safe decode
         * @return something encoded
         */
        byte[] decodeUrlSafe(byte[] source);

    }

    public static class Java7Base64Delegate implements Base64Delegate {

        @Override
        public byte[] encode(byte[] source) throws EncodeException {
            if (ArrayUtils.isEmpty(source)) { return source; }
            return DatatypeConverter.printBase64Binary(source).getBytes();
        }

        @Override
        public byte[] decode(byte[] source) throws DecodeException {
            if (ArrayUtils.isEmpty(source)) { return source; }
            return DatatypeConverter.parseBase64Binary(new String(source));
        }

        @Override
        public byte[] encodeUrlSafe(byte[] source) {
            if (ArrayUtils.isEmpty(source)) { return source; }
            String encode = DatatypeConverter.printBase64Binary(source);
            encode = StringUtils.replace(encode, PLUS, MINUS);
            encode = StringUtils.replace(encode, SLASH, UNDERLINE);
            return encode.getBytes();
        }

        @Override
        public byte[] decodeUrlSafe(byte[] source) {
            if (ArrayUtils.isEmpty(source)) { return source; }
            String decode = new String(source);
            decode = StringUtils.replace(decode, MINUS, PLUS);
            decode = StringUtils.replace(decode, UNDERLINE, SLASH);
            return DatatypeConverter.parseBase64Binary(decode);
        }

    }

    public static class Java8Base64Delegate implements Base64Delegate {

        @Override
        public byte[] encode(byte[] source) throws EncodeException {
            if (ArrayUtils.isEmpty(source)) { return source; }
            return java.util.Base64.getEncoder().encode(source);
        }

        @Override
        public byte[] decode(byte[] source) throws DecodeException {
            if (ArrayUtils.isEmpty(source)) { return source; }
            return java.util.Base64.getDecoder().decode(source);
        }

        @Override
        public byte[] encodeUrlSafe(byte[] source) {
            if (ArrayUtils.isEmpty(source)) { return source; }
            return java.util.Base64.getUrlEncoder().encode(source);
        }

        @Override
        public byte[] decodeUrlSafe(byte[] source) {
            if (ArrayUtils.isEmpty(source)) { return source; }
            return java.util.Base64.getUrlDecoder().decode(source);
        }

    }

}
