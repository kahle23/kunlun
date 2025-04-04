/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.reflect;

import kunlun.exception.ExceptionUtil;
import kunlun.logging.Logger;
import kunlun.logging.LoggerFactory;
import kunlun.reflect.support.SimpleReflectService;
import kunlun.util.Assert;

import java.beans.PropertyDescriptor;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * The reflection tools.
 * @author Kahle
 */
public class ReflectUtil {
    private static final Logger log = LoggerFactory.getLogger(ReflectUtil.class);
    private static volatile ReflectService reflectService;

    public static ReflectService getReflectService() {
        if (reflectService != null) { return reflectService; }
        synchronized (ReflectUtil.class) {
            if (reflectService != null) { return reflectService; }
            ReflectUtil.setReflectService(new SimpleReflectService());
            return reflectService;
        }
    }

    public static void setReflectService(ReflectService reflectService) {
        Assert.notNull(reflectService, "Parameter \"reflectService\" must not null. ");
        log.debug("Set reflect service: {}", reflectService.getClass().getName());
        ReflectUtil.reflectService = reflectService;
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

    public static <T> Constructor<T> getConstructor(Class<T> clazz, Class<?>... parameterTypes) {
        try {
            return getReflectService().getConstructor(clazz, parameterTypes);
        } catch (NoSuchMethodException e) {
            throw ExceptionUtil.wrap(e);
        }
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

    public static Field getField(Class<?> clazz, String fieldName) {
        try {
            return getReflectService().getField(clazz, fieldName);
        } catch (NoSuchFieldException e) {
            throw ExceptionUtil.wrap(e);
        }
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

    public static Method getMethod(Class<?> clazz, String methodName, Class<?>... parameterTypes) {
        try {
            return getReflectService().getMethod(clazz, methodName, parameterTypes);
        } catch (NoSuchMethodException e) {
            throw ExceptionUtil.wrap(e);
        }
    }

    public static Method getSimilarMethod(Class<?> clazz, String methodName, Class<?>... parameterTypes) {
        try {
            return getReflectService().getSimilarMethod(clazz, methodName, parameterTypes);
        } catch (NoSuchMethodException e) {
            throw ExceptionUtil.wrap(e);
        }
    }

    public static PropertyDescriptor[] getPropertyDescriptors(Class<?> clazz) {

        return getReflectService().getPropertyDescriptors(clazz);
    }

    public static <T> T newInstance(Class<T> clazz, Object... args) {
        try {
            return getReflectService().newInstance(clazz, args);
        } catch (Exception e) {
            throw ExceptionUtil.wrap(e);
        }
    }

}
