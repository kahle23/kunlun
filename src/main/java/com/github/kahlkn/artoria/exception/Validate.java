package com.github.kahlkn.artoria.exception;

import com.github.kahlkn.artoria.util.ArrayUtils;
import com.github.kahlkn.artoria.util.CollectionUtils;
import com.github.kahlkn.artoria.util.MapUtils;
import com.github.kahlkn.artoria.util.StringUtils;

import java.util.Collection;
import java.util.Map;

/**
 * Validate tools.
 * @author Kahle
 */
public class Validate {

    public static void state(boolean expression, ErrorCode code) {
        if (!expression) {
            throw new BusinessException(code);
        }
    }

    public static void state(boolean expression, ErrorCode code, String description) {
        if (!expression) {
            throw new BusinessException(code)
                    .setDescription(description);
        }
    }

    public static void state(boolean expression, String message) {
        if (!expression) {
            throw new BusinessException(message);
        }
    }

    public static void state(boolean expression, String message, String description) {
        if (!expression) {
            throw new BusinessException(message)
                    .setDescription(description);
        }
    }

    public static void isFalse(boolean expression, ErrorCode code) {
        if (expression) {
            throw new BusinessException(code);
        }
    }

    public static void isFalse(boolean expression, ErrorCode code, String description) {
        if (expression) {
            throw new BusinessException(code)
                    .setDescription(description);
        }
    }

    public static void isFalse(boolean expression, String message) {
        if (expression) {
            throw new BusinessException(message);
        }
    }

    public static void isFalse(boolean expression, String message, String description) {
        if (expression) {
            throw new BusinessException(message)
                    .setDescription(description);
        }
    }

    public static void isTrue(boolean expression, ErrorCode code) {
        if (!expression) {
            throw new BusinessException(code);
        }
    }

    public static void isTrue(boolean expression, ErrorCode code, String description) {
        if (!expression) {
            throw new BusinessException(code)
                    .setDescription(description);
        }
    }

    public static void isTrue(boolean expression, String message) {
        if (!expression) {
            throw new BusinessException(message);
        }
    }

    public static void isTrue(boolean expression, String message, String description) {
        if (!expression) {
            throw new BusinessException(message)
                    .setDescription(description);
        }
    }

    public static void isNull(Object object, ErrorCode code) {
        if (object != null) {
            throw new BusinessException(code);
        }
    }

    public static void isNull(Object object, ErrorCode code, String description) {
        if (object != null) {
            throw new BusinessException(code)
                    .setDescription(description);
        }
    }

    public static void isNull(Object object, String message) {
        if (object != null) {
            throw new BusinessException(message);
        }
    }

    public static void isNull(Object object, String message, String description) {
        if (object != null) {
            throw new BusinessException(message)
                    .setDescription(description);
        }
    }

    public static void notNull(Object object, ErrorCode code) {
        if (object == null) {
            throw new BusinessException(code);
        }
    }

    public static void notNull(Object object, ErrorCode code, String description) {
        if (object == null) {
            throw new BusinessException(code)
                    .setDescription(description);
        }
    }

    public static void notNull(Object object, String message) {
        if (object == null) {
            throw new BusinessException(message);
        }
    }

    public static void notNull(Object object, String message, String description) {
        if (object == null) {
            throw new BusinessException(message)
                    .setDescription(description);
        }
    }

    public static void isEmpty(byte[] array, ErrorCode code) {
        if (ArrayUtils.isNotEmpty(array)) {
            throw new BusinessException(code);
        }
    }

    public static void isEmpty(byte[] array, ErrorCode code, String description) {
        if (ArrayUtils.isNotEmpty(array)) {
            throw new BusinessException(code)
                    .setDescription(description);
        }
    }

    public static void isEmpty(byte[] array, String message) {
        if (ArrayUtils.isNotEmpty(array)) {
            throw new BusinessException(message);
        }
    }

    public static void isEmpty(byte[] array, String message, String description) {
        if (ArrayUtils.isNotEmpty(array)) {
            throw new BusinessException(message)
                    .setDescription(description);
        }
    }

