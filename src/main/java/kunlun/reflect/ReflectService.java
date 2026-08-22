/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.reflect;

import java.beans.PropertyDescriptor;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 反射服务接口，统一封装常用的反射操作，屏蔽底层 API 的繁琐细节。方法按"可访问性 / 字段 / 构造器 / 方法 /
 * 操作"五组划分。
 * <p>命名约定：单个成员的查找方法成对提供——{@code getXxx} 未找到时返回 {@code null}，{@code getXxxOrThrow} 未找到时抛
 * {@link RuntimeException}（cause 为 JDK 原始异常）；"没找到是错误"的场景用 OrThrow，
 * "没找到是正常情况"的场景用普通 getXxx。
 *
 * @author Kahle
 */
public interface ReflectService {

    // region ======== 可访问性 ========
    /**
     * 取消指定可访问对象的访问限制（如私有构造器、字段、方法等）。
     *
     * @param accessible 待操作的可访问对象
     */
    void makeAccessible(AccessibleObject accessible);

    /**
     * 判断指定可访问对象当前是否可直接访问，无需再取消访问限制：public 成员且声明类为 public（字段还要求非
     * final）时天然可访问，否则以 setAccessible 标记为准。注意与 {@link AccessibleObject#isAccessible()} 的区别——
     * 后者仅返回 setAccessible 标记，public 成员未调用过 setAccessible 时它返回 {@code false}，但实际可直接访问。
     *
     * @param accessible 待检查的可访问对象
     * @return 可直接访问返回 {@code true}，否则返回 {@code false}
     */
    boolean isAccessible(AccessibleObject accessible);
    // endregion ======== 可访问性 ========


    // region ======== 字段 ========
    /**
     * 获取指定类及其父类中所有可访问的字段（含非 public，已取消访问限制）。收集规则：本类声明的全部字段 + 父类中非
     * private 的字段，按字段名去重（子类优先）。
     *
     * @param clazz 待访问的类
     * @return 字段数组
     */
    Field[] getAllFields(Class<?> clazz);

    /**
     * 判断指定类中是否存在指定名称的字段（沿父类链向上查找）。
     *
     * @param clazz     待访问的类
     * @param fieldName 字段名
     * @return 存在返回 {@code true}
     */
    boolean hasField(Class<?> clazz, String fieldName);

    /**
     * 按字段名获取指定类中匹配的字段（沿父类链向上查找），未找到返回 {@code null}。
     *
     * @param clazz     待访问的类
     * @param fieldName 字段名
     * @return 匹配到的字段，未找到返回 {@code null}
     */
    Field getField(Class<?> clazz, String fieldName);

    /**
     * 按字段名获取指定类中匹配的字段（沿父类链向上查找），未找到时抛出异常。
     *
     * @param clazz     待访问的类
     * @param fieldName 字段名
     * @return 匹配到的字段
     * @throws RuntimeException 找不到指定字段时抛出（cause 为 {@code NoSuchFieldException}）
     */
    Field getFieldOrThrow(Class<?> clazz, String fieldName);

    /**
     * 获取指定类自身声明的所有字段（不含父类，含非 public）。
     *
     * @param clazz 待访问的类
     * @return 字段数组
     */
    Field[] getDeclaredFields(Class<?> clazz);

    /**
     * 获取指定类及其父类中所有 public 字段。
     *
     * @param clazz 待访问的类
     * @return 字段数组
     * @deprecated 该语义与 {@link Class#getFields()} 完全一致，请直接使用 JDK API；
     *             常用的"全层次可访问字段"请用 {@link #getAllFields(Class)}
     */
    @Deprecated
    Field[] getFields(Class<?> clazz);

    /**
     * 获取指定类及其父类中所有可访问的字段（含非 public，已取消访问限制）。
     *
     * @param clazz 待访问的类
     * @return 字段数组
     * @deprecated 请用 {@link #getAllFields(Class)}
     */
    @Deprecated
    Field[] getAccessibleFields(Class<?> clazz);
    // endregion ======== 字段 ========


    // region ======== 构造器 ========
    /**
     * 获取指定类的所有构造器（自身声明的全部构造器，含 public 与非 public）。
     *
     * @param clazz 待访问的类
     * @return 构造器数组
     */
    Constructor<?>[] getConstructors(Class<?> clazz);

