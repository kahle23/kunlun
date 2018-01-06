package com.apyhs.artoria.beans;

import com.apyhs.artoria.converter.Converter;
import com.apyhs.artoria.converter.ConvertUtils;
import com.apyhs.artoria.exception.ReflectionException;
import com.apyhs.artoria.serialize.SerializeUtils;
import com.apyhs.artoria.util.Assert;
import com.apyhs.artoria.util.ReflectUtils;
import com.apyhs.artoria.util.StringUtils;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Map;

import static com.apyhs.artoria.util.StringConstant.STRING_GET;
import static com.apyhs.artoria.util.StringConstant.STRING_SET;

/**
 * Bean tools.
 * @author Kahle
 */
public class BeanUtils {
    private static final Integer GET_OR_SET_LENGTH = 3;

    private static final Converter CONVERTER = new Converter() {
        @Override
        public Object convert(Object source, Class<?> target) {
            return ConvertUtils.convert(source, target);
        }
    };

    public static <T> T ifNull(T value, T defaultValue) {
        return value != null ? value : defaultValue;
    }

    public static boolean isNull(Object obj) {
        return obj == null;
    }

    public static boolean isEmpty(Object obj) {
        if (obj == null) {
            return true;
        }
        if (obj.getClass().isArray()) {
            return Array.getLength(obj) == 0;
        }
        if (obj instanceof CharSequence) {
            return ((CharSequence) obj).length() == 0;
        }
        if (obj instanceof Collection) {
            return ((Collection) obj).isEmpty();
        }
        if (obj instanceof Map) {
            return ((Map) obj).isEmpty();
        }
        return false;
    }

    public static boolean isArray(Object obj) {
        return obj != null && obj.getClass().isArray();
    }

    public static Object clone(Object obj) throws ReflectionException {
        Class<?> clazz = obj.getClass();
        Object clone = ReflectUtils.newInstance(clazz);
        BeanUtils.copyProperties(obj, clone);
        return clone;
    }

    public static Object deepClone(Object obj) {
        byte[] data = SerializeUtils.serialize(obj);
        return SerializeUtils.deserialize(data);
    }

    public static void copyProperties(Object src, Map<String, Object> dest) {
        Assert.notNull(src, "Source must is not null. ");
        Assert.notNull(dest, "Destination must is not null. ");
        Class<?> clazz = src.getClass();
        Map<String, Method> mths = ReflectUtils.findAllGetterAndSetter(clazz);
        for (Map.Entry<String, Method> entry : mths.entrySet()) {
            String name = entry.getKey();
            if (!name.startsWith(STRING_GET)) {
                continue;
            }
            name = name.substring(GET_OR_SET_LENGTH);
            name = StringUtils.uncapitalize(name);
            Method mth = entry.getValue();
            try {
                Object invoke = mth.invoke(src);
                dest.put(name, invoke);
            }
            catch (Exception e) { /*ignore*/ }
        }
    }

    public static void copyProperties(Map<String, ?> src, Object dest) {
        BeanUtils.copyProperties(src, dest, CONVERTER);
    }

    public static void copyProperties(Map<String, ?> src, Object dest, Converter cvt) {
        Assert.notNull(src, "Source must is not null. ");
        Assert.notNull(dest, "Destination must is not null. ");
        Class<?> clazz = dest.getClass();
        Map<String, Method> mths = ReflectUtils.findAllGetterAndSetter(clazz);
        for (Map.Entry<String, ?> entry : src.entrySet()) {
            String name = entry.getKey();
            if (!name.startsWith(STRING_SET)) {
                name = STRING_SET + StringUtils.capitalize(name);
            }
            Method mth = mths.get(name);
            if (mth == null) { continue; }
            Class<?> dType = mth.getParameterTypes()[0];
            Object input = entry.getValue();
            // do convert
            input = cvt != null ? cvt.convert(input, dType) : input;
            try { mth.invoke(dest, input); }
            catch (Exception e) { /*ignore*/ }
        }
    }

    public static void copyProperties(Object src, Object dest) {
        BeanUtils.copyProperties(src, dest, CONVERTER);
    }

    public static void copyProperties(Object src, Object dest, Converter cvt) {
        Assert.notNull(src, "Source must is not null. ");
        Assert.notNull(dest, "Destination must is not null. ");
        Class<?> srcClass = src.getClass();
        Class<?> destClass = dest.getClass();
        Map<String, Method> srcMths = ReflectUtils.findAllGetterAndSetter(srcClass);
        Map<String, Method> destMths = ReflectUtils.findAllGetterAndSetter(destClass);
        for (Map.Entry<String, Method> entry : srcMths.entrySet()) {
            String name = entry.getKey();
            if (!name.startsWith(STRING_GET)) { continue; }
            name = name.substring(GET_OR_SET_LENGTH);
            name = STRING_SET + name;
            Method destMth = destMths.get(name);
            if (destMth == null) { continue; }
            Method srcMth = entry.getValue();
            try {
                Object input = srcMth.invoke(src);
                Class<?> dType = destMth.getParameterTypes()[0];
                // do convert
                input = cvt != null ? cvt.convert(input, dType) : input;
                destMth.invoke(dest, input);
            }
            catch (Exception e) { /*ignore*/ }
        }
    }

}
