package artoria.util;

import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Map;

import static artoria.common.Constants.EIGHT;

/**
 * Class tools.
 * @author Kahle
 */
public class ClassUtils {
    /**
     * Map with primitive type as key and corresponding wrapper type as value.
     * For example: int.class -> Integer.class.
     */
    private static final Map<Class<?>, Class<?>> PRIMITIVE_TYPE_TO_WRAPPER_MAP;
    /**
     * Map with primitive wrapper type as key and corresponding primitive type as value.
     * For example: Integer.class -> int.class.
     */
    private static final Map<Class<?>, Class<?>> PRIMITIVE_WRAPPER_TYPE_MAP;

    static {
        Map<Class<?>, Class<?>> primitiveTypeToWrapperMap = new IdentityHashMap<Class<?>, Class<?>>();
        primitiveTypeToWrapperMap.put(char.class,     Character.class);
        primitiveTypeToWrapperMap.put(boolean.class,  Boolean.class);
        primitiveTypeToWrapperMap.put(byte.class,     Byte.class);
        primitiveTypeToWrapperMap.put(short.class,    Short.class);
        primitiveTypeToWrapperMap.put(int.class,      Integer.class);
        primitiveTypeToWrapperMap.put(long.class,     Long.class);
        primitiveTypeToWrapperMap.put(float.class,    Float.class);
        primitiveTypeToWrapperMap.put(double.class,   Double.class);
        PRIMITIVE_TYPE_TO_WRAPPER_MAP = Collections.unmodifiableMap(primitiveTypeToWrapperMap);
        Map<Class<?>, Class<?>> primitiveWrapperTypeMap = new IdentityHashMap<Class<?>, Class<?>>(EIGHT);
        for (Map.Entry<Class<?>, Class<?>> entry : primitiveTypeToWrapperMap.entrySet()) {
            primitiveWrapperTypeMap.put(entry.getValue(), entry.getKey());
        }
        PRIMITIVE_WRAPPER_TYPE_MAP = Collections.unmodifiableMap(primitiveWrapperTypeMap);
    }

    public static Class<?> getWrapper(Class<?> type) {
        Assert.notNull(type, "Parameter \"type\" must not null. ");
        if (!type.isPrimitive()) { return type; }
        if (type == void.class) { return type; }
        Class<?> result = PRIMITIVE_TYPE_TO_WRAPPER_MAP.get(type);
        return result != null ? result : type;
    }

    public static Class<?> getPrimitive(Class<?> type) {
        Assert.notNull(type, "Parameter \"type\" must not null. ");
        if (type.isPrimitive()) { return type; }
        Class<?> result = PRIMITIVE_WRAPPER_TYPE_MAP.get(type);
        return result != null ? result : type;
    }

    public static boolean isPresent(String className, ClassLoader classLoader) {

        return ClassUtils.isPresent(className, true, classLoader);
    }

    public static boolean isPresent(String className, boolean initialize, ClassLoader classLoader) {
        Assert.notNull(classLoader, "Parameter \"classLoader\" must not null. ");
        Assert.notNull(className, "Parameter \"className\" must not null. ");
        try {
            Class.forName(className, initialize, classLoader);
            return true;
        }
        catch (Throwable e) {
            return false;
        }
    }

}
