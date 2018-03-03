package com.apyhs.artoria.beans;

import com.apyhs.artoria.exception.UncheckedException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Bean and map convert tools.
 * @author Kahle
 */
class BeanHandler {
    private static final Integer MAP_INIT_CAPACITY = 20;

    public static <R, K, V> R mapToBean(Map<K, V> from, R to) {
        return beanToBean(from, to);
    }

    public static <R, K, V> R mapToBean(Map<K, V> from, Class<R> toClass) {
        return beanToBean(from, toClass);
    }

    public static <F> Map<String, Object> beanToMap(F from) {
        if (from == null) { return null; }
        Map<String, Object> result = new HashMap<String, Object>(MAP_INIT_CAPACITY);
        BeanUtils.copy(from, result);
        return result;
    }

    public static <F, T> T beanToBean(F from, T to) {
        if (from == null) { return null; }
        if (from instanceof Map) {
            BeanUtils.copy((Map) from, to);
        }
        else {
            BeanUtils.copy(from, to);
        }
        return to;
    }

    public static <F, T> T beanToBean(F from, Class<T> toClass) {
        if (from == null) { return null; }
        try {
            T to = toClass.newInstance();
            if (from instanceof Map) {
                BeanUtils.copy((Map) from, to);
            }
            else {
                BeanUtils.copy(from, to);
            }
            return to;
        }
        catch (Exception e) {
            throw new UncheckedException(e);
        }
    }

    public static <R, K, V> List<R> mapToBeanInList(List<Map<K, V>> from, Class<R> toClass) {
        if (from == null) { return null; }
        List<R> result = new ArrayList<R>();
        for (Map<K, V> m : from) {
            result.add(mapToBean(m, toClass));
        }
        return result;
    }

    public static <F> List<Map<String, Object>> beanToMapInList(List<F> from) {
        if (from == null) { return null; }
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        for (F f : from) {
            result.add(beanToMap(f));
        }
        return result;
    }

    public static <F, T> List<T> beanToBeanInList(List<F> from, Class<T> toClass) {
        if (from == null) { return null; }
        List<T> result = new ArrayList<T>();
        for (F f : from) {
            result.add(beanToBean(f, toClass));
        }
        return result;
    }

}
