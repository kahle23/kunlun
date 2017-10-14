package saber.util;

import java.lang.reflect.*;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

public class Reflect {
    private Class<?> clazz;
    private Object bean;

    public Class<?> getClazz() {
        return clazz;
    }

    public Reflect setClazz(Class<?> clazz) {
        this.clazz = clazz;
        return this;
    }

    public Object getBean() {
        return bean;
    }

    public Reflect setBean(Object bean) {
        this.bean = bean;
        return this;
    }

    public Reflect(Class<?> clazz) {
        this.clazz = clazz;
    }

    public Reflect(Class<?> clazz, Object bean) {
        this.clazz = clazz;
        this.bean = bean;
    }

    private boolean isSimilarMethod(Method method, String name, Class<?>[] types) {
        return method.getName().equals(name) && match(method.getParameterTypes(), types);
    }

    public Reflect create() throws ReflectiveOperationException {
        return create(new Object[0]);
    }

    public Reflect create(Object... args) throws ReflectiveOperationException {
        Class<?>[] types = types(args);
        Constructor<?> constructor = create(types);
        bean = accessible(constructor).newInstance(args);
        return this;
    }

    public Constructor<?> create(Class<?>[] types) throws ReflectiveOperationException {
        // Try invoking the "canonical" constructor, i.e. the one with exact
        // matching argument types
        try {
            return clazz.getDeclaredConstructor(types);
        }
        // If there is no exact match, try to find one that has a "similar"
        // signature if primitive argument types are converted to their wrappers
        catch (NoSuchMethodException e) {
            for (Constructor<?> constructor : clazz.getDeclaredConstructors()) {
                if (match(constructor.getParameterTypes(), types)) {
                    return constructor;
                }
            }
            throw new ReflectiveOperationException(e);
        }
    }

    public Object get(String name) throws ReflectiveOperationException {
        return accessible(field(name)).get(bean);
    }

    public Reflect set(String name, Object value) throws ReflectiveOperationException {
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
                    if (!result.containsKey(name)) result.put(name, field(name));
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
        Class<?>[] types = types(args);
        // Try invoking the "canonical" method, i.e. the one with exact
        // matching argument types
        try {
            Method method = exactMethod(name, types);
            return accessible(method).invoke(bean, args);
        }
        // If there is no exact match, try to find a method that has a "similar"
        // signature if primitive argument types are converted to their wrappers
        catch (NoSuchMethodException e) {
            try {
                Method method = similarMethod(name, types);
                return accessible(method).invoke(bean, args);
            } catch (NoSuchMethodException e1) {
                throw new ReflectiveOperationException(e1);
            }
        }
    }

    public Method exactMethod(String name, Class<?>[] types) throws NoSuchMethodException {
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

    public Method similarMethod(String name, Class<?>[] types) throws NoSuchMethodException {
        Class<?> cls = clazz;
        // first priority: find a public method with a "similar" signature in class hierarchy
        // similar interpreted in when primitive argument types are converted to their wrappers
        for (Method method : cls.getMethods()) {
            if (isSimilarMethod(method, name, types)) {
                return method;
            }
        }
        // second priority: find a non-public method with a "similar" signature on declaring class
        do {
            for (Method method : cls.getDeclaredMethods()) {
                if (isSimilarMethod(method, name, types)) {
                    return method;
                }
            }
            cls = cls.getSuperclass();
        }
        while (cls != null);
        throw new NoSuchMethodException("No similar method " + name + " with params " + Arrays.toString(types) + " could be found on type " + clazz + ".");
    }

    @Override
    public int hashCode() {
        return clazz.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Reflect) {
            Reflect another = (Reflect) obj;
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

    private static class NULL {}

    private static Class<?> wrapper(Class<?> type) {
        if (type == null) {
            return null;
        }
        if (type.isPrimitive()) {
            if (boolean.class == type) {
                return Boolean.class;
            }
            else if (int.class == type) {
                return Integer.class;
            }
            else if (long.class == type) {
                return Long.class;
            }
            else if (short.class == type) {
                return Short.class;
            }
            else if (byte.class == type) {
                return Byte.class;
            }
            else if (double.class == type) {
                return Double.class;
            }
            else if (float.class == type) {
                return Float.class;
            }
            else if (char.class == type) {
                return Character.class;
            }
            else if (void.class == type) {
                return Void.class;
            }
        }
        return type;
    }

    private boolean match(Class<?>[] declaredTypes, Class<?>[] actualTypes) {
        if (declaredTypes.length == actualTypes.length) {
            for (int i = 0; i < actualTypes.length; i++) {
                if (actualTypes[i] == NULL.class) continue;
                if (wrapper(declaredTypes[i])
                        .isAssignableFrom(wrapper(actualTypes[i]))) continue;
                return false;
            }
            return true;
        }
        else {
            return false;
        }
    }

    public static Reflect on(Class<?> clazz) {
        return new Reflect(clazz);
    }

    public static Reflect on(Class<?> clazz, Object bean) {
        return new Reflect(clazz, bean);
    }

    public static Reflect on(Object bean) {
        return new Reflect(bean == null ? Object.class : bean.getClass(), bean);
    }

    public static Reflect on(String name) throws ClassNotFoundException {
        return on(Class.forName(name));
    }

    public static Reflect on(String name, ClassLoader classLoader) throws ClassNotFoundException {
        return on(Class.forName(name, true, classLoader));
    }

    public static Class<?>[] types(Object... values) {
        if (values == null) {
            return new Class[0];
        }

        Class<?>[] result = new Class[values.length];
        for (int i = 0; i < values.length; i++) {
            Object value = values[i];
            result[i] = value == null ? NULL.class : value.getClass();
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
        // [jOOQ #3392] The accessible flag is set to false by default, also for public members.
        if (!accessible.isAccessible()) {
            accessible.setAccessible(true);
        }
        return accessible;
    }

    public static Class<?> forName(String name) throws ClassNotFoundException {
        return Class.forName(name);
    }

    public static Class<?> forName(String name, ClassLoader loader) throws ClassNotFoundException {
        return Class.forName(name, true, loader);
    }

    public static Class<?> forName(String name, boolean initialize, ClassLoader loader) throws ClassNotFoundException {
        return Class.forName(name, initialize, loader);
    }

    public static ClassLoader getClassLoader() {
        ClassLoader classLoader = null;
        try {
            classLoader = Thread.currentThread().getContextClassLoader();
        }
        catch (Throwable ex) {
            // Cannot access thread context ClassLoader - falling back...
        }
        if (classLoader == null) {
            // No thread context class loader -> use class loader of this class.
            classLoader = Reflect.class.getClassLoader();
            if (classLoader == null) {
                // getClassLoader() returning null indicates the bootstrap ClassLoader
                try {
                    classLoader = ClassLoader.getSystemClassLoader();
                }
                catch (Throwable ex) {
                    // Cannot access system ClassLoader - oh well, maybe the caller can live with null...
                }
            }
        }
        return classLoader;
    }

    public static boolean isPresent(String className, ClassLoader classLoader) {
        return isPresent(className, true, classLoader);
    }

    public static boolean isPresent(String className, boolean initialize, ClassLoader classLoader) {
        try {
            Class.forName(className, initialize, classLoader);
            return true;
        } catch (Throwable e) {
            return false;
        }
    }

}
