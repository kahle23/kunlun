/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.common.constant;

import java.nio.charset.Charset;

/**
 * The common charset constants.
 * @author Kahle
 */
public class Charsets {

    public static final String STR_DEFAULT_CHARSET = Charset.defaultCharset().name();
    public static final String STR_US_ASCII = "US-ASCII";
    public static final String STR_ISO_8859_1 = "ISO-8859-1";
    public static final String STR_GBK = "GBK";
    public static final String STR_GB2312 = "GB2312";
    public static final String STR_UTF_8 = "UTF-8";
    public static final String STR_UTF_16 = "UTF-16";

    public static final Charset DEFAULT_CHARSET = Charset.defaultCharset();
    public static final Charset US_ASCII = Charset.forName(STR_US_ASCII);
    public static final Charset ISO_8859_1 = Charset.forName(STR_ISO_8859_1);
    public static final Charset GBK = Charset.forName(STR_GBK);
    public static final Charset GB2312 = Charset.forName(STR_GB2312);
    public static final Charset UTF_8 = Charset.forName(STR_UTF_8);
    public static final Charset UTF_16 = Charset.forName(STR_UTF_16);

    private Charsets() {

        throw new UnsupportedOperationException("Don't allow instantiation. ");
    }

}
