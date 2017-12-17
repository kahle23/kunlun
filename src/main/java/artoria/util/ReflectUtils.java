package artoria.util;

import java.lang.reflect.*;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Kahle
 */
public class ReflectUtils {

    public static Method similarMethod(Class<?> clazz, String name, Class<?>[] types) throws NoSuchMethodException {
        Class<?> cls = clazz;
        // first priority: find a public method with a "similar" signature in class hierarchy
        // similar interpreted in when primitive argument types are converted to their wrappers
        for (Method method : cls.getMethods()) {
            if (ReflectUtils.isSimilarMethod(method, name, types)) {
                return method;
            }
        }
        // second priority: find a non-public method with a "similar" signature on declaring class
        do {
            for (Method method : cls.getDeclaredMethods()) {
                if (ReflectUtils.isSimilarMethod(method, name, types)) {
                    return method;
                }
            }
            cls = cls.getSuperclass();
        }
        while (cls != null);
        throw new NoSuchMethodException("No similar method " + name + " with params " + Arrays.toString(types) + " could be found on type " + clazz + ".");
    }

    public static Method exactMethod(Class<?> clazz, String name, Class<?>[] types) throws NoSuchMethodException {
        Class<?> cls = clazz;
        // first priority: find a public method with exact signature match in class hierarchy
        try {
            return cls.getMethod(name, types);
        }
        // second priority: find a private method with exact signature match on declaring class
        catch (NoSuchMethodException e) {
            do {
                try {
                    return cls.getDeclaredMethod(name, types);
                }
                catch (NoSuchMethodException ignore) {
                }
                cls = cls.getSuperclass();
            }
            while (cls != null);
            throw new NoSuchMethodException(e.getMessage());
        }
    }

    public static Class<?>[] types(Object... values) {
        if (values == null) {
            return new Class[0];
        }

        Class<?>[] result = new Class[values.length];
        for (int i = 0; i < values.length; i++) {
            Object value = values[i];
            result[i] = value == null ? Null.class : value.getClass();
        }

        return result;
    }

    public static <T extends AccessibleObject> T accessible(T accessible) {
        if (accessible == null) {
            return null;
        }
        if (accessible instanceof Member) {
            Member member = (Member) accessible;

            if (Modifier.isPublic(member.getModifiers()) &&
                    Modifier.isPublic(member.getDeclaringClass().getModifiers())) {

                return accessible;
            }
        }
        // The accessible flag is set to false by default, also for public members.
        if (!accessible.isAccessible()) {
            accessible.setAccessible(true);
        }
        return accessible;
    }

    private static boolean isSimilarMethod(Method method, String name, Class<?>[] types) {
        return method.getName().equals(name) && ReflectUtils.match(method.getParameterTypes(), types);
    }

    private static boolean match(Class<?>[] declaredTypes, Class<?>[] actualTypes) {
        if (declaredTypes.length == actualTypes.length) {
            for (int i = 0; i < actualTypes.length; i++) {
                if (actualTypes[i] == Null.class) {
                    continue;
                }
                if (ClassUtils.getWrapper(declaredTypes[i])
                        .isAssignableFrom(ClassUtils.getWrapper(actualTypes[i]))) {
                    continue;
                }
                return false;
            }
            return true;
        }
        else {
            return false;
        }
    }

    private static class Null {}

    public static ReflectUtils create(String clazz) throws ClassNotFoundException {
        Class<?> aClass = ClassUtils.forName(clazz);
        return new ReflectUtils(aClass);
    }

    public static ReflectUtils create(String clazz, Object bean) throws ClassNotFoundException {
        Class<?> aClass = ClassUtils.forName(clazz);
        return new ReflectUtils(aClass, bean);
    }

    public static ReflectUtils create(Class<?> clazz) {
        return new ReflectUtils(clazz);
    }

    public static ReflectUtils create(Class<?> clazz, Object bean) {
        return new ReflectUtils(clazz, bean);
    }

    private Class<?> clazz;
    private Object bean;

