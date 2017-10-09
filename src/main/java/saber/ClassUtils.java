package saber;

public abstract class ClassUtils {

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

    public static boolean isPresent(String className, ClassLoader classLoader) {
        return isPresent(className, true, classLoader);
    }

    public static boolean isPresent(String className, boolean initialize, ClassLoader classLoader) {
        try {
            Class.forName(className, initialize, classLoader);
            return true;
        } catch (Throwable e) {
            return false;
        }
    }

}
