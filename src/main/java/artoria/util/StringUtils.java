package artoria.util;

import java.lang.reflect.Array;
import java.util.Collection;

import static artoria.common.Constants.*;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

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

    public static String remove(String text, String remove) {
        if (isEmpty(text) || isEmpty(remove)) { return text; }
        return replace(text, remove, EMPTY_STRING);
    }

    public static String replace(String text, String searchString, String replacement) {
        if (StringUtils.isEmpty(text)
                || StringUtils.isEmpty(searchString)
                || replacement == null) {
            return text;
        }
        StringBuilder builder = new StringBuilder();
        // our position in the old string
        int pos = ZERO;
        int index = text.indexOf(searchString);
        // the index of an occurrence we've found, or -1
        int searchLen = searchString.length();
        while (index >= ZERO) {
            builder.append(text.substring(pos, index));
            builder.append(replacement);
            pos = index + searchLen;
            index = text.indexOf(searchString, pos);
        }
        builder.append(text.substring(pos));
        // remember to append any characters to the right of a match
        return builder.toString();
    }

    public static String join(Object objects) {
        if (objects == null) { return null; }
        return join(objects, EMPTY_STRING, ZERO, null);
    }

    public static String join(Object objects, Object separator) {
        if (objects == null) { return null; }
        return join(objects, separator, ZERO, null);
    }

    /**
     * Joins the elements of the provided 'objects' into a single String containing the provided list of elements.
     */
    public static String join(Object objects, Object separator, Integer startIndex, Integer endIndex) {
        if (objects == null) { return null; }
        if (separator == null) { separator = EMPTY_STRING; }
        if (startIndex == null || startIndex < ZERO) {
            startIndex = ZERO;
        }
        boolean isArray = objects.getClass().isArray();
        boolean isColl = objects instanceof Collection;
        if (!isArray && !isColl) {
            throw new UnsupportedOperationException(
                    "The input 'objects' must be an array or 'Collection'. "
            );
        }
        StringBuilder builder = new StringBuilder();
        if (isArray) {
            int length = Array.getLength(objects);
            if (endIndex == null || endIndex > length) {
                endIndex = length;
            }
            if ((endIndex - startIndex) <= ZERO) {
                return EMPTY_STRING;
            }
            for (int i = startIndex; i < endIndex; i++) {
                if (i > startIndex) {
                    builder.append(separator);
                }
                Object obj = Array.get(objects, i);
                if (obj != null) { builder.append(obj); }
            }
        }
        if (isColl) {
            Collection collection = (Collection) objects;
            int size = collection.size();
            if (endIndex == null || endIndex > size) {
                endIndex = size;
            }
            if ((endIndex - startIndex) <= ZERO) {
                return EMPTY_STRING;
            }
            int i = ZERO;
            for (Object obj : collection) {
                if (i > startIndex && i < endIndex) {
                    builder.append(separator);
                }
                if (i >= startIndex && i < endIndex) {
                    if (obj != null) { builder.append(obj); }
                }
                i++;
            }
        }
        return builder.toString();
    }

    public static String capitalize(String str) {

        return StringUtils.changeFirstCharacterCase(str, TRUE);
    }

    public static String uncapitalize(String str) {

        return StringUtils.changeFirstCharacterCase(str, FALSE);
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
