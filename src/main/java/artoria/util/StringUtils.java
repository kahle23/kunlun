package artoria.util;

import static artoria.common.Constants.*;

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
        for (int i = ZERO; i < strLen; i++) {
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
        boolean b = str.startsWith(MINUS)
                || str.startsWith(PLUS);
        int start = b ? ONE : ZERO;
        boolean firstDot = false;
        for (int i = str.length() - ONE; i >= start; i--) {
            char c = str.charAt(i);
            boolean isDg = Character.isDigit(c)
                    || (!firstDot && (firstDot = 0x2E == c));
            if (!isDg) { return false; }
        }
        return true;
    }

    public static boolean containsWhitespace(String str) {
        if (StringUtils.isEmpty(str)) {
            return false;
        }
        int strLen = str.length();
        for (int i = ZERO; i < strLen; i++) {
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
            int codePoint = (int) sb.charAt(ZERO);
            boolean isWs = Character.isWhitespace(codePoint);
            isLoop = sb.length() > ZERO && isWs;
            if (isLoop) { sb.deleteCharAt(ZERO); }
        } while (isLoop);
        do {
            int codePoint = (int) sb.charAt(sb.length() - ONE);
            boolean isWs = Character.isWhitespace(codePoint);
            isLoop = sb.length() > ZERO && isWs;
            if (isLoop) { sb.deleteCharAt(sb.length() - ONE); }
        } while (isLoop);
        return sb.toString();
    }

    public static String trimAllWhitespace(String str) {
        if (StringUtils.isEmpty(str)) { return str; }
        int len = str.length();
        StringBuilder sb = new StringBuilder(str.length());
        for (int i = ZERO; i < len; i++) {
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
        int pos = ZERO;
        int index = inString.indexOf(oldPattern);
        // the index of an occurrence we've found, or -1
        int patLen = oldPattern.length();
        while (index >= ZERO) {
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
        if (str == null || str.length() == ZERO) { return str; }
        StringBuilder sb = new StringBuilder(str.length());
        char c = str.charAt(ZERO);
        sb.append(capitalize ?
                Character.toUpperCase(c) :
                Character.toLowerCase(c));
        sb.append(str.substring(ONE));
        return sb.toString();
    }

    public static String camelToUnderline(String str) {
        if (str == null) { return null; }
        int length = str.length();
        StringBuilder builder = new StringBuilder();
        char ch;
        int lowerCaseCount = ZERO;
        boolean lastIsUpper = false, lastIsUnderline = false;
        for (int i = ZERO; i < length; i++) {
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
        if (lowerCaseCount == ZERO) {
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
        int underlineCount = ZERO, lowerCaseCount = ZERO;
        for (int i = ZERO; i < length; i++) {
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
        if (underlineCount == ZERO && lowerCaseCount > ZERO) {
            // No underline and have lower case is camel.
            return str;
        }
        return builder.toString();
    }

}
