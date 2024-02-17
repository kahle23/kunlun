/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.util;

import java.util.Collection;
import java.util.Map;

import static kunlun.common.constant.Numbers.TWO;
import static kunlun.common.constant.Numbers.ZERO;

/**
 * To assert, verify data state, if failure, will throw exception.
 * @author Kahle
 */
public class Assert {
    // "Parameter \"parameter\" must not blank. "
    // "Variable \"variable\" must not null. "

    public static void state(boolean expression, String message) {
        if (!expression) {
            throw new IllegalStateException(message);
        }
    }

    public static void isFalse(boolean expression, String message) {
        if (expression) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void isTrue(boolean expression, String message) {
        if (!expression) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void isNull(Object object, String message) {
        if (object != null) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void notNull(Object object, String message) {
        if (object == null) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void isEmpty(byte[] array, String message) {
        if (ArrayUtils.isNotEmpty(array)) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void notEmpty(byte[] array, String message) {
        if (ArrayUtils.isEmpty(array)) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void isEmpty(Object[] array, String message) {
        if (ArrayUtils.isNotEmpty(array)) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void notEmpty(Object[] array, String message) {
        if (ArrayUtils.isEmpty(array)) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void isEmpty(Collection<?> collection, String message) {
        if (CollectionUtils.isNotEmpty(collection)) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void notEmpty(Collection<?> collection, String message) {
        if (CollectionUtils.isEmpty(collection)) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void isEmpty(Map<?, ?> map, String message) {
        if (MapUtils.isNotEmpty(map)) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void notEmpty(Map<?, ?> map, String message) {
        if (MapUtils.isEmpty(map)) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void isEmpty(String text, String message) {
        if (StringUtils.isNotEmpty(text)) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void notEmpty(String text, String message) {
        if (StringUtils.isEmpty(text)) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void isBlank(String text, String message) {
        if (StringUtils.isNotBlank(text)) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void notBlank(String text, String message) {
        if (StringUtils.isBlank(text)) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void isContain(String textToSearch, String substring, String message) {
        if (!textToSearch.contains(substring)) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void notContain(String textToSearch, String substring, String message) {
        if (textToSearch.contains(substring)) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void isSupport(Class<?> targetClass, boolean assignable, Class<?>... supportClasses) {
        Assert.notEmpty(supportClasses, "Parameter \"supportClasses\" must not empty. ");
        if (ClassUtils.isSupport(supportClasses, assignable, targetClass)) { return; }
        StringBuilder builder = new StringBuilder();
        String separator = ", ", content = "is empty";
        for (Class<?> supportClass : supportClasses) {
        if (supportClass == null) { continue; }
            builder.append(supportClass.getName()).append(separator);
        }
        int length = builder.length();
        if (length >= TWO) {
            content = builder.substring(ZERO, length - TWO);
        }
        String format = String.format(
            "Parameter \"targetClass\" is not supported. (Only support %s)", content
        );
        throw new IllegalArgumentException(format);
    }

    public static void isSupport(Class<?>[] supportClasses, boolean assignable, Class<?> targetClass, String message) {
        if (!ClassUtils.isSupport(supportClasses, assignable, targetClass)) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void isAssignable(Class<?> superType, Class<?> subType, String message) {
        if (subType == null || !superType.isAssignableFrom(subType)) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void isInstanceOf(Class<?> type, Object obj) {
        if (type == null) { throw new NullPointerException(); }
        String objName = obj != null ? obj.getClass().getName() : "null";
        String format = String.format(
                "The parameter of type \"%s\" must instance of \"%s\". ", objName, type.getName());
        isInstanceOf(type, obj, format);
    }

    public static void isInstanceOf(Class<?> type, Object obj, String message) {
        if (!type.isInstance(obj)) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void noNullElements(Object[] array, String message) {
        for (Object element : array) {
            if (element == null) {
                throw new IllegalArgumentException(message);
            }
        }
    }

}
