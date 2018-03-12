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

    public static void state(boolean expression, ErrorCode code, String... descriptions) {
        if (!expression) {
            BusinessException ex = new BusinessException(code);
            throw Validate.handleDescription(ex, descriptions);
        }
    }

    public static void state(boolean expression, String message, String... descriptions) {
        if (!expression) {
            BusinessException ex = new BusinessException(message);
            throw Validate.handleDescription(ex, descriptions);
        }
    }

    public static void isFalse(boolean expression, ErrorCode code, String... descriptions) {
        if (expression) {
            BusinessException ex = new BusinessException(code);
            throw Validate.handleDescription(ex, descriptions);
        }
    }

    public static void isFalse(boolean expression, String message, String... descriptions) {
        if (expression) {
            BusinessException ex = new BusinessException(message);
            throw Validate.handleDescription(ex, descriptions);
        }
    }

    public static void isTrue(boolean expression, ErrorCode code, String... descriptions) {
        if (!expression) {
            BusinessException ex = new BusinessException(code);
            throw Validate.handleDescription(ex, descriptions);
        }
    }

    public static void isTrue(boolean expression, String message, String... descriptions) {
        if (!expression) {
            BusinessException ex = new BusinessException(message);
            throw Validate.handleDescription(ex, descriptions);
        }
    }

    public static void isNull(Object object, ErrorCode code, String... descriptions) {
        if (object != null) {
            BusinessException ex = new BusinessException(code);
            throw Validate.handleDescription(ex, descriptions);
        }
    }

    public static void isNull(Object object, String message, String... descriptions) {
        if (object != null) {
            BusinessException ex = new BusinessException(message);
            throw Validate.handleDescription(ex, descriptions);
        }
    }

    public static void notNull(Object object, ErrorCode code, String... descriptions) {
        if (object == null) {
            BusinessException ex = new BusinessException(code);
            throw Validate.handleDescription(ex, descriptions);
        }
    }

    public static void notNull(Object object, String message, String... descriptions) {
        if (object == null) {
            BusinessException ex = new BusinessException(message);
            throw Validate.handleDescription(ex, descriptions);
        }
    }

    public static void isEmpty(byte[] array, ErrorCode code, String... descriptions) {
        if (ArrayUtils.isNotEmpty(array)) {
            BusinessException ex = new BusinessException(code);
            throw Validate.handleDescription(ex, descriptions);
        }
    }

    public static void isEmpty(byte[] array, String message, String... descriptions) {
        if (ArrayUtils.isNotEmpty(array)) {
            BusinessException ex = new BusinessException(message);
            throw Validate.handleDescription(ex, descriptions);
        }
    }

    public static void notEmpty(byte[] array, ErrorCode code, String... descriptions) {
        if (ArrayUtils.isEmpty(array)) {
            BusinessException ex = new BusinessException(code);
            throw Validate.handleDescription(ex, descriptions);
        }
    }

    public static void notEmpty(byte[] array, String message, String... descriptions) {
        if (ArrayUtils.isEmpty(array)) {
            BusinessException ex = new BusinessException(message);
            throw Validate.handleDescription(ex, descriptions);
        }
    }

    public static void isEmpty(Object[] array, ErrorCode code, String... descriptions) {
        if (ArrayUtils.isNotEmpty(array)) {
            BusinessException ex = new BusinessException(code);
            throw Validate.handleDescription(ex, descriptions);
        }
    }

    public static void isEmpty(Object[] array, String message, String... descriptions) {
        if (ArrayUtils.isNotEmpty(array)) {
            BusinessException ex = new BusinessException(message);
            throw Validate.handleDescription(ex, descriptions);
        }
    }

    public static void notEmpty(Object[] array, ErrorCode code, String... descriptions) {
        if (ArrayUtils.isEmpty(array)) {
            BusinessException ex = new BusinessException(code);
            throw Validate.handleDescription(ex, descriptions);
        }
    }

    public static void notEmpty(Object[] array, String message, String... descriptions) {
        if (ArrayUtils.isEmpty(array)) {
            BusinessException ex = new BusinessException(message);
            throw Validate.handleDescription(ex, descriptions);
        }
    }

    public static void isEmpty(Collection<?> collection, ErrorCode code, String... descriptions) {
        if (CollectionUtils.isNotEmpty(collection)) {
            BusinessException ex = new BusinessException(code);
            throw Validate.handleDescription(ex, descriptions);
        }
    }

    public static void isEmpty(Collection<?> collection, String message, String... descriptions) {
        if (CollectionUtils.isNotEmpty(collection)) {
            BusinessException ex = new BusinessException(message);
            throw Validate.handleDescription(ex, descriptions);
        }
    }

    public static void notEmpty(Collection<?> collection, ErrorCode code, String... descriptions) {
        if (CollectionUtils.isEmpty(collection)) {
            BusinessException ex = new BusinessException(code);
            throw Validate.handleDescription(ex, descriptions);
        }
    }

    public static void notEmpty(Collection<?> collection, String message, String... descriptions) {
        if (CollectionUtils.isEmpty(collection)) {
            BusinessException ex = new BusinessException(message);
            throw Validate.handleDescription(ex, descriptions);
        }
    }

    public static void isEmpty(Map<?, ?> map, ErrorCode code, String... descriptions) {
        if (MapUtils.isNotEmpty(map)) {
            BusinessException ex = new BusinessException(code);
            throw Validate.handleDescription(ex, descriptions);
        }
    }

    public static void isEmpty(Map<?, ?> map, String message, String... descriptions) {
        if (MapUtils.isNotEmpty(map)) {
            BusinessException ex = new BusinessException(message);
            throw Validate.handleDescription(ex, descriptions);
        }
    }

    public static void notEmpty(Map<?, ?> map, ErrorCode code, String... descriptions) {
        if (MapUtils.isEmpty(map)) {
            BusinessException ex = new BusinessException(code);
            throw Validate.handleDescription(ex, descriptions);
        }
    }

    public static void notEmpty(Map<?, ?> map, String message, String... descriptions) {
        if (MapUtils.isEmpty(map)) {
            BusinessException ex = new BusinessException(message);
            throw Validate.handleDescription(ex, descriptions);
        }
    }

    public static void isEmpty(String text, ErrorCode code, String... descriptions) {
        if (StringUtils.isNotEmpty(text)) {
            BusinessException ex = new BusinessException(code);
            throw Validate.handleDescription(ex, descriptions);
        }
    }

    public static void isEmpty(String text, String message, String... descriptions) {
        if (StringUtils.isNotEmpty(text)) {
            BusinessException ex = new BusinessException(message);
            throw Validate.handleDescription(ex, descriptions);
        }
    }

    public static void notEmpty(String text, ErrorCode code, String... descriptions) {
        if (StringUtils.isEmpty(text)) {
            BusinessException ex = new BusinessException(code);
            throw Validate.handleDescription(ex, descriptions);
        }
    }

    public static void notEmpty(String text, String message, String... descriptions) {
        if (StringUtils.isEmpty(text)) {
            BusinessException ex = new BusinessException(message);
            throw Validate.handleDescription(ex, descriptions);
        }
    }

    public static void isBlank(String text, ErrorCode code, String... descriptions) {
        if (StringUtils.isNotBlank(text)) {
            BusinessException ex = new BusinessException(code);
            throw Validate.handleDescription(ex, descriptions);
        }
    }

    public static void isBlank(String text, String message, String... descriptions) {
        if (StringUtils.isNotBlank(text)) {
            BusinessException ex = new BusinessException(message);
            throw Validate.handleDescription(ex, descriptions);
        }
    }

    public static void notBlank(String text, ErrorCode code, String... descriptions) {
        if (StringUtils.isBlank(text)) {
            BusinessException ex = new BusinessException(code);
            throw Validate.handleDescription(ex, descriptions);
        }
    }

    public static void notBlank(String text, String message, String... descriptions) {
        if (StringUtils.isBlank(text)) {
            BusinessException ex = new BusinessException(message);
            throw Validate.handleDescription(ex, descriptions);
        }
    }

    private static BusinessException handleDescription(BusinessException ex, String... descriptions) {
        if (ArrayUtils.isNotEmpty(descriptions)) {
            StringBuilder builder = new StringBuilder();
            for (String description : descriptions) {
                builder.append(description);
            }
            ex.setDescription(builder);
        }
        return ex;
    }

}
