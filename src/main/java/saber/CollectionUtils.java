package saber;

import java.lang.reflect.Method;
import java.security.SecureRandom;
import java.util.*;

public class CollectionUtils {
    private static final Random RANDOM = new SecureRandom();

    public static boolean isEmpty(Collection c) {
        return (c == null || c.isEmpty());
    }

    public static boolean isNotEmpty(Collection c) {
        return (c != null && !c.isEmpty());
    }

    public static boolean isEmpty(Map m) {
        return (m == null || m.isEmpty());
    }

    public static boolean isNotEmpty(Map m) {
        return (m != null && !m.isEmpty());
    }

    public static <T> void reverse(List<T> l) {
        for (int start = 0, end = l.size() - 1; start < end; start++, end--) {
            T temp = l.get(end);
            l.set(end, l.get(start));
            l.set(start, temp);
        }
    }

    public static <T> List<T> confuse(List<T> list) {
        for (int i = list.size() - 1; i > 1; --i) {
            int nextInt = RANDOM.nextInt(i);
            T tmp = list.get(nextInt);
            list.set(nextInt, list.get(i));
            list.set(i, tmp);
        }
        return list;
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
