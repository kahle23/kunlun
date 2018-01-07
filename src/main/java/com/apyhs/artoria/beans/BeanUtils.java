package com.apyhs.artoria.beans;

import com.apyhs.artoria.converter.ConvertUtils;
import com.apyhs.artoria.converter.Converter;
import com.apyhs.artoria.exception.UncheckedException;
import com.apyhs.artoria.serialize.SerializeUtils;
import com.apyhs.artoria.util.Assert;
import com.apyhs.artoria.util.ReflectUtils;
import com.apyhs.artoria.util.StringUtils;

import java.lang.reflect.Method;
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

    private static BeanCopier beanCopier = new JdkBeanCopier();

    public static Object clone(Object obj) {
        try {
            Class<?> clazz = obj.getClass();
            Object clone = ReflectUtils.newInstance(clazz);
            BeanUtils.copy(obj, clone);
            return clone;
        }
        catch (Exception e) {
            throw new UncheckedException(e);
        }
    }

    public static Object deepClone(Object obj) {
        byte[] data = SerializeUtils.serialize(obj);
        return SerializeUtils.deserialize(data);
    }

    public static void copy(Object from, Map<String, Object> to) {
        Assert.notNull(from, "Source must is not null. ");
        Assert.notNull(to, "Destination must is not null. ");
        Class<?> clazz = from.getClass();
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
                Object invoke = mth.invoke(from);
                to.put(name, invoke);
            }
            catch (Exception e) { /*ignore*/ }
        }
    }

    public static void copy(Map<String, ?> from, Object to) {
        BeanUtils.copy(from, to, CONVERTER);
    }

    public static void copy(Map<String, ?> from, Object to, Converter cvt) {
        Assert.notNull(from, "Source must is not null. ");
        Assert.notNull(to, "Destination must is not null. ");
        Class<?> clazz = to.getClass();
        Map<String, Method> mths = ReflectUtils.findAllGetterAndSetter(clazz);
        for (Map.Entry<String, ?> entry : from.entrySet()) {
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
            try { mth.invoke(to, input); }
            catch (Exception e) { /*ignore*/ }
        }
    }

    public static void copy(Object from, Object to) {
        BeanUtils.copy(from, to, CONVERTER);
    }

    public static void copy(Object from, Object to, Converter cvt) {
        beanCopier.copy(from, to, null, cvt);
    }

}
