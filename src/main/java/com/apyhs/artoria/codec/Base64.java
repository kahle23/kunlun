package com.apyhs.artoria.codec;

import com.apyhs.artoria.constant.Const;
import com.apyhs.artoria.logging.Logger;
import com.apyhs.artoria.logging.LoggerFactory;
import com.apyhs.artoria.util.*;

import javax.xml.bind.DatatypeConverter;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Base64 encode and decode tools.
 * @author Kahle
 * @see org.apache.commons.codec.binary.Base64
 * @see java.util.Base64
 * @see DatatypeConverter#printBase64Binary
 * @see DatatypeConverter#parseBase64Binary
 */
public class Base64 {
    private static final String JAVAX_XML_DATATYPE_CONVERTER = "javax.xml.bind.DatatypeConverter";
    private static final String COMMONS_CODEC_BASE64 = "org.apache.commons.codec.binary.Base64";
    private static final String JAVA_UTIL_BASE64 = "java.util.Base64";
    private static final Pattern BASE64_URL_SAFE = Pattern.compile("^[a-zA-Z0-9-_]+={0,2}$");
    private static final Pattern BASE64_URL_UNSAFE = Pattern.compile("^[a-zA-Z0-9+/]+={0,2}$");
    private static final Base64Delegate DELEGATE;

    private static Logger log = LoggerFactory.getLogger(Base64.class);

    static {
        Base64Delegate delegateToUse = null;
        ClassLoader classLoader = Base64.class.getClassLoader();
        // Apache Commons Codec present on the classpath?
        if (ClassUtils.isPresent(COMMONS_CODEC_BASE64, classLoader)) {
            log.info("Use base64 class : " + COMMONS_CODEC_BASE64);
            delegateToUse = new CommonsCodecBase64Delegate();
        }
        // JDK 8's java.util.Base64 class present?
        else if (ClassUtils.isPresent(JAVA_UTIL_BASE64, classLoader)) {
            log.info("Use base64 class : " + JAVA_UTIL_BASE64);
            delegateToUse = new Java8Base64Delegate();
        }
        // maybe all jdk is ok?
        else if (ClassUtils.isPresent(JAVAX_XML_DATATYPE_CONVERTER, classLoader)) {
            log.info("Use base64 class : " + JAVAX_XML_DATATYPE_CONVERTER);
            delegateToUse = new Java7Base64Delegate();
        }
        DELEGATE = delegateToUse;
    }

    private static void assertDelegateAvailable() {
        Assert.notNull(DELEGATE, "Neither Java 8, Java 7, Java 6 " +
                "nor Apache Commons Codec found - Base64 encoding between byte arrays not supported");
    }

    public static byte[] encode(byte[] src) {
        Base64.assertDelegateAvailable();
        return DELEGATE.encode(src);
    }

    public static byte[] decode(byte[] src) {
        Base64.assertDelegateAvailable();
        return DELEGATE.decode(src);
    }

    public static byte[] encodeUrlSafe(byte[] src) {
        Base64.assertDelegateAvailable();
        return DELEGATE.encodeUrlSafe(src);
    }

    public static byte[] decodeUrlSafe(byte[] src) {
        Base64.assertDelegateAvailable();
        return DELEGATE.decodeUrlSafe(src);
    }

    public static boolean isUrlSafeString(String base64) {
        Assert.notNull(base64, "Base64 string must is not null. ");
        Matcher matcher = BASE64_URL_SAFE.matcher(base64);
        return matcher.matches();
    }

    public static boolean isUrlUnsafeString(String base64) {
        Assert.notNull(base64, "Base64 string must is not null. ");
        Matcher matcher = BASE64_URL_UNSAFE.matcher(base64);
        return matcher.matches();
    }

    public static String encodeToString(byte[] src) {
        byte[] encode = Base64.encode(src);
        Charset charset = Charset.forName(Const.DEFAULT_CHARSET_NAME);
        return new String(encode, charset);
    }

    public static String encodeToString(byte[] src, String charset) {
        Assert.notBlank(charset, "Charset must is not blank. ");
        Charset encoding = Charset.forName(charset);
        byte[] encode = Base64.encode(src);
        return new String(encode, encoding);
    }

    public static byte[] decodeFromString(String src) {
        Assert.notNull(src, "Source must is not null. ");
        Charset charset = Charset.forName(Const.DEFAULT_CHARSET_NAME);
        byte[] srcBytes = src.getBytes(charset);
        return Base64.decode(srcBytes);
    }

    public static byte[] decodeFromString(String src, String charset) {
        Assert.notNull(src, "Source must is not null. ");
        Assert.notBlank(charset, "Charset must is not blank. ");
        Charset encoding = Charset.forName(charset);
        byte[] srcBytes = src.getBytes(encoding);
        return Base64.decode(srcBytes);
    }

