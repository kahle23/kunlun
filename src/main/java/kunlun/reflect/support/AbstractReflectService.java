/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.reflect.support;

import kunlun.exception.UncheckedException;
import kunlun.reflect.ReflectService;
import kunlun.util.ArrayUtil;
import kunlun.util.Assert;
import kunlun.util.ClassUtil;

import java.lang.reflect.*;
import java.util.Arrays;

import static kunlun.common.constant.Numbers.ZERO;
import static kunlun.common.constant.Symbols.POUND_SIGN;

/**
 * {@link ReflectService} 的抽象基类，实现"组合层"逻辑：OrThrow 包装方法、按名定位成员的操作方法、
 * 废弃方法桥接、JDK 无争议的镜像方法（declared / public 系列）以及参数类型匹配等通用工具。
 * <p>本类未实现的"原语层"方法由子类提供，共 11 个：getAllFields、getField、getAllMethods、getMethod、
 * getSimilarMethod、getPropertyDescriptors、getConstructor、newInstance、
 * getFieldValue(target, field)、setFieldValue(target, field, value)、invoke(target, method, args)。
 * <p>接口将来新增方法时，优先以模板方法形式在本基类提供实现，避免破坏既有子类。
 *
 * @author Kahle
 */
public abstract class AbstractReflectService implements ReflectService {


    // region ======== 可访问性 ========

    @Override
    public void makeAccessible(AccessibleObject accessible) {
        if (!isAccessible(accessible)) {
            accessible.setAccessible(true);
        }
    }

    @Override
    public boolean isAccessible(AccessibleObject accessible) {
        Assert.notNull(accessible, "Parameter \"accessible\" must not null. ");
        if (accessible instanceof Member) {
            Member member = (Member) accessible;
            boolean b = Modifier.isPublic(member.getModifiers());
            Class<?> declaringClass = member.getDeclaringClass();
            b = b && Modifier.isPublic(declaringClass.getModifiers());
            if (accessible instanceof Field) {
                Field field = (Field) accessible;
                b = b && !Modifier.isFinal(field.getModifiers());
            }
            if (b) { return true; }
        }
        return accessible.isAccessible();
    }

    // endregion ======== 可访问性 ========


    // region ======== 字段 ========

    @Override
    public Field getFieldOrThrow(Class<?> clazz, String fieldName) {
        Field field = getField(clazz, fieldName);
        if (field == null) { throw buildFieldNotFoundException(clazz, fieldName); }
        return field;
    }

    @Override
    public Field[] getDeclaredFields(Class<?> clazz) {
        Assert.notNull(clazz, "Parameter \"clazz\" must not null. ");
        return clazz.getDeclaredFields();
    }

    @Deprecated
    @Override
    public Field[] getFields(Class<?> clazz) {
        Assert.notNull(clazz, "Parameter \"clazz\" must not null. ");
        return clazz.getFields();
    }

    @Deprecated
    @Override
    public Field[] getAccessibleFields(Class<?> clazz) {
        return getAllFields(clazz);
    }

    // endregion ======== 字段 ========


    // region ======== 构造器 ========

    @Override
    public Constructor<?>[] getConstructors(Class<?> clazz) {
        Assert.notNull(clazz, "Parameter \"clazz\" must not null. ");
        return clazz.getDeclaredConstructors();
    }

    @Override
    public <T> Constructor<T> getConstructorOrThrow(Class<T> clazz, Class<?>... parameterTypes) {
        Constructor<T> constructor = getConstructor(clazz, parameterTypes);
        if (constructor == null) {
            String msg = "Constructor with params \"" + Arrays.toString(parameterTypes)
                    + "\" is not found on class \"" + clazz.getName() + "\". ";
            throw new UncheckedException(msg, new NoSuchMethodException(msg));
        }
        return constructor;
    }

    // endregion ======== 构造器 ========


    // region ======== 方法 ========

    @Override
    public Method getMethodOrThrow(Class<?> clazz, String methodName, Class<?>... parameterTypes) {
        Method method = getMethod(clazz, methodName, parameterTypes);
        if (method == null) {
            String msg = "Method \"" + methodName + "\" with params \"" + Arrays.toString(parameterTypes)
                    + "\" is not found on class \"" + clazz.getName() + "\". ";
            throw new UncheckedException(msg, new NoSuchMethodException(msg));
        }
        return method;
    }

    @Override
    public Method getSimilarMethodOrThrow(Class<?> clazz, String methodName, Class<?>... parameterTypes) {
        Method method = getSimilarMethod(clazz, methodName, parameterTypes);
        if (method == null) {
            // 拼装未找到方法的异常信息
            String msg = "No similar method \"" + methodName + "\" with params \""
                    + Arrays.toString(parameterTypes) + "\" could be found on type \""
                    + clazz + "\". ";
            throw new UncheckedException(msg, new NoSuchMethodException(msg));
        }
        return method;
    }

