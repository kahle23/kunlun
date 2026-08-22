/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.db.jdbc;

import kunlun.logging.Logger;
import kunlun.logging.LoggerFactory;
import kunlun.util.Assert;

import java.util.Collection;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;

/**
 * JDBC 工具集门面：统一持有/管理各类 JDBC 相关工具。
 * <p>当前内置：{@link FieldValueClearer}（字段值清除器）。
 * <p>每类工具按实现类缓存实例，命名统一：getDefaultXxx()/setXxx() 访问默认实现、
 * getXxx(Class) 取指定实现、registerXxx/unregisterXxx 注册管理，以及 use+clear/use+append
 * 合并入参的便捷静态方法。
 * @author Kahle
 */
public final class JdbcUtil {
    private static final Logger log = LoggerFactory.getLogger(JdbcUtil.class);
    private static final Map<Class<? extends FieldValueClearer>, FieldValueClearer> FIELD_VALUE_CLEARER_MAP
            = new ConcurrentHashMap<Class<? extends FieldValueClearer>, FieldValueClearer>();
    private static volatile FieldValueClearer defaultFieldValueClearer;


    // region ======== 字段值清除器：访问与注册 ========

    /**
     * 取默认字段值清除器：已设置则用，否则经 ServiceLoader 找，找不到抛异常。
     * <p>实现方可通过 {@link #setDefaultFieldValueClearer(FieldValueClearer)} 覆盖，
     * 或在 META-INF/services/kunlun.db.jdbc.FieldValueClearer 注册实现类。
     */
    public static FieldValueClearer getDefaultFieldValueClearer() {
        // 快速路径：已设置则直接返回，避免锁竞争
        FieldValueClearer current = defaultFieldValueClearer;
        if (current != null) {
            return current;
        }
        // 双重检查初始化：进锁后再判一次，仍为空则经 SPI 加载
        synchronized (JdbcUtil.class) {
            if (defaultFieldValueClearer == null) {
                for (FieldValueClearer c : ServiceLoader.load(FieldValueClearer.class)) {
                    defaultFieldValueClearer = c;
                    break;
                }
            }
            Assert.state(defaultFieldValueClearer != null, "No FieldValueClearer found via SPI; "
                    + "register via setDefaultFieldValueClearer(...) or add META-INF/services/kunlun.db.jdbc.FieldValueClearer. ");
            return defaultFieldValueClearer;
        }
    }

    public static void setDefaultFieldValueClearer(FieldValueClearer instance) {
        Assert.notNull(instance, "Parameter \"instance\" must not null. ");
        defaultFieldValueClearer = instance;
    }

    /**
     * 取指定实现：已注册/缓存则直接返回，否则反射无参构造并缓存。
     */
    public static FieldValueClearer getFieldValueClearer(Class<? extends FieldValueClearer> implClass) {
        // 未指定实现类则回退默认实现
        if (implClass == null) {
            return getDefaultFieldValueClearer();
        }
        // 取缓存实例，命中则直接返回
        FieldValueClearer clearer = FIELD_VALUE_CLEARER_MAP.get(implClass);
        if (clearer != null) {
            return clearer;
        }
        // 未命中则反射无参构造一个
        try {
            clearer = implClass.newInstance();
        } catch (Exception e) {
            throw new IllegalStateException("JdbcUtil: cannot instantiate field value clearer " + implClass, e);
        }
        // 双重检查缓存：进锁后再取一次，仍无则写入并返回
        synchronized (JdbcUtil.class) {
            FieldValueClearer prev = FIELD_VALUE_CLEARER_MAP.get(implClass);
            if (prev != null) {
                return prev;
            }
            FIELD_VALUE_CLEARER_MAP.put(implClass, clearer);
            return clearer;
        }
    }

    public static FieldValueClearer registerFieldValueClearer(FieldValueClearer instance) {
        Assert.notNull(instance, "Parameter \"instance\" must not null. ");
        return registerFieldValueClearer(instance.getClass(), instance);
    }

    public static FieldValueClearer registerFieldValueClearer(Class<? extends FieldValueClearer> key,
                                                              FieldValueClearer instance) {
        Assert.notNull(key, "Parameter \"key\" must not null. ");
        Assert.notNull(instance, "Parameter \"instance\" must not null. ");
        FIELD_VALUE_CLEARER_MAP.put(key, instance);
        return instance;
    }

    public static void unregisterFieldValueClearer(Class<? extends FieldValueClearer> implClass) {
        Assert.notNull(implClass, "Parameter \"implClass\" must not null. ");
        FIELD_VALUE_CLEARER_MAP.remove(implClass);
    }

    // endregion


    // region ======== 执行型（use+clear 合并） ========

    public static boolean clearFields(Object service, Object id, Collection<String> fields) {

        return getDefaultFieldValueClearer().clear(service, id, fields);
    }

    public static boolean clearFields(Class<? extends FieldValueClearer> impl,
                                      Object service, Object id, Collection<String> fields) {
        return getFieldValueClearer(impl).clear(service, id, fields);
    }

    public static boolean clearFields(Object service, Class<?> entityClass,
                                      Object id, Collection<String> fields) {
        return getDefaultFieldValueClearer().clear(service, entityClass, id, fields);
    }

    public static boolean clearFields(Class<? extends FieldValueClearer> impl,
                                      Object service, Class<?> entityClass,
                                      Object id, Collection<String> fields) {
        return getFieldValueClearer(impl).clear(service, entityClass, id, fields);
    }

    // endregion


    // region ======== 追加型（use+append 合并，不执行） ========

    public static Object appendClearFields(Object wrapper, Class<?> entityClass, Collection<String> fields) {

        return getDefaultFieldValueClearer().appendClearFields(wrapper, entityClass, fields);
    }

    public static Object appendClearFields(Class<? extends FieldValueClearer> impl,
                                           Object wrapper, Class<?> entityClass, Collection<String> fields) {
        return getFieldValueClearer(impl).appendClearFields(wrapper, entityClass, fields);
    }

    // endregion


    // region ======== 私有的构造方法 ========

    private JdbcUtil() {

        throw new UnsupportedOperationException("Don't allow instantiation. ");
    }

    // endregion

}
