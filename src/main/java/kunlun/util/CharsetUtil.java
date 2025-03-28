/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.util;

import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;

/**
 * The charset tools.
 * @author Kahle
 */
public class CharsetUtil {

    // region ======== default charset ========

    public static String defaultCharsetName() {

        return defaultCharset().name();
    }

    public static Charset defaultCharset() {

        return Charset.defaultCharset();
    }

    // endregion ======== default charset ========


    // region ======== parse ========

    public static Charset parse(String charsetName) {

        return parse(charsetName, Charset.defaultCharset());
    }

    public static Charset parse(String charsetName, Charset defaultCharset) {
        if (StrUtil.isBlank(charsetName)) { return defaultCharset; }
        Charset result;
        try {
            result = Charset.forName(charsetName);
        } catch (UnsupportedCharsetException e) {
            result = defaultCharset;
        }
        return result;
    }

    // endregion ======== parse ========


    // region ======== convert ========

//    public static File convert(File file, Charset oldCharset, Charset newCharset) {
//        String str = FileUtil.readString(file, oldCharset);
//        return FileUtil.writeString(str, file, newCharset);
//    }

    public static String convert(String source, String oldCharset, String newCharset) {

        return convert(source, Charset.forName(oldCharset), Charset.forName(newCharset));
    }

    public static String convert(String source, Charset oldCharset, Charset newCharset) {
        if (oldCharset == null || newCharset == null) { return source; }
        if (StrUtil.isBlank(source) || oldCharset.equals(newCharset)) {
            return source;
        }
        return new String(source.getBytes(oldCharset), newCharset);
    }

    // endregion ======== convert ========

}