    public static void notEmpty(byte[] array, ErrorCode code) {
        if (ArrayUtils.isEmpty(array)) {
            throw new BusinessException(code);
        }
    }

    public static void notEmpty(byte[] array, ErrorCode code, String description) {
        if (ArrayUtils.isEmpty(array)) {
            throw new BusinessException(code)
                    .setDescription(description);
        }
    }

    public static void notEmpty(byte[] array, String message) {
        if (ArrayUtils.isEmpty(array)) {
            throw new BusinessException(message);
        }
    }

    public static void notEmpty(byte[] array, String message, String description) {
        if (ArrayUtils.isEmpty(array)) {
            throw new BusinessException(message)
                    .setDescription(description);
        }
    }

    public static void isEmpty(Object[] array, ErrorCode code) {
        if (ArrayUtils.isNotEmpty(array)) {
            throw new BusinessException(code);
        }
    }

    public static void isEmpty(Object[] array, ErrorCode code, String description) {
        if (ArrayUtils.isNotEmpty(array)) {
            throw new BusinessException(code)
                    .setDescription(description);
        }
    }

    public static void isEmpty(Object[] array, String message) {
        if (ArrayUtils.isNotEmpty(array)) {
            throw new BusinessException(message);
        }
    }

    public static void isEmpty(Object[] array, String message, String description) {
        if (ArrayUtils.isNotEmpty(array)) {
            throw new BusinessException(message)
                    .setDescription(description);
        }
    }

    public static void notEmpty(Object[] array, ErrorCode code) {
        if (ArrayUtils.isEmpty(array)) {
            throw new BusinessException(code);
        }
    }

    public static void notEmpty(Object[] array, ErrorCode code, String description) {
        if (ArrayUtils.isEmpty(array)) {
            throw new BusinessException(code)
                    .setDescription(description);
        }
    }

    public static void notEmpty(Object[] array, String message) {
        if (ArrayUtils.isEmpty(array)) {
            throw new BusinessException(message);
        }
    }

    public static void notEmpty(Object[] array, String message, String description) {
        if (ArrayUtils.isEmpty(array)) {
            throw new BusinessException(message)
                    .setDescription(description);
        }
    }

    public static void isEmpty(Collection<?> collection, ErrorCode code) {
        if (CollectionUtils.isNotEmpty(collection)) {
            throw new BusinessException(code);
        }
    }

    public static void isEmpty(Collection<?> collection, ErrorCode code, String description) {
        if (CollectionUtils.isNotEmpty(collection)) {
            throw new BusinessException(code)
                    .setDescription(description);
        }
    }

    public static void isEmpty(Collection<?> collection, String message) {
        if (CollectionUtils.isNotEmpty(collection)) {
            throw new BusinessException(message);
        }
    }

    public static void isEmpty(Collection<?> collection, String message, String description) {
        if (CollectionUtils.isNotEmpty(collection)) {
            throw new BusinessException(message)
                    .setDescription(description);
        }
    }

    public static void notEmpty(Collection<?> collection, ErrorCode code) {
        if (CollectionUtils.isEmpty(collection)) {
            throw new BusinessException(code);
        }
    }

    public static void notEmpty(Collection<?> collection, ErrorCode code, String description) {
        if (CollectionUtils.isEmpty(collection)) {
            throw new BusinessException(code)
                    .setDescription(description);
        }
    }

    public static void notEmpty(Collection<?> collection, String message) {
        if (CollectionUtils.isEmpty(collection)) {
            throw new BusinessException(message);
        }
    }

    public static void notEmpty(Collection<?> collection, String message, String description) {
        if (CollectionUtils.isEmpty(collection)) {
            throw new BusinessException(message)
                    .setDescription(description);
        }
    }

    public static void isEmpty(Map<?, ?> map, ErrorCode code) {
        if (MapUtils.isNotEmpty(map)) {
            throw new BusinessException(code);
        }
    }

