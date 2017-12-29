package com.apyhs.artoria.util;

import java.util.Collection;
import java.util.Map;

/**
 * Assert, verify data state, if failure, will throw exception.
 * @author Kahle
 */
public class Assert {
    private static final String MESSAGE_NOT_BLANK = "Parameter \"message\" must is not blank. ";

    public static void state(boolean expression, String message) {
        Assert.notBlank(message, MESSAGE_NOT_BLANK);
        if (!expression) {
            throw new IllegalStateException(message);
        }
    }

    public static void isFalse(boolean expression, String message) {
        Assert.notBlank(message, MESSAGE_NOT_BLANK);
        if (expression) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void isTrue(boolean expression, String message) {
        Assert.notBlank(message, MESSAGE_NOT_BLANK);
        if (!expression) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void isNull(Object object, String message) {
        Assert.notBlank(message, MESSAGE_NOT_BLANK);
        if (object != null) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void notNull(Object object, String message) {
        Assert.notBlank(message, MESSAGE_NOT_BLANK);
        if (object == null) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void isEmpty(byte[] array, String message) {
        Assert.notBlank(message, MESSAGE_NOT_BLANK);
        if (ArrayUtils.isNotEmpty(array)) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void notEmpty(byte[] array, String message) {
        Assert.notBlank(message, MESSAGE_NOT_BLANK);
        if (ArrayUtils.isEmpty(array)) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void isEmpty(Object[] array, String message) {
        Assert.notBlank(message, MESSAGE_NOT_BLANK);
        if (ArrayUtils.isNotEmpty(array)) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void notEmpty(Object[] array, String message) {
        Assert.notBlank(message, MESSAGE_NOT_BLANK);
        if (ArrayUtils.isEmpty(array)) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void isEmpty(Collection<?> collection, String message) {
        Assert.notBlank(message, MESSAGE_NOT_BLANK);
        if (CollectionUtils.isNotEmpty(collection)) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void notEmpty(Collection<?> collection, String message) {
        Assert.notBlank(message, MESSAGE_NOT_BLANK);
        if (CollectionUtils.isEmpty(collection)) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void isEmpty(Map<?, ?> map, String message) {
        Assert.notBlank(message, MESSAGE_NOT_BLANK);
        if (MapUtils.isNotEmpty(map)) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void notEmpty(Map<?, ?> map, String message) {
        Assert.notBlank(message, MESSAGE_NOT_BLANK);
        if (MapUtils.isEmpty(map)) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void isEmpty(String text, String message) {
        Assert.notBlank(message, MESSAGE_NOT_BLANK);
        if (StringUtils.isNotEmpty(text)) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void notEmpty(String text, String message) {
        Assert.notBlank(message, MESSAGE_NOT_BLANK);
        if (StringUtils.isEmpty(text)) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void isBlank(String text, String message) {
        Assert.notBlank(message, MESSAGE_NOT_BLANK);
        if (StringUtils.isNotBlank(text)) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void notBlank(String text, String message) {
        if (StringUtils.isBlank(message)) {
            throw new IllegalArgumentException(MESSAGE_NOT_BLANK);
        }
        if (StringUtils.isBlank(text)) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void isContain(String textToSearch, String substring, String message) {
        Assert.notBlank(message, MESSAGE_NOT_BLANK);
        Assert.notEmpty(textToSearch, "Parameter \"textToSearch\" must is not empty. ");
        Assert.notEmpty(substring, "Parameter \"substring\" must is not empty. ");
        if (!textToSearch.contains(substring)) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void notContain(String textToSearch, String substring, String message) {
        Assert.notBlank(message, MESSAGE_NOT_BLANK);
        Assert.notEmpty(textToSearch, "Parameter \"textToSearch\" must is not empty. ");
        Assert.notEmpty(substring, "Parameter \"substring\" must is not empty. ");
        if (textToSearch.contains(substring)) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void isAssignable(Class<?> superType, Class<?> subType, String message) {
        Assert.notBlank(message, MESSAGE_NOT_BLANK);
        Assert.notNull(superType, "Parameter \"superType\" must is not null. ");
        if (subType == null || !superType.isAssignableFrom(subType)) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void isInstanceOf(Class<?> type, Object obj, String message) {
        Assert.notBlank(message, MESSAGE_NOT_BLANK);
        Assert.notNull(type, "Parameter \"type\" must is not null. ");
        if (obj == null || !type.isInstance(obj)) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void noNullElements(Object[] array, String message) {
        Assert.notBlank(message, MESSAGE_NOT_BLANK);
        Assert.notEmpty(array, "Parameter \"array\" must is not empty. ");
        for (Object element : array) {
            if (element == null) {
                throw new IllegalArgumentException(message);
            }
        }
    }

}
