package artoria.reflect;

import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import artoria.util.Assert;

import java.lang.reflect.*;
import java.util.Map;

/**
 * Reflect tools.
 * @author Kahle
 */
public class ReflectUtils {
    private static final Reflecter DEFAULT_REFLECTER = new DefaultReflecter();
    private static Logger log = LoggerFactory.getLogger(ReflectUtils.class);
    private static Reflecter reflecter;

    public static Reflecter getReflecter() {

        return reflecter != null ? reflecter : DEFAULT_REFLECTER;
    }

    public static void setReflecter(Reflecter reflecter) {
        Assert.notNull(reflecter, "Parameter \"reflecter\" must not null. ");
        log.info("Set reflecter: {}", reflecter.getClass().getName());
        ReflectUtils.reflecter = reflecter;
    }

    public static Class<?>[] findParameterTypes(Object... values) {

        return getReflecter().findParameterTypes(values);
    }

    public static boolean matchParameterTypes(Class<?>[] declaredTypes, Class<?>[] actualTypes) {

        return getReflecter().matchParameterTypes(declaredTypes, actualTypes);
    }

    public static <T extends AccessibleObject> boolean checkAccessible(T accessible) {

        return getReflecter().checkAccessible(accessible);
    }

    public static <T extends AccessibleObject> void makeAccessible(T accessible) {

        getReflecter().makeAccessible(accessible);
    }

    public static <T> T newInstance(Class<T> clazz, Object... args) throws NoSuchMethodException
            , IllegalAccessException, InvocationTargetException, InstantiationException {

        return getReflecter().newInstance(clazz, args);
    }

    public static <T> Constructor<T>[] findConstructors(Class<T> clazz) {

        return getReflecter().findConstructors(clazz);
    }

    public static <T> Constructor<T> findConstructor(Class<T> clazz, Class<?>... parameterTypes) throws NoSuchMethodException {

        return getReflecter().findConstructor(clazz, parameterTypes);
    }

    public static Field[] findFields(Class<?> clazz) {

        return getReflecter().findFields(clazz);
    }

    public static Field[] findDeclaredFields(Class<?> clazz) {

        return getReflecter().findDeclaredFields(clazz);
    }

    public static Field[] findAccessFields(Class<?> clazz) {

        return getReflecter().findAccessFields(clazz);
    }

    public static Field findField(Class<?> clazz, String fieldName) throws NoSuchFieldException {

        return getReflecter().findField(clazz, fieldName);
    }

    public static Method[] findMethods(Class<?> clazz) {

        return getReflecter().findMethods(clazz);
    }

    public static Method[] findDeclaredMethods(Class<?> clazz) {

        return getReflecter().findDeclaredMethods(clazz);
    }

    public static Method[] findAccessMethods(Class<?> clazz) {

        return getReflecter().findAccessMethods(clazz);
    }

    public static Map<String, Method> findReadMethods(Class<?> clazz) {

        return getReflecter().findReadMethods(clazz);
    }

    public static Map<String, Method> findWriteMethods(Class<?> clazz) {

        return getReflecter().findWriteMethods(clazz);
    }

    public static Method findMethod(Class<?> clazz, String methodName, Class<?>... parameterTypes) throws NoSuchMethodException {

        return getReflecter().findMethod(clazz, methodName, parameterTypes);
    }

    public static Method findSimilarMethod(Class<?> clazz, String methodName, Class<?>... parameterTypes) throws NoSuchMethodException {

        return getReflecter().findSimilarMethod(clazz, methodName, parameterTypes);
    }

}
