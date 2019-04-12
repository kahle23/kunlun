package artoria.random;

import artoria.converter.TypeConvertUtils;
import artoria.exception.ExceptionUtils;
import artoria.reflect.ReflectUtils;
import artoria.util.ClassUtils;
import artoria.util.NumberUtils;

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
public class DefaultRandomizer implements Randomizer {
    private static final char[] DEFAULT_CHAR_ARRAY = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
    private static final Long TIME_INTERVAL = 6 * 60 * 60 * 1000L;
    private static volatile Long lastUpdatedTime = 0L;
    private static final Integer DEFAULT_BOUND = 8192;
    private static final Integer DEFAULT_SIZE = 8;
    private static Random random;

    private Random getRandom() {
        if ((System.currentTimeMillis() - lastUpdatedTime) <= TIME_INTERVAL) {
            return random;
        }
        synchronized (this) {
            if ((System.currentTimeMillis() - lastUpdatedTime) <= TIME_INTERVAL) {
                return random;
            }
            random = new SecureRandom();
            lastUpdatedTime = System.currentTimeMillis();
            return random;
        }
    }

    @Override
    public <T> T[] confuse(T[] arr) {
        for (int i = arr.length - 1; i > 1; --i) {
            int nextInt = this.getRandom().nextInt(i);
            T tmp = arr[nextInt];
            arr[nextInt] = arr[i];
            arr[i] = tmp;
        }
        return arr;
    }

    @Override
    public <T> List<T> confuse(List<T> list) {
        for (int i = list.size() - 1; i > 1; --i) {
            int nextInt = this.getRandom().nextInt(i);
            T tmp = list.get(nextInt);
            list.set(nextInt, list.get(i));
            list.set(i, tmp);
        }
        return list;
    }

    @Override
    public int nextInt() {

        return this.getRandom().nextInt();
    }

    @Override
    public int nextInt(int bound) {

        return this.getRandom().nextInt(bound);
    }

    @Override
    public long nextLong() {

        return this.getRandom().nextLong();
    }

    @Override
    public float nextFloat() {

        return this.getRandom().nextFloat();
    }

    @Override
    public double nextDouble() {

        return this.getRandom().nextDouble();
    }

    @Override
    public boolean nextBoolean() {

        return this.getRandom().nextBoolean();
    }

    @Override
    public void nextBytes(byte[] bytes) {

        this.getRandom().nextBytes(bytes);
    }

    @Override
    public byte[] nextBytes(int length) {
        byte[] bytes = new byte[length];
        this.getRandom().nextBytes(bytes);
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
            int nextInt = this.getRandom().nextInt(len);
            result.append(charArray[nextInt]);
        }
        return result.toString();
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T nextObject(Class<T> clazz) {
        Class<?> wrapper = ClassUtils.getWrapper(clazz);
        boolean isUnsupported = wrapper.isArray()
                || List.class.isAssignableFrom(wrapper)
                || Map.class.isAssignableFrom(wrapper);
        if (isUnsupported) {
            throw new UnsupportedOperationException("\"List\", \"Map\" and array is unsupported. ");
        }
        if (Number.class.isAssignableFrom(wrapper)) {
            double num = this.getRandom().nextDouble() * DEFAULT_BOUND;
            num = NumberUtils.round(num).doubleValue();
            return (T) TypeConvertUtils.convert(num, wrapper);
        }
        if (Boolean.class.isAssignableFrom(wrapper)) {
            return (T) (Object) this.nextBoolean();
        }
        if (Character.class.isAssignableFrom(wrapper)) {
            int index = this.nextInt(DEFAULT_CHAR_ARRAY.length);
            return (T) (Object) DEFAULT_CHAR_ARRAY[index];
        }
        if (Date.class.isAssignableFrom(wrapper)) {
            return (T) TypeConvertUtils.convert(new Date(), wrapper);
        }
        if (String.class.isAssignableFrom(wrapper)) {
            int size = this.nextInt(DEFAULT_SIZE);
            return (T) this.nextString(++size);
        }
        if (Object.class.equals(wrapper)) {
            return (T) new Object();
        }
        try {
            return this.generateObject(clazz);
        }
        catch (Exception e) {
            throw ExceptionUtils.wrap(e);
        }
    }

    protected <T> T generateObject(Class<T> clazz) throws Exception {
        Map<String, Method> methods = ReflectUtils.findWriteMethods(clazz);
        T bean = ReflectUtils.newInstance(clazz);
        for (Map.Entry<String, Method> entry : methods.entrySet()) {
            Method method = entry.getValue();
            Type type = method.getGenericParameterTypes()[0];
            Object val;
            if (type instanceof ParameterizedType) {
                ParameterizedType realType = (ParameterizedType) type;
                Class<?> rawType = (Class<?>) realType.getRawType();
                Type[] args = realType.getActualTypeArguments();
                boolean hasArgs = args != null;
                boolean isMap = Map.class.isAssignableFrom(rawType);
                isMap = isMap && hasArgs && args.length >= 2;
                boolean isList = List.class.isAssignableFrom(rawType);
                isList = isList && hasArgs && args.length >= 1;
                boolean isArr = rawType.isArray();
                val = isMap || isList || isArr ? null : this.nextObject(rawType);
            }
            else {
                val = this.nextObject((Class<?>) type);
            }
            method.invoke(bean, val);
        }
        return bean;
    }

}
