/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.util;

import java.util.Arrays;
import java.util.Map;

/**
 * To assert, verify data state, if failure, will throw exception.
 * @author Kahle
 */
public class Assert {
    // ====

    public static void state(boolean expression, String message) {
        if (!expression) {
            throw new IllegalStateException(message);
        }
    }

    public static void state(boolean expression) {

        state(expression, "[Assertion failed] - This state invariant must be true. ");
    }

    public static void isFalse(boolean expression, String message) {
        if (expression) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void isFalse(boolean expression) {

        isFalse(expression, "[Assertion failed] - This expression must be false. ");
    }

    public static void isTrue(boolean expression, String message) {
        if (!expression) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void isTrue(boolean expression) {

        isTrue(expression, "[Assertion failed] - This expression must be true. ");
    }

    // ====

    public static void isNull(Object object, String message) {
        if (object != null) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void isNull(Object object) {

        isNull(object, "[Assertion failed] - The object argument must be null. ");
    }

    public static <T> T notNull(T object, String message) {
        if (object == null) {
            throw new IllegalArgumentException(message);
        }
        return object;
    }

    public static <T> T notNull(T object) {

        return notNull(object, "[Assertion failed] - This argument is required; it must not be null. ");
    }

    // ====

    public static <T> void isEmpty(T[] array, String message) {
        if (ArrayUtil.isNotEmpty(array)) {
            throw new IllegalArgumentException(message);
        }
    }

    public static <T> void isEmpty(T[] array) {

        isEmpty(array, "[Assertion failed] - This array must empty. ");
    }

    public static <T> T[] notEmpty(T[] array, String message) {
        if (ArrayUtil.isEmpty(array)) {
            throw new IllegalArgumentException(message);
        }
        return array;
    }

    public static <T> T[] notEmpty(T[] array) {

        return notEmpty(array, "[Assertion failed] - This array must not be empty: it must contain at least one element. ");
    }

    // ====

    public static void isEmpty(byte[] array, String message) {
        if (ArrayUtil.isNotEmpty(array)) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void isEmpty(byte[] array) {

        isEmpty(array, "[Assertion failed] - This array must empty. ");
    }

    public static byte[] notEmpty(byte[] array, String message) {
        if (ArrayUtil.isEmpty(array)) {
            throw new IllegalArgumentException(message);
        }
        return array;
    }

    public static byte[] notEmpty(byte[] array) {

        return notEmpty(array, "[Assertion failed] - This array must not be empty: it must contain at least one element. ");
    }

    // ====

    public static void isEmpty(Iterable<?> collection, String message) {
        if (IterUtil.isNotEmpty(collection)) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void isEmpty(Iterable<?> collection) {

        isEmpty(collection, "[Assertion failed] - This collection must empty. ");
    }

    public static <E, T extends Iterable<E>> T notEmpty(T collection, String message) {
        if (IterUtil.isEmpty(collection)) {
            throw new IllegalArgumentException(message);
        }
        return collection;
    }

    public static <E, T extends Iterable<E>> T notEmpty(T collection) {

        return notEmpty(collection, "[Assertion failed] - This collection must not be empty: it must contain at least one element. ");
    }

    // ====

    public static void isEmpty(Map<?, ?> map, String message) {
        if (MapUtil.isNotEmpty(map)) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void isEmpty(Map<?, ?> map) {

        isEmpty(map, "[Assertion failed] - This map must empty. ");
    }

    public static <K, V, T extends Map<K, V>> T notEmpty(T map, String message) {
        if (MapUtil.isEmpty(map)) {
            throw new IllegalArgumentException(message);
        }
        return map;
    }

    public static <K, V, T extends Map<K, V>> T notEmpty(T map) {

        return notEmpty(map, "[Assertion failed] - This map must not be empty: it must contain at least one entry. ");
    }

    // ====

    public static void isEmpty(CharSequence text, String message) {
        if (StrUtil.isNotEmpty(text)) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void isEmpty(CharSequence text) {

        isEmpty(text, "[Assertion failed] - This string argument must no length: it must be null or empty. ");
    }

    public static <T extends CharSequence> T notEmpty(T text, String message) {
        if (StrUtil.isEmpty(text)) {
            throw new IllegalArgumentException(message);
        }
        return text;
    }

    public static <T extends CharSequence> T notEmpty(T text) {

        return notEmpty(text, "[Assertion failed] - This string argument must have length: it must not be null or empty. ");
    }

    public static void isBlank(CharSequence text, String message) {
        if (StrUtil.isNotBlank(text)) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void isBlank(CharSequence text) {

        isBlank(text, "[Assertion failed] - This string argument must no text: it must be null, empty, or blank. ");
    }

    public static <T extends CharSequence> T notBlank(T text, String message) {
        if (StrUtil.isBlank(text)) {
            throw new IllegalArgumentException(message);
        }
        return text;
    }

    public static <T extends CharSequence> T notBlank(T text) {

        return notBlank(text, "[Assertion failed] - This string argument must have text: it must not be null, empty, or blank. ");
    }

    // ====

    public static <T extends CharSequence> T isContain(String textToSearch, T substring, String message) {
        if (!textToSearch.contains(substring)) {
            throw new IllegalArgumentException(message);
        }
        return substring;
    }

    public static <T extends CharSequence> T isContain(String textToSearch, T substring) {

        return isContain(textToSearch, substring, "[Assertion failed] - This string argument must contain the substring [" + substring + "]. ");
    }

    public static <T extends CharSequence> T notContain(String textToSearch, T substring, String message) {
        if (textToSearch.contains(substring)) {
            throw new IllegalArgumentException(message);
        }
        return substring;
    }

    public static <T extends CharSequence> T notContain(String textToSearch, T substring) {

        return notContain(textToSearch, substring, "[Assertion failed] - This string argument must not contain the substring [" + substring + "]. ");
    }

    // ====

    public static <T> T isInstanceOf(Class<?> type, T obj, String message) {
        notNull(type, "Type to check against must not be null. ");
        if (!type.isInstance(obj)) {
            throw new IllegalArgumentException(message);
        }
        return obj;
    }

    public static <T> T isInstanceOf(Class<?> type, T obj) {

        return isInstanceOf(type, obj, "[Assertion failed] - This object [" + obj + "] is not instanceof [" + type + "]. ");
    }

    // ====

    public static void isAssignable(Class<?> superType, Class<?> subType, String message) {
        notNull(superType, "Type to check against must not be null. ");
        if (subType == null || !superType.isAssignableFrom(subType)) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void isAssignable(Class<?> superType, Class<?> subType) {

        isAssignable(superType, subType, "[Assertion failed] - This [" + subType + "] is not assignable to [" + superType + "]. ");
    }

    // ====

    public static void isSupport(Class<?>[] supportClasses, boolean assignable, Class<?> targetClass, String message) {
        if (!ClassUtil.isSupport(supportClasses, assignable, targetClass)) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void isSupport(Class<?> targetClass, boolean assignable, Class<?>... supportClasses) {

        isSupport(supportClasses, assignable, targetClass, "[Assertion failed] - This [" + targetClass + "] is not support to [" + Arrays.toString(supportClasses) + "]. ");
    }

    // ====

    public static void noNullElements(Object[] array, String message) {
        for (Object element : array) {
            if (element == null) {
                throw new IllegalArgumentException(message);
            }
        }
    }

    // ====
}
