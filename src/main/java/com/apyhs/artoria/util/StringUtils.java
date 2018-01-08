package com.apyhs.artoria.util;

import static com.apyhs.artoria.util.Const.*;

/**
 * String tools.
 * @author Kahle
 */
public class StringUtils {

    public static boolean equals(String str1, String str2) {
        return ObjectUtils.equals(str1, str2);
    }

    public static boolean isEmpty(Object obj) {
        return obj == null || EMPTY_STRING.equals(obj);
    }

    public static boolean isNotEmpty(Object obj) {
        return obj != null && !EMPTY_STRING.equals(obj);
    }

    public static boolean isBlank(CharSequence str) {
        return !StringUtils.isNotBlank(str);
    }

    public static boolean isNotBlank(CharSequence str) {
        if (StringUtils.isEmpty(str)) { return false; }
        int strLen = str.length();
        for (int i = 0; i < strLen; i++) {
            int codePoint = (int) str.charAt(i);
            boolean isWs = Character.isWhitespace(codePoint);
            if (!isWs) { return true; }
        }
        return false;
    }

    public static boolean isNumeric(String str) {
        boolean b = str.startsWith(MINUS);
        b = b || str.startsWith(PLUS);
        if (b) { str = str.substring(1); }
        for (int i = str.length() - 1; i >= 0; i--) {
            char c = str.charAt(i);
            boolean isDg = Character.isDigit(c);
            if (!isDg) { return false; }
        }
        return true;
    }

    public static boolean containsWhitespace(CharSequence str) {
        if (StringUtils.isEmpty(str)) { return false; }
        int strLen = str.length();
        for (int i = 0; i < strLen; i++) {
            int codePoint = (int) str.charAt(i);
            boolean isWs = Character.isWhitespace(codePoint);
            if (isWs) { return true; }
        }
        return false;
    }

    public static String trimWhitespace(String str) {
        if (StringUtils.isEmpty(str)) { return str; }
        StringBuilder sb = new StringBuilder(str);
        boolean isLoop;
        do {
            int codePoint = (int) sb.charAt(0);
            boolean isWs = Character.isWhitespace(codePoint);
            isLoop = sb.length() > 0 && isWs;
            if (isLoop) { sb.deleteCharAt(0); }
        } while (isLoop);
        do {
            int codePoint = (int) sb.charAt(sb.length() - 1);
            boolean isWs = Character.isWhitespace(codePoint);
            isLoop = sb.length() > 0 && isWs;
            if (isLoop) { sb.deleteCharAt(sb.length() - 1); }
        } while (isLoop);
        return sb.toString();
    }

    public static String trimAllWhitespace(String str) {
        if (StringUtils.isEmpty(str)) { return str; }
        int len = str.length();
        StringBuilder sb = new StringBuilder(str.length());
        for (int i = 0; i < len; i++) {
            char c = str.charAt(i);
            boolean isWs = Character.isWhitespace((int) c);
            if (!isWs) { sb.append(c); }
        }
        return sb.toString();
    }

    public static String replace(String inString, String oldPattern, String newPattern) {
        if (StringUtils.isEmpty(inString)
                || StringUtils.isEmpty(oldPattern)
                || newPattern == null) {
            return inString;
        }
        StringBuilder sb = new StringBuilder();
        // our position in the old string
        int pos = 0;
        int index = inString.indexOf(oldPattern);
        // the index of an occurrence we've found, or -1
        int patLen = oldPattern.length();
        while (index >= 0) {
            sb.append(inString.substring(pos, index));
            sb.append(newPattern);
            pos = index + patLen;
            index = inString.indexOf(oldPattern, pos);
        }
        sb.append(inString.substring(pos));
        // remember to append any characters to the right of a match
        return sb.toString();
    }

    public static String delete(String inString, String pattern) {
        return StringUtils.replace(inString, pattern, EMPTY_STRING);
    }

    public static String capitalize(String str) {
        return StringUtils.changeFirstCharacterCase(str, true);
    }

    public static String uncapitalize(String str) {
        return StringUtils.changeFirstCharacterCase(str, false);
    }

    private static String changeFirstCharacterCase(String str, boolean capitalize) {
        if (str == null || str.length() == 0) { return str; }
        StringBuilder sb = new StringBuilder(str.length());
        char c = str.charAt(0);
        sb.append(capitalize ?
                Character.toUpperCase(c) :
                Character.toLowerCase(c));
        sb.append(str.substring(1));
        return sb.toString();
    }

}
