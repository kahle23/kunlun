package com.github.kahlkn.artoria.reflect;

import com.github.kahlkn.artoria.util.Assert;
import com.github.kahlkn.artoria.util.ClassUtils;

import java.lang.reflect.*;
import java.util.*;

import static com.github.kahlkn.artoria.util.Const.GET;
import static com.github.kahlkn.artoria.util.Const.SET;

/**
 * Reflect tools implements by jdk.
 * @author Kahle
 */
public class JdkReflecter implements Reflecter {
    private static final Integer MAP_INITIAL_CAPACITY = 8;
    private static final String GET_CLASS = "getClass";

    protected boolean notAccess(Class<?> thisClazz, Class<?> superClazz, Member member) {
        // In this class all, and super class not private.
        return thisClazz != superClazz && Modifier.isPrivate(member.getModifiers());
    }

    @Override
    public Class<?> forName(String className) throws ClassNotFoundException {
        ClassLoader loader = ClassUtils.getDefaultClassLoader();
        return this.forName(className, true, loader);
    }

    @Override
    public Class<?> forName(String className, boolean initialize, ClassLoader loader) throws ClassNotFoundException {
        Assert.notBlank(className, "Parameter \"className\" must not blank. ");
        Assert.notNull(loader, "Parameter \"loader\" must not null. ");
        return Class.forName(className, initialize, loader);
    }

    @Override
    public Class<?>[] findParameterTypes(Object... parameters) {
        if (parameters.length == 0) { return new Class[0]; }
        Class<?>[] result = new Class[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            Object value = parameters[i];
            // Parameter null, maybe in method can input null.
            // So in match method ignore null type.
            result[i] = value == null ? null : value.getClass();
        }
        return result;
    }

    @Override
    public boolean matchParameterTypes(Class<?>[] declaredTypes, Class<?>[] actualTypes) {
        Assert.notNull(declaredTypes, "Parameter \"declaredTypes\" must not null. ");
        Assert.notNull(actualTypes, "Parameter \"actualTypes\" must not null. ");
        if (declaredTypes.length != actualTypes.length) {
            return false;
        }
        for (int i = 0; i < actualTypes.length; i++) {
            // Method has parameter, but input null, so continue.
            if (actualTypes[i] == null) { continue; }
            Class<?> declared = ClassUtils.getWrapper(declaredTypes[i]);
            Class<?> actual = ClassUtils.getWrapper(actualTypes[i]);
            if (declared.isAssignableFrom(actual)) { continue; }
            return false;
        }
        return true;
    }

    @Override
    public <T extends AccessibleObject> boolean checkAccessible(T accessible) {
        Assert.notNull(accessible, "Parameter \"accessible\" must not null. ");
        if (accessible instanceof Member) {
            Member member = (Member) accessible;
            boolean b = Modifier.isPublic(member.getModifiers());
            Class<?> declaringClass = member.getDeclaringClass();
            b = b && Modifier.isPublic(declaringClass.getModifiers());
            if (accessible instanceof Field) {
                Field field = (Field) accessible;
                b = b && !Modifier.isFinal(field.getModifiers());
            }
            if (b) { return true; }
        }
        return accessible.isAccessible();
    }

    @Override
    public <T extends AccessibleObject> void makeAccessible(T accessible) {
        if (!this.checkAccessible(accessible)) {
            accessible.setAccessible(true);
        }
    }

    @Override
    public Constructor<?>[] findConstructors(Class<?> clazz) {
        Assert.notNull(clazz, "Parameter \"clazz\" must not null. ");
        return clazz.getDeclaredConstructors();
    }

