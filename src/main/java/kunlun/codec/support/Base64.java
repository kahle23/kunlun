/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.codec.support;

import kunlun.codec.ByteCodec;
import kunlun.core.Codec;
import kunlun.util.Assert;
import kunlun.util.StringUtils;

import javax.xml.bind.DatatypeConverter;

import static kunlun.common.constant.Numbers.ZERO;
import static kunlun.common.constant.Symbols.*;

/**
 * The base64 encode and decode tools.
 * @author Kahle
 */
public class Base64 extends ByteCodec {
    private final Cfg config;

    public Base64(Cfg cfg) {

        this.config = cfg;
    }

    public Base64() {

        this(Cfg.of());
    }

    public Cfg getConfig() {

        return config;
    }

    @Override
    public byte[] encode(Codec.Config config, byte[] source) {
        String encode = encodeToString(config, source);
        return encode != null ? encode.getBytes() : null;
    }

    @Override
    public byte[] decode(Codec.Config config, byte[] source) {

        return decodeFromString(config, source != null ? new String(source) : null);
    }

    @Override
    public String encodeToString(Codec.Config config, byte[] source) {
        Assert.notNull(source, "Parameter \"source\" must not null. ");
        Cfg cfg = config != null ? (Cfg) config : getConfig();
        String encode = DatatypeConverter.printBase64Binary(source);
        if (cfg.isUrlSafe()) {
            encode = StringUtils.replace(encode, PLUS, MINUS);
            encode = StringUtils.replace(encode, SLASH, UNDERLINE);
        }
        else if (cfg.isMime()) {
            String lineSeparator = cfg.getLineSeparator();
            int lineLength = cfg.getLineLength();
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
        return encode;
    }

    @Override
    public byte[] decodeFromString(Codec.Config config, String source) {
        Assert.notNull(source, "Parameter \"source\" must not null. ");
        Cfg cfg = config != null ? (Cfg) config : getConfig();
        if (cfg.isUrlSafe()) {
            source = StringUtils.replace(source, MINUS, PLUS);
            source = StringUtils.replace(source, UNDERLINE, SLASH);
        }
        return DatatypeConverter.parseBase64Binary(source);
    }

    /**
     * The configuration of the base64.
     * @author Kahle
     */
    public static class Cfg implements Config {

        public static Cfg ofMime(Integer lineLength, String lineSeparator) {
            Cfg of = ofMime();
            if (StringUtils.isNotEmpty(lineSeparator)) {
                of.setLineSeparator(lineSeparator);
            }
            if (lineLength != null) {
                of.setLineLength(lineLength);
            }
            return of;
        }

        public static Cfg ofMime() {

            return of().setMime(true);
        }

        public static Cfg ofUrlSafe() {

            return of().setUrlSafe(true);
        }

        public static Cfg of() {

            return new Cfg();
        }

        /**
         * The line separator when encoded to MIME.
         * The default MIME line separator is "\r\n".
         */
        private String lineSeparator = "\r\n";
        /**
         * The line length when encoded to MIME.
         * The default MIME line length is 76.
         */
        private int lineLength = 76;
        /**
         * Encoded to support MIME.
         */
        private boolean mime = false;
        /**
         * Encoded as URL safe.
         */
        private boolean urlSafe = false;

        public String getLineSeparator() {

            return lineSeparator;
        }

        public Cfg setLineSeparator(String lineSeparator) {
            if (lineSeparator != null) {
                this.lineSeparator = lineSeparator;
            }
            return this;
        }

        public int getLineLength() {

            return lineLength;
        }

        public Cfg setLineLength(int lineLength) {
            if (lineLength > ZERO) {
                this.lineLength = lineLength;
            }
            return this;
        }

        public boolean isMime() {

            return mime;
        }

        public Cfg setMime(boolean mime) {
            this.mime = mime;
            return this;
        }

        public boolean isUrlSafe() {

            return urlSafe;
        }

        public Cfg setUrlSafe(boolean urlSafe) {
            this.urlSafe = urlSafe;
            return this;
        }
    }

}
