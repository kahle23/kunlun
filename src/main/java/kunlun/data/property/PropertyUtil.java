/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.data.property;

import kunlun.common.constant.Nil;
import kunlun.data.property.support.SimplePropertySource;
import kunlun.logging.Logger;
import kunlun.logging.LoggerFactory;
import kunlun.util.Assert;

import java.util.Map;
import java.util.Set;

import static kunlun.common.constant.Numbers.ZERO;
import static kunlun.common.constant.Symbols.EMPTY_STRING;

/**
 * The property tools.
 * @author Kahle
 */
public class PropertyUtil {
    private static final Logger log = LoggerFactory.getLogger(PropertyUtil.class);
    private static volatile PropertyProvider propertyProvider;

    public static PropertyProvider getPropertyProvider() {
        if (propertyProvider != null) { return propertyProvider; }
        synchronized (PropertyUtil.class) {
            if (propertyProvider != null) { return propertyProvider; }
            PropertyUtil.setPropertyProvider(new SimplePropertyProvider());
            registerSource(EMPTY_STRING, new SimplePropertySource());
            return propertyProvider;
        }
    }

    public static void setPropertyProvider(PropertyProvider propertyProvider) {
        Assert.notNull(propertyProvider, "Parameter \"propertyProvider\" must not null. ");
        log.debug("Set property provider: {}", propertyProvider.getClass().getName());
        PropertyUtil.propertyProvider = propertyProvider;
    }


    // region ======== source manage ========

    public static String getDefaultSourceName() {

        return getPropertyProvider().getDefaultSourceName();
    }

    public static void setDefaultSourceName(String defaultSourceName) {

        getPropertyProvider().setDefaultSourceName(defaultSourceName);
    }

    public static void registerSource(String sourceName, PropertySource propertySource) {

        getPropertyProvider().registerSource(sourceName, propertySource);
    }

    public static void deregisterSource(String sourceName) {

        getPropertyProvider().deregisterSource(sourceName);
    }

    public static PropertySource getPropertySource(String sourceName) {

        return getPropertyProvider().getPropertySource(sourceName);
    }

    // endregion


    // region ======== source methods ========

    public static boolean containsProperty(String source, String name) {

        return getPropertyProvider().containsProperty(source, name);
    }

    public static Object getProperty(String source, String name) {

        return getPropertyProvider().getProperty(source, name);
    }

    public static Object setProperty(String source, String name, Object value) {

        return getPropertyProvider().setProperty(source, name, value);
    }

    public static Object removeProperty(String source, String name) {

        return getPropertyProvider().removeProperty(source, name);
    }

    public static Set<String> getPropertyNames(String source) {

        return getPropertyProvider().getPropertyNames(source);
    }

    public static Map<String, Object> getProperties(String source) {

        return getPropertyProvider().getProperties(source);
    }

    public static void setProperties(String source, Map<?, ?> properties) {

        getPropertyProvider().setProperties(source, properties);
    }

    // endregion


    // region ======== extension methods ========

    public static <T> T getRequiredProperty(String source, String name, Class<T> targetType) {

        return getPropertyProvider().getRequiredProperty(source, name, targetType);
    }

    public static <T> T getProperty(String source, String name, Class<T> targetType, T defaultValue) {

        return getPropertyProvider().getProperty(source, name, targetType, defaultValue);
    }

    public static Object getProperty(String source, String name, Object defaultValue) {

        return getPropertyProvider().getProperty(source, name, defaultValue);
    }

    // endregion


    // region ======== extension methods 1 ========

    public static boolean contains(String source, String name) {

        return getPropertyProvider().containsProperty(source, name);
    }

    public static Object get(String source, String name) {

        return getPropertyProvider().getProperty(source, name);
    }

    public static Object set(String source, String name, Object value) {

        return getPropertyProvider().setProperty(source, name, value);
    }

    public static Object remove(String source, String name) {

        return getPropertyProvider().removeProperty(source, name);
    }

    public static <T> T getOrThrow(String source, String name, Class<T> targetType) {

        return getPropertyProvider().getRequiredProperty(source, name, targetType);
    }

    public static <T> T get(String source, String name, Class<T> targetType) {

        return get(source, name, targetType, Nil.<T>g());
    }

    public static <T> T get(String source, String name, Class<T> targetType, T defaultValue) {

        return getPropertyProvider().getProperty(source, name, targetType, defaultValue);
    }

    public static Object get(String source, String name, Object defaultValue) {

        return getPropertyProvider().getProperty(source, name, defaultValue);
    }

    // endregion


    // region ======== extension methods 2 ========

    public static boolean contains(String name) {

        return contains(EMPTY_STRING, name);
    }

    public static Object get(String name) {

        return get(EMPTY_STRING, name);
    }

    public static Object set(String name, Object value) {

        return set(EMPTY_STRING, name, value);
    }

    public static Object remove(String name) {

        return remove(EMPTY_STRING, name);
    }

    public static <T> T getOrThrow(String name, Class<T> targetType) {

        return getOrThrow(EMPTY_STRING, name, targetType);
    }

    public static <T> T get(String name, Class<T> targetType) {

        return get(EMPTY_STRING, name, targetType);
    }

    public static <T> T get(String name, Class<T> targetType, T defaultValue) {

        return get(EMPTY_STRING, name, targetType, defaultValue);
    }

    public static Object get(String name, Object defaultValue) {

        return get(EMPTY_STRING, name, defaultValue);
    }

    // endregion


    // region ======== get to type ========

    public static String getStr(String source, String name, String defaultValue) {

        return get(source, name, String.class, defaultValue);
    }

    public static String getStr(String source, String name) {

        return get(source, name, String.class, Nil.STR);
    }

    // There is no "getStr(String name, String defaultValue)".

    public static String getStr(String name) {

        return get(EMPTY_STRING, name, String.class, Nil.STR);
    }

    public static boolean getBool(String source, String name, boolean defaultValue) {

        return get(source, name, Boolean.class, defaultValue);
    }

    public static boolean getBool(String source, String name) {

        return get(source, name, Boolean.class, Boolean.FALSE);
    }

    public static boolean getBool(String name, boolean defaultValue) {

        return get(EMPTY_STRING, name, Boolean.class, defaultValue);
    }

    public static boolean getBool(String name) {

        return get(EMPTY_STRING, name, Boolean.class, Boolean.FALSE);
    }

    public static int getInt(String source, String name, int defaultValue) {

        return get(source, name, Integer.class, defaultValue);
    }

    public static int getInt(String source, String name) {

        return get(source, name, Integer.class, ZERO);
    }

    public static int getInt(String name, int defaultValue) {

        return get(EMPTY_STRING, name, Integer.class, defaultValue);
    }

    public static int getInt(String name) {

        return get(EMPTY_STRING, name, Integer.class, ZERO);
    }

    // endregion

}
