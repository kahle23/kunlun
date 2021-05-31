package artoria.util;

import java.lang.reflect.Array;
import java.util.*;

import static artoria.common.Constants.*;
import static java.lang.Boolean.FALSE;

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

    private static void addToClassHierarchy(Class<?> type, int index, boolean isArray,
                                            Set<Class<?>> visited, List<Class<?>> hierarchy) {
        if (isArray) {
            Object instance = Array.newInstance(type, ZERO);
            type = instance.getClass();
        }
        if (visited.add(type)) { hierarchy.add(index, type); }
    }

    public static List<Class<?>> getClassHierarchy(Class<?> type) {
        Assert.notNull(type, "Parameter \"type\" must not null. ");
        List<Class<?>> hierarchy = new ArrayList<Class<?>>(TWENTY);
        Set<Class<?>> visited = new HashSet<Class<?>>(TWENTY);
        boolean isArray = type.isArray();
        addToClassHierarchy(getWrapper(type), ZERO, FALSE, visited, hierarchy);
        // Handle the ordinary bean.
        for (int i = ZERO; i < hierarchy.size(); i++) {
            Class<?> candidate = hierarchy.get(i);
            candidate = isArray ? candidate.getComponentType() : getWrapper(candidate);
            Class<?> superclass = candidate.getSuperclass();
            boolean flag = superclass != null &&
                    superclass != Object.class && superclass != Enum.class;
            if (flag) {
                addToClassHierarchy(superclass, (i + ONE), isArray, visited, hierarchy);
            }
            Class<?>[] interfaces = candidate.getInterfaces();
            for (Class<?> clazz : interfaces) {
                addToClassHierarchy(clazz, hierarchy.size(), isArray, visited, hierarchy);
            }
        }
        // Handle the enumeration.
        if (Enum.class.isAssignableFrom(type)) {
            addToClassHierarchy(Enum.class, hierarchy.size(), isArray, visited, hierarchy);
            addToClassHierarchy(Enum.class, hierarchy.size(), FALSE, visited, hierarchy);
            Class<?>[] interfaces = Enum.class.getInterfaces();
            for (Class<?> clazz : interfaces) {
                addToClassHierarchy(clazz, hierarchy.size(), isArray, visited, hierarchy);
            }
        }
        // Handle the 'Object'.
        addToClassHierarchy(Object.class, hierarchy.size(), isArray, visited, hierarchy);
        addToClassHierarchy(Object.class, hierarchy.size(), FALSE, visited, hierarchy);
        return hierarchy;
    }

    public static boolean isPresent(String className, ClassLoader classLoader) {

        return ClassUtils.isPresent(className, true, classLoader);
    }

    public static boolean isPresent(String className, boolean initialize, ClassLoader classLoader) {
        Assert.notNull(className, "Parameter \"className\" must not null. ");
        Assert.notNull(classLoader, "Parameter \"classLoader\" must not null. ");
        try {
            Class.forName(className, initialize, classLoader);
            return true;
        }
        catch (Throwable e) {
            return false;
        }
    }

}
