package com.github.kahlkn.artoria.util;

import com.github.kahlkn.artoria.converter.ConvertUtils;
import com.github.kahlkn.artoria.exception.UncheckedException;
import com.github.kahlkn.artoria.reflect.ReflectUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.github.kahlkn.artoria.util.Const.GET;

/**
 * Data handle tools.
 * @author Kahle
 */
public class DataUtils {

    public static <T> T takeFirstNotNullElement(List<T> list) {
        if (list == null) {
            return null;
        }
        for (T bean : list) {
            if (bean != null) {
                return bean;
            }
        }
        return null;
    }

    public static <T> List<List<T>> listToListList(List<T> list, int groupSize) {
        List<List<T>> result = new ArrayList<List<T>>();
        if (CollectionUtils.isEmpty(list)) { return result; }
        Assert.state(groupSize != 0, "Parameter \"groupSize\" must not equal 0. ");
        int listSize = list.size();
        int count = listSize / groupSize;
        count = listSize % groupSize == 0 ? count : ++count;
        List<T> tmp;
        for (int i = 0; i < count; i++) {
            int fromIndex = i * groupSize;
            int toIndex = (i + 1) * groupSize;
            toIndex = toIndex > listSize ? listSize : toIndex;
            tmp = new ArrayList<T>();
            tmp.addAll(list.subList(fromIndex, toIndex));
            result.add(tmp);
        }
        return result;
    }

    public static <T> Map<String, T> listToMapBean(List<T> list, String... propertyNames) {
        try {
            // Handle parameters
            Map<String, T> result = new HashMap<String, T>(list.size());
            if (CollectionUtils.isEmpty(list)) { return result; }
            Assert.notEmpty(propertyNames, "Parameter \"propertyNames\" must not empty. ");
            T tmp = DataUtils.takeFirstNotNullElement(list);
            Assert.notNull(tmp, "Elements in list all is null. ");
            List<Method> methods = DataUtils.findMethodsByNames(tmp.getClass(), propertyNames);
            // Convert to map
            StringBuilder key = new StringBuilder();
            for (T bean : list) {
                if (bean == null) { continue; }
                key.setLength(0);
                for (Method method : methods) {
                    Object val = method.invoke(bean);
                    key.append(val);
                }
                result.put(key.toString(), bean);
            }
            return result;
        }
        catch (Exception e) {
            throw new UncheckedException(e);
        }
    }

    public static <T> Map<String, List<T>> listToMapList(List<T> list, String... propertyNames) {
        try {
            // Handle parameters
            Map<String, List<T>> result = new HashMap<String, List<T>>(list.size());
            if (CollectionUtils.isEmpty(list)) { return result; }
            Assert.notEmpty(propertyNames, "Parameter \"propertyNames\" must not empty. ");
            T tmp = DataUtils.takeFirstNotNullElement(list);
            Assert.notNull(tmp, "Elements in list all is null. ");
            List<Method> methods = DataUtils.findMethodsByNames(tmp.getClass(), propertyNames);
            // Convert to map
            StringBuilder builder = new StringBuilder();
            for (T bean : list) {
                if (bean == null) { continue; }
                builder.setLength(0);
                for (Method method : methods) {
                    Object val = method.invoke(bean);
                    builder.append(val);
                }
                String key = builder.toString();
                List<T> val = result.get(key);
                if (val == null) {
                    val = new ArrayList<T>();
                    result.put(key, val);
                }
                val.add(bean);
            }
            return result;
        }
        catch (Exception e) {
            throw new UncheckedException(e);
        }
    }

    @SuppressWarnings("unchecked")
    public static <R, P> List<R> listToListProperty(List<P> list, String propertyName, Class<R> propertyClass) {
        try {
            // Handle parameters
            List<R> result = new ArrayList<R>();
            if (CollectionUtils.isEmpty(list)) { return result; }
            Assert.notBlank(propertyName, "Parameter \"propertyName\" must not blank. ");
            Assert.notNull(propertyClass, "Parameter \"propertyClass\" must not null. ");
            P tmp = DataUtils.takeFirstNotNullElement(list);
            Assert.notNull(tmp, "Elements in list all is null. ");
            Method method = DataUtils.findMethodByName(tmp.getClass(), propertyName);
            // Handle result
            for (P bean : list) {
                if (bean == null) { continue; }
                Object val = method.invoke(bean);
                val = ConvertUtils.convert(val, propertyClass);
                result.add((R) val);
            }
            return result;
        }
        catch (Exception e) {
            throw new UncheckedException(e);
        }
    }

    private static Method findMethodByName(Class<?> clazz, String propertyName) throws NoSuchMethodException {
        String methodName = GET + StringUtils.capitalize(propertyName);
        return ReflectUtils.findMethod(clazz, methodName);
    }

    private static List<Method> findMethodsByNames(Class<?> clazz, String... propertyNames) throws NoSuchMethodException {
        List<Method> methods = new ArrayList<Method>();
        for (String name : propertyNames) {
            methods.add(DataUtils.findMethodByName(clazz, name));
        }
        return methods;
    }

}
