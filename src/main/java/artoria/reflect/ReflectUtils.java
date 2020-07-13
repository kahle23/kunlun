package artoria.reflect;

import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import artoria.util.Assert;

import java.beans.PropertyDescriptor;
import java.lang.reflect.*;

/**
 * Reflect tools.
 * @author Kahle
 */
public class ReflectUtils {
    private static Logger log = LoggerFactory.getLogger(ReflectUtils.class);
    private static ReflectProvider reflectProvider;

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

    public static boolean matchTypes(Class<?>[] declaredTypes, Class<?>[] actualTypes) {

        return getReflectProvider().matchTypes(declaredTypes, actualTypes);
    }

    public static boolean checkAccessible(AccessibleObject accessible) {

        return getReflectProvider().checkAccessible(accessible);
    }

    public static void makeAccessible(AccessibleObject accessible) {

        getReflectProvider().makeAccessible(accessible);
    }

    public static Class<?>[] findTypes(Object[] params) {

        return getReflectProvider().findTypes(params);
    }

    public static <T> Constructor<T>[] findConstructors(Class<T> clazz) {

        return getReflectProvider().findConstructors(clazz);
    }

    public static <T> Constructor<T> findConstructor(Class<T> clazz, Class<?>... parameterTypes) throws NoSuchMethodException {

        return getReflectProvider().findConstructor(clazz, parameterTypes);
    }

    public static Field[] findFields(Class<?> clazz) {

        return getReflectProvider().findFields(clazz);
    }

    public static Field[] findDeclaredFields(Class<?> clazz) {

        return getReflectProvider().findDeclaredFields(clazz);
    }

    public static Field[] findAccessFields(Class<?> clazz) {

        return getReflectProvider().findAccessFields(clazz);
    }

    public static Field findField(Class<?> clazz, String fieldName) throws NoSuchFieldException {

        return getReflectProvider().findField(clazz, fieldName);
    }

    public static Method[] findMethods(Class<?> clazz) {

        return getReflectProvider().findMethods(clazz);
    }

    public static Method[] findDeclaredMethods(Class<?> clazz) {

        return getReflectProvider().findDeclaredMethods(clazz);
    }

    public static Method[] findAccessMethods(Class<?> clazz) {

        return getReflectProvider().findAccessMethods(clazz);
    }

    public static Method findMethod(Class<?> clazz, String methodName, Class<?>... parameterTypes) throws NoSuchMethodException {

        return getReflectProvider().findMethod(clazz, methodName, parameterTypes);
    }

    public static Method findSimilarMethod(Class<?> clazz, String methodName, Class<?>... parameterTypes) throws NoSuchMethodException {

        return getReflectProvider().findSimilarMethod(clazz, methodName, parameterTypes);
    }

    public static PropertyDescriptor[] findPropertyDescriptors(Class<?> clazz) {

        return getReflectProvider().findPropertyDescriptors(clazz);
    }

    public static <T> T newInstance(Class<T> clazz, Object... args) throws NoSuchMethodException
            , IllegalAccessException, InvocationTargetException, InstantiationException {

        return getReflectProvider().newInstance(clazz, args);
    }

}
