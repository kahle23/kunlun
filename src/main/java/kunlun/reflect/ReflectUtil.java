/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.reflect;

import kunlun.logging.Logger;
import kunlun.logging.LoggerFactory;
import kunlun.reflect.support.CacheReflectService;
import kunlun.reflect.support.JdkReflectService;
import kunlun.util.Assert;

import java.beans.PropertyDescriptor;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 反射工具类（静态门面），委托 {@link ReflectService} 执行，可经
 * {@link #setReflectService(ReflectService)} 替换后端实现。
 *
 * @author Kahle
 */
public class ReflectUtil {
    private static final Logger log = LoggerFactory.getLogger(ReflectUtil.class);
    private static volatile ReflectService reflectService;

    public static ReflectService getReflectService() {
        if (reflectService != null) { return reflectService; }
        synchronized (ReflectUtil.class) {
            if (reflectService != null) { return reflectService; }
            ReflectUtil.setReflectService(new CacheReflectService(new JdkReflectService()));
            return reflectService;
        }
    }

    public static void setReflectService(ReflectService reflectService) {
        Assert.notNull(reflectService, "Parameter \"reflectService\" must not null. ");
        log.debug("Set reflect service: {}", reflectService.getClass().getName());
        ReflectUtil.reflectService = reflectService;
    }


    // region ======== 可访问性 ========

    public static void makeAccessible(AccessibleObject accessible) {

        getReflectService().makeAccessible(accessible);
    }

    /**
     * 判断指定可访问对象当前是否可直接访问，无需再取消访问限制。注意与 {@link AccessibleObject#isAccessible()} 的区别——
     * 后者仅返回 setAccessible 标记，public 成员未调用过 setAccessible 时它返回 {@code false}，但实际可直接访问。
     *
     * @param accessible 待检查的可访问对象
     * @return 可直接访问返回 {@code true}，否则返回 {@code false}
     */
    public static boolean isAccessible(AccessibleObject accessible) {

        return getReflectService().isAccessible(accessible);
    }

    // endregion ======== 可访问性 ========


    // region ======== 字段 ========

    public static Field[] getAllFields(Class<?> clazz) {

        return getReflectService().getAllFields(clazz);
    }

    public static boolean hasField(Class<?> clazz, String fieldName) {

        return getReflectService().hasField(clazz, fieldName);
    }

    public static Field getField(Class<?> clazz, String fieldName) {

        return getReflectService().getField(clazz, fieldName);
    }

    public static Field getFieldOrThrow(Class<?> clazz, String fieldName) {

        return getReflectService().getFieldOrThrow(clazz, fieldName);
    }

    public static Field[] getDeclaredFields(Class<?> clazz) {

        return getReflectService().getDeclaredFields(clazz);
    }

    /**
     * @deprecated 语义与 {@link Class#getFields()} 一致，请直接使用 JDK API；
     *             全层次可访问字段用 {@link #getAllFields(Class)}
     */
    @Deprecated
    public static Field[] getFields(Class<?> clazz) {

        return getReflectService().getFields(clazz);
    }

    /**
     * @deprecated 请用 {@link #getAllFields(Class)}
     */
    @Deprecated
    public static Field[] getAccessibleFields(Class<?> clazz) {

        return getReflectService().getAccessibleFields(clazz);
    }

    // endregion ======== 字段 ========


    // region ======== 构造器 ========

    public static Constructor<?>[] getConstructors(Class<?> clazz) {

        return getReflectService().getConstructors(clazz);
    }

    public static <T> Constructor<T> getConstructor(Class<T> clazz, Class<?>... parameterTypes) {

        return getReflectService().getConstructor(clazz, parameterTypes);
    }

    public static <T> Constructor<T> getConstructorOrThrow(Class<T> clazz, Class<?>... parameterTypes) {

        return getReflectService().getConstructorOrThrow(clazz, parameterTypes);
    }

    // endregion ======== 构造器 ========


    // region ======== 方法 ========

    public static Method[] getAllMethods(Class<?> clazz) {

        return getReflectService().getAllMethods(clazz);
    }

    public static Method getMethod(Class<?> clazz, String methodName, Class<?>... parameterTypes) {

        return getReflectService().getMethod(clazz, methodName, parameterTypes);
    }

    public static Method getMethodOrThrow(Class<?> clazz, String methodName, Class<?>... parameterTypes) {

        return getReflectService().getMethodOrThrow(clazz, methodName, parameterTypes);
    }

    public static Method getSimilarMethod(Class<?> clazz, String methodName, Class<?>... parameterTypes) {

        return getReflectService().getSimilarMethod(clazz, methodName, parameterTypes);
    }

    public static Method getSimilarMethodOrThrow(Class<?> clazz, String methodName, Class<?>... parameterTypes) {

        return getReflectService().getSimilarMethodOrThrow(clazz, methodName, parameterTypes);
    }

    public static Method[] getDeclaredMethods(Class<?> clazz) {

        return getReflectService().getDeclaredMethods(clazz);
    }

    /**
     * @deprecated 语义与 {@link Class#getMethods()} 一致，请直接使用 JDK API；
     *             全层次可访问方法用 {@link #getAllMethods(Class)}
     */
    @Deprecated
    public static Method[] getMethods(Class<?> clazz) {

        return getReflectService().getMethods(clazz);
    }

    /**
     * @deprecated 请用 {@link #getAllMethods(Class)}
     */
    @Deprecated
    public static Method[] getAccessibleMethods(Class<?> clazz) {

        return getReflectService().getAccessibleMethods(clazz);
    }

    // endregion ======== 方法 ========


    // region ======== 操作 ========

    public static PropertyDescriptor[] getPropertyDescriptors(Class<?> clazz) {

        return getReflectService().getPropertyDescriptors(clazz);
    }

    public static <T> T newInstance(Class<T> clazz, Object... args) {

        return getReflectService().newInstance(clazz, args);
    }

    public static <T> T getFieldValue(Object target, String fieldName) {

        return getReflectService().getFieldValue(target, fieldName);
    }

    public static <T> T getFieldValue(Object target, Field field) {

        return getReflectService().getFieldValue(target, field);
    }

    public static void setFieldValue(Object target, String fieldName, Object value) {

        getReflectService().setFieldValue(target, fieldName, value);
    }

    public static void setFieldValue(Object target, Field field, Object value) {

        getReflectService().setFieldValue(target, field, value);
    }

    public static <T> T invoke(Object target, String methodName, Object... args) {

        return getReflectService().invoke(target, methodName, args);
    }

    public static <T> T invoke(Object target, Method method, Object... args) {

        return getReflectService().invoke(target, method, args);
    }

    public static <T> T invokeStatic(Class<?> clazz, String methodName, Object... args) {

        return getReflectService().invokeStatic(clazz, methodName, args);
    }

    // endregion ======== 操作 ========


    // region ======== 私有的构造方法 ========

    private ReflectUtil() {

        throw new UnsupportedOperationException("Don't allow instantiation. ");
    }

    // endregion ======== 私有的构造方法 ========

}
