package artoria.util;

import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class BeanUtils {

    public static <T> T val(T value, T defaultValue) {
        return value != null ? value : defaultValue;
    }

    public static boolean isNull(Object obj) {
        return obj == null;
    }

    public static boolean isArray(Object obj) {
        return (obj != null && obj.getClass().isArray());
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

    public static Object clone(Object obj)
            throws ReflectiveOperationException {
        return Reflect.on(obj).call("clone");
    }

    public static Object deepClone(Object obj)
            throws IOException, ClassNotFoundException {
        byte[] data = SerializeUtils.serialize(obj);
        return SerializeUtils.deserialize(data);
    }



    public static Map beanToMap(Object o)
            throws ReflectiveOperationException {
        Map<Object, Object> result = new HashMap<>();
        Class<?> clazz = o.getClass();
        Method[] methods = clazz.getMethods();
        for (Method method : methods) {
            String name = method.getName();
            if (method.isAccessible() &&
                    name.length() > 3 &&
                    name.indexOf("get") == 0) {
                name = "get" + StringUtils.capitalize(name);
                result.put(name, method.invoke(o));
            }
        }
        return result;
    }

    public static <T> T mapToBean(Map<?, ?> m, Class<T> clazz)
            throws ReflectiveOperationException {
        T bean = clazz.newInstance();
        for (Map.Entry<?, ?> entry : m.entrySet()) {
            String name = entry.getKey() + "";
            if (!StringUtils.hasText(name)) continue;
            Object value = entry.getValue();
            name = "set" + StringUtils.capitalize(name);
            Method method = clazz.getMethod(name, value.getClass());
            if (!method.isAccessible()) continue;
            method.invoke(bean, value);
        }
        return bean;
    }
}
