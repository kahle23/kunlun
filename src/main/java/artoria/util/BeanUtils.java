package artoria.util;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

import static artoria.util.StringConstant.STRING_GET;
import static artoria.util.StringConstant.STRING_SET;

public class BeanUtils {

    public static <T> T ifNull(T value, T defaultValue) {
        return value != null ? value : defaultValue;
    }

    public static boolean isNull(Object obj) {
        return obj == null;
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

    public static boolean isArray(Object obj) {
        return obj != null && obj.getClass().isArray();
    }

    public static Object clone(Object obj) {
        Class<?> clazz = obj.getClass();
        return null;
    }

    public static Object deepClone(Object obj) {
        byte[] data = SerializeUtils.serialize(obj);
        return SerializeUtils.deserialize(data);
    }

    public static void copyProperties(Map<String, ?> src, Object dest) {
    }

    public static void copyProperties(Object src, Map<String, ?> dest) {
    }

    public static void copyProperties(Object src, Object dest) {
    }

    static Map<String, Method> findAllGetterOrSetterMethods(Class<?> clazz) {
        Assert.notNull(clazz, "Clazz must is not null. ");
        Map<String, Method> result = new HashMap<String, Method>();
        do {
            Method[] methods = clazz.getMethods();
            for (Method method : methods) {
                String name = method.getName();
                int pSize = method.getParameterTypes().length;
                int mod = method.getModifiers();
                boolean isStc = Modifier.isStatic(mod);
                boolean stGet = name.startsWith(STRING_GET);
                boolean stSet = name.startsWith(STRING_SET);
                boolean b = isStc || !stGet || !stSet;
                b = b || (stGet && pSize != 0);
                b = b || (stSet && pSize != 1);
                if (b) { continue; }
                result.put(name, method);
            }
            clazz = clazz.getSuperclass();
        }
        while (clazz != null);
        return result;
    }

}
