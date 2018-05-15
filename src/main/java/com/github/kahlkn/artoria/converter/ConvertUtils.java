package com.github.kahlkn.artoria.converter;

import com.github.kahlkn.artoria.util.Assert;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

/**
 * Convert tools.
 * @author Kahle
 */
public class ConvertUtils {
    private static Logger log = Logger.getLogger(ConvertUtils.class.getName());
    private static final Map<Class<?>, Converter> CONVERTERS;

    static {
        CONVERTERS = new ConcurrentHashMap<Class<?>, Converter>();
        ConvertUtils.register(Date.class, new DateConverter());
        ConvertUtils.register(String.class, new StringConverter());
        ConvertUtils.register(Number.class, new NumberConverter());
        ConvertUtils.register(Object.class, new ObjectConverter());
    }

    public static Converter unregister(Class<?> clazz) {
        Assert.notNull(clazz, "Parameter \"clazz\" must not null. ");
        Converter remove = CONVERTERS.remove(clazz);
        log.info("Unregister: " + clazz.getName() + " >> " + remove.getClass().getName());
        return remove;
    }

    public static void register(Class<?> clazz, Converter converter) {
        Assert.notNull(clazz, "Parameter \"clazz\" must not null. ");
        Assert.notNull(converter, "Parameter \"converter\" must not null. ");
        CONVERTERS.put(clazz, converter);
        log.info("Register: " + clazz.getName() + " >> " + converter.getClass().getName());
    }

    public static Object convert(Object source, Class<?> target) {
        if (source == null) { return null; }
        for (Map.Entry<Class<?>, Converter> entry : CONVERTERS.entrySet()) {
            Class<?> clazz = source.getClass();
            if (target.isAssignableFrom(clazz)) {
                return source;
            }
            Class<?> cvtClass = entry.getKey();
            Converter converter = entry.getValue();
            if (cvtClass.isAssignableFrom(clazz)) {
                source = converter.convert(source, target);
            }
        }
        return source;
    }

}
