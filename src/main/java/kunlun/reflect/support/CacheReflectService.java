/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.reflect.support;

import kunlun.cache.Cache;
import kunlun.cache.support.SimpleCache;
import kunlun.cache.support.SimpleCacheConfig;
import kunlun.data.ReferenceType;
import kunlun.exception.UncheckedException;
import kunlun.reflect.ReflectService;
import kunlun.util.Assert;

import java.beans.PropertyDescriptor;
import java.lang.reflect.*;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.Callable;

import static kunlun.common.constant.Numbers.ZERO;

/**
 * 基于 {@link Cache} 的 {@link ReflectService} 静态代理实现。包装任意 {@link ReflectService} 后端，
 * 以 Class 为键缓存类反射元数据（全字段、全方法、属性描述符及按名、按签名索引），查找类方法命中缓存，
 * 成员级原始操作直接委托被代理对象执行（组合层的名字版操作经模板方法自动走本类的缓存查找）。
 * 默认内存缓存（无 TTL、不限容量、SOFT 值引用，内存压力下可被回收，miss 后重算，无正确性问题），
 * 可通过 {@link #setCache(Cache)} 或构造器注入自定义缓存实现，注入 {@link kunlun.cache.support.NoCache} 可禁用缓存。
 * <p>典型用法：{@code new CacheReflectService(new JdkReflectService())}。
 *
 * @author Kahle
 */
public class CacheReflectService extends AbstractReflectService {

    private final ReflectService reflectService;
    private Cache cache;

    public CacheReflectService(ReflectService reflectService) {

        this(reflectService, new SimpleCache(new SimpleCacheConfig(ReferenceType.SOFT, null)));
    }

    public CacheReflectService(ReflectService reflectService, Cache cache) {

        Assert.notNull(reflectService, "Parameter \"reflectService\" must not null. ");
        Assert.notNull(cache, "Parameter \"cache\" must not null. ");
        this.reflectService = reflectService;
        this.cache = cache;
    }

    public ReflectService getDelegate() {

        return reflectService;
    }

    public Cache getCache() {

        return cache;
    }

    public void setCache(Cache cache) {

        Assert.notNull(cache, "Parameter \"cache\" must not null. ");
        this.cache = cache;
    }


    // region ======== 字段 ========

    @Override
    public Field[] getAllFields(Class<?> clazz) {
        Assert.notNull(clazz, "Parameter \"clazz\" must not null. ");
        return getCacheEntry(clazz).allFields.clone();
    }

    @Override
    public Field getField(Class<?> clazz, String fieldName) {
        Assert.notBlank(fieldName, "Parameter \"fieldName\" must not blank. ");
        Assert.notNull(clazz, "Parameter \"clazz\" must not null. ");
        // 优先获取类层次（含接口）中的 public 字段，失败则在缓存的类元数据中按名查找（含非 public）
        try {
            return clazz.getField(fieldName);
        }
        catch (NoSuchFieldException e) {
            CacheEntry entry = getCacheEntry(clazz);
            return entry.fieldByName.get(fieldName);
        }
    }

    // endregion ======== 字段 ========


    // region ======== 构造器 ========

    @Override
    public <T> Constructor<T> getConstructor(Class<T> clazz, Class<?>... parameterTypes) {

        return reflectService.getConstructor(clazz, parameterTypes);
    }

    // endregion ======== 构造器 ========


    // region ======== 方法 ========

    @Override
    public Method[] getAllMethods(Class<?> clazz) {
        Assert.notNull(clazz, "Parameter \"clazz\" must not null. ");
        return getCacheEntry(clazz).allMethods.clone();
    }

    @Override
    public Method getMethod(Class<?> clazz, String methodName, Class<?>... parameterTypes) {
        Assert.notBlank(methodName, "Parameter \"methodName\" must not blank. ");
        Assert.notNull(clazz, "Parameter \"clazz\" must not null. ");
        // 优先在类层次（含接口）中精确匹配 public 方法，失败则在缓存的类元数据中按签名查找（含非 public）
        try {
            return clazz.getMethod(methodName, parameterTypes);
        }
        catch (NoSuchMethodException e) {
            CacheEntry entry = getCacheEntry(clazz);
            String signature = buildMethodSignature(methodName, parameterTypes);
            return entry.methodBySignature.get(signature);
        }
    }

