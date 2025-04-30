/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.util;

import kunlun.core.function.Supplier;
import kunlun.renderer.support.FormatTextRenderer;
import kunlun.renderer.support.PrintfTextRenderer;

import java.util.Arrays;
import java.util.Map;
import java.util.logging.Logger;

/**
 * To assert, verify data state, if failure, will throw exception.
 * <p>
 * Some example messages:
 *      "Parameter \"param\" must not blank. "
 *      "Parameter \"param\" must not null. "
 * @author Kahle
 */
public class Assert {
    private static final Logger log = Logger.getLogger(Assert.class.getName());
    private static volatile FormatTextRenderer messageRenderer;


    // region ======== get / set renderer and render ========

    public static FormatTextRenderer getMessageRenderer() {
        if (messageRenderer != null) { return messageRenderer; }
        synchronized (Assert.class) {
            if (messageRenderer != null) { return messageRenderer; }
            setMessageRenderer(new PrintfTextRenderer());
        }
        return messageRenderer;
    }

    public static void setMessageRenderer(FormatTextRenderer messageRenderer) {
        Assert.notNull(messageRenderer, "Parameter \"messageRenderer\" must not null. ");
        log.fine("Set message renderer: " + messageRenderer.getClass().getName());
        Assert.messageRenderer = messageRenderer;
    }

    public static String renderMessage(String template, Object... arguments) {
        if (ArrayUtil.isEmpty(arguments)) { return template; }
        if (StrUtil.isBlank(template)) { return template; }
        return getMessageRenderer().render(template, arguments);
    }
    // endregion ======== get / set renderer and render ========


    // region ======== state, is false, is true ========

    public static <X extends Throwable> void state(boolean expression, Supplier<X> errorSupplier) throws X {
        if (!expression) {
            throw errorSupplier.get();
        }
    }

    public static void state(boolean expression, String message, Object... arguments) {
        if (!expression) {
            throw new IllegalStateException(renderMessage(message, arguments));
        }
    }

    public static void state(boolean expression) {

        state(expression, "[Assertion failed] - This state invariant must be true. ");
    }

    public static <X extends Throwable> void isFalse(boolean expression, Supplier<X> errorSupplier) throws X {
        if (expression) {
            throw errorSupplier.get();
        }
    }

    public static void isFalse(boolean expression, String message, Object... arguments) {
        if (expression) {
            throw new IllegalArgumentException(renderMessage(message, arguments));
        }
    }

    public static void isFalse(boolean expression) {

        isFalse(expression, "[Assertion failed] - This expression must be false. ");
    }

    public static <X extends Throwable> void isTrue(boolean expression, Supplier<X> errorSupplier) throws X {
        if (!expression) {
            throw errorSupplier.get();
        }
    }

    public static void isTrue(boolean expression, String message, Object... arguments) {
        if (!expression) {
            throw new IllegalArgumentException(renderMessage(message, arguments));
        }
    }

    public static void isTrue(boolean expression) {

        isTrue(expression, "[Assertion failed] - This expression must be true. ");
    }
    // endregion ======== state, is false, is true ========


    // region ======== is null, not null ========

    public static <X extends Throwable> void isNull(Object object, Supplier<X> errorSupplier) throws X {
        if (object != null) {
            throw errorSupplier.get();
        }
    }

    public static void isNull(Object object, String message, Object... arguments) {
        if (object != null) {
            throw new IllegalArgumentException(renderMessage(message, arguments));
        }
    }

    public static void isNull(Object object) {

        isNull(object, "[Assertion failed] - The object argument must be null. ");
    }

    public static <T, X extends Throwable> T notNull(T object, Supplier<X> errorSupplier) throws X {
        if (object == null) {
            throw errorSupplier.get();
        }
        return object;
    }

    public static <T> T notNull(T object, String message, Object... arguments) {
        if (object == null) {
            throw new IllegalArgumentException(renderMessage(message, arguments));
        }
        return object;
    }

    public static <T> T notNull(T object) {

        return notNull(object, "[Assertion failed] - This argument is required; it must not be null. ");
    }
    // endregion ======== is null, not null ========


