package artoria.util;

import artoria.exception.ReflectionException;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

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

    public static void copyProperties(Map<String, ?> src, Object dest) throws ReflectionException {
        try {
            Class<?> clazz = dest.getClass();
            Map<String, Method> methods = BeanUtils.findAllGetOrSetMethods(clazz);
            for (Map.Entry<String, ?> entry : src.entrySet()) {
                String name = entry.getKey();
                if (!name.startsWith(STRING_SET)) {
                    name = STRING_SET + StringUtils.capitalize(name);
                }
                Method method = methods.get(name);
                if (method == null) { continue; }
                Class<?> destType = method.getParameterTypes()[0];
                Object inputValue = entry.getValue();
                Class<?> inputType = inputValue.getClass();
                // TODO: do type conver
                try {
                    method.invoke(dest, inputValue);
                }
                catch (Exception e) {
                    // ignore
                }
            }
        }
        catch (Exception e) {
            throw new ReflectionException(e);
        }
    }

    public static void copyProperties(Object src, Map<String, Object> dest) throws ReflectionException {
        try {
            Class<?> clazz = src.getClass();
            Map<String, Method> methods = BeanUtils.findAllGetOrSetMethods(clazz);
            for (Map.Entry<String, Method> entry : methods.entrySet()) {
                String name = entry.getKey();
                if (!name.startsWith(STRING_GET)) { continue; }
                name = name.substring(3);
                Method method = entry.getValue();
                try {
                    Object invoke = method.invoke(src);
                    dest.put(name, invoke);
                }
                catch (Exception e) {
                    // ignore
                }
            }
        }
        catch (Exception e) {
            throw new ReflectionException(e);
        }
    }

    public static void copyProperties(Object src, Object dest) {
    }

    static Map<String, Method> findAllGetOrSetMethods(Class<?> clazz) {
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
                boolean b = isStc || (!stGet && !stSet);
                // has get and parameters not equal 0
                b = b || (stGet && pSize != 0);
                // has set and parameters not equal 1
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
