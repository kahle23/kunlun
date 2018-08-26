package artoria.util;

import artoria.exception.ExceptionUtils;
import artoria.reflect.ReflectUtils;

import java.lang.reflect.Method;
import java.util.*;

import static artoria.common.Constants.GET;

/**
 * Array tools.
 * @author Kahle
 */
public class ArrayUtils {

    /**
     * Take java bean array first not null element.
     * @param arr A java bean array
     * @param <T> Java bean type
     * @return A not null java bean
     */
    public static <T> T takeFirstNotNullElement(T[] arr) {
        if (arr == null) {
            return null;
        }
        for (T bean : arr) {
            if (bean != null) {
                return bean;
            }
        }
        return null;
    }

    public static boolean equal(byte[] bytes1, byte[] bytes2) {
        if (bytes1 == null || bytes2 == null) {
            return bytes1 == null && bytes2 == null;
        }
        if (bytes1.length != bytes2.length) {
            return false;
        }
        for (int i = 0; i < bytes1.length; i++) {
            if (bytes1[i] != bytes2[i]) { return false; }
        }
        return true;
    }

    public static boolean isEmpty(byte[] arr) {
        return arr == null || arr.length == 0;
    }

    public static boolean isNotEmpty(byte[] arr) {
        return arr != null && arr.length > 0;
    }

    public static byte[] reverse(byte[] arr) {
        Assert.notNull(arr, "Parameter \"arr\" must not null. ");
        for (int start = 0, end = arr.length - 1; start < end; start++, end--) {
            byte temp = arr[end];
            arr[end] = arr[start];
            arr[start] = temp;
        }
        return arr;
    }

    public static <T> boolean equal(T[] ts1, T[] ts2) {
        if (ts1 == null || ts2 == null) {
            return ts1 == null && ts2 == null;
        }
        if (ts1.length != ts2.length) {
            return false;
        }
        for (int i = 0; i < ts1.length; i++) {
            if (!ts1[i].equals(ts2[i])) { return false; }
        }
        return true;
    }

    public static <T> boolean isEmpty(T[] arr) {
        return arr == null || arr.length == 0;
    }

    public static <T> boolean isNotEmpty(T[] arr) {
        return arr != null && arr.length > 0;
    }

    public static <T> T[] reverse(T[] arr) {
        Assert.notNull(arr, "Parameter \"arr\" must not null. ");
        for (int start = 0, end = arr.length - 1; start < end; start++, end--) {
            T temp = arr[end];
            arr[end] = arr[start];
            arr[start] = temp;
        }
        return arr;
    }

    @SuppressWarnings("unchecked")
    public static <T> void sort(T[] arr, boolean isAsc, String... properties) {
        if (ArrayUtils.isEmpty(arr) || ArrayUtils.isEmpty(properties)) {
            return;
        }
        T bean = ArrayUtils.takeFirstNotNullElement(arr);
        Assert.notNull(bean, "Elements in array parameter \"arr\" all is null. ");
        Class<?> clazz = bean.getClass();
        final boolean sortAsc = isAsc;
        final List<Method> methods = new ArrayList<Method>();
        Map<String, Method> readMethods = ReflectUtils.findReadMethods(clazz);
        for (String property : properties) {
            property = GET + StringUtils.capitalize(property);
            Method method = readMethods.get(property);
            if (method != null) { methods.add(method); }
        }
        Arrays.sort(arr, new Comparator<T>() {
            @Override
            public int compare(T o1, T o2) {
                if (o1 == null || o2 == null) {
                    return sortAsc ?
                            // Asc null element in first.
                            o1 == null ? o2 == null ? 0 : -1 : 1 :
                            // Desc null element in last.
                            o1 == null ? o2 == null ? 0 : 1 : -1;
                }
                try {
                    for (Method method : methods) {
                        Object res1 = method.invoke(o1);
                        Object res2 = method.invoke(o2);
                        if (res1 == null || res2 == null) {
                            // Maybe res1 or res2 is null.
                            // So it is can not compare, next.
                            continue;
                        }
                        // Method return type maybe is different.
                        if (res1.getClass().equals(res2.getClass())
                                && res1 instanceof Comparable
                                && res2 instanceof Comparable) {
                            return sortAsc ?
                                    ((Comparable) res1).compareTo(res2) :
                                    ((Comparable) res2).compareTo(res1);
                        }
                    }
                    return 0;
                }
                catch (Exception e) {
                    throw ExceptionUtils.wrap(e);
                }
            }
        });
    }

}
