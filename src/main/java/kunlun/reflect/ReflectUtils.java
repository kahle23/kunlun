/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.reflect;

import kunlun.logging.Logger;
import kunlun.logging.LoggerFactory;
import kunlun.reflect.support.SimpleReflectService;
import kunlun.util.Assert;

import java.beans.PropertyDescriptor;
import java.lang.reflect.*;

/**
 * The reflection tools.
 * @author Kahle
 */
public class ReflectUtils {
    private static final Logger log = LoggerFactory.getLogger(ReflectUtils.class);
    private static volatile ReflectService reflectService;

    public static ReflectService getReflectService() {
        if (reflectService != null) { return reflectService; }
        synchronized (ReflectUtils.class) {
            if (reflectService != null) { return reflectService; }
            ReflectUtils.setReflectService(new SimpleReflectService());
            return reflectService;
        }
    }

    public static void setReflectService(ReflectService reflectService) {
        Assert.notNull(reflectService, "Parameter \"reflectService\" must not null. ");
        log.info("Set reflect service: {}", reflectService.getClass().getName());
        ReflectUtils.reflectService = reflectService;
    }

    public static void makeAccessible(AccessibleObject accessible) {

        getReflectService().makeAccessible(accessible);
    }

    public static boolean checkAccessible(AccessibleObject accessible) {

        return getReflectService().checkAccessible(accessible);
    }

    public static <T> Constructor<T>[] getConstructors(Class<T> clazz) {

        return getReflectService().getConstructors(clazz);
    }

    public static <T> Constructor<T> getConstructor(Class<T> clazz, Class<?>... parameterTypes) throws NoSuchMethodException {

        return getReflectService().getConstructor(clazz, parameterTypes);
    }

    public static Field[] getFields(Class<?> clazz) {

        return getReflectService().getFields(clazz);
    }

    public static Field[] getDeclaredFields(Class<?> clazz) {

        return getReflectService().getDeclaredFields(clazz);
    }

    public static Field[] getAccessibleFields(Class<?> clazz) {

        return getReflectService().getAccessibleFields(clazz);
    }

    public static Field getField(Class<?> clazz, String fieldName) throws NoSuchFieldException {

        return getReflectService().getField(clazz, fieldName);
    }

    public static Method[] getMethods(Class<?> clazz) {

        return getReflectService().getMethods(clazz);
    }

    public static Method[] getDeclaredMethods(Class<?> clazz) {

        return getReflectService().getDeclaredMethods(clazz);
    }

    public static Method[] getAccessibleMethods(Class<?> clazz) {

        return getReflectService().getAccessibleMethods(clazz);
    }

    public static Method getMethod(Class<?> clazz, String methodName, Class<?>... parameterTypes) throws NoSuchMethodException {

        return getReflectService().getMethod(clazz, methodName, parameterTypes);
    }

    public static Method getSimilarMethod(Class<?> clazz, String methodName, Class<?>... parameterTypes) throws NoSuchMethodException {

        return getReflectService().getSimilarMethod(clazz, methodName, parameterTypes);
    }

    public static PropertyDescriptor[] getPropertyDescriptors(Class<?> clazz) {

        return getReflectService().getPropertyDescriptors(clazz);
    }

    public static <T> T newInstance(Class<T> clazz, Object... args) throws NoSuchMethodException
            , IllegalAccessException, InvocationTargetException, InstantiationException {

        return getReflectService().newInstance(clazz, args);
    }

}
