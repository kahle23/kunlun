/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.util;

import static kunlun.util.Assert.notNull;

/**
 * 转换工具类.
 * @author Zerox
 */
public class CastUtil {

    /**
     * 将指定的对象强制转换为返回值泛型类型.
     * @param value 传入的对象
     * @return 预期的结果
     */
    @SuppressWarnings("unchecked")
    public static <T> T cast(Object value) {

        return (T) value;
    }

    @Deprecated
    @SuppressWarnings("unchecked")
    public static <T> T cast(Object object, Class<T> clazz) {

        return (T) object;
    }

    /**
     * 将指定的对象转换为指定的类型，基于 Class.cast 方法.
     * @param targetType 指定的类型
     * @param value 指定的对象
     * @return 转换后的结果
     */
    public static <T> T castTo(Class<T> targetType, Object value) {

        return notNull(targetType).cast(value);
    }

}
