package artoria.util;

import artoria.lang.BasicType;

import java.net.URI;
import java.net.URL;
import java.util.Date;
import java.util.Locale;

/**
 * The class tools.
 * @author Kahle
 */
public class ClassUtils {

    public static Class<?> getPrimitive(Class<?> type) {

        return BasicType.getPrimitive(type);
    }

    public static Class<?> getWrapper(Class<?> type) {

        return BasicType.getWrapper(type);
    }

    public static boolean isPresent(String className) {

        return ClassUtils.isPresent(className, null);
    }

    public static boolean isPresent(String className, ClassLoader classLoader) {

        return ClassUtils.isPresent(className, Boolean.FALSE, classLoader);
    }

    public static boolean isPresent(String className, boolean initialize, ClassLoader classLoader) {
        Assert.notNull(className, "Parameter \"className\" must not null. ");
        if (classLoader == null) { classLoader = ClassLoaderUtils.getDefaultClassLoader(); }
        try {
            Class.forName(className, initialize, classLoader);
            return true;
        }
        catch (Throwable ex) {
            // Class or one of its dependencies is not present...
            return false;
        }
    }

    public static boolean isSimpleValueType(Class<?> type) {
        Assert.notNull(type, "Parameter \"type\" must not null. ");
        return BasicType.isPrimitive(type)
                || BasicType.isWrapper(type)
                || type.isEnum()
                || CharSequence.class.isAssignableFrom(type)
                || Number.class.isAssignableFrom(type)
                || Date.class.isAssignableFrom(type)
                || type.equals(URI.class)
                || type.equals(URL.class)
                || type.equals(Locale.class)
                || type.equals(Class.class);
    }

}