    @Override
    public Constructor<?> findConstructor(Class<?> clazz, Class<?>... parameterTypes) throws NoSuchMethodException {
        Assert.notNull(clazz, "Parameter \"clazz\" must not null. ");
        // Try invoking the "canonical" constructor,
        // i.e. the one with exact matching argument types
        try {
            return clazz.getDeclaredConstructor(parameterTypes);
        }
        // If there is no exact match, try to find one that has a "similar"
        // signature if primitive argument types are converted to their wrappers
        catch (NoSuchMethodException e) {
            Constructor<?>[] cts = this.findConstructors(clazz);
            for (Constructor<?> ct : cts) {
                Class<?>[] pTypes = ct.getParameterTypes();
                boolean b = this.matchParameterTypes(pTypes, parameterTypes);
                if (b) { return ct; }
            }
            throw e;
        }
    }

    @Override
    public Field[] findFields(Class<?> clazz) {
        Assert.notNull(clazz, "Parameter \"clazz\" must not null. ");
        return clazz.getFields();
    }

    @Override
    public Field[] findDeclaredFields(Class<?> clazz) {
        Assert.notNull(clazz, "Parameter \"clazz\" must not null. ");
        return clazz.getDeclaredFields();
    }

    @Override
    public Field[] findAccessFields(Class<?> clazz) {
        Assert.notNull(clazz, "Parameter \"clazz\" must not null. ");
        Class<?> inputClazz = clazz;
        List<Field> list = new ArrayList<Field>();
        List<String> names = new ArrayList<String>();
        while (clazz != null) {
            Field[] fields = this.findDeclaredFields(clazz);
            for (Field field : fields) {
                // find this class all, and super class not private.
                if (this.notAccess(inputClazz, clazz, field)) {
                    continue;
                }
                String fieldName = field.getName();
                if (names.contains(fieldName)) {
                    continue;
                }
                list.add(field);
                names.add(fieldName);
            }
            // Field in interface is public
            // Will inherit subclass
            clazz = clazz.getSuperclass();
        }
        Field[] result = new Field[list.size()];
        list.toArray(result);
        return result;
    }

    @Override
    public Field findField(Class<?> clazz, String fieldName) throws NoSuchFieldException {
        Assert.notNull(clazz, "Parameter \"clazz\" must not null. ");
        Assert.notBlank(fieldName, "Parameter \"fieldName\" must not blank. ");
        // Try getting a public field
        try {
            return clazz.getField(fieldName);
        }
        // Try again, getting a non-public field
        catch (NoSuchFieldException e) {
            Class<?> inputClazz = clazz;
            do {
                try {
                    Field field = clazz.getDeclaredField(fieldName);
                    if (this.notAccess(inputClazz, clazz, field)) {
                        continue;
                    }
                    return field;
                }
                catch (NoSuchFieldException nsfe) {
                    // ignore
                }
                clazz = clazz.getSuperclass();
            }
            while (clazz != null);
            throw e;
        }
    }

    @Override
    public Method[] findMethods(Class<?> clazz) {
        Assert.notNull(clazz, "Parameter \"clazz\" must not null. ");
        return clazz.getMethods();
    }

    @Override
    public Method[] findDeclaredMethods(Class<?> clazz) {
        Assert.notNull(clazz, "Parameter \"clazz\" must not null. ");
        return clazz.getDeclaredMethods();
    }

    @Override
    public Method[] findAccessMethods(Class<?> clazz) {
        Assert.notNull(clazz, "Parameter \"clazz\" must not null. ");
        Class<?> inputClazz = clazz;
        List<Method> list = new ArrayList<Method>();
        List<String> names = new ArrayList<String>();
        while (clazz != null) {
            Method[] methods = this.findDeclaredMethods(clazz);
            for (Method method : methods) {
                if (this.notAccess(inputClazz, clazz, method)) {
                    continue;
                }
                String methodName = method.getName()
                        + Arrays.toString(method.getParameterTypes());
                if (names.contains(methodName)) {
                    continue;
                }
                list.add(method);
                names.add(methodName);
            }
            clazz = clazz.getSuperclass();
        }
        Method[] methods = new Method[list.size()];
        list.toArray(methods);
        return methods;
    }