    /**
     * 按形参类型获取指定类中匹配的构造器（精确匹配失败时尝试相似匹配），未找到返回 {@code null}。
     *
     * @param clazz          待访问的类
     * @param parameterTypes 构造器的形参类型
     * @param <T>            构造器构造的对象类型
     * @return 匹配到的构造器，未找到返回 {@code null}
     */
    <T> Constructor<T> getConstructor(Class<T> clazz, Class<?>... parameterTypes);

    /**
     * 按形参类型获取指定类中匹配的构造器（精确匹配失败时尝试相似匹配），未找到时抛出异常。
     *
     * @param clazz          待访问的类
     * @param parameterTypes 构造器的形参类型
     * @param <T>            构造器构造的对象类型
     * @return 匹配到的构造器
     * @throws RuntimeException 找不到匹配的构造器时抛出（cause 为 {@code NoSuchMethodException}）
     */
    <T> Constructor<T> getConstructorOrThrow(Class<T> clazz, Class<?>... parameterTypes);
    // endregion ======== 构造器 ========


    // region ======== 方法 ========
    /**
     * 获取指定类及其父类中所有可访问的方法（含非 public，已取消访问限制）。收集规则：本类声明的全部方法 + 父类中非
     * private 的方法，按"方法名 + 形参类型"去重（子类优先）。
     *
     * @param clazz 待访问的类
     * @return 方法数组
     */
    Method[] getAllMethods(Class<?> clazz);

    /**
     * 按方法名与形参类型获取指定类中精确匹配的方法（沿父类链向上查找），未找到返回 {@code null}。
     *
     * @param clazz          待访问的类
     * @param methodName     方法名
     * @param parameterTypes 形参类型
     * @return 匹配到的方法，未找到返回 {@code null}
     */
    Method getMethod(Class<?> clazz, String methodName, Class<?>... parameterTypes);

    /**
     * 按方法名与形参类型获取指定类中精确匹配的方法（沿父类链向上查找），未找到时抛出异常。
     *
     * @param clazz          待访问的类
     * @param methodName     方法名
     * @param parameterTypes 形参类型
     * @return 匹配到的方法
     * @throws RuntimeException 找不到匹配的方法时抛出（cause 为 {@code NoSuchMethodException}）
     */
    Method getMethodOrThrow(Class<?> clazz, String methodName, Class<?>... parameterTypes);

    /**
     * 按方法名与形参类型获取指定类中相似匹配的方法，参数类型不完全一致时亦尽力匹配（基本类型与包装类型互通、实参为
     * {@code null} 时视为可匹配），未找到返回 {@code null}。
     *
     * @param clazz          待访问的类
     * @param methodName     方法名
     * @param parameterTypes 形参类型
     * @return 匹配到的方法，未找到返回 {@code null}
     */
    Method getSimilarMethod(Class<?> clazz, String methodName, Class<?>... parameterTypes);

    /**
     * 按方法名与形参类型获取指定类中相似匹配的方法（匹配规则同
     * {@link #getSimilarMethod(Class, String, Class[])}），未找到时抛出异常。
     *
     * @param clazz          待访问的类
     * @param methodName     方法名
     * @param parameterTypes 形参类型
     * @return 匹配到的方法
     * @throws RuntimeException 找不到匹配的方法时抛出（cause 为 {@code NoSuchMethodException}）
     */
    Method getSimilarMethodOrThrow(Class<?> clazz, String methodName, Class<?>... parameterTypes);

    /**
     * 获取指定类自身声明的所有方法（不含父类，含非 public）。
     *
     * @param clazz 待访问的类
     * @return 方法数组
     */
    Method[] getDeclaredMethods(Class<?> clazz);

    /**
     * 获取指定类及其父类中所有 public 方法。
     *
     * @param clazz 待访问的类
     * @return 方法数组
     * @deprecated 该语义与 {@link Class#getMethods()} 完全一致，请直接使用 JDK API；
     *             常用的"全层次可访问方法"请用 {@link #getAllMethods(Class)}
     */
    @Deprecated
    Method[] getMethods(Class<?> clazz);

