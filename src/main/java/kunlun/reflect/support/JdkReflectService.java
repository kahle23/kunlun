/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.reflect.support;

import kunlun.exception.ExceptionUtil;
import kunlun.reflect.ReflectService;
import kunlun.util.Assert;
import kunlun.util.CastUtil;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.*;
import java.util.LinkedHashMap;
import java.util.Map;

import static kunlun.common.constant.Numbers.ZERO;

/**
 * 基于 JDK 原生反射 API 的 {@link ReflectService} 实现，不做任何缓存，每次调用实时沿类层次遍历。
 * 仅实现"原语层"（成员查找、构造器匹配、实例化与成员级原始操作），组合层逻辑继承自
 * {@link AbstractReflectService}。通常作为 {@link CacheReflectService} 的被代理对象使用，
 * 如 {@code new CacheReflectService(new JdkReflectService())}。
 *
 * @author Kahle
 */
public class JdkReflectService extends AbstractReflectService {


    // region ======== 字段 ========

    @Override
    public Field[] getAllFields(Class<?> clazz) {
        Assert.notNull(clazz, "Parameter \"clazz\" must not null. ");
        Class<?> inputClazz = clazz;
        Map<String, Field> fieldByName = new LinkedHashMap<String, Field>();
        while (clazz != null) {
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                // 本类全部取，父类仅取非 private；同名子类优先
                if (notAccess(inputClazz, clazz, field)) { continue; }
                if (fieldByName.containsKey(field.getName())) { continue; }
                makeAccessible(field);
                fieldByName.put(field.getName(), field);
            }
            // 接口中的字段均为 public，会被子类继承
            clazz = clazz.getSuperclass();
        }
        return fieldByName.values().toArray(new Field[ZERO]);
    }

    @Override
    public Field getField(Class<?> clazz, String fieldName) {
        Assert.notBlank(fieldName, "Parameter \"fieldName\" must not blank. ");
        Assert.notNull(clazz, "Parameter \"clazz\" must not null. ");
        // 优先获取类层次（含接口）中的 public 字段，失败则沿父类链逐级查找声明字段（含非 public）
        try {
            return clazz.getField(fieldName);
        }
        catch (NoSuchFieldException e) {
            Class<?> inputClazz = clazz;
            while (clazz != null) {
                try {
                    Field field = clazz.getDeclaredField(fieldName);
                    if (!notAccess(inputClazz, clazz, field)) { return field; }
                }
                catch (NoSuchFieldException ignored) { }
                clazz = clazz.getSuperclass();
            }
            return null;
        }
    }

    // endregion ======== 字段 ========


    // region ======== 构造器 ========

    @Override
    public <T> Constructor<T> getConstructor(Class<T> clazz, Class<?>... parameterTypes) {
        Assert.notNull(clazz, "Parameter \"clazz\" must not null. ");
        // 优先尝试"规范"构造器（形参类型精确匹配），精确匹配失败时尝试相似签名匹配（基本类型按包装类型对齐）
        try {
            return clazz.getDeclaredConstructor(parameterTypes);
        }
        catch (NoSuchMethodException e) {
            Constructor<?>[] cts = getConstructors(clazz);
            for (Constructor<?> ct : cts) {
                Class<?>[] pTypes = ct.getParameterTypes();
                boolean b = matchParameterTypes(pTypes, parameterTypes);
                if (b) { return CastUtil.cast(ct); }
            }
            return null;
        }
    }

    // endregion ======== 构造器 ========


    // region ======== 方法 ========

    @Override
    public Method[] getAllMethods(Class<?> clazz) {
        Assert.notNull(clazz, "Parameter \"clazz\" must not null. ");
        Class<?> inputClazz = clazz;
        Map<String, Method> methodBySignature = new LinkedHashMap<String, Method>();
        while (clazz != null) {
            Method[] methods = clazz.getDeclaredMethods();
            for (Method method : methods) {
                // 本类全部取，父类仅取非 private；"名 + 形参类型"相同子类优先
                if (notAccess(inputClazz, clazz, method)) { continue; }
                String signature = buildMethodSignature(method.getName(), method.getParameterTypes());
                if (methodBySignature.containsKey(signature)) { continue; }
                makeAccessible(method);
                methodBySignature.put(signature, method);
            }
            clazz = clazz.getSuperclass();
        }
        return methodBySignature.values().toArray(new Method[ZERO]);
    }

    @Override
    public Method getMethod(Class<?> clazz, String methodName, Class<?>... parameterTypes) {
        Assert.notBlank(methodName, "Parameter \"methodName\" must not blank. ");
        Assert.notNull(clazz, "Parameter \"clazz\" must not null. ");
        // 优先在类层次（含接口）中精确匹配 public 方法，失败则沿父类链逐级查找声明方法（含非 public）
        try {
            return clazz.getMethod(methodName, parameterTypes);
        }
        catch (NoSuchMethodException e) {
            Class<?> inputClazz = clazz;
            while (clazz != null) {
                try {
                    Method method = clazz.getDeclaredMethod(methodName, parameterTypes);
                    if (!notAccess(inputClazz, clazz, method)) { return method; }
                }
                catch (NoSuchMethodException ignored) { }
                clazz = clazz.getSuperclass();
            }
            return null;
        }
    }

    @Override
    public Method getSimilarMethod(Class<?> clazz, String methodName, Class<?>... parameterTypes) {
        Assert.notBlank(methodName, "Parameter \"methodName\" must not blank. ");
        Assert.notNull(clazz, "Parameter \"clazz\" must not null. ");
        // 相似指实参基本类型与包装类型可互相转换。第一优先级在类层次（含接口）的 public 方法中查找，
        // 第二优先级在全层次可访问方法中查找（含非 public）
        for (Method method : clazz.getMethods()) {
            if (methodName.equals(method.getName()) &&
                    matchParameterTypes(method.getParameterTypes(), parameterTypes)) {
                return method;
            }
        }
        for (Method method : getAllMethods(clazz)) {
            if (methodName.equals(method.getName()) &&
                    matchParameterTypes(method.getParameterTypes(), parameterTypes)) {
                return method;
            }
        }
        return null;
    }

    // endregion ======== 方法 ========


    // region ======== 操作 ========

    @Override
    public PropertyDescriptor[] getPropertyDescriptors(Class<?> clazz) {
        Assert.notNull(clazz, "Parameter \"clazz\" must not null. ");
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(clazz);
            PropertyDescriptor[] descriptors = beanInfo.getPropertyDescriptors();
            return descriptors != null ? descriptors : new PropertyDescriptor[ZERO];
        }
        catch (Exception e) { throw ExceptionUtil.wrap(e); }
    }

    @Override
    public <T> T newInstance(Class<T> clazz, Object... args) {
        Assert.notNull(clazz, "Parameter \"clazz\" must not null. ");
        Class<?>[] types = getParameterTypes(args);
        try {
            Constructor<T> constructor = getConstructorOrThrow(clazz, types);
            makeAccessible(constructor);
            return constructor.newInstance(args);
        }
        catch (InvocationTargetException e) {
            // 解包并抛出构造器内的业务异常
            Throwable cause = e.getCause() != null ? e.getCause() : e;
            throw ExceptionUtil.wrap(cause);
        }
        catch (Exception e) { throw ExceptionUtil.wrap(e); }
    }

    @Override
    public <T> T getFieldValue(Object target, Field field) {
        Assert.notNull(field, "Parameter \"field\" must not null. ");
        try {
            makeAccessible(field);
            return CastUtil.cast(field.get(target));
        }
        catch (Exception e) { throw ExceptionUtil.wrap(e); }
    }

    @Override
    public void setFieldValue(Object target, Field field, Object value) {
        Assert.notNull(field, "Parameter \"field\" must not null. ");
        try {
            makeAccessible(field);
            field.set(target, value);
        }
        catch (Exception e) { throw ExceptionUtil.wrap(e); }
    }

    @Override
    public <T> T invoke(Object target, Method method, Object... args) {
        Assert.notNull(method, "Parameter \"method\" must not null. ");
        try {
            makeAccessible(method);
            return CastUtil.cast(method.invoke(target, args));
        }
        catch (InvocationTargetException e) {
            // 解包并抛出目标方法内的业务异常
            Throwable cause = e.getCause() != null ? e.getCause() : e;
            throw ExceptionUtil.wrap(cause);
        }
        catch (Exception e) { throw ExceptionUtil.wrap(e); }
    }

    // endregion ======== 操作 ========


    // region ======== 内部工具 ========

    protected boolean notAccess(Class<?> thisClazz, Class<?> superClazz, Member member) {
        // 本类成员全部可访问；来自父类的 private 成员不可访问
        return thisClazz != superClazz && Modifier.isPrivate(member.getModifiers());
    }

    // endregion ======== 内部工具 ========

}