    public static String encodeToUrlSafeString(byte[] src) {
        byte[] urlSafe = Base64.encodeUrlSafe(src);
        Charset charset = Charset.forName(Const.DEFAULT_CHARSET_NAME);
        return new String(urlSafe, charset);
    }

    public static String encodeToUrlSafeString(byte[] src, String charset) {
        Assert.notBlank(charset, "Charset must is not blank. ");
        Charset encoding = Charset.forName(charset);
        byte[] urlSafe = Base64.encodeUrlSafe(src);
        return new String(urlSafe, encoding);
    }

    public static byte[] decodeFromUrlSafeString(String src) {
        Assert.notNull(src, "Source must is not null. ");
        Charset charset = Charset.forName(Const.DEFAULT_CHARSET_NAME);
        byte[] srcBytes = src.getBytes(charset);
        return Base64.decodeUrlSafe(srcBytes);
    }

    public static byte[] decodeFromUrlSafeString(String src, String charset) {
        Assert.notNull(src, "Source must is not null. ");
        Assert.notBlank(charset, "Charset must is not blank. ");
        Charset encoding = Charset.forName(charset);
        byte[] srcBytes = src.getBytes(encoding);
        return Base64.decodeUrlSafe(srcBytes);
    }

    interface Base64Delegate {

        /**
         * Base64 encode
         * @param src something will encode
         * @return something encoded
         */
        byte[] encode(byte[] src);

        /**
         * Base64 decode
         * @param src something will decode
         * @return something encoded
         */
        byte[] decode(byte[] src);

        /**
         * Base64 url safe encode
         * @param src something will url safe encode
         * @return something encoded
         */
        byte[] encodeUrlSafe(byte[] src);

        /**
         * Base64 url safe decode
         * @param src something will url safe decode
         * @return something encoded
         */
        byte[] decodeUrlSafe(byte[] src);

    }

    static class Java7Base64Delegate implements Base64Delegate {

        @Override
        public byte[] encode(byte[] src) {
            if (ArrayUtils.isEmpty(src)) { return src; }
            return DatatypeConverter.printBase64Binary(src).getBytes();
        }

        @Override
        public byte[] decode(byte[] src) {
            if (ArrayUtils.isEmpty(src)) { return src; }
            return DatatypeConverter.parseBase64Binary(new String(src));
        }

        @Override
        public byte[] encodeUrlSafe(byte[] src) {
            if (ArrayUtils.isEmpty(src)) { return src; }
            String s = DatatypeConverter.printBase64Binary(src);
            s = StringUtils.replace(s, Const.PLUS, Const.MINUS);
            s = StringUtils.replace(s, Const.SLASH, Const.UNDERLINE);
            return s.getBytes();
        }

        @Override
        public byte[] decodeUrlSafe(byte[] src) {
            if (ArrayUtils.isEmpty(src)) { return src; }
            String s = new String(src);
            s = StringUtils.replace(s, Const.MINUS, Const.PLUS);
            s = StringUtils.replace(s, Const.UNDERLINE, Const.SLASH);
            return DatatypeConverter.parseBase64Binary(s);
        }

    }

    static class Java8Base64Delegate implements Base64Delegate {

        @Override
        public byte[] encode(byte[] src) {
            if (ArrayUtils.isEmpty(src)) { return src; }
            return java.util.Base64.getEncoder().encode(src);
        }

        @Override
        public byte[] decode(byte[] src) {
            if (ArrayUtils.isEmpty(src)) { return src; }
            return java.util.Base64.getDecoder().decode(src);
        }

        @Override
        public byte[] encodeUrlSafe(byte[] src) {
            if (ArrayUtils.isEmpty(src)) { return src; }
            return java.util.Base64.getUrlEncoder().encode(src);
        }

        @Override
        public byte[] decodeUrlSafe(byte[] src) {
            if (ArrayUtils.isEmpty(src)) { return src; }
            return java.util.Base64.getUrlDecoder().decode(src);
        }

    }

    static class CommonsCodecBase64Delegate implements Base64Delegate {

        private final org.apache.commons.codec.binary.Base64 base64 =
                new org.apache.commons.codec.binary.Base64();

        private final org.apache.commons.codec.binary.Base64 base64UrlSafe =
                new org.apache.commons.codec.binary.Base64(0, null, true);

        @Override
        public byte[] encode(byte[] src) {
            return this.base64.encode(src);
        }

        @Override
        public byte[] decode(byte[] src) {
            return this.base64.decode(src);
        }

        @Override
        public byte[] encodeUrlSafe(byte[] src) {
            return this.base64UrlSafe.encode(src);
        }

        @Override
        public byte[] decodeUrlSafe(byte[] src) {
            return this.base64UrlSafe.decode(src);
        }

    }

}