    // region ======== is empty, not empty >> bytes array ========

    public static <X extends Throwable> void isEmpty(byte[] array, Supplier<X> errorSupplier) throws X {
        if (ArrayUtil.isNotEmpty(array)) {
            throw errorSupplier.get();
        }
    }

    public static void isEmpty(byte[] array, String message, Object... arguments) {
        if (ArrayUtil.isNotEmpty(array)) {
            throw new IllegalArgumentException(renderMessage(message, arguments));
        }
    }

    public static void isEmpty(byte[] array) {

        isEmpty(array, "[Assertion failed] - This array must empty. ");
    }

    public static <X extends Throwable> byte[] notEmpty(byte[] array, Supplier<X> errorSupplier) throws X {
        if (ArrayUtil.isEmpty(array)) {
            throw errorSupplier.get();
        }
        return array;
    }

    public static byte[] notEmpty(byte[] array, String message, Object... arguments) {
        if (ArrayUtil.isEmpty(array)) {
            throw new IllegalArgumentException(renderMessage(message, arguments));
        }
        return array;
    }

    public static byte[] notEmpty(byte[] array) {

        return notEmpty(array, "[Assertion failed] - This array must not be empty: it must contain at least one element. ");
    }
    // endregion ======== is empty, not empty >> bytes array ========


    // region ======== is empty, not empty >> objects array ========

    public static <T, X extends Throwable> void isEmpty(T[] array, Supplier<X> errorSupplier) throws X {
        if (ArrayUtil.isNotEmpty(array)) {
            throw errorSupplier.get();
        }
    }

    public static <T> void isEmpty(T[] array, String message, Object... arguments) {
        if (ArrayUtil.isNotEmpty(array)) {
            throw new IllegalArgumentException(renderMessage(message, arguments));
        }
    }

    public static <T> void isEmpty(T[] array) {

        isEmpty(array, "[Assertion failed] - This array must empty. ");
    }

    public static <T, X extends Throwable> T[] notEmpty(T[] array, Supplier<X> errorSupplier) throws X {
        if (ArrayUtil.isEmpty(array)) {
            throw errorSupplier.get();
        }
        return array;
    }

    public static <T> T[] notEmpty(T[] array, String message, Object... arguments) {
        if (ArrayUtil.isEmpty(array)) {
            throw new IllegalArgumentException(renderMessage(message, arguments));
        }
        return array;
    }

    public static <T> T[] notEmpty(T[] array) {

        return notEmpty(array, "[Assertion failed] - This array must not be empty: it must contain at least one element. ");
    }
    // endregion ======== is empty, not empty >> objects array ========


    // region ======== is empty, not empty >> iterable ========

    public static <X extends Throwable> void isEmpty(Iterable<?> collection, Supplier<X> errorSupplier) throws X {
        if (IterUtil.isNotEmpty(collection)) {
            throw errorSupplier.get();
        }
    }

    public static void isEmpty(Iterable<?> collection, String message, Object... arguments) {
        if (IterUtil.isNotEmpty(collection)) {
            throw new IllegalArgumentException(renderMessage(message, arguments));
        }
    }

    public static void isEmpty(Iterable<?> collection) {

        isEmpty(collection, "[Assertion failed] - This collection must empty. ");
    }

    public static <E, T extends Iterable<E>, X extends Throwable> T notEmpty(T collection, Supplier<X> errorSupplier) throws X {
        if (IterUtil.isEmpty(collection)) {
            throw errorSupplier.get();
        }
        return collection;
    }

    public static <E, T extends Iterable<E>> T notEmpty(T collection, String message, Object... arguments) {
        if (IterUtil.isEmpty(collection)) {
            throw new IllegalArgumentException(renderMessage(message, arguments));
        }
        return collection;
    }

    public static <E, T extends Iterable<E>> T notEmpty(T collection) {

        return notEmpty(collection, "[Assertion failed] - This collection must not be empty: it must contain at least one element. ");
    }
    // endregion ======== is empty, not empty >> iterable ========


    // region ======== is empty, not empty >> map ========

