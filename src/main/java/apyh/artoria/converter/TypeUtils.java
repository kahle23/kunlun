package apyh.artoria.converter;

import apyh.artoria.util.ArrayUtils;
import apyh.artoria.util.Assert;

import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Type tools.
 * @author Kahle
 */
public class TypeUtils {
    private static final Map<Class<?>, Converter> CONVERTERS;

    static {
        CONVERTERS = new ConcurrentHashMap<Class<?>, Converter>();
        CONVERTERS.put(Date.class, new DateConverter());
        CONVERTERS.put(String.class, new StringConverter());
        CONVERTERS.put(Number.class, new NumberConverter());
        CONVERTERS.put(Object.class, new ObjectConverter());
    }

    public static Converter unregisterConverter(Class<?> clazz) {
        Assert.notNull(clazz, "Clazz must is not null. ");
        return CONVERTERS.remove(clazz);
    }

    public static void registerConverter(Class<?> clazz, Converter converter) {
        Assert.notNull(clazz, "Clazz must is not null. ");
        Assert.notNull(converter, "Converter must is not null. ");
        CONVERTERS.put(clazz, converter);
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