    @Override
    public Map<String, Method> findReadMethods(Class<?> clazz) {
        Assert.notNull(clazz, "Parameter \"clazz\" must not null. ");
        Map<String, Method> result = new HashMap<String, Method>(MAP_INITIAL_CAPACITY);
        Method[] methods = this.findMethods(clazz);
        for (Method method : methods) {
            String name = method.getName();
            int pSize = method.getParameterTypes().length;
            int mod = method.getModifiers();
            boolean isStc = Modifier.isStatic(mod);
            boolean stGet = name.startsWith(GET);
            // has get and parameters must equal 0
            boolean b = isStc || !stGet || pSize != 0;
            b = b || GET_CLASS.equals(name);
            if (b) { continue; }
            result.put(name, method);
        }
        return result;
    }

    @Override
    public Map<String, Method> findWriteMethods(Class<?> clazz) {
        Assert.notNull(clazz, "Parameter \"clazz\" must not null. ");
        Map<String, Method> result = new HashMap<String, Method>(MAP_INITIAL_CAPACITY);
        Method[] methods = this.findMethods(clazz);
        for (Method method : methods) {
            String name = method.getName();
            int pSize = method.getParameterTypes().length;
            int mod = method.getModifiers();
            boolean isStc = Modifier.isStatic(mod);
            boolean stSet = name.startsWith(SET);
            // has set and parameters not equal 1
            boolean b = isStc || !stSet || pSize != 1;
            if (b) { continue; }
            result.put(name, method);
        }
        return result;
    }

    @Override
    public Method findMethod(Class<?> clazz, String methodName, Class<?>... parameterTypes) throws NoSuchMethodException {
        Assert.notNull(clazz, "Parameter \"clazz\" must not null. ");
        Assert.notBlank(methodName, "Parameter \"methodName\" must not blank. ");
        // first priority : find a public method with exact signature match in class hierarchy
        try {
            return clazz.getMethod(methodName, parameterTypes);
        }
        // second priority : find a private method with exact signature match on declaring class
        catch (NoSuchMethodException e) {
            Class<?> inputClazz = clazz;
            do {
                try {
                    Method method = clazz.getDeclaredMethod(methodName, parameterTypes);
                    if (this.notAccess(inputClazz, clazz, method)) {
                        continue;
                    }
                    return method;
                }
                catch (NoSuchMethodException nsme) {
                    // ignore
                }
                clazz = clazz.getSuperclass();
            }
            while (clazz != null);
            throw e;
        }
    }

    @Override
    public Method findSimilarMethod(Class<?> clazz, String methodName, Class<?>... parameterTypes) throws NoSuchMethodException {
        Assert.notNull(clazz, "Parameter \"clazz\" must not null. ");
        Assert.notBlank(methodName, "Parameter \"methodName\" must not blank. ");
        // first priority : find a public method with a "similar" signature in class hierarchy
        // similar interpreted in when primitive argument types are converted to their wrappers
        for (Method method : this.findMethods(clazz)) {
            if (methodName.equals(method.getName()) &&
                    this.matchParameterTypes(method.getParameterTypes(), parameterTypes)) {
                return method;
            }
        }
        // second priority : find a non-public method with a "similar" signature on declaring class
        Class<?> inputClazz = clazz;
        do {
            for (Method method : this.findDeclaredMethods(clazz)) {
                if (this.notAccess(inputClazz, clazz, method)) {
                    continue;
                }
                if (methodName.equals(method.getName()) &&
                        this.matchParameterTypes(method.getParameterTypes(), parameterTypes)) {
                    return method;
                }
            }
            clazz = clazz.getSuperclass();
        }
        while (clazz != null);
        // build message
        String msg = "No similar method " + methodName + " with params ";
        msg += Arrays.toString(parameterTypes) + " could be found on type ";
        msg += inputClazz + ".";
        throw new NoSuchMethodException(msg);
    }

}