    public static <X extends Throwable> void isEmpty(Map<?, ?> map, Supplier<X> errorSupplier) throws X {
        if (MapUtil.isNotEmpty(map)) {
            throw errorSupplier.get();
        }
    }

    public static void isEmpty(Map<?, ?> map, String message, Object... arguments) {
        if (MapUtil.isNotEmpty(map)) {
            throw new IllegalArgumentException(renderMessage(message, arguments));
        }
    }

    public static void isEmpty(Map<?, ?> map) {

        isEmpty(map, "[Assertion failed] - This map must empty. ");
    }

    public static <K, V, T extends Map<K, V>, X extends Throwable> T notEmpty(T map, Supplier<X> errorSupplier) throws X {
        if (MapUtil.isEmpty(map)) {
            throw errorSupplier.get();
        }
        return map;
    }

    public static <K, V, T extends Map<K, V>> T notEmpty(T map, String message, Object... arguments) {
        if (MapUtil.isEmpty(map)) {
            throw new IllegalArgumentException(renderMessage(message, arguments));
        }
        return map;
    }

    public static <K, V, T extends Map<K, V>> T notEmpty(T map) {

        return notEmpty(map, "[Assertion failed] - This map must not be empty: it must contain at least one entry. ");
    }
    // endregion ======== is empty, not empty >> map ========


    // region ======== is empty, not empty, is blank, not blank ========

    public static <X extends Throwable> void isEmpty(CharSequence text, Supplier<X> errorSupplier) throws X {
        if (StrUtil.isNotEmpty(text)) {
            throw errorSupplier.get();
        }
    }

    public static void isEmpty(CharSequence text, String message, Object... arguments) {
        if (StrUtil.isNotEmpty(text)) {
            throw new IllegalArgumentException(renderMessage(message, arguments));
        }
    }

    public static void isEmpty(CharSequence text) {

        isEmpty(text, "[Assertion failed] - This string argument must no length: it must be null or empty. ");
    }

    public static <T extends CharSequence, X extends Throwable> T notEmpty(T text, Supplier<X> errorSupplier) throws X {
        if (StrUtil.isEmpty(text)) {
            throw errorSupplier.get();
        }
        return text;
    }

    public static <T extends CharSequence> T notEmpty(T text, String message, Object... arguments) {
        if (StrUtil.isEmpty(text)) {
            throw new IllegalArgumentException(renderMessage(message, arguments));
        }
        return text;
    }

    public static <T extends CharSequence> T notEmpty(T text) {

        return notEmpty(text, "[Assertion failed] - This string argument must have length: it must not be null or empty. ");
    }

    public static <X extends Throwable> void isBlank(CharSequence text, Supplier<X> errorSupplier) throws X {
        if (StrUtil.isNotBlank(text)) {
            throw errorSupplier.get();
        }
    }

    public static void isBlank(CharSequence text, String message, Object... arguments) {
        if (StrUtil.isNotBlank(text)) {
            throw new IllegalArgumentException(renderMessage(message, arguments));
        }
    }

    public static void isBlank(CharSequence text) {

        isBlank(text, "[Assertion failed] - This string argument must no text: it must be null, empty, or blank. ");
    }

    public static <T extends CharSequence, X extends Throwable> T notBlank(T text, Supplier<X> errorSupplier) throws X {
        if (StrUtil.isBlank(text)) {
            throw errorSupplier.get();
        }
        return text;
    }

    public static <T extends CharSequence> T notBlank(T text, String message, Object... arguments) {
        if (StrUtil.isBlank(text)) {
            throw new IllegalArgumentException(renderMessage(message, arguments));
        }
        return text;
    }

    public static <T extends CharSequence> T notBlank(T text) {

        return notBlank(text, "[Assertion failed] - This string argument must have text: it must not be null, empty, or blank. ");
    }
    // endregion ======== is empty, not empty, is blank, not blank ========


    // region ======== is contain, not contain >> string ========

    public static <T extends CharSequence, X extends Throwable> T isContain(String textToSearch, T substring, Supplier<X> errorSupplier) throws X {
        if (!textToSearch.contains(substring)) {
            throw errorSupplier.get();
        }
        return substring;
    }

