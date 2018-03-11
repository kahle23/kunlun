package com.github.kahlkn.artoria.util;

import static com.github.kahlkn.artoria.util.Const.*;

/**
 * String tools.
 * @author Kahle
 */
public class StringUtils {
    private static final char UNDERLINE = '_';

    public static boolean equals(String str1, String str2) {
        return ObjectUtils.equals(str1, str2);
    }

    public static boolean isEmpty(Object obj) {
        return obj == null || EMPTY_STRING.equals(obj);
    }

    public static boolean isNotEmpty(Object obj) {
        return obj != null && !EMPTY_STRING.equals(obj);
    }

    public static boolean isBlank(CharSequence cs) {
        return !StringUtils.isNotBlank(cs);
    }

    public static boolean isNotBlank(CharSequence cs) {
        if (StringUtils.isEmpty(cs)) { return false; }
        int strLen = cs.length();
        for (int i = 0; i < strLen; i++) {
            int codePoint = (int) cs.charAt(i);
            boolean isWs = Character.isWhitespace(codePoint);
            if (!isWs) { return true; }
        }
        return false;
    }

    public static boolean isNumeric(String str) {
        if (StringUtils.isBlank(str)) {
            return false;
        }
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

    public static boolean containsWhitespace(String str) {
        if (StringUtils.isEmpty(str)) {
            return false;
        }
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

    public static String camelToUnderline(String str) {
        if (str == null) { return null; }
        int length = str.length();
        StringBuilder builder = new StringBuilder();
        char ch;
        int lowerCaseCount = 0;
        boolean lastIsUpper = false, lastIsUnderline = false;
        for (int i = 0; i < length; i++) {
            ch = str.charAt(i);
            if (UNDERLINE == ch) {
                lastIsUnderline = true;
                builder.append(ch);
                continue;
            }
            if (Character.isUpperCase(ch)) {
                // Last is not upper case and is not underline,
                // So can append underline.
                if (!lastIsUpper && !lastIsUnderline) {
                    builder.append(UNDERLINE);
                }
                builder.append(ch);
                lastIsUpper = true;
            }
            else {
                lowerCaseCount++;
                lastIsUpper = false;
                ch = Character.toUpperCase(ch);
                builder.append(ch);
            }
            lastIsUnderline = false;
        }
        if (lowerCaseCount == 0) {
            // No lower case is underline.
            return str;
        }
        return builder.toString();
    }

    public static String underlineToCamel(String str) {
        if (str == null) { return null; }
        int length = str.length();
        StringBuilder builder = new StringBuilder();
        char ch;
        int underlineCount = 0, lowerCaseCount = 0;
        for (int i = 0; i < length; i++) {
            if (str.charAt(i) == UNDERLINE) {
                ch = str.charAt(++i);
                ch = Character.toUpperCase(ch);
                underlineCount++;
            }
            else {
                ch = str.charAt(i);
                if (Character.isLowerCase(ch)) {
                    lowerCaseCount++;
                }
                else {
                    ch = Character.toLowerCase(ch);
                }
            }
            builder.append(ch);
        }
        if (underlineCount == 0 && lowerCaseCount > 0) {
            // No underline and have lower case is camel.
            return str;
        }
        return builder.toString();
    }

}
