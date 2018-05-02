package com.github.kahlkn.artoria.util;

import com.github.kahlkn.artoria.converter.ConvertUtils;
import com.github.kahlkn.artoria.exception.ExceptionUtils;
import com.github.kahlkn.artoria.reflect.ReflectUtils;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.security.SecureRandom;
import java.util.*;

import static com.github.kahlkn.artoria.util.Const.MINUS;

/**
 * Random tools.
 * @author Kahle
 */
@SuppressWarnings("unchecked")
public class RandomUtils {
    private static final char[] DEFAULT_CHAR_ARRAY = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
    private static final Random RANDOM = new SecureRandom();
    private static final Integer DEFAULT_BOUND = 8192;
    private static final Integer DEFAULT_SIZE = 8;
    private static final Integer COLLECTION_NEST_COUNT = 3;
    private static final String METHOD_NEXT_ARRAY = "nextArray";
    private static final String METHOD_NEXT_LIST = "nextList";
    private static final String METHOD_NEXT_MAP = "nextMap";

    /**
     * Confuse array elements.
     * @param arr A array will be confuse
     * @param <T> Array element type
     * @return A array reference inputted
     */
    public static <T> T[] confuse(T[] arr) {
        for (int i = arr.length - 1; i > 1; --i) {
            int nextInt = RANDOM.nextInt(i);
            T tmp = arr[nextInt];
            arr[nextInt] = arr[i];
            arr[i] = tmp;
        }
        return arr;
    }

    /**
     * Confuse list elements.
     * @param list A list will be confuse
     * @param <T> List element type
     * @return A list reference inputted
     */
    public static <T> List<T> confuse(List<T> list) {
        for (int i = list.size() - 1; i > 1; --i) {
            int nextInt = RANDOM.nextInt(i);
            T tmp = list.get(nextInt);
            list.set(nextInt, list.get(i));
            list.set(i, tmp);
        }
        return list;
    }

    /**
     * Random generation UUID.
     * @return A UUID string
     */
    public static String nextUUID() {
        return UUID.randomUUID().toString();
    }

    /**
     * Random generation UUID.
     * @param separator UUID separator
     * @return A UUID string
     */
    public static String nextUUID(String separator) {
        String uuid = UUID.randomUUID().toString();
        if (separator != null && !MINUS.equals(separator)) {
            return StringUtils.replace(uuid, MINUS, separator);
        }
        else {
            return uuid;
        }
    }

    /**
     * Random generation boolean.
     * @return A random boolean
     */
    public static boolean nextBoolean() {
        return RANDOM.nextBoolean();
    }

    /**
     * Random generation int.
     * @param bound The int value bound
     * @return A random int
     */
    public static int nextInt(int bound) {
        return RANDOM.nextInt(bound);
    }

    /**
     * Random generation byte array.
     * @param length The byte array length
     * @return A random byte array
     */
    public static byte[] nextBytes(int length) {
        byte[] bytes = new byte[length];
        RANDOM.nextBytes(bytes);
        return bytes;
    }

    /**
     * Random generation string.
     * @param length The string length
     * @return A random string
     */
    public static String nextString(int length) {
        return RandomUtils.nextString(DEFAULT_CHAR_ARRAY, length);
    }

    /**
     * Random generation string.
     * @param charArray Candidate char array
     * @param length The string length
     * @return A random string
     */
    public static String nextString(char[] charArray, int length) {
        StringBuilder result = new StringBuilder();
        int len = charArray.length;
        for (int i = 0; i < length; i++) {
            int nextInt = RANDOM.nextInt(len);
            result.append(charArray[nextInt]);
        }
        return result.toString();
    }

    /**
     * Random generation decimal.
     * @param length The decimal length
     * @return A random decimal
     */
    public static BigDecimal nextBigDecimal(int length) {
        StringBuilder result = new StringBuilder();
        result.append(RANDOM.nextInt(9) + 1);
        int len = length - 1;
        for (int i = 0; i < len; i++) {
            result.append(RANDOM.nextInt(10));
        }
        return new BigDecimal(result.toString());
    }

    /**
     * Random generation object who type is specified.
     * @param clazz The object class
     * @param <T> The object type
     * @return A random object
     */
    public static <T> T nextObject(Class<T> clazz) {
        Class<?> wrapper = ClassUtils.getWrapper(clazz);
        if (Number.class.isAssignableFrom(wrapper)) {
            double num = RANDOM.nextDouble() * DEFAULT_BOUND;
            num = NumberUtils.round(num);
            return (T) ConvertUtils.convert(num, wrapper);
        }
        else if (Boolean.class.isAssignableFrom(wrapper)) {
            return (T) (Object) RandomUtils.nextBoolean();
        }
        else if (Character.class.isAssignableFrom(wrapper)) {
            int index = RandomUtils.nextInt(DEFAULT_CHAR_ARRAY.length);
            return (T) (Object) DEFAULT_CHAR_ARRAY[index];
        }
        else if (Date.class.isAssignableFrom(wrapper)) {
            return (T) ConvertUtils.convert(new Date(), wrapper);
        }
        else if (String.class.isAssignableFrom(wrapper)) {
            int size = RandomUtils.nextInt(DEFAULT_SIZE);
            return (T) RandomUtils.nextString(++ size);
        }
        else if (Object.class.equals(wrapper)) {
            return (T) new Object();
        }
        else if (wrapper.isArray()) {
            Class<?> componentType = wrapper.getComponentType();
            return (T) RandomUtils.nextArray(componentType);
        }
        else if (List.class.isAssignableFrom(wrapper)) {
            return (T) new ArrayList(DEFAULT_SIZE);
        }
        else if (Map.class.isAssignableFrom(wrapper)) {
            return (T) new HashMap(DEFAULT_SIZE);
        }
        else {
            return (T) RandomUtils.nextBean(wrapper);
        }
    }

