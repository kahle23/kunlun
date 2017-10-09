package saber;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;

public class ObjectUtils {

    public static <T> T val(T value, T defaultValue) {
        return value != null ? value : defaultValue;
    }

    public static boolean isNull(Object obj) {
        return obj == null;
    }

    public static boolean isArray(Object obj) {
        return (obj != null && obj.getClass().isArray());
    }

    public static boolean isEmpty(Object[] array) {
        return (array == null || array.length == 0);
    }

    public static boolean isEmpty(Object obj) {
        if (obj == null) {
            return true;
        }

        if (obj.getClass().isArray()) {
            return Array.getLength(obj) == 0;
        }
        if (obj instanceof CharSequence) {
            return ((CharSequence) obj).length() == 0;
        }
        if (obj instanceof Collection) {
            return ((Collection) obj).isEmpty();
        }
        if (obj instanceof Map) {
            return ((Map) obj).isEmpty();
        }

        // else
        return false;
    }

    public static Object simpleClone(Object obj)
            throws ReflectiveOperationException {
        return Reflect.on(obj).call("clone");
    }

    public static Object deepClone(Object obj)
            throws IOException, ClassNotFoundException {
        byte[] data = SerializeUtils.serialize(obj);
        return SerializeUtils.deserialize(data);
    }

}
