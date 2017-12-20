package apyh.artoria.util;

import apyh.artoria.exception.ReflectionException;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class BeanUtils {

    private static final Integer GET_OR_SET_LENGTH = 3;

    public static <T> T ifNull(T value, T defaultValue) {
        return value != null ? value : defaultValue;
    }

    public static boolean isNull(Object obj) {
        return obj == null;
    }

    public static boolean isEmpty(Object obj) {
        if (obj == null) {
            return true;
        }
        if (obj.getClass().isArray()) {
            return Array.getLength(obj) == 0;
        }
        if (obj instanceof CharSequence) {
            return ((CharSequence) obj).length() == 0;
        }
        if (obj instanceof Collection) {
            return ((Collection) obj).isEmpty();
        }
        if (obj instanceof Map) {
            return ((Map) obj).isEmpty();
        }
        // else
        return false;
    }

    public static boolean isArray(Object obj) {
        return obj != null && obj.getClass().isArray();
    }

    public static Object clone(Object obj) throws ReflectionException {
        Class<?> clazz = obj.getClass();
        Object clone = ReflectUtils.newInstance(clazz);
        BeanUtils.copyProperties(obj, clone);
        return clone;
    }

    public static Object deepClone(Object obj) {
        byte[] data = SerializeUtils.serialize(obj);
        return SerializeUtils.deserialize(data);
    }

    public static void copyProperties(Map<String, ?> src, Object dest) throws ReflectionException {
        Assert.notNull(src, "Source must is not null. ");
        Assert.notNull(dest, "Destination must is not null. ");
        try {
            Class<?> clazz = dest.getClass();
            Map<String, Method> mths = BeanUtils.findAllGetOrSetMethods(clazz);
            for (Map.Entry<String, ?> entry : src.entrySet()) {
                String name = entry.getKey();
                if (!name.startsWith(StringConstant.STRING_SET)) {
                    name = StringConstant.STRING_SET + StringUtils.capitalize(name);
                }
                Method mth = mths.get(name);
                if (mth == null) { continue; }
                Class<?> destType = mth.getParameterTypes()[0];
                Object inputValue = entry.getValue();
                Class<?> inputType = inputValue.getClass();
                // TODO: do type conver
                try {
                    mth.invoke(dest, inputValue);
                }
                catch (Exception e) {
                    // ignore
                }
            }
        }
        catch (Exception e) {
            throw new ReflectionException(e);
        }
    }

    public static void copyProperties(Object src, Map<String, Object> dest) throws ReflectionException {
        Assert.notNull(src, "Source must is not null. ");
        Assert.notNull(dest, "Destination must is not null. ");
        try {
            Class<?> clazz = src.getClass();
            Map<String, Method> mths = BeanUtils.findAllGetOrSetMethods(clazz);
            for (Map.Entry<String, Method> entry : mths.entrySet()) {
                String name = entry.getKey();
                if (!name.startsWith(StringConstant.STRING_GET)) { continue; }
                name = name.substring(GET_OR_SET_LENGTH);
                name = StringUtils.uncapitalize(name);
                Method mth = entry.getValue();
                try {
                    Object invoke = mth.invoke(src);
                    dest.put(name, invoke);
                }
                catch (Exception e) {
                    // ignore
                }
            }
        }
        catch (Exception e) {
            throw new ReflectionException(e);
        }
    }

    public static void copyProperties(Object src, Object dest) throws ReflectionException {
        Assert.notNull(src, "Source must is not null. ");
        Assert.notNull(dest, "Destination must is not null. ");
        try {
            Class<?> srcClass = src.getClass();
            Class<?> destClass = dest.getClass();
            Map<String, Method> srcMths = BeanUtils.findAllGetOrSetMethods(srcClass);
            Map<String, Method> destMths = BeanUtils.findAllGetOrSetMethods(destClass);
            for (Map.Entry<String, Method> entry : srcMths.entrySet()) {
                String name = entry.getKey();
                if (!name.startsWith(StringConstant.STRING_GET)) { continue; }
                name = name.substring(GET_OR_SET_LENGTH);
                name = StringConstant.STRING_SET + name;
                Method destMth = destMths.get(name);
                if (destMth == null) { continue; }
                Method srcMth = entry.getValue();
                try {
                    Object inputValue = srcMth.invoke(src);
                    Class<?> inputType = inputValue.getClass();
                    Class<?> destType = destMth.getParameterTypes()[0];
                    // TODO: do type conver
                    destMth.invoke(dest, inputValue);
                }
                catch (Exception e) {
                    // ignore
                }
            }
        }
        catch (Exception e) {
            throw new ReflectionException(e);
        }
    }

    static Map<String, Method> findAllGetOrSetMethods(Class<?> clazz) {
        Assert.notNull(clazz, "Clazz must is not null. ");
        Map<String, Method> result = new HashMap<String, Method>();
        do {
            Method[] methods = clazz.getMethods();
            for (Method method : methods) {
                String name = method.getName();
                int pSize = method.getParameterTypes().length;
                int mod = method.getModifiers();
                boolean isStc = Modifier.isStatic(mod);
                boolean stGet = name.startsWith(StringConstant.STRING_GET);
                boolean stSet = name.startsWith(StringConstant.STRING_SET);
                boolean b = isStc || (!stGet && !stSet);
                // has get and parameters not equal 0
                b = b || (stGet && pSize != 0);
                // has set and parameters not equal 1
                b = b || (stSet && pSize != 1);
                if (b) { continue; }
                result.put(name, method);
            }
            clazz = clazz.getSuperclass();
        }
        while (clazz != null);
        return result;
    }

}