    /**
     * Random generation bean who type is specified.
     * @param clazz The bean class
     * @param <T> The bean type
     * @return A random bean
     */
    public static <T> T nextBean(Class<T> clazz) {
        try {
            Object bean = ReflectUtils.newInstance(clazz);
            Map<String, Method> methods = ReflectUtils.findWriteMethods(clazz);
            for (Map.Entry<String, Method> entry : methods.entrySet()) {
                Method method = entry.getValue();
                Type type = method.getGenericParameterTypes()[0];
                Object val;
                if (type instanceof ParameterizedType) {
                    ParameterizedType realType = (ParameterizedType) type;
                    Type[] args = realType.getActualTypeArguments();
                    Class<?> rawType = (Class<?>) realType.getRawType();
                    boolean hasArgs = args != null;
                    boolean isMap = Map.class.isAssignableFrom(rawType);
                    isMap = isMap && hasArgs && args.length >= 2;
                    boolean isList = List.class.isAssignableFrom(rawType);
                    isList = isList && hasArgs && args.length >= 1;
                    val = isMap ? RandomUtils.nextMap((Class<?>) args[0]
                            , (Class<?>) args[1]) : isList
                            ? RandomUtils.nextList((Class<?>) args[0])
                            : RandomUtils.nextObject(rawType);
                }
                else {
                    val = RandomUtils.nextObject((Class<?>) type);
                }
                method.invoke(bean, val);
            }
            return (T) bean;
        }
        catch (Exception e) {
            throw ExceptionUtils.wrap(e);
        }
    }

    /**
     * Random generation array who type is specified.
     * @param clazz The array class
     * @param <T> The array type
     * @return A random array
     */
    public static <T> T[] nextArray(Class<T> clazz) {
        int size = RandomUtils.nextInt(DEFAULT_SIZE);
        T[] array = (T[]) Array.newInstance(clazz, size);
        StackTraceElement[] elements;
        boolean isEmpty = Object.class.equals(clazz) ||
                ((elements = new Throwable().getStackTrace()).length >= COLLECTION_NEST_COUNT &&
                        METHOD_NEXT_ARRAY.equals(elements[COLLECTION_NEST_COUNT].getMethodName()));
        if (isEmpty) { return array; }
        for (int i = 0; i < size; i++) {
            Object obj = RandomUtils.nextObject(clazz);
            Array.set(array, i, obj);
        }
        return array;
    }

    /**
     * Random generation list who generic type is specified.
     * @param clazz The list generic type class
     * @param <T> The list generic type
     * @return A random list
     */
    public static <T> List<T> nextList(Class<T> clazz) {
        StackTraceElement[] elements;
        boolean isEmpty = Object.class.equals(clazz) ||
                ((elements = new Throwable().getStackTrace()).length >= COLLECTION_NEST_COUNT &&
                        METHOD_NEXT_LIST.equals(elements[COLLECTION_NEST_COUNT].getMethodName()));
        if (isEmpty) { return new ArrayList<T>(DEFAULT_SIZE); }
        int size = RandomUtils.nextInt(DEFAULT_SIZE);
        List<T> list = new ArrayList<T>(size);
        for (int i = 0; i < size; i++) {
            list.add(RandomUtils.nextObject(clazz));
        }
        return list;
    }

    /**
     * Random generation map who generic type is specified.
     * @param keyClass The map key class
     * @param valClass The map value class
     * @param <K> The map key type
     * @param <V> The map value type
     * @return A random map
     */
    public static <K, V> Map<K, V> nextMap(Class<K> keyClass, Class<V> valClass) {
        StackTraceElement[] elements;
        boolean isEmpty = Object.class.equals(keyClass) || Object.class.equals(valClass) ||
                ((elements = new Throwable().getStackTrace()).length >= COLLECTION_NEST_COUNT &&
                        METHOD_NEXT_MAP.equals(elements[COLLECTION_NEST_COUNT].getMethodName()));
        if (isEmpty) { return new HashMap<K, V>(DEFAULT_SIZE); }
        int size = RandomUtils.nextInt(DEFAULT_SIZE);
        Map<K, V> map = new HashMap<K, V>(size);
        for (int i = 0; i < size; i++) {
            K key = RandomUtils.nextObject(keyClass);
            V val = RandomUtils.nextObject(valClass);
            map.put(key, val);
        }
        return map;
    }

}