    public static <T extends CharSequence> T isContain(String textToSearch, T substring, String message, Object... arguments) {
        if (!textToSearch.contains(substring)) {
            throw new IllegalArgumentException(renderMessage(message, arguments));
        }
        return substring;
    }

    public static <T extends CharSequence> T isContain(String textToSearch, T substring) {

        return isContain(textToSearch, substring, "[Assertion failed] - This string argument must contain the substring [" + substring + "]. ");
    }

    public static <T extends CharSequence, X extends Throwable> T notContain(String textToSearch, T substring, Supplier<X> errorSupplier) throws X {
        if (textToSearch.contains(substring)) {
            throw errorSupplier.get();
        }
        return substring;
    }

    public static <T extends CharSequence> T notContain(String textToSearch, T substring, String message, Object... arguments) {
        if (textToSearch.contains(substring)) {
            throw new IllegalArgumentException(renderMessage(message, arguments));
        }
        return substring;
    }

    public static <T extends CharSequence> T notContain(String textToSearch, T substring) {

        return notContain(textToSearch, substring, "[Assertion failed] - This string argument must not contain the substring [" + substring + "]. ");
    }
    // endregion ======== is contain, not contain >> string ========


    // region ======== is instance of ========

    public static <T, X extends Throwable> T isInstanceOf(Class<?> type, T obj, Supplier<X> errorSupplier) throws X {
        notNull(type, "Type to check against must not be null. ");
        if (!type.isInstance(obj)) {
            throw errorSupplier.get();
        }
        return obj;
    }

    public static <T> T isInstanceOf(Class<?> type, T obj, String message, Object... arguments) {
        notNull(type, "Type to check against must not be null. ");
        if (!type.isInstance(obj)) {
            throw new IllegalArgumentException(renderMessage(message, arguments));
        }
        return obj;
    }

    public static <T> T isInstanceOf(Class<?> type, T obj) {

        return isInstanceOf(type, obj, "[Assertion failed] - This object [" + obj + "] is not instanceof [" + type + "]. ");
    }
    // endregion ======== is instance of ========


    // region ======== is assignable ========

    public static <X extends Throwable> void isAssignable(Class<?> superType, Class<?> subType, Supplier<X> errorSupplier) throws X {
        notNull(superType, "Type to check against must not be null. ");
        if (subType == null || !superType.isAssignableFrom(subType)) {
            throw errorSupplier.get();
        }
    }

    public static void isAssignable(Class<?> superType, Class<?> subType, String message, Object... arguments) {
        notNull(superType, "Type to check against must not be null. ");
        if (subType == null || !superType.isAssignableFrom(subType)) {
            throw new IllegalArgumentException(renderMessage(message, arguments));
        }
    }

    public static void isAssignable(Class<?> superType, Class<?> subType) {

        isAssignable(superType, subType, "[Assertion failed] - This [" + subType + "] is not assignable to [" + superType + "]. ");
    }
    // endregion ======== is assignable ========


    // region ======== is support ========

    public static <X extends Throwable> void isSupport(Class<?>[] supportClasses, boolean assignable, Class<?> targetClass, Supplier<X> errorSupplier) throws X {
        if (!ClassUtil.isSupport(supportClasses, assignable, targetClass)) {
            throw errorSupplier.get();
        }
    }

    public static void isSupport(Class<?>[] supportClasses, boolean assignable, Class<?> targetClass, String message, Object... arguments) {
        if (!ClassUtil.isSupport(supportClasses, assignable, targetClass)) {
            throw new IllegalArgumentException(renderMessage(message, arguments));
        }
    }

    public static void isSupport(Class<?> targetClass, boolean assignable, Class<?>... supportClasses) {

        isSupport(supportClasses, assignable, targetClass, "[Assertion failed] - This [" + targetClass + "] is not support to [" + Arrays.toString(supportClasses) + "]. ");
    }
    // endregion ======== is support ========


    // region ======== no null elements ========

    public static void noNullElements(Object[] array, String message, Object... arguments) {
        for (Object element : array) {
            if (element == null) {
                throw new IllegalArgumentException(renderMessage(message, arguments));
            }
        }
    }
    // endregion ======== no null elements ========

}
