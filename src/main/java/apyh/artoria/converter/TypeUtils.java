package apyh.artoria.converter;

import apyh.artoria.util.ArrayUtils;

import java.util.Collections;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Type tools.
 * @author Kahle
 */
public class TypeUtils {
    private static final Map<Class<?>, Converter> cvts;

    static {
        cvts = new ConcurrentHashMap<Class<?>, Converter>();
        cvts.put(String.class, new StringConverter());
        cvts.put(Number.class, new NumberConverter());
        cvts.put(Object.class, new SimpleConverter());
    }

    public static void registerConverter(Class<?> clazz, Converter converter) {
        cvts.put(clazz, converter);
    }

    public static Converter unregisterConverter(Class<?> clazz) {
        return cvts.remove(clazz);
    }

    public static Object convert(Object source, Class<?> target) {
        Class<?> clazz = source.getClass();
        LinkedList<Class<?>> list = new LinkedList<Class<?>>();
        list.addLast(clazz);
        while (list.size() != 0) {
            clazz = list.removeFirst();
            Converter converter = cvts.get(clazz);
            if (converter != null) {
                return converter.convert(source, target);
            }
            Class<?> superclass = clazz.getSuperclass();
            if (superclass != null) {
                list.addFirst(superclass);
            }
            Class<?>[] interfaces = clazz.getInterfaces();
            if (ArrayUtils.isNotEmpty(interfaces)) {
                Collections.addAll(list, interfaces);
            }
        }
        return source;
    }

}
