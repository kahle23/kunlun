package apyh.artoria.util;

import java.util.Collection;
import java.util.Map;

/**
 * Assert, verify data state, if failure, will throw exception.
 * @author Kahle
 */
public class Assert {

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

    public static void notEmpty(Object[] array, String message) {
        if (ArrayUtils.isEmpty(array)) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void notEmpty(Collection<?> collection, String message) {
        if (CollectionUtils.isEmpty(collection)) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void notEmpty(Map<?, ?> map, String message) {
        if (MapUtils.isEmpty(map)) {
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

    public static void noNullElements(Object[] array, String message) {
        if (array != null) {
            for (Object element : array) {
                if (element == null) {
                    throw new IllegalArgumentException(message);
                }
            }
        }
    }

    public static void doesNotContain(String textToSearch, String substring, String message) {
        if (StringUtils.isNotEmpty(textToSearch)
                && StringUtils.isNotEmpty(substring)
                && textToSearch.contains(substring)) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void isAssignable(Class<?> superType, Class<?> subType) {
        Assert.isAssignable(superType, subType, null);
    }

    public static void isAssignable(Class<?> superType, Class<?> subType, String message) {
        Assert.notNull(superType, "Super type must is not null. ");
        if (subType == null || !superType.isAssignableFrom(subType)) {
            message = StringUtils.isNotBlank(message) ? message + StringConstant.BLANK_SPACE : StringConstant.EMPTY_STRING;
            message += subType + " is not assignable to " + superType;
            throw new IllegalArgumentException(message);
        }
    }

    public static void isInstanceOf(Class<?> type, Object obj) {
        Assert.isInstanceOf(type, obj, null);
    }

    public static void isInstanceOf(Class<?> type, Object obj, String message) {
        Assert.notNull(type, "Type to check against must not be null");
        if (!type.isInstance(obj)) {
            message = StringUtils.isNotBlank(message) ? message + StringConstant.BLANK_SPACE : StringConstant.EMPTY_STRING;
            message += "Object of class [";
            message += obj != null ? obj.getClass().getName() : StringConstant.STRING_NULL;
            message += "] must be an instance of " + type;
            throw new IllegalArgumentException(message);
        }
    }

}