    @Override
    public Method getSimilarMethod(Class<?> clazz, String methodName, Class<?>... parameterTypes) {
        Assert.notBlank(methodName, "Parameter \"methodName\" must not blank. ");
        Assert.notNull(clazz, "Parameter \"clazz\" must not null. ");
        // 相似指实参基本类型与包装类型可互相转换。第一优先级在类层次（含接口）的 public 方法中查找，
        // 第二优先级在缓存的全层次可访问方法中查找（含非 public）
        for (Method method : clazz.getMethods()) {
            if (methodName.equals(method.getName()) &&
                    matchParameterTypes(method.getParameterTypes(), parameterTypes)) {
                return method;
            }
        }
        for (Method method : getCacheEntry(clazz).allMethods) {
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
        return getCacheEntry(clazz).propertyDescriptors.clone();
    }

    @Override
    public <T> T newInstance(Class<T> clazz, Object... args) {

        return reflectService.newInstance(clazz, args);
    }

    @Override
    public <T> T getFieldValue(Object target, Field field) {

        return reflectService.getFieldValue(target, field);
    }

    @Override
    public void setFieldValue(Object target, Field field, Object value) {

        reflectService.setFieldValue(target, field, value);
    }

    @Override
    public <T> T invoke(Object target, Method method, Object... args) {

        return reflectService.invoke(target, method, args);
    }

    // endregion ======== 操作 ========


    // region ======== 内部工具 ========

    /**
     * 获取指定类的反射元数据（get-or-load，经缓存）。
     */
    protected CacheEntry getCacheEntry(final Class<?> clazz) {
        Object entry = cache.get(clazz, new Callable<Object>() {
            @Override
            public Object call() {
                return buildCacheEntry(clazz);
            }
        });
        if (entry == null) {
            throw new UncheckedException("Build class metadata cache entry failed for \"" + clazz.getName() + "\". ");
        }
        return (CacheEntry) entry;
    }

    /**
     * 基于被代理对象收集指定类的反射元数据：全字段按名建索引，全方法按"名 + 形参类型"建索引（先见者留，子类优先）。
     */
    protected CacheEntry buildCacheEntry(Class<?> clazz) {
        Map<String, Field> fieldByName = new LinkedHashMap<String, Field>();
        for (Field field : reflectService.getAllFields(clazz)) {
            fieldByName.put(field.getName(), field);
        }
        Map<String, Method> methodBySignature = new LinkedHashMap<String, Method>();
        for (Method method : reflectService.getAllMethods(clazz)) {
            String signature = buildMethodSignature(method.getName(), method.getParameterTypes());
            methodBySignature.put(signature, method);
        }
        CacheEntry entry = new CacheEntry();
        entry.allFields = fieldByName.values().toArray(new Field[ZERO]);
        entry.fieldByName = fieldByName;
        entry.allMethods = methodBySignature.values().toArray(new Method[ZERO]);
        entry.methodBySignature = methodBySignature;
        entry.propertyDescriptors = reflectService.getPropertyDescriptors(clazz);
        return entry;
    }

    /**
     * 类反射元数据缓存条目：同一 Class 的元数据在其生命周期内不可变，缓存后无需失效。
     */
    protected static class CacheEntry {
        private Field[] allFields = new Field[ZERO];
        private Map<String, Field> fieldByName = new LinkedHashMap<String, Field>();
        private Method[] allMethods = new Method[ZERO];
        private Map<String, Method> methodBySignature = new LinkedHashMap<String, Method>();
        private PropertyDescriptor[] propertyDescriptors = new PropertyDescriptor[ZERO];
    }

    // endregion ======== 内部工具 ========

}
