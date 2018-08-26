package artoria.random;

import artoria.converter.TypeConvertUtils;
import artoria.exception.ExceptionUtils;
import artoria.reflect.ReflectUtils;
import artoria.util.ClassUtils;
import artoria.util.NumberUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.security.SecureRandom;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Random generator simple implement by jdk.
 * @author Kahle
 */
public class SimpleRandomizer implements Randomizer {
    private static final char[] DEFAULT_CHAR_ARRAY = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
    private static final Long TIME_INTERVAL = 6 * 60 * 60 * 1000L;
    private static final Integer DEFAULT_BOUND = 8192;
    private static final Integer DEFAULT_SIZE = 8;
    private static volatile Long lastTime = 0L;
    private static Random random;

    @SuppressWarnings("unchecked")
    private <T> T handleOtherClass(Class<T> clazz) throws NoSuchMethodException
            , InstantiationException, IllegalAccessException, InvocationTargetException {
        Object bean = ReflectUtils.newInstance(clazz);
        Map<String, Method> methods = ReflectUtils.findWriteMethods(clazz);
        for (Map.Entry<String, Method> entry : methods.entrySet()) {
            Method method = entry.getValue();
            Type type = method.getGenericParameterTypes()[0];
            Object val;
            if (type instanceof ParameterizedType) {
                ParameterizedType realType = (ParameterizedType) type;
                Class<?> rawType = (Class<?>) realType.getRawType();
                // Type[] args = realType.getActualTypeArguments();
                // boolean hasArgs = args != null;
                boolean isMap = Map.class.isAssignableFrom(rawType);
                // isMap = isMap && hasArgs && args.length >= 2;
                boolean isList = List.class.isAssignableFrom(rawType);
                // isList = isList && hasArgs && args.length >= 1;
                boolean isArr = rawType.isArray();
                val = isMap || isList || isArr ? null : RandomUtils.nextObject(rawType);
            }
            else {
                val = RandomUtils.nextObject((Class<?>) type);
            }
            method.invoke(bean, val);
        }
        return (T) bean;
    }

    private Random takeRandom() {
        if ((System.currentTimeMillis() - lastTime) <= TIME_INTERVAL) {
            return random;
        }
        synchronized (this) {
            if ((System.currentTimeMillis() - lastTime) <= TIME_INTERVAL) {
                return random;
            }
            random = new SecureRandom();
            lastTime = System.currentTimeMillis();
            return random;
        }
    }

    @Override
    public <T> T[] confuse(T[] arr) {
        for (int i = arr.length - 1; i > 1; --i) {
            int nextInt = this.takeRandom().nextInt(i);
            T tmp = arr[nextInt];
            arr[nextInt] = arr[i];
            arr[i] = tmp;
        }
        return arr;
    }

    @Override
    public <T> List<T> confuse(List<T> list) {
        for (int i = list.size() - 1; i > 1; --i) {
            int nextInt = this.takeRandom().nextInt(i);
            T tmp = list.get(nextInt);
            list.set(nextInt, list.get(i));
            list.set(i, tmp);
        }
        return list;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T nextObject(Class<T> clazz) {
        Class<?> wrapper = ClassUtils.getWrapper(clazz);
        if (Number.class.isAssignableFrom(wrapper)) {
            double num = this.takeRandom().nextDouble() * DEFAULT_BOUND;
            num = NumberUtils.round(num).doubleValue();
            return (T) TypeConvertUtils.convert(num, wrapper);
        }
        if (Boolean.class.isAssignableFrom(wrapper)) {
            return (T) (Object) RandomUtils.nextBoolean();
        }
        if (Character.class.isAssignableFrom(wrapper)) {
            int index = RandomUtils.nextInt(DEFAULT_CHAR_ARRAY.length);
            return (T) (Object) DEFAULT_CHAR_ARRAY[index];
        }
        if (Date.class.isAssignableFrom(wrapper)) {
            return (T) TypeConvertUtils.convert(new Date(), wrapper);
        }
        if (String.class.isAssignableFrom(wrapper)) {
            int size = RandomUtils.nextInt(DEFAULT_SIZE);
            return (T) RandomUtils.nextString(++size);
        }
        if (Object.class.equals(wrapper)) {
            return (T) new Object();
        }
        if (wrapper.isArray()
                || List.class.isAssignableFrom(wrapper)
                || Map.class.isAssignableFrom(wrapper)) {
            throw new UnsupportedOperationException("\"List\", \"Map\" and array is unsupported. ");
        }
        try {
            return this.handleOtherClass(clazz);
        }
        catch (Exception e) {
            throw ExceptionUtils.wrap(e);
        }
    }

    @Override
    public int nextInt() {

        return this.takeRandom().nextInt();
    }

    @Override
    public int nextInt(int bound) {

        return this.takeRandom().nextInt(bound);
    }

    @Override
    public long nextLong() {

        return this.takeRandom().nextLong();
    }

    @Override
    public float nextFloat() {

        return this.takeRandom().nextFloat();
    }

    @Override
    public double nextDouble() {

        return this.takeRandom().nextDouble();
    }

    @Override
    public boolean nextBoolean() {

        return this.takeRandom().nextBoolean();
    }

    @Override
    public void nextBytes(byte[] bytes) {

        this.takeRandom().nextBytes(bytes);
    }

    @Override
    public byte[] nextBytes(int length) {
        byte[] bytes = new byte[length];
        this.takeRandom().nextBytes(bytes);
        return bytes;
    }

    @Override
    public String nextString(int length) {

        return this.nextString(DEFAULT_CHAR_ARRAY, length);
    }

    @Override
    public String nextString(char[] charArray, int length) {
        StringBuilder result = new StringBuilder();
        int len = charArray.length;
        for (int i = 0; i < length; i++) {
            int nextInt = this.takeRandom().nextInt(len);
            result.append(charArray[nextInt]);
        }
        return result.toString();
    }

}
