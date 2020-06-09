package artoria.convert.type;

import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import artoria.util.ArrayUtils;
import artoria.util.Assert;
import artoria.util.ClassUtils;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Type convert tools.
 * @author Kahle
 */
public class TypeConvertUtils {
    private static Logger log = LoggerFactory.getLogger(TypeConvertUtils.class);
    private static ConvertProvider convertProvider;

    public static ConvertProvider getConvertProvider() {
        if (convertProvider != null) { return convertProvider; }
        synchronized (TypeConvertUtils.class) {
            if (convertProvider != null) { return convertProvider; }
            TypeConvertUtils.setConvertProvider(new SimpleConvertProvider());
            return convertProvider;
        }
    }

    public static void setConvertProvider(ConvertProvider convertProvider) {
        Assert.notNull(convertProvider, "Parameter \"convertProvider\" must not null. ");
        log.info("Set type convert provider: {}", convertProvider.getClass().getName());
        TypeConvertUtils.convertProvider = convertProvider;
    }

    public static void register(Class<?> clazz, TypeConverter converter) {

        getConvertProvider().register(clazz, converter);
    }

    public static TypeConverter unregister(Class<?> clazz) {

        return getConvertProvider().unregister(clazz);
    }

    public static Object convert(Object source, Class<?> target) {

        return getConvertProvider().convert(source, target);
    }

    public interface ConvertProvider extends TypeConverter {

        void register(Class<?> clazz, TypeConverter converter);

        TypeConverter unregister(Class<?> clazz);

    }

    public static class SimpleConvertProvider implements ConvertProvider {
        private final Map<Class<?>, TypeConverter> converterMap;

        public SimpleConvertProvider() {
            converterMap = new ConcurrentHashMap<Class<?>, TypeConverter>();
            register(Object.class, new ObjectConverter());
            register(String.class, new StringConverter());
            register(Number.class, new NumberConverter());
            register(Date.class,   new DateConverter());
        }

        @Override
        public void register(Class<?> clazz, TypeConverter converter) {
            Assert.notNull(clazz, "Parameter \"clazz\" must not null. ");
            Assert.notNull(converter, "Parameter \"converter\" must not null. ");
            String converterClassName = converter.getClass().getName();
            String clazzName = clazz.getName();
            log.info("Register \"{}\" to \"{}\". ", converterClassName, clazzName);
            converterMap.put(clazz, converter);
        }

        @Override
        public TypeConverter unregister(Class<?> clazz) {
            Assert.notNull(clazz, "Parameter \"clazz\" must not null. ");
            TypeConverter remove = converterMap.remove(clazz);
            if (remove != null) {
                String removeClassName = remove.getClass().getName();
                String clazzName = clazz.getName();
                log.info("Unregister \"{}\" to \"{}\". ", removeClassName, clazzName);
            }
            return remove;
        }

        @Override
        public Object convert(Object source, Class<?> target) {
            if (source == null) { return null; }
            Class<?> clazz = source.getClass();
            target = ClassUtils.getWrapper(target);
            clazz = ClassUtils.getWrapper(clazz);
            if (target.isAssignableFrom(clazz)) { return source; }
            do {
                TypeConverter converter = converterMap.get(clazz);
                if (converter != null) {
                    source = converter.convert(source, target);
                    if (source == null) { return null; }
                    if (target.isAssignableFrom(source.getClass())) {
                        return source;
                    }
                }
                Class<?>[] interfaces = clazz.getInterfaces();
                if (ArrayUtils.isEmpty(interfaces)) { continue; }
                for (Class<?> inter : interfaces) {
                    converter = converterMap.get(inter);
                    if (converter == null) { continue; }
                    source = converter.convert(source, target);
                    if (target.isAssignableFrom(source.getClass())) {
                        return source;
                    }
                }
            } while ((clazz = clazz.getSuperclass()) != null);
            return source;
        }

    }

}