    public static void isEmpty(Map<?, ?> map, ErrorCode code, String description) {
        if (MapUtils.isNotEmpty(map)) {
            throw new BusinessException(code)
                    .setDescription(description);
        }
    }

    public static void isEmpty(Map<?, ?> map, String message) {
        if (MapUtils.isNotEmpty(map)) {
            throw new BusinessException(message);
        }
    }

    public static void isEmpty(Map<?, ?> map, String message, String description) {
        if (MapUtils.isNotEmpty(map)) {
            throw new BusinessException(message)
                    .setDescription(description);
        }
    }

    public static void notEmpty(Map<?, ?> map, ErrorCode code) {
        if (MapUtils.isEmpty(map)) {
            throw new BusinessException(code);
        }
    }

    public static void notEmpty(Map<?, ?> map, ErrorCode code, String description) {
        if (MapUtils.isEmpty(map)) {
            throw new BusinessException(code)
                    .setDescription(description);
        }
    }

    public static void notEmpty(Map<?, ?> map, String message) {
        if (MapUtils.isEmpty(map)) {
            throw new BusinessException(message);
        }
    }

    public static void notEmpty(Map<?, ?> map, String message, String description) {
        if (MapUtils.isEmpty(map)) {
            throw new BusinessException(message)
                    .setDescription(description);
        }
    }

    public static void isEmpty(String text, ErrorCode code) {
        if (StringUtils.isNotEmpty(text)) {
            throw new BusinessException(code);
        }
    }

    public static void isEmpty(String text, ErrorCode code, String description) {
        if (StringUtils.isNotEmpty(text)) {
            throw new BusinessException(code)
                    .setDescription(description);
        }
    }

    public static void isEmpty(String text, String message) {
        if (StringUtils.isNotEmpty(text)) {
            throw new BusinessException(message);
        }
    }

    public static void isEmpty(String text, String message, String description) {
        if (StringUtils.isNotEmpty(text)) {
            throw new BusinessException(message)
                    .setDescription(description);
        }
    }

    public static void notEmpty(String text, ErrorCode code) {
        if (StringUtils.isEmpty(text)) {
            throw new BusinessException(code);
        }
    }

    public static void notEmpty(String text, ErrorCode code, String description) {
        if (StringUtils.isEmpty(text)) {
            throw new BusinessException(code)
                    .setDescription(description);
        }
    }

    public static void notEmpty(String text, String message) {
        if (StringUtils.isEmpty(text)) {
            throw new BusinessException(message);
        }
    }

    public static void notEmpty(String text, String message, String description) {
        if (StringUtils.isEmpty(text)) {
            throw new BusinessException(message)
                    .setDescription(description);
        }
    }

    public static void isBlank(String text, ErrorCode code) {
        if (StringUtils.isNotBlank(text)) {
            throw new BusinessException(code);
        }
    }

    public static void isBlank(String text, ErrorCode code, String description) {
        if (StringUtils.isNotBlank(text)) {
            throw new BusinessException(code)
                    .setDescription(description);
        }
    }

    public static void isBlank(String text, String message) {
        if (StringUtils.isNotBlank(text)) {
            throw new BusinessException(message);
        }
    }

    public static void isBlank(String text, String message, String description) {
        if (StringUtils.isNotBlank(text)) {
            throw new BusinessException(message)
                    .setDescription(description);
        }
    }

    public static void notBlank(String text, ErrorCode code) {
        if (StringUtils.isBlank(text)) {
            throw new BusinessException(code);
        }
    }

    public static void notBlank(String text, ErrorCode code, String description) {
        if (StringUtils.isBlank(text)) {
            throw new BusinessException(code)
                    .setDescription(description);
        }
    }

    public static void notBlank(String text, String message) {
        if (StringUtils.isBlank(text)) {
            throw new BusinessException(message);
        }
    }

    public static void notBlank(String text, String message, String description) {
        if (StringUtils.isBlank(text)) {
            throw new BusinessException(message)
                    .setDescription(description);
        }
    }

}
