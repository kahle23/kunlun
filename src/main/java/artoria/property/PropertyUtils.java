package artoria.property;

import artoria.common.constant.Nulls;
import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import artoria.util.Assert;

import java.util.Map;

import static artoria.common.constant.Numbers.ZERO;
import static artoria.common.constant.Symbols.EMPTY_STRING;

/**
 * The property tools.
 * @author Kahle
 */
public class PropertyUtils {
    private static final Logger log = LoggerFactory.getLogger(PropertyUtils.class);
    private static volatile PropertyProvider propertyProvider;

    public static PropertyProvider getPropertyProvider() {
        if (propertyProvider != null) { return propertyProvider; }
        synchronized (PropertyUtils.class) {
            if (propertyProvider != null) { return propertyProvider; }
            PropertyUtils.setPropertyProvider(new SimplePropertyProvider());
            return propertyProvider;
        }
    }

    public static void setPropertyProvider(PropertyProvider propertyProvider) {
        Assert.notNull(propertyProvider, "Parameter \"propertyProvider\" must not null. ");
        log.info("Set property provider: {}", propertyProvider.getClass().getName());
        PropertyUtils.propertyProvider = propertyProvider;
    }

    public static void registerSource(PropertySource propertySource) {

        getPropertyProvider().registerSource(propertySource);
    }

    public static void deregisterSource(String sourceName) {

        getPropertyProvider().deregisterSource(sourceName);
    }

    public static PropertySource getPropertySource(String sourceName) {

        return getPropertyProvider().getPropertySource(sourceName);
    }

    public static String getString(String name) {

        return getProperty(EMPTY_STRING, name, String.class, Nulls.STR);
    }

    // There is no "getString(String name, String defaultValue)".

    public static String getString(String source, String name) {

        return getProperty(source, name, String.class, Nulls.STR);
    }

    public static String getString(String source, String name, String defaultValue) {

        return getProperty(source, name, String.class, defaultValue);
    }

    public static boolean getBoolean(String name) {

        return getProperty(EMPTY_STRING, name, Boolean.class, false);
    }

    public static boolean getBoolean(String name, boolean defaultValue) {

        return getProperty(EMPTY_STRING, name, Boolean.class, defaultValue);
    }

    public static boolean getBoolean(String source, String name) {

        return getProperty(source, name, Boolean.class, false);
    }

    public static boolean getBoolean(String source, String name, boolean defaultValue) {

        return getProperty(source, name, Boolean.class, defaultValue);
    }

    public static int getInteger(String name) {

        return getProperty(EMPTY_STRING, name, Integer.class, ZERO);
    }

    public static int getInteger(String name, int defaultValue) {

        return getProperty(EMPTY_STRING, name, Integer.class, defaultValue);
    }

    public static int getInteger(String source, String name) {

        return getProperty(source, name, Integer.class, ZERO);
    }

    public static int getInteger(String source, String name, int defaultValue) {

        return getProperty(source, name, Integer.class, defaultValue);
    }

    public static <T> T getRequiredProperty(String name, Class<T> targetType) {

        return getRequiredProperty(EMPTY_STRING, name, targetType);
    }

    public static <T> T getRequiredProperty(String source, String name, Class<T> targetType) {

        return getPropertyProvider().getRequiredProperty(source, name, targetType);
    }

    public static <T> T getProperty(String name, Class<T> targetType) {

        return getProperty(EMPTY_STRING, name, targetType, null);
    }

    public static <T> T getProperty(String name, Class<T> targetType, T defaultValue) {

        return getProperty(EMPTY_STRING, name, targetType, defaultValue);
    }

    public static <T> T getProperty(String source, String name, Class<T> targetType) {

        return getProperty(source, name, targetType, null);
    }

    public static <T> T getProperty(String source, String name, Class<T> targetType, T defaultValue) {

        return getPropertyProvider().getProperty(source, name, targetType, defaultValue);
    }

    public static void setProperties(String source, Map<?, ?> properties) {

        getPropertyProvider().setProperties(source, properties);
    }

    public static Map<String, Object> getProperties(String source) {

        return getPropertyProvider().getProperties(source);
    }

    public static boolean containsProperty(String name) {

        return containsProperty(EMPTY_STRING, name);
    }

    public static boolean containsProperty(String source, String name) {

        return getPropertyProvider().containsProperty(source, name);
    }

    public static Object getProperty(String name) {

        return getProperty(EMPTY_STRING, name, Nulls.OBJ);
    }

    public static Object getProperty(String name, Object defaultValue) {

        return getProperty(EMPTY_STRING, name, defaultValue);
    }

    public static Object getProperty(String source, String name) {

        return getProperty(source, name, Nulls.OBJ);
    }

    public static Object getProperty(String source, String name, Object defaultValue) {

        return getPropertyProvider().getProperty(source, name, defaultValue);
    }

    public static Object setProperty(String name, Object value) {

        return setProperty(EMPTY_STRING, name, value);
    }

    public static Object setProperty(String source, String name, Object value) {

        return getPropertyProvider().setProperty(source, name, value);
    }

    public static Object removeProperty(String name) {

        return removeProperty(EMPTY_STRING, name);
    }

    public static Object removeProperty(String source, String name) {

        return getPropertyProvider().removeProperty(source, name);
    }

}
