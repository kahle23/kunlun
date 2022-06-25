package artoria.reflect;

import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import artoria.util.Assert;

import java.beans.PropertyDescriptor;
import java.lang.reflect.*;

/**
 * The reflect tools.
 * @author Kahle
 */
public class ReflectUtils {
    private static Logger log = LoggerFactory.getLogger(ReflectUtils.class);
    private static volatile ReflectProvider reflectProvider;

    public static ReflectProvider getReflectProvider() {
        if (reflectProvider != null) { return reflectProvider; }
        synchronized (ReflectUtils.class) {
            if (reflectProvider != null) { return reflectProvider; }
            ReflectUtils.setReflectProvider(new SimpleReflectProvider());
            return reflectProvider;
        }
    }

    public static void setReflectProvider(ReflectProvider reflectProvider) {
        Assert.notNull(reflectProvider, "Parameter \"reflectProvider\" must not null. ");
        log.info("Set reflect provider: {}", reflectProvider.getClass().getName());
        ReflectUtils.reflectProvider = reflectProvider;
    }

    public static void makeAccessible(AccessibleObject accessible) {

        getReflectProvider().makeAccessible(accessible);
    }

    public static boolean checkAccessible(AccessibleObject accessible) {

        return getReflectProvider().checkAccessible(accessible);
    }

    public static <T> Constructor<T>[] getConstructors(Class<T> clazz) {

        return getReflectProvider().getConstructors(clazz);
    }

    public static <T> Constructor<T> getConstructor(Class<T> clazz, Class<?>... parameterTypes) throws NoSuchMethodException {

        return getReflectProvider().getConstructor(clazz, parameterTypes);
    }

    public static Field[] getFields(Class<?> clazz) {

        return getReflectProvider().getFields(clazz);
    }

    public static Field[] getDeclaredFields(Class<?> clazz) {

        return getReflectProvider().getDeclaredFields(clazz);
    }

    public static Field[] getAccessibleFields(Class<?> clazz) {

        return getReflectProvider().getAccessibleFields(clazz);
    }

    public static Field getField(Class<?> clazz, String fieldName) throws NoSuchFieldException {

        return getReflectProvider().getField(clazz, fieldName);
    }

    public static Method[] getMethods(Class<?> clazz) {

        return getReflectProvider().getMethods(clazz);
    }

    public static Method[] getDeclaredMethods(Class<?> clazz) {

        return getReflectProvider().getDeclaredMethods(clazz);
    }

    public static Method[] getAccessibleMethods(Class<?> clazz) {

        return getReflectProvider().getAccessibleMethods(clazz);
    }

    public static Method getMethod(Class<?> clazz, String methodName, Class<?>... parameterTypes) throws NoSuchMethodException {

        return getReflectProvider().getMethod(clazz, methodName, parameterTypes);
    }

    public static Method getSimilarMethod(Class<?> clazz, String methodName, Class<?>... parameterTypes) throws NoSuchMethodException {

        return getReflectProvider().getSimilarMethod(clazz, methodName, parameterTypes);
    }

    public static PropertyDescriptor[] getPropertyDescriptors(Class<?> clazz) {

        return getReflectProvider().getPropertyDescriptors(clazz);
    }

    public static <T> T newInstance(Class<T> clazz, Object... args) throws NoSuchMethodException
            , IllegalAccessException, InvocationTargetException, InstantiationException {

        return getReflectProvider().newInstance(clazz, args);
    }

}
