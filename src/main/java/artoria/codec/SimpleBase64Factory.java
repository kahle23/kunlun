package artoria.codec;

import artoria.util.ArrayUtils;
import artoria.util.ClassUtils;

import static artoria.common.Constants.MINUS_ONE;
import static artoria.common.Constants.ZERO;
import static java.lang.Boolean.TRUE;

public class SimpleBase64Factory implements Base64Factory {
    protected static final String JAVA_BASE64_CLASS = "java.util.Base64";
    protected static ClassLoader classLoader = Base64.class.getClassLoader();
    protected Base64Factory delegate;

    protected Base64Factory getDelegate() {
        if (delegate != null) { return delegate; }
        synchronized (this) {
            if (delegate != null) { return delegate; }
            // If have "java.util.Base64", to use it.
            boolean present = ClassUtils.isPresent(JAVA_BASE64_CLASS, classLoader);
            return (delegate = present ? javaBase64Delegate() : base64Delegate());
        }
    }

    protected Base64Factory base64Delegate() {
        return new Base64Factory() {
            private final Base64 mimeBase64 = new Base64(TRUE, MINUS_ONE, null);
            private final Base64 urlBase64 = new Base64(TRUE);
            private final Base64 base64 = new Base64();
            @Override
            public Base64 getInstance() {
                return base64;
            }
            @Override
            public Base64 getInstance(boolean urlSafe) {
                // Nothing.
                return urlSafe ? urlBase64 : base64;
            }
            @Override
            public Base64 getInstance(boolean mime, int lineLength, byte[] lineSeparator) {
                if (!mime) { return base64; }
                if (lineLength == MINUS_ONE && lineSeparator == null) { return mimeBase64; }
                return new Base64(TRUE, lineLength, lineSeparator);
            }
        };
    }

    protected Base64Factory javaBase64Delegate() {
        return new Base64Factory() {
            private final Base64 mimeBase64 = new JavaBase64(TRUE, MINUS_ONE, null);
            private final Base64 urlBase64 = new JavaBase64(TRUE);
            private final Base64 base64 = new JavaBase64();
            @Override
            public Base64 getInstance() {
                return base64;
            }
            @Override
            public Base64 getInstance(boolean urlSafe) {
                // Nothing.
                return urlSafe ? urlBase64 : base64;
            }
            @Override
            public Base64 getInstance(boolean mime, int lineLength, byte[] lineSeparator) {
                if (!mime) { return base64; }
                if (lineLength == MINUS_ONE && lineSeparator == null) { return mimeBase64; }
                return new JavaBase64(TRUE, lineLength, lineSeparator);
            }
        };
    }

    @Override
    public Base64 getInstance() {

        return getDelegate().getInstance();
    }

    @Override
    public Base64 getInstance(boolean urlSafe) {

        return getDelegate().getInstance(urlSafe);
    }

    @Override
    public Base64 getInstance(boolean mime, int lineLength, byte[] lineSeparator) {

        return getDelegate().getInstance(mime, lineLength, lineSeparator);
    }

    protected static class JavaBase64 extends Base64 {
        private java.util.Base64.Encoder encoder;
        private java.util.Base64.Decoder decoder;

        protected JavaBase64() {

            this(false, false, MINUS_ONE, null);
        }

        protected JavaBase64(boolean urlSafe) {

            this(urlSafe, false, MINUS_ONE, null);
        }

        protected JavaBase64(boolean mime, int lineLength, byte[] lineSeparator) {

            this(false, mime, lineLength, lineSeparator);
        }

        protected JavaBase64(boolean urlSafe, boolean mime, int lineLength, byte[] lineSeparator) {
            super(urlSafe, mime, lineLength, lineSeparator);
            if (urlSafe) {
                encoder = java.util.Base64.getUrlEncoder();
                decoder = java.util.Base64.getUrlDecoder();
            }
            else if (mime) {
                encoder = lineLength > ZERO && ArrayUtils.isNotEmpty(lineSeparator)
                        ? java.util.Base64.getMimeEncoder(lineLength, lineSeparator)
                        : java.util.Base64.getMimeEncoder();
                decoder = java.util.Base64.getMimeDecoder();
            }
            else {
                encoder = java.util.Base64.getEncoder();
                decoder = java.util.Base64.getDecoder();
            }
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
