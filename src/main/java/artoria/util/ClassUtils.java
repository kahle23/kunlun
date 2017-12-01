package artoria.util;

/**
 * @author Kahle
 */
public class ClassUtils {

    public static ClassLoader getDefaultClassLoader() {
        ClassLoader classLoader = null;
        try {
            classLoader = Thread.currentThread().getContextClassLoader();
        }
        catch (Throwable ex) {
            // Cannot access thread context ClassLoader - falling back...
        }
        if (classLoader == null) {
            // No thread context class loader -> use class loader of this class.
            classLoader = ClassUtils.class.getClassLoader();
            if (classLoader == null) {
                // getClassLoader() returning null indicates the bootstrap ClassLoader
                try {
                    classLoader = ClassLoader.getSystemClassLoader();
                }
                catch (Throwable ex) {
                    // Cannot access system ClassLoader - oh well, maybe the caller can live with null...
                }
            }
        }
        return classLoader;
    }

    public static Class<?> forName(String name) throws ClassNotFoundException {
        Assert.notNull(name);
        return Class.forName(name);
    }

    public static Class<?> forName(String name, ClassLoader loader) throws ClassNotFoundException {
        Assert.notNull(name);
        return Class.forName(name, true, loader);
    }

    public static Class<?> forName(String name, boolean initialize, ClassLoader loader) throws ClassNotFoundException {
        Assert.notNull(name);
        return Class.forName(name, initialize, loader);
    }

    public static boolean isPresent(String className, ClassLoader classLoader) {
        return isPresent(className, true, classLoader);
    }

    public static boolean isPresent(String className, boolean initialize, ClassLoader classLoader) {
        try {
            Class.forName(className, initialize, classLoader);
            return true;
        }
        catch (Throwable e) {
            return false;
        }
    }

}
