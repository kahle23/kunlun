package artoria.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Class tools.
 * @author Kahle
 */
public class ClassUtils {
    private static final Map<Class, Class> WRAPPER;
    private static final Map<Class, Class> PRIMITIVE;

    static {
        Map<Class, Class> wMap = new HashMap<Class, Class>();
        wMap.put(boolean.class, Boolean.class);
        wMap.put(int.class, Integer.class);
        wMap.put(long.class, Long.class);
        wMap.put(short.class, Short.class);
        wMap.put(byte.class, Byte.class);
        wMap.put(double.class, Double.class);
        wMap.put(float.class, Float.class);
        wMap.put(char.class, Character.class);
        wMap.put(void.class, Void.class);
        WRAPPER = Collections.unmodifiableMap(wMap);
        Map<Class, Class> pMap = new HashMap<Class, Class>();
        for (Map.Entry<Class, Class> entry : wMap.entrySet()) {
            pMap.put(entry.getValue(), entry.getKey());
        }
        PRIMITIVE = Collections.unmodifiableMap(pMap);
    }

    /**
     *
     * @param type
     * @return
     */
    public static Class<?> getWrapper(Class<?> type) {
        Assert.notNull(type, "Parameter \"type\" must not null. ");
        if (!type.isPrimitive() || !WRAPPER.containsKey(type)) {
            return type;
        }
        return WRAPPER.get(type);
    }

    /**
     *
     * @param type
     * @return
     */
    public static Class<?> getPrimitive(Class<?> type) {
        Assert.notNull(type, "Parameter \"type\" must not null. ");
        if (type.isPrimitive() || !PRIMITIVE.containsKey(type)) {
            return type;
        }
        return WRAPPER.get(type);
    }

    /**
     *
     * @param className
     * @param classLoader
     * @return
     */
    public static boolean isPresent(String className, ClassLoader classLoader) {

        return ClassUtils.isPresent(className, true, classLoader);
    }

    /**
     *
     * @param className
     * @param initialize
     * @param classLoader
     * @return
     */
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
