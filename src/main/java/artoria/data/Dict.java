package artoria.data;

import artoria.convert.ConversionUtils;
import artoria.util.ArrayUtils;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * The dictionary is a mutable container model used to hold data with mapped relationships.
 * @author Kahle
 */
public class Dict extends LinkedHashMap<Object, Object> {

    public static Dict of() {

        return new Dict();
    }

    public static Dict of(Object key, Object value) {

        return of().set(key, value);
    }

    public static Dict of(Dict dict) {

        return of().set(dict);
    }

    public static Dict of(Map<?, ?> map) {

        return of().set(map);
    }

    public Dict set(Object key, Object value) {
        put(key, value);
        return this;
    }

    public Dict set(Dict dict) {
        if (dict != null) {
            putAll(dict);
        }
        return this;
    }

    public Dict set(Map<?, ?> map) {
        if (map != null) {
            putAll(map);
        }
        return this;
    }

    public Dict delete(Object... keys) {
        if (ArrayUtils.isEmpty(keys)) { return this; }
        for (Object key : keys) { remove(key); }
        return this;
    }

    public <T> T get(Object key, Class<T> clazz) {
        Object result = get(key);
        if (result == null) { return null; }
        return ConversionUtils.convert(result, clazz);
    }

    public <T> T get(Object key, Class<T> clazz, T defaultValue) {
        T result = get(key, clazz);
        return result != null ? result : defaultValue;
    }

    public Byte getByte(Object key) {

        return get(key, Byte.class);
    }

    public byte getByte(Object key, byte defaultValue) {

        return get(key, Byte.class, defaultValue);
    }

    public Short getShort(Object key) {

        return get(key, Short.class);
    }

    public short getShort(Object key, short defaultValue) {

        return get(key, Short.class, defaultValue);
    }

    public Integer getInteger(Object key) {

        return get(key, Integer.class);
    }

    public int getInteger(Object key, int defaultValue) {

        return get(key, Integer.class, defaultValue);
    }

    public Long getLong(Object key) {

        return get(key, Long.class);
    }

    public long getLong(Object key, long defaultValue) {

        return get(key, Long.class, defaultValue);
    }

    public Float getFloat(Object key) {

        return get(key, Float.class);
    }

    public float getFloat(Object key, float defaultValue) {

        return get(key, Float.class, defaultValue);
    }

    public Double getDouble(Object key) {

        return get(key, Double.class);
    }

    public double getDouble(Object key, double defaultValue) {

        return get(key, Double.class, defaultValue);
    }

    public Boolean getBoolean(Object key) {

        return get(key, Boolean.class);
    }

    public boolean getBoolean(Object key, boolean defaultValue) {

        return get(key, Boolean.class, defaultValue);
    }

    public String getString(Object key) {

        return get(key, String.class);
    }

    public String getString(Object key, String defaultValue) {

        return get(key, String.class, defaultValue);
    }

    public Dict getDict(Object key) {
        Object result = get(key);
        if (result == null) { return null; }
        if (result instanceof Dict) {
            return (Dict) result;
        }
        if (result instanceof Map) {
            return Dict.of((Map) result);
        }
        throw new IllegalStateException("The data type is not supported. ");
    }

}
