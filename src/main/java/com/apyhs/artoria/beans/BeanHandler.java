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

    public static <T> T mapToBean(Map<String, Object> from, T to) {
        return beanToBean(from, to);
    }

    public static <T> T mapToBean(Map<String, Object> from, Class<T> toClass) {
        return beanToBean(from, toClass);
    }

    public static <F> Map<String, Object> beanToMap(F from) {
        HashMap<String, Object> result = new HashMap<String, Object>(MAP_INIT_CAPACITY);
        BeanUtils.copy(from, result);
        return result;
    }

    @SuppressWarnings("unchecked")
    public static <F, T> T beanToBean(F from, T to) {
        if (from instanceof Map) {
            BeanUtils.copy((Map<String, ?>) from, to);
        }
        else {
            BeanUtils.copy(from, to);
        }
        return to;
    }

    @SuppressWarnings("unchecked")
    public static <F, T> T beanToBean(F from, Class<T> toClass) {
        try {
            T to = toClass.newInstance();
            if (from instanceof Map) {
                BeanUtils.copy((Map<String, ?>) from, to);
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

    public static <T> List<T> mapToBeanInList(List<Map<String, Object>> from, Class<T> toClass) {
        List<T> result = new ArrayList<T>();
        for (Map<String, Object> m : from) {
            result.add(mapToBean(m, toClass));
        }
        return result;
    }

    public static <F> List<Map<String, Object>> beanToMapInList(List<F> from) {
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        for (F f : from) {
            result.add(beanToMap(f));
        }
        return result;
    }

    public static <F, T> List<T> beanToBeanInList(List<F> from, Class<T> toClass) {
        List<T> result = new ArrayList<T>();
        for (F f : from) {
            result.add(beanToBean(f, toClass));
        }
        return result;
    }

}