    @Override
    public Method[] getDeclaredMethods(Class<?> clazz) {
        Assert.notNull(clazz, "Parameter \"clazz\" must not null. ");
        return clazz.getDeclaredMethods();
    }

    @Deprecated
    @Override
    public Method[] getMethods(Class<?> clazz) {
        Assert.notNull(clazz, "Parameter \"clazz\" must not null. ");
        return clazz.getMethods();
    }

    @Deprecated
    @Override
    public Method[] getAccessibleMethods(Class<?> clazz) {
        return getAllMethods(clazz);
    }

    // endregion ======== 方法 ========


    // region ======== 操作 ========

    @Override
    public <T> T getFieldValue(Object target, String fieldName) {
        Assert.notNull(target, "Parameter \"target\" must not null. ");
        Assert.notBlank(fieldName, "Parameter \"fieldName\" must not blank. ");
        Field field = getFieldOrThrow(getClassOf(target), fieldName);
        return getFieldValue(target, field);
    }

    @Override
    public void setFieldValue(Object target, String fieldName, Object value) {
        Assert.notNull(target, "Parameter \"target\" must not null. ");
        Assert.notBlank(fieldName, "Parameter \"fieldName\" must not blank. ");
        Field field = getFieldOrThrow(getClassOf(target), fieldName);
        setFieldValue(target, field, value);
    }

    @Override
    public <T> T invoke(Object target, String methodName, Object... args) {
        Assert.notNull(target, "Parameter \"target\" must not null. ");
        Assert.notBlank(methodName, "Parameter \"methodName\" must not blank. ");
        Class<?>[] types = getParameterTypes(args);
        Method method = getSimilarMethodOrThrow(getClassOf(target), methodName, types);
        // 静态方法调用时接收者传 null
        boolean isStatic = Modifier.isStatic(method.getModifiers());
        return invoke(isStatic ? null : target, method, args);
    }

    @Override
    public <T> T invokeStatic(Class<?> clazz, String methodName, Object... args) {
        Assert.notNull(clazz, "Parameter \"clazz\" must not null. ");
        Assert.notBlank(methodName, "Parameter \"methodName\" must not blank. ");
        Class<?>[] types = getParameterTypes(args);
        Method method = getSimilarMethodOrThrow(clazz, methodName, types);
        return invoke(null, method, args);
    }

    // endregion ======== 操作 ========


    // region ======== 内部工具 ========
    /**
     * 获取目标对象对应的类：目标本身是 Class 对象时返回其自身（用于静态成员访问）。
     */
    protected Class<?> getClassOf(Object target) {
        return target instanceof Class ? (Class<?>) target : target.getClass();
    }

    protected Class<?>[] getParameterTypes(Object[] params) {
        if (ArrayUtil.isEmpty(params)) { return new Class[ZERO]; }
        Class<?>[] result = new Class[params.length];
        for (int i = ZERO; i < params.length; i++) {
            Object value = params[i];
            // 实参为 null 时类型记为 null，匹配阶段按"可空"处理
            result[i] = value == null ? null : value.getClass();
        }
        return result;
    }

    protected boolean matchParameterTypes(Class<?>[] declaredTypes, Class<?>[] actualTypes) {
        Assert.notNull(declaredTypes, "Parameter \"declaredTypes\" must not null. ");
        Assert.notNull(actualTypes, "Parameter \"actualTypes\" must not null. ");
        if (declaredTypes.length != actualTypes.length) {
            return false;
        }
        for (int i = ZERO; i < actualTypes.length; i++) {
            // 形参存在但实参传 null，视为可匹配，跳过类型校验
            if (actualTypes[i] == null) { continue; }
            Class<?> declared = ClassUtil.getWrapper(declaredTypes[i]);
            Class<?> actual = ClassUtil.getWrapper(actualTypes[i]);
            if (declared.isAssignableFrom(actual)) { continue; }
            return false;
        }
        return true;
    }

    protected String buildMethodSignature(String methodName, Class<?>[] parameterTypes) {
        return methodName + POUND_SIGN + Arrays.toString(parameterTypes);
    }

    protected UncheckedException buildFieldNotFoundException(Class<?> clazz, String fieldName) {
        String msg = "Field \"" + fieldName + "\" is not found on class \"" + clazz.getName() + "\". ";
        return new UncheckedException(msg, new NoSuchFieldException(msg));
    }

    // endregion ======== 内部工具 ========

}
