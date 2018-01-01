package com.apyhs.artoria.converter;

import com.apyhs.artoria.logging.Logger;
import com.apyhs.artoria.logging.LoggerFactory;
import com.apyhs.artoria.util.ArrayUtils;
import com.apyhs.artoria.util.Assert;

import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Type tools.
 * @author Kahle
 */
public class ConvertUtils {
    private static final Logger log = LoggerFactory.getLogger(ConvertUtils.class);

    private static final Map<Class<?>, Converter> CONVERTERS;

    static {
        CONVERTERS = new ConcurrentHashMap<Class<?>, Converter>();
        ConvertUtils.register(Date.class, new DateConverter());
        ConvertUtils.register(String.class, new StringConverter());
        ConvertUtils.register(Number.class, new NumberConverter());
        ConvertUtils.register(Object.class, new ObjectConverter());
    }

    public static Converter unregister(Class<?> clazz) {
        Assert.notNull(clazz, "Clazz must is not null. ");
        Converter remove = CONVERTERS.remove(clazz);
        log.info("Unregister: " + clazz.getName() + " >> " + remove.getClass().getName());
        return remove;
    }

    public static void register(Class<?> clazz, Converter converter) {
        Assert.notNull(clazz, "Clazz must is not null. ");
        Assert.notNull(converter, "Converter must is not null. ");
        CONVERTERS.put(clazz, converter);
        log.info("Register: " + clazz.getName() + " >> " + converter.getClass().getName());
    }

    public static Object convert(Object source, Class<?> target) {
        Class<?> clazz = source.getClass();
        LinkedList<Class<?>> list = new LinkedList<Class<?>>();
        list.addLast(clazz);
        while (list.size() != 0) {
            clazz = list.removeFirst();
            Converter converter = CONVERTERS.get(clazz);
            if (converter != null) {
                return converter.convert(source, target);
            }
            Class<?>[] interfaces = clazz.getInterfaces();
            if (ArrayUtils.isNotEmpty(interfaces)) {
                Collections.addAll(list, interfaces);
            }
            Class<?> superclass = clazz.getSuperclass();
            if (superclass != null) {
                list.addLast(superclass);
            }
        }
        return source;
    }

}