    private ReflectUtils(Class<?> clazz) {
        this.clazz = clazz;
    }

    private ReflectUtils(Class<?> clazz, Object bean) {
        this.clazz = clazz;
        this.bean = bean;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public ReflectUtils setClazz(Class<?> clazz) {
        this.clazz = clazz;
        return this;
    }

    public Object getBean() {
        return bean;
    }

    public ReflectUtils setBean(Object bean) {
        this.bean = bean;
        return this;
    }

    public ReflectUtils construct() throws ReflectiveOperationException {
        return construct(new Object[0]);
    }

    public ReflectUtils construct(Object... args) throws ReflectiveOperationException {
        Class<?>[] types = ReflectUtils.types(args);
        Constructor<?> constructor = construct(types);
        bean = ReflectUtils.accessible(constructor).newInstance(args);
        return this;
    }

    public Constructor<?> construct(Class<?>[] types) throws ReflectiveOperationException {
        // Try invoking the "canonical" constructor, i.e. the one with exact
        // matching argument types
        try {
            return clazz.getDeclaredConstructor(types);
        }
        // If there is no exact match, try to find one that has a "similar"
        // signature if primitive argument types are converted to their wrappers
        catch (NoSuchMethodException e) {
            for (Constructor<?> constructor : clazz.getDeclaredConstructors()) {
                if (ReflectUtils.match(constructor.getParameterTypes(), types)) {
                    return constructor;
                }
            }
            throw new ReflectiveOperationException(e);
        }
    }

    public Object get(String name) throws ReflectiveOperationException {
        return ReflectUtils.accessible(field(name)).get(bean);
    }

    public ReflectUtils set(String name, Object value) throws ReflectiveOperationException {
        field(name).set(bean, value);
        return this;
    }

    public Field field(String name) throws ReflectiveOperationException {
        // Try getting a public field
        try {
            return clazz.getField(name);
        }
        // Try again, getting a non-public field
        catch (NoSuchFieldException e) {
            do {
                try {
                    return clazz.getDeclaredField(name);
                }
                catch (NoSuchFieldException ignore) {
                }
                clazz = clazz.getSuperclass();
            }
            while (clazz != null);
            throw new ReflectiveOperationException(e);
        }
    }

    public Map<String, Field> fields() throws ReflectiveOperationException {
        Map<String, Field> result = new LinkedHashMap<>();
        Class<?> cls = clazz;

        do {
            for (Field field : cls.getDeclaredFields()) {
                if (clazz != cls ^ Modifier.isStatic(field.getModifiers())) {
                    String name = field.getName();
                    if (!result.containsKey(name)) {
                        result.put(name, field(name));
                    }
                }
            }
            cls = cls.getSuperclass();
        }
        while (cls != null);

        return result;
    }

    public Object call(String name) throws ReflectiveOperationException {
        return call(name, new Object[0]);
    }

    public Object call(String name, Object... args) throws ReflectiveOperationException {
        Class<?>[] types = ReflectUtils.types(args);
        // Try invoking the "canonical" method, i.e. the one with exact
        // matching argument types
        try {
            Method method = ReflectUtils.exactMethod(clazz, name, types);
            return ReflectUtils.accessible(method).invoke(bean, args);
        }
        // If there is no exact match, try to find a method that has a "similar"
        // signature if primitive argument types are converted to their wrappers
        catch (NoSuchMethodException e) {
            try {
                Method method = ReflectUtils.similarMethod(clazz, name, types);
                return ReflectUtils.accessible(method).invoke(bean, args);
            } catch (NoSuchMethodException e1) {
                throw new ReflectiveOperationException(e1);
            }
        }
    }

    @Override
    public int hashCode() {
        return clazz.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ReflectUtils) {
            ReflectUtils another = (ReflectUtils) obj;
            return bean.equals(another.getBean())
                    && clazz.equals(another.getClazz());
        }
        return false;
    }

    @Override
    public String toString() {
        return "Reflect{" +
                "clazz=" + clazz +
                ", bean=" + bean +
                '}';
    }

}
