package artoria.util;

import java.util.Collection;
import java.util.Map;

import static artoria.util.StringConstant.*;

/**
 * Assert, verify data state, if failure, will throw exception.
 * @author Kahle
 */
public class Assert {

    public static void state(boolean expression) {
        Assert.state(expression, "[Assertion failed] - " +
                "this state invariant must be true");
    }

    public static void state(boolean expression, String message) {
        if (!expression) {
            throw new IllegalStateException(message);
        }
    }

    public static void isTrue(boolean expression) {
        Assert.isTrue(expression, "[Assertion failed] - " +
                "this expression must be true");
    }

    public static void isTrue(boolean expression, String message) {
        if (!expression) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void isNull(Object object) {
        Assert.isNull(object, "[Assertion failed] - " +
                "the object argument must be null");
    }

    public static void isNull(Object object, String message) {
        if (object != null) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void notNull(Object object) {
        Assert.notNull(object, "[Assertion failed] - " +
                "this argument is required; it must not be null");
    }

    public static void notNull(Object object, String message) {
        if (object == null) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void notEmpty(Object[] array) {
        Assert.notEmpty(array, "[Assertion failed] - " +
                "this array must not be empty: it must contain at least 1 element");
    }

    public static void notEmpty(Object[] array, String message) {
        if (ArrayUtils.isEmpty(array)) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void notEmpty(Collection<?> collection) {
        Assert.notEmpty(collection, "[Assertion failed] - " +
                "this collection must not be empty: it must contain at least 1 element");
    }

    public static void notEmpty(Collection<?> collection, String message) {
        if (CollectionUtils.isEmpty(collection)) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void notEmpty(Map<?, ?> map) {
        Assert.notEmpty(map, "[Assertion failed] - " +
                "this map must not be empty; it must contain at least one entry");
    }

    public static void notEmpty(Map<?, ?> map, String message) {
        if (MapUtils.isEmpty(map)) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void notEmpty(String text) {
        Assert.notEmpty(text, "[Assertion failed] - " +
                "this String argument must have length; it must not be null or empty");
    }

    public static void notEmpty(String text, String message) {
        if (StringUtils.isEmpty(text)) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void notBlank(String text) {
        Assert.notBlank(text, "[Assertion failed] - " +
                "this String argument must have text; it must not be null, empty, or blank");
    }

    public static void notBlank(String text, String message) {
        if (StringUtils.isBlank(text)) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void noNullElements(Object[] array) {
        Assert.noNullElements(array, "[Assertion failed] - " +
                "this array must not contain any null elements");
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

    public static void doesNotContain(String textToSearch, String substring) {
        Assert.doesNotContain(textToSearch, substring, "[Assertion failed] - " +
                "this String argument must not contain the substring [" + substring + "]");
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
        Assert.notNull(superType, "Type to check against must not be null");
        if (subType == null || !superType.isAssignableFrom(subType)) {
            message = StringUtils.isNotBlank(message) ? message + BLANK_SPACE : EMPTY_STRING;
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
            message = StringUtils.isNotBlank(message) ? message + BLANK_SPACE : EMPTY_STRING;
            message += "Object of class [";
            message += obj != null ? obj.getClass().getName() : STRING_NULL;
            message += "] must be an instance of " + type;
            throw new IllegalArgumentException(message);
        }
    }

}
