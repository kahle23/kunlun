package artoria.validate;

import artoria.exception.BusinessException;
import artoria.exception.ErrorCode;
import artoria.util.ArrayUtils;
import artoria.util.CollectionUtils;
import artoria.util.MapUtils;
import artoria.util.StringUtils;

import java.util.Collection;
import java.util.Map;

/**
 * Validate tools.
 * @author Kahle
 */
public class ValidateUtils {

    public static void state(boolean expression, ErrorCode errorCode) {
        if (!expression) {
            throw new BusinessException(errorCode);
        }
    }

    public static void isFalse(boolean expression, ErrorCode errorCode) {
        if (expression) {
            throw new BusinessException(errorCode);
        }
    }

    public static void isTrue(boolean expression, ErrorCode errorCode) {
        if (!expression) {
            throw new BusinessException(errorCode);
        }
    }

    public static void isNull(Object object, ErrorCode errorCode) {
        if (object != null) {
            throw new BusinessException(errorCode);
        }
    }

    public static void notNull(Object object, ErrorCode errorCode) {
        if (object == null) {
            throw new BusinessException(errorCode);
        }
    }

    public static void isEmpty(byte[] array, ErrorCode errorCode) {
        if (ArrayUtils.isNotEmpty(array)) {
            throw new BusinessException(errorCode);
        }
    }

    public static void notEmpty(byte[] array, ErrorCode errorCode) {
        if (ArrayUtils.isEmpty(array)) {
            throw new BusinessException(errorCode);
        }
    }

    public static void isEmpty(Object[] array, ErrorCode errorCode) {
        if (ArrayUtils.isNotEmpty(array)) {
            throw new BusinessException(errorCode);
        }
    }

    public static void notEmpty(Object[] array, ErrorCode errorCode) {
        if (ArrayUtils.isEmpty(array)) {
            throw new BusinessException(errorCode);
        }
    }

    public static void isEmpty(Collection<?> collection, ErrorCode errorCode) {
        if (CollectionUtils.isNotEmpty(collection)) {
            throw new BusinessException(errorCode);
        }
    }

    public static void notEmpty(Collection<?> collection, ErrorCode errorCode) {
        if (CollectionUtils.isEmpty(collection)) {
            throw new BusinessException(errorCode);
        }
    }

    public static void isEmpty(Map<?, ?> map, ErrorCode errorCode) {
        if (MapUtils.isNotEmpty(map)) {
            throw new BusinessException(errorCode);
        }
    }

    public static void notEmpty(Map<?, ?> map, ErrorCode errorCode) {
        if (MapUtils.isEmpty(map)) {
            throw new BusinessException(errorCode);
        }
    }

    public static void isEmpty(String text, ErrorCode errorCode) {
        if (StringUtils.isNotEmpty(text)) {
            throw new BusinessException(errorCode);
        }
    }

    public static void notEmpty(String text, ErrorCode errorCode) {
        if (StringUtils.isEmpty(text)) {
            throw new BusinessException(errorCode);
        }
    }

    public static void isBlank(String text, ErrorCode errorCode) {
        if (StringUtils.isNotBlank(text)) {
            throw new BusinessException(errorCode);
        }
    }

    public static void notBlank(String text, ErrorCode errorCode) {
        if (StringUtils.isBlank(text)) {
            throw new BusinessException(errorCode);
        }
    }

    public static void isContain(String textToSearch, String substring, ErrorCode errorCode) {
        if (!textToSearch.contains(substring)) {
            throw new BusinessException(errorCode);
        }
    }

    public static void notContain(String textToSearch, String substring, ErrorCode errorCode) {
        if (textToSearch.contains(substring)) {
            throw new BusinessException(errorCode);
        }
    }

    public static void isAssignable(Class<?> superType, Class<?> subType, ErrorCode errorCode) {
        if (subType == null || !superType.isAssignableFrom(subType)) {
            throw new BusinessException(errorCode);
        }
    }

    public static void isInstanceOf(Class<?> type, Object object, ErrorCode errorCode) {
        if (!type.isInstance(object)) {
            throw new BusinessException(errorCode);
        }
    }

    public static void noNullElements(Object[] array, ErrorCode errorCode) {
        for (Object element : array) {
            if (element == null) {
                throw new BusinessException(errorCode);
            }
        }
    }

}
