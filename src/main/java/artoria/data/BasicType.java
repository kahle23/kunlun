package artoria.data;

import artoria.util.Assert;

import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Map;

import static artoria.common.Constants.EIGHT;

/**
 * The java basic data type.
 * @see <a href="https://en.wikipedia.org/wiki/Primitive_data_type">Primitive data type</a>
 * @author Kahle
 */
public enum BasicType {
    /**
     * The byte type.
     */
    BYTE(byte.class,       Byte.class),
    /**
     * The short type.
     */
    SHORT(short.class,     Short.class),
    /**
     * The int type.
     */
    INT(int.class,         Integer.class),
    /**
     * The long type.
     */
    LONG(long.class,       Long.class),
    /**
     * The float type.
     */
    FLOAT(float.class,     Float.class),
    /**
     * The double type.
     */
    DOUBLE(double.class,   Double.class),
    /**
     * The boolean type.
     */
    BOOLEAN(boolean.class, Boolean.class),
    /**
     * The char type.
     */
    CHAR(char.class,       Character.class),
    ;

    private final Class<?> primitive;
    private final Class<?> wrapper;

    BasicType(Class<?> primitive, Class<?> wrapper) {
        this.primitive = primitive;
        this.wrapper = wrapper;
    }

    public Class<?> getPrimitive() {

        return primitive;
    }

    public Class<?> getWrapper() {

        return wrapper;
    }

    /**
     * Map with primitive type as key and corresponding wrapper type as value.
     * For example: int.class -> Integer.class.
     */
    private static final Map<Class<?>, Class<?>> PRIMITIVE_TYPE_TO_WRAPPER_MAP;
    /**
     * Map with primitive type as key and corresponding "BasicType" as value.
     * For example: int.class -> BasicType.INT.
     */
    private static final Map<Class<?>, BasicType> PRIMITIVE_TYPE_TO_BASIC_MAP;
    /**
     * Map with primitive wrapper type as key and corresponding primitive type as value.
     * For example: Integer.class -> int.class.
     */
    private static final Map<Class<?>, Class<?>> WRAPPER_TYPE_TO_PRIMITIVE_MAP;

    static {
        Map<Class<?>, Class<?>> primitiveTypeToWrapperMap = new IdentityHashMap<Class<?>, Class<?>>(EIGHT);
        Map<Class<?>, BasicType> primitiveTypeToBasicMap = new IdentityHashMap<Class<?>, BasicType>(EIGHT);
        Map<Class<?>, Class<?>> wrapperTypeToPrimitiveMap = new IdentityHashMap<Class<?>, Class<?>>(EIGHT);
        BasicType[] basicTypes = values();
        for (BasicType basicType : basicTypes) {
            Class<?> primitive = basicType.getPrimitive();
            Class<?> wrapper = basicType.getWrapper();
            primitiveTypeToWrapperMap.put(primitive, wrapper);
            primitiveTypeToBasicMap.put(primitive, basicType);
            wrapperTypeToPrimitiveMap.put(wrapper, primitive);
        }
        PRIMITIVE_TYPE_TO_WRAPPER_MAP = Collections.unmodifiableMap(primitiveTypeToWrapperMap);
        PRIMITIVE_TYPE_TO_BASIC_MAP = Collections.unmodifiableMap(primitiveTypeToBasicMap);
        WRAPPER_TYPE_TO_PRIMITIVE_MAP = Collections.unmodifiableMap(wrapperTypeToPrimitiveMap);
    }

    public static boolean isPrimitive(Class<?> type) {
        Assert.notNull(type, "Parameter \"type\" must not null. ");
        return PRIMITIVE_TYPE_TO_WRAPPER_MAP.containsKey(type);
    }

    public static boolean isWrapper(Class<?> type) {
        Assert.notNull(type, "Parameter \"type\" must not null. ");
        return WRAPPER_TYPE_TO_PRIMITIVE_MAP.containsKey(type);
    }

    public static Class<?> getPrimitive(Class<?> type) {
        Assert.notNull(type, "Parameter \"type\" must not null. ");
        if (isPrimitive(type)) { return type; }
        Class<?> result = WRAPPER_TYPE_TO_PRIMITIVE_MAP.get(type);
        return result != null ? result : type;
    }

    public static Class<?> getWrapper(Class<?> type) {
        Assert.notNull(type, "Parameter \"type\" must not null. ");
        if (isWrapper(type)) { return type; }
        Class<?> result = PRIMITIVE_TYPE_TO_WRAPPER_MAP.get(type);
        return result != null ? result : type;
    }

    public static BasicType parse(Class<?> type) {
        Assert.notNull(type, "Parameter \"type\" must not null. ");
        return PRIMITIVE_TYPE_TO_BASIC_MAP.get(getPrimitive(type));
    }

}
