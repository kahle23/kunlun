package artoria.util;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;

import static artoria.common.constant.Numbers.ZERO;

/**
 * The object tools.
 * @author Kahle
 */
public class ObjectUtils {

    public static boolean isNull(Object obj) {

        return obj == null;
    }

    public static <T> T ifNull(T value, T defaultValue) {

        return value != null ? value : defaultValue;
    }

    public static boolean isArray(Object obj) {

        return obj != null && obj.getClass().isArray();
    }

    public static boolean isEmpty(Object obj) {
        if (obj == null) {
            return true;
        }
        if (obj.getClass().isArray()) {
            return Array.getLength(obj) == ZERO;
        }
        if (obj instanceof CharSequence) {
            return ((CharSequence) obj).length() == ZERO;
        }
        if (obj instanceof Collection) {
            //noinspection rawtypes
            return ((Collection) obj).isEmpty();
        }
        if (obj instanceof Map) {
            //noinspection rawtypes
            return ((Map) obj).isEmpty();
        }
        return false;
    }

    public static boolean equals(Object obj1, Object obj2) {

        return obj1 == null ? obj2 == null : obj1.equals(obj2);
    }

    @SuppressWarnings("unchecked")
    public static <T> T cast(Object object) {

        return (T) object;
    }

    @SuppressWarnings("unchecked")
    public static <T> T cast(Object object, Class<T> clazz) {

        return (T) object;
    }

}