    /**
     * 获取指定类及其父类中所有可访问的方法（含非 public，已取消访问限制）。
     *
     * @param clazz 待访问的类
     * @return 方法数组
     * @deprecated 请用 {@link #getAllMethods(Class)}
     */
    @Deprecated
    Method[] getAccessibleMethods(Class<?> clazz);
    // endregion ======== 方法 ========


    // region ======== 操作 ========
    /**
     * 获取指定类的所有属性描述符，用于读写 JavaBean 属性。
     *
     * @param clazz 待访问的类
     * @return 属性描述符数组
     */
    PropertyDescriptor[] getPropertyDescriptors(Class<?> clazz);

    /**
     * 通过反射调用构造器创建实例（构造器不可访问时自动取消访问限制）。
     *
     * @param clazz 待实例化的类
     * @param args  传入构造器的实参
     * @param <T>   实例类型
     * @return 创建出的实例
     * @throws RuntimeException 实例化失败时抛出（找不到构造器、不可访问、底层构造器抛出异常等）
     */
    <T> T newInstance(Class<T> clazz, Object... args);

    /**
     * 按字段名读取目标对象的字段值（沿父类链向上查找，私有字段自动取消访问限制）。
     *
     * @param target    目标对象；目标为 {@code Class} 对象时读取静态字段
     * @param fieldName 字段名
     * @param <T>       返回值类型（按赋值目标推断）
     * @return 字段值
     * @throws RuntimeException 找不到字段或读取失败时抛出
     */
    <T> T getFieldValue(Object target, String fieldName);

    /**
     * 读取指定字段的目标对象取值（私有字段自动取消访问限制）。
     *
     * @param target 字段所属对象；静态字段时可传 {@code null}
     * @param field  目标字段
     * @param <T>    返回值类型（按赋值目标推断）
     * @return 字段值
     * @throws RuntimeException 读取失败时抛出
     */
    <T> T getFieldValue(Object target, Field field);

    /**
     * 按字段名写入目标对象的字段值（沿父类链向上查找，私有字段自动取消访问限制）。
     *
     * @param target    目标对象；目标为 {@code Class} 对象时写入静态字段
     * @param fieldName 字段名
     * @param value     待写入的值
     * @throws RuntimeException 找不到字段或写入失败时抛出
     */
    void setFieldValue(Object target, String fieldName, Object value);

    /**
     * 向指定字段写入目标对象的字段值（私有字段自动取消访问限制）。
     *
     * @param target 字段所属对象；静态字段时可传 {@code null}
     * @param field  目标字段
     * @param value  待写入的值
     * @throws RuntimeException 写入失败时抛出
     */
    void setFieldValue(Object target, Field field, Object value);

    /**
     * 按方法名反射调用目标对象的方法（按实参类型相似匹配，含私有方法）。
     *
     * @param target     目标对象；目标为 {@code Class} 对象时按静态方法调用
     * @param methodName 方法名
     * @param args       调用实参
     * @param <T>        返回值类型（按赋值目标推断）
     * @return 方法返回值
     * @throws RuntimeException 找不到方法或调用失败时抛出；目标方法抛出运行时异常时原样抛出，
     *                           抛出受检异常时包装后抛出（cause 为业务异常）
     */
    <T> T invoke(Object target, String methodName, Object... args);

    /**
     * 反射调用指定方法（私有方法自动取消访问限制）。
     *
     * @param target 方法所属对象；静态方法时可传 {@code null}
     * @param method 目标方法
     * @param args   调用实参
     * @param <T>    返回值类型（按赋值目标推断）
     * @return 方法返回值
     * @throws RuntimeException 调用失败时抛出；目标方法抛出运行时异常时原样抛出，
     *                           抛出受检异常时包装后抛出（cause 为业务异常）
     */
    <T> T invoke(Object target, Method method, Object... args);

    /**
     * 按方法名反射调用指定类的静态方法（按实参类型相似匹配，含私有方法）。
     *
     * @param clazz      目标类
     * @param methodName 方法名
     * @param args       调用实参
     * @param <T>        返回值类型（按赋值目标推断）
     * @return 方法返回值
     * @throws RuntimeException 找不到方法或调用失败时抛出，异常行为同 {@link #invoke(Object, String, Object...)}
     */
    <T> T invokeStatic(Class<?> clazz, String methodName, Object... args);
    // endregion ======== 操作 ========

}
