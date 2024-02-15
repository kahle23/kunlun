package artoria.reflect;

import artoria.exception.ExceptionUtils;
import artoria.util.ArrayUtils;
import artoria.util.Assert;
import artoria.util.ClassUtils;
import artoria.util.ObjectUtils;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static artoria.common.constant.Numbers.ZERO;

/**
 * The reflection provider based on jdk simple implementation.
 * @author Kahle
 */
public class SimpleReflectProvider implements ReflectProvider {

    protected boolean notAccess(Class<?> thisClazz, Class<?> superClazz, Member member) {
        // In this class all, and super class not private.
        return thisClazz != superClazz && Modifier.isPrivate(member.getModifiers());
    }

    protected Class<?>[] getParameterTypes(Object[] params) {
        if (ArrayUtils.isEmpty(params)) { return new Class[ZERO]; }
        Class<?>[] result = new Class[params.length];
        for (int i = ZERO; i < params.length; i++) {
            Object value = params[i];
            // Parameter null, maybe in method can input null.
            // So in match method ignore null type.
            result[i] = value == null ? null : value.getClass();
        }
        return result;
    }

    protected boolean matchParameterTypes(Class<?>[] declaredTypes, Class<?>[] actualTypes) {
        Assert.notNull(declaredTypes, "Parameter \"declaredTypes\" must not null. ");
        Assert.notNull(actualTypes, "Parameter \"actualTypes\" must not null. ");
        if (declaredTypes.length != actualTypes.length) {
            return false;
        }
        for (int i = ZERO; i < actualTypes.length; i++) {
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
    public void makeAccessible(AccessibleObject accessible) {
        if (!checkAccessible(accessible)) {
            accessible.setAccessible(true);
        }
    }

    @Override
    public boolean checkAccessible(AccessibleObject accessible) {
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
    public <T> Constructor<T>[] getConstructors(Class<T> clazz) {
        Assert.notNull(clazz, "Parameter \"clazz\" must not null. ");
        return ObjectUtils.cast(clazz.getDeclaredConstructors());
    }

    @Override
    public <T> Constructor<T> getConstructor(Class<T> clazz, Class<?>... parameterTypes) throws NoSuchMethodException {
        Assert.notNull(clazz, "Parameter \"clazz\" must not null. ");
        // Try invoking the "canonical" constructor,
        // i.e. the one with exact matching argument types
        try {
            return clazz.getDeclaredConstructor(parameterTypes);
        }
        // If there is no exact match, try to find one that has a "similar"
        // signature if primitive argument types are converted to their wrappers
        catch (NoSuchMethodException e) {
            Constructor<?>[] cts = getConstructors(clazz);
            for (Constructor<?> ct : cts) {
                Class<?>[] pTypes = ct.getParameterTypes();
                boolean b = matchParameterTypes(pTypes, parameterTypes);
                if (b) { return ObjectUtils.cast(ct); }
            }
            throw e;
        }
    }

    @Override
    public Field[] getFields(Class<?> clazz) {
        Assert.notNull(clazz, "Parameter \"clazz\" must not null. ");
        return clazz.getFields();
    }

    @Override
    public Field[] getDeclaredFields(Class<?> clazz) {
        Assert.notNull(clazz, "Parameter \"clazz\" must not null. ");
        return clazz.getDeclaredFields();
    }

    @Override
    public Field[] getAccessibleFields(Class<?> clazz) {
        Assert.notNull(clazz, "Parameter \"clazz\" must not null. ");
        Class<?> inputClazz = clazz;
        List<String> names = new ArrayList<String>();
        List<Field> list = new ArrayList<Field>();
        while (clazz != null) {
            Field[] fields = getDeclaredFields(clazz);
            for (Field field : fields) {
                // find this class all, and super class not private.
                if (notAccess(inputClazz, clazz, field)) {
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
        return list.toArray(new Field[ZERO]);
    }

    @Override
    public Field getField(Class<?> clazz, String fieldName) throws NoSuchFieldException {
        Assert.notBlank(fieldName, "Parameter \"fieldName\" must not blank. ");
        Assert.notNull(clazz, "Parameter \"clazz\" must not null. ");
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
                    if (notAccess(inputClazz, clazz, field)) {
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
    public Method[] getMethods(Class<?> clazz) {
        Assert.notNull(clazz, "Parameter \"clazz\" must not null. ");
        return clazz.getMethods();
    }

    @Override
    public Method[] getDeclaredMethods(Class<?> clazz) {
        Assert.notNull(clazz, "Parameter \"clazz\" must not null. ");
        return clazz.getDeclaredMethods();
    }

    @Override
    public Method[] getAccessibleMethods(Class<?> clazz) {
        Assert.notNull(clazz, "Parameter \"clazz\" must not null. ");
        List<String> names = new ArrayList<String>();
        List<Method> list = new ArrayList<Method>();
        Class<?> inputClazz = clazz;
        while (clazz != null) {
            Method[] methods = getDeclaredMethods(clazz);
            for (Method method : methods) {
                if (notAccess(inputClazz, clazz, method)) {
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
        return list.toArray(new Method[ZERO]);
    }

    @Override
    public Method getMethod(Class<?> clazz, String methodName, Class<?>... parameterTypes) throws NoSuchMethodException {
        Assert.notBlank(methodName, "Parameter \"methodName\" must not blank. ");
        Assert.notNull(clazz, "Parameter \"clazz\" must not null. ");
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
                    if (notAccess(inputClazz, clazz, method)) {
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
    public Method getSimilarMethod(Class<?> clazz, String methodName, Class<?>... parameterTypes) throws NoSuchMethodException {
        Assert.notBlank(methodName, "Parameter \"methodName\" must not blank. ");
        Assert.notNull(clazz, "Parameter \"clazz\" must not null. ");
        // first priority : find a public method with a "similar" signature in class hierarchy
        // similar interpreted in when primitive argument types are converted to their wrappers
        for (Method method : getMethods(clazz)) {
            if (methodName.equals(method.getName()) &&
                    matchParameterTypes(method.getParameterTypes(), parameterTypes)) {
                return method;
            }
        }
        // second priority : find a non-public method with a "similar" signature on declaring class
        Class<?> inputClazz = clazz;
        do {
            for (Method method : getDeclaredMethods(clazz)) {
                if (notAccess(inputClazz, clazz, method)) {
                    continue;
                }
                if (methodName.equals(method.getName()) &&
                        matchParameterTypes(method.getParameterTypes(), parameterTypes)) {
                    return method;
                }
            }
            clazz = clazz.getSuperclass();
        }
        while (clazz != null);
        // build message
        String msg = "No similar method \"" + methodName + "\" with params \"";
        msg += Arrays.toString(parameterTypes) + "\" could be found on type \"";
        msg += inputClazz + "\". ";
        throw new NoSuchMethodException(msg);
    }

    @Override
    public PropertyDescriptor[] getPropertyDescriptors(Class<?> clazz) {
        Assert.notNull(clazz, "Parameter \"clazz\" must not null. ");
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(clazz);
            PropertyDescriptor[] descriptors = beanInfo.getPropertyDescriptors();
            return descriptors != null ? descriptors : new PropertyDescriptor[ZERO];
        } catch (Exception e) { throw ExceptionUtils.wrap(e); }
    }

    @Override
    public <T> T newInstance(Class<T> clazz, Object... args) throws NoSuchMethodException
            , IllegalAccessException, InvocationTargetException, InstantiationException {
        Assert.notNull(clazz, "Parameter \"clazz\" must not null. ");
        if (ArrayUtils.isEmpty(args)) { return clazz.newInstance(); }
        Class<?>[] types = getParameterTypes(args);
        Constructor<T> constructor = getConstructor(clazz, types);
        makeAccessible(constructor);
        return constructor.newInstance(args);
    }

}
