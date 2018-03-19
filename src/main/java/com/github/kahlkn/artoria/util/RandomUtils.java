package com.github.kahlkn.artoria.util;

import com.github.kahlkn.artoria.aop.Enhancer;
import com.github.kahlkn.artoria.aop.Interceptor;
import com.github.kahlkn.artoria.converter.ConvertUtils;

import java.lang.reflect.*;
import java.math.BigDecimal;
import java.security.SecureRandom;
import java.util.*;

import static com.github.kahlkn.artoria.util.Const.*;

/**
 * Random tools.
 * @author Kahle
 */
public class RandomUtils {
    private static final char[] DEFAULT_CHAR_ARRAY = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
    private static final Random RANDOM = new SecureRandom();
    private static final Integer DEFAULT_BOUND = 8192;
    private static final Integer DEFAULT_SIZE = 8;
    private static final Integer MAX_COLLECTION_NEST = 3;

    public static <T> T[] confuse(T[] arr) {
        for (int i = arr.length - 1; i > 1; --i) {
            int nextInt = RANDOM.nextInt(i);
            T tmp = arr[nextInt];
            arr[nextInt] = arr[i];
            arr[i] = tmp;
        }
        return arr;
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

    public static String nextUUID() {
        return UUID.randomUUID().toString();
    }

    public static String nextUUID(String separator) {
        String uuid = UUID.randomUUID().toString();
        if (separator != null && !MINUS.equals(separator)) {
            return StringUtils.replace(uuid, MINUS, separator);
        }
        else {
            return uuid;
        }
    }

    public static int nextInt() {
        return RandomUtils.nextInt(DEFAULT_BOUND);
    }

    public static int nextInt(int bound) {
        return RANDOM.nextInt(bound);
    }

    public static byte[] nextBytes(int length) {
        byte[] bytes = new byte[length];
        RANDOM.nextBytes(bytes);
        return bytes;
    }

    public static String nextString(int length) {
        return RandomUtils.nextString(DEFAULT_CHAR_ARRAY, length);
    }

    public static String nextString(char[] charArray, int length) {
        StringBuilder result = new StringBuilder();
        int len = charArray.length;
        for (int i = 0; i < length; i++) {
            int nextInt = RANDOM.nextInt(len);
            result.append(charArray[nextInt]);
        }
        return result.toString();
    }

    public static BigDecimal nextBigDecimal(int length) {
        StringBuilder result = new StringBuilder();
        result.append(RANDOM.nextInt(9) + 1);
        int len = length - 1;
        for (int i = 0; i < len; i++) {
            result.append(RANDOM.nextInt(10));
        }
        return new BigDecimal(result.toString());
    }

    public static <T> List<T> nextList(Class<T> clazz) {
        if (Object.class.equals(clazz)) {
            return new ArrayList<T>(DEFAULT_SIZE);
        }
        int size = RandomUtils.nextInt(DEFAULT_SIZE) + 2;
        List<T> list = new ArrayList<T>(size);
        for (int i = 0; i < size; i++) {
            list.add(RandomUtils.nextObject(clazz));
        }
        return list;
    }

    public static <K, V> Map<K, V> nextMap(Class<K> keyClass, Class<V> valClass) {
        if (Object.class.equals(keyClass) || Object.class.equals(valClass)) {
            return new HashMap<K, V>(DEFAULT_SIZE);
        }
        int size = RandomUtils.nextInt(DEFAULT_SIZE) + 2;
        Map<K, V> map = new HashMap<K, V>(size);
        for (int i = 0; i < size; i++) {
            K key = RandomUtils.nextObject(keyClass);
            V val = RandomUtils.nextObject(valClass);
            map.put(key, val);
        }
        return map;
    }

    @SuppressWarnings("unchecked")
    public static <T> T nextObject(Class<T> clazz) {
        Class<?> wrapper = ClassUtils.getWrapper(clazz);
        if (Number.class.isAssignableFrom(wrapper)) {
            double num = RANDOM.nextDouble() * DEFAULT_BOUND;
            num = NumberUtils.round(num);
            return (T) ConvertUtils.convert(num, wrapper);
        }
        else if (Boolean.class.isAssignableFrom(wrapper)) {
            Object obj = RandomUtils.nextInt() % 2 == 0;
            return (T) obj;
        }
        else if (Character.class.isAssignableFrom(wrapper)) {
            int index = RandomUtils.nextInt(DEFAULT_CHAR_ARRAY.length);
            Object obj = DEFAULT_CHAR_ARRAY[index];
            return (T) obj;
        }
        else if (Date.class.isAssignableFrom(wrapper)) {
            Object obj = ConvertUtils.convert(new Date(), wrapper);
            return (T) obj;
        }
        else if (String.class.isAssignableFrom(wrapper)) {
            int size = RandomUtils.nextInt(DEFAULT_SIZE) + 2;
            Object obj = RandomUtils.nextString(size);
            return (T) obj;
        }
        else if (Object.class.equals(wrapper)) {
            return (T) new Object();
        }
        else if (wrapper.isArray()) {
            Class<?> componentType = wrapper.getComponentType();
            int size = RandomUtils.nextInt(DEFAULT_SIZE) + 2;
            T array = (T) Array.newInstance(componentType, size);
            for (int i = 0; i < size; i++) {
                Object obj = RandomUtils.nextObject(componentType);
                Array.set(array, i, obj);
            }
            return array;
        }
        else if (List.class.isAssignableFrom(wrapper)) {
            return (T) new ArrayList(DEFAULT_SIZE);
        }
        else if (Map.class.isAssignableFrom(wrapper)) {
            return (T) new HashMap(DEFAULT_SIZE);
        }
        else {
            return (T) Enhancer.enhance(wrapper, new BeanInterceptor());
        }
    }

    private static Object nextMethodResult(Type type) {
        if (type instanceof Class) {
            return RandomUtils.nextObject((Class) type);
        }
        else if (type instanceof ParameterizedType) {
            ParameterizedType realType = (ParameterizedType) type;
            Type[] arguments = realType.getActualTypeArguments();
            Class<?> returnType = (Class<?>) realType.getRawType();
            if (Map.class.isAssignableFrom(returnType)) {
                if ( null != arguments && arguments.length == 2 ) {
                    return RandomUtils.nextMap((Class) arguments[0], (Class) arguments[1]);
                }
                else {
                    return new HashMap();
                }
            }
            else if (List.class.isAssignableFrom(returnType)) {
                if ( null != arguments && arguments.length == 1 ) {
                    return RandomUtils.nextList((Class) arguments[0]);
                }
                else {
                    return new ArrayList();
                }
            }
            else {
                return RandomUtils.nextObject(returnType);
            }
        }
        else if (type instanceof TypeVariable) {
            return new Object();
        }
        else {
            return null;
        }
    }

    private static class BeanInterceptor implements Interceptor {
        Map<String, Object> beanMap = new HashMap<String, Object>();

        @Override
        public Object intercept(Object proxyObject, Method method, Object[] args) throws Throwable {
            String methodName = method.getName();
            boolean checkGet = methodName.startsWith(GET);
            boolean checkSet = methodName.startsWith(SET);
            boolean checkLen = methodName.length() > GET_OR_SET_LENGTH;
            if ((checkGet || checkSet) & checkLen) {
                String property = methodName.substring(GET_OR_SET_LENGTH);
                property = StringUtils.uncapitalize(property);
                if (checkGet) {
                    Object val = beanMap.get(property);
                    if (val == null) {
                        Type returnType = method.getGenericReturnType();
                        val = RandomUtils.nextMethodResult(returnType);
                        beanMap.put(property, val);
                    }
                    return val;
                }
                Object val = args != null && args.length > 0 ? args[0] : null;
                if (val != null) { return beanMap.put(property, val); }
            }
            if (Modifier.isStatic(method.getModifiers())) {
                return method.invoke(null, args);
            }
            return null;
        }

    }

}
