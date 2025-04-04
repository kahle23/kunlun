/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.util;

import java.util.Collection;
import java.util.Map;

/**
 * The object tools.
 * @author Kahle
 */
public class ObjUtil {

    public static boolean isNull(Object obj) {

        return obj == null;
    }

    public static <T> T ifNull(T value, T defaultValue) {

        return value != null ? value : defaultValue;
    }

    public static boolean isArray(Object obj) {

        return obj != null && obj.getClass().isArray();
    }

    public static boolean isEmpty(Object obj) {
        if (obj == null) { return true; }
        if (obj instanceof CharSequence) {
            return StrUtil.isEmpty((CharSequence) obj);
        }
        if (obj instanceof Collection) {
            //noinspection unchecked,rawtypes
            return CollUtil.isEmpty((Collection) obj);
        }
        if (obj instanceof Map) {
            //noinspection unchecked,rawtypes
            return MapUtil.isEmpty((Map) obj);
        }
        if (ArrayUtil.isArray(obj)) {
            return ArrayUtil.isEmpty(obj);
        }
        return false;
    }

    public static boolean isNotEmpty(Object obj) {

        return !isEmpty(obj);
    }

    public static boolean equals(Object obj1, Object obj2) {

        return obj1 == null ? obj2 == null : obj1.equals(obj2);
    }

    @SuppressWarnings("unchecked")
    public static <T> T cast(Object object) {

        return (T) object;
    }

    @SuppressWarnings("unchecked")
    public static <T> T cast(Object object, Class<T> clazz) {

        return (T) object;
    }

}
