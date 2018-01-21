package com.apyhs.artoria.reflect;

import com.apyhs.artoria.logging.Logger;
import com.apyhs.artoria.logging.LoggerFactory;
import com.apyhs.artoria.util.Assert;

import java.lang.reflect.*;
import java.util.Map;

/**
 * Reflect tools.
 * @author Kahle
 */
public class ReflectUtils {
    private static Logger log = LoggerFactory.getLogger(ReflectUtils.class);
    private static Reflecter reflecter;

    static {
        ReflectUtils.setReflecter(new JdkReflecter());
    }

    public static Reflecter getReflecter() {
        return reflecter;
    }

    public static void setReflecter(Reflecter reflecter) {
        Assert.notNull(reflecter, "Parameter \"reflecter\" must not null. ");
        log.info("Set reflecter: " + reflecter.getClass().getName());
        ReflectUtils.reflecter = reflecter;
    }

    public static Object newInstance(String className, Object... args) throws ClassNotFoundException
            , InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        Class<?> clazz = reflecter.forName(className);
        return args.length == 0 ? clazz.newInstance() : ReflectUtils.newInstance(clazz, args);
    }

    public static Object newInstance(Class<?> clazz, Object... args) throws InvocationTargetException
            , NoSuchMethodException, InstantiationException, IllegalAccessException {
        Assert.notNull(clazz, "Parameter \"clazz\" must not null. ");
        Class<?>[] types = reflecter.findParameterTypes(args);
        Constructor<?> constructor = reflecter.findConstructor(clazz, types);
        reflecter.makeAccessible(constructor);
        return constructor.newInstance(args);
    }

    public static Class<?>[] findParameterTypes(Object... values) {
        return reflecter.findParameterTypes(values);
    }

    public static boolean matchParameterTypes(Class<?>[] declaredTypes, Class<?>[] actualTypes) {
        return reflecter.matchParameterTypes(declaredTypes, actualTypes);
    }

    public static <T extends AccessibleObject> boolean checkAccessible(T accessible) {
        return reflecter.checkAccessible(accessible);
    }

    public static <T extends AccessibleObject> void makeAccessible(T accessible) {
        reflecter.makeAccessible(accessible);
    }

    public static Constructor<?>[] findConstructors(Class<?> clazz) {
        return reflecter.findConstructors(clazz);
    }

    public static Constructor<?> findConstructor(Class<?> clazz, Class<?>... parameterTypes) throws NoSuchMethodException {
        return reflecter.findConstructor(clazz, parameterTypes);
    }

    public static Field[] findFields(Class<?> clazz) {
        return reflecter.findFields(clazz);
    }

    public static Field[] findDeclaredFields(Class<?> clazz) {
        return reflecter.findDeclaredFields(clazz);
    }

    public static Field[] findAccessFields(Class<?> clazz) {
        return reflecter.findAccessFields(clazz);
    }

    public static Field findField(Class<?> clazz, String fieldName) throws NoSuchFieldException {
        return reflecter.findField(clazz, fieldName);
    }

    public static Method[] findMethods(Class<?> clazz) {
        return reflecter.findMethods(clazz);
    }

    public static Method[] findDeclaredMethods(Class<?> clazz) {
        return reflecter.findDeclaredMethods(clazz);
    }

    public static Method[] findAccessMethods(Class<?> clazz) {
        return reflecter.findAccessMethods(clazz);
    }

    public static Map<String, Method> findReadMethods(Class<?> clazz) {
        return reflecter.findReadMethods(clazz);
    }

    public static Map<String, Method> findWriteMethods(Class<?> clazz) {
        return reflecter.findWriteMethods(clazz);
    }

    public static Method findMethod(Class<?> clazz, String methodName, Class<?>... parameterTypes) throws NoSuchMethodException {
        return reflecter.findMethod(clazz, methodName, parameterTypes);
    }

    public static Method findSimilarMethod(Class<?> clazz, String methodName, Class<?>... parameterTypes) throws NoSuchMethodException {
        return reflecter.findSimilarMethod(clazz, methodName, parameterTypes);
    }

}
