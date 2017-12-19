package artoria.util;

import artoria.exception.ReflectionException;

import java.lang.reflect.*;
import java.util.*;

import static artoria.util.StringConstant.STRING_GET;
import static artoria.util.StringConstant.STRING_SET;

/**
 * Reflect tools.
 * @author Kahle
 */
public class ReflectUtils {

    public static Object newInstance(String clazzString) throws ReflectionException {
        Class<?> clazz;
        try {
            clazz = ClassUtils.forName(clazzString);
        }
        catch (ClassNotFoundException e) {
            throw new ReflectionException(e);
        }
        return ReflectUtils.newInstance(clazz);
    }

    public static Object newInstance(Class<?> clazz, Object... args) throws ReflectionException {
        Assert.notNull(clazz, "Clazz must is not null. ");
        Assert.notNull(args, "Args must is not null. ");
        try {
            Class<?>[] types = ReflectUtils.findTypes(args);
            Constructor<?> constructor = ReflectUtils.findConstructor(clazz, types);
            return constructor.newInstance(args);
        }
        catch (Exception e) {
            throw new ReflectionException(e);
        }
    }

    public static Field findField(Class<?> clazz, String fieldName) throws NoSuchFieldException {
        Assert.notNull(clazz, "Clazz must is not null. ");
        Assert.notBlank(fieldName, "Field name must is not blank. ");
        // Try getting a public field
        try {
            return clazz.getField(fieldName);
        }
        // Try again, getting a non-public field
        catch (NoSuchFieldException e) {
            do {
                try {
                    return clazz.getDeclaredField(fieldName);
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

    public static Map<String, Field> findFields(Class<?> clazz) {
        Assert.notNull(clazz, "Clazz must is not null. ");
        Map<String, Field> result = new LinkedHashMap<String, Field>();
        Class<?> inputClazz = clazz;
        LinkedList<Class<?>> list = new LinkedList<Class<?>>();
        list.addLast(clazz);
        while (list.size() != 0) {
            clazz = list.removeFirst();
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                // find this class all, and super class not private.
                boolean b = inputClazz != clazz;
                b = b && Modifier.isPrivate(field.getModifiers());
                if (b) {
                    continue;
                }
                String name = field.getName();
                boolean isContains = result.containsKey(name);
                if (isContains) {
                    continue;
                }
                field = ReflectUtils.accessible(field);
                result.put(name, field);
            }
            Class<?>[] interfaces = clazz.getInterfaces();
            if (ArrayUtils.isNotEmpty(interfaces)) {
                Collections.addAll(list, interfaces);
            }
            Class<?> superclass = clazz.getSuperclass();
            if (superclass != null) {
                list.addLast(superclass);
            }
        }
        return result;
    }

    public static Constructor<?> findConstructor(Class<?> clazz, Class<?>[] types) throws NoSuchMethodException {
        Assert.notNull(clazz, "Clazz must is not null. ");
        Assert.notNull(types, "Types must is not null. ");
        // Try invoking the "canonical" constructor,
        // i.e. the one with exact matching argument types
        try {
            return clazz.getDeclaredConstructor(types);
        }
        // If there is no exact match, try to find one that has a "similar"
        // signature if primitive argument types are converted to their wrappers
        catch (NoSuchMethodException e) {
            Constructor<?>[] cts = clazz.getDeclaredConstructors();
            for (Constructor<?> ct : cts) {
                Class<?>[] pTypes = ct.getParameterTypes();
                if (ReflectUtils.matchTypes(pTypes, types)) {
                    return ct;
                }
            }
            throw e;
        }
    }

    public static Method findMethod(Class<?> clazz, String methodName, Class<?>[] types) throws NoSuchMethodException {
        Assert.notNull(clazz, "Clazz must is not null. ");
        Assert.notBlank(methodName, "Method name must is not blank. ");
        Assert.notNull(types, "Types must is not null. ");
        // first priority : find a public method with exact signature match in class hierarchy
        try {
            return clazz.getMethod(methodName, types);
        }
        // second priority : find a private method with exact signature match on declaring class
        catch (NoSuchMethodException e) {
            do {
                try {
                    return clazz.getDeclaredMethod(methodName, types);
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

    public static Method findSimilarMethod(Class<?> clazz, String methodName, Class<?>[] types) throws NoSuchMethodException {
        Assert.notNull(clazz, "Clazz must is not null. ");
        Assert.notBlank(methodName, "Method name must is not blank. ");
        Assert.notNull(types, "Types must is not null. ");
        // first priority : find a public method with a "similar" signature in class hierarchy
        // similar interpreted in when primitive argument types are converted to their wrappers
        for (Method method : clazz.getMethods()) {
            boolean isSimilar =
                    ReflectUtils.isSimilarMethod(method, methodName, types);
            if (isSimilar) {
                return method;
            }
        }
        Class<?> inputClazz = clazz;
        // second priority : find a non-public method with a "similar" signature on declaring class
        do {
            for (Method method : clazz.getDeclaredMethods()) {
                boolean isSimilar =
                        ReflectUtils.isSimilarMethod(method, methodName, types);
                if (isSimilar) {
                    return method;
                }
            }
            clazz = clazz.getSuperclass();
        }
        while (clazz != null);
        // build message
        String msg = "No similar method " + methodName + " with params ";
        msg += Arrays.toString(types) + " could be found on type ";
        msg += inputClazz + ".";
        throw new NoSuchMethodException(msg);
    }

    public static Class<?>[] findTypes(Object... values) {
        if (ArrayUtils.isEmpty(values)) {
            return new Class[0];
        }
        Class<?>[] result = new Class[values.length];
        for (int i = 0; i < values.length; i++) {
            Object value = values[i];
            // Parameter null, maybe in method can input null.
            // So in match method ignore null type.
            result[i] = value == null ? null : value.getClass();
        }
        return result;
    }

    public static <T extends AccessibleObject> T accessible(T accessible) {
        Assert.notNull(accessible, "Accessible object must is not null. ");
        if (accessible instanceof Member) {
            Member member = (Member) accessible;
            boolean b = Modifier.isPublic(member.getModifiers());
            b = b && Modifier.isPublic(member.getDeclaringClass().getModifiers());
            if (b) {
                return accessible;
            }
        }
        // The accessible flag is set to false by default, also for public members.
        if (!accessible.isAccessible()) {
            accessible.setAccessible(true);
        }
        return accessible;
    }

    private static boolean isSimilarMethod(Method method, String methodName, Class<?>[] types) {
        Assert.notNull(method, "Method must is not null. ");
        Assert.notBlank(methodName, "Method name must is not blank. ");
        Assert.notNull(types, "Types must is not null. ");
        boolean b = method.getName().equals(methodName);
        b = b && ReflectUtils.matchTypes(method.getParameterTypes(), types);
        return b;
    }

    private static boolean matchTypes(Class<?>[] declaredTypes, Class<?>[] actualTypes) {
        Assert.notNull(declaredTypes, "Declared types must is not null. ");
        Assert.notNull(actualTypes, "Actual types must is not null. ");
        if (declaredTypes.length != actualTypes.length) {
            return false;
        }
        for (int i = 0; i < actualTypes.length; i++) {
            // Method has parameter, but input null, so continue.
            if (actualTypes[i] == null) {
                continue;
            }
            Class<?> declared = ClassUtils.getWrapper(declaredTypes[i]);
            Class<?> actual = ClassUtils.getWrapper(actualTypes[i]);
            if (declared.isAssignableFrom(actual)) {
                continue;
            }
            return false;
        }
        return true;
    }

    public static ReflectUtils create(String clazz) throws ReflectionException {
        try {
            Class<?> aClass = ClassUtils.forName(clazz);
            return new ReflectUtils(aClass, null);
        }
        catch (Exception e) {
            throw new ReflectionException(e);
        }
    }

    public static ReflectUtils create(String clazz, Object bean) throws ReflectionException {
        try {
            Class<?> aClass = ClassUtils.forName(clazz);
            return new ReflectUtils(aClass, bean);
        }
        catch (Exception e) {
            throw new ReflectionException(e);
        }
    }

    public static ReflectUtils create(Class<?> clazz) {
        return new ReflectUtils(clazz, null);
    }

    public static ReflectUtils create(Class<?> clazz, Object bean) {
        return new ReflectUtils(clazz, bean);
    }

    private Class<?> clazz;
    private Object bean;

    private ReflectUtils(Class<?> clazz, Object bean) {
        this.assertClazzNotNull(clazz);
        this.clazz = clazz;
        this.assertBeanInstanceOfClazz(bean);
        this.bean = bean;
    }

    private void assertClazzNotNull(Class<?> clazz) {
        // Maybe use reflect to set clazz is null, so check more.
        String msg = "Clazz must is not null. ";
        Assert.notNull(clazz, msg);
    }

    private void assertBeanInstanceOfClazz(Object bean) {
        if (bean == null) {
            return;
        }
        String msg = "The bean must is instance of clazz. ";
        Assert.isInstanceOf(this.clazz, bean, msg);
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public ReflectUtils setClazz(Class<?> clazz) {
        this.assertClazzNotNull(clazz);
        if (clazz.equals(this.clazz)) {
            return this;
        }
        this.clazz = clazz;
        boolean b = this.bean != null;
        b = b && !(this.clazz.isInstance(bean));
        if (b) {
            this.bean = null;
        }
        return this;
    }

    public Object getBean() {
        return bean;
    }

    public ReflectUtils setBean(Object bean) {
        this.assertClazzNotNull(this.clazz);
        this.assertBeanInstanceOfClazz(bean);
        this.bean = bean;
        return this;
    }

    public ReflectUtils newInstance() throws ReflectionException {
        return this.newInstance(new Object[0]);
    }

    public ReflectUtils newInstance(Object... args) throws ReflectionException {
        this.assertClazzNotNull(this.clazz);
        // You parameters can null, but the args must is not null.
        Assert.notNull(args, "Args must is not null. ");
        try {
            Class<?>[] types = ReflectUtils.findTypes(args);
            Constructor<?> ct = ReflectUtils.findConstructor(clazz, types);
            ct = ReflectUtils.accessible(ct);
            bean = ct.newInstance(args);
            return this;
        }
        catch (Exception e) {
            throw new ReflectionException(e);
        }
    }

    public Object get(String fieldName) throws ReflectionException {
        this.assertClazzNotNull(this.clazz);
        this.assertBeanInstanceOfClazz(this.bean);
        Assert.notBlank(fieldName, "Field name must is not blank. ");
        try {
            Field field = ReflectUtils.findField(clazz, fieldName);
            field = ReflectUtils.accessible(field);
            return field.get(bean);
        }
        catch (Exception e) {
            throw new ReflectionException(e);
        }
    }

    public ReflectUtils set(String fieldName, Object value) throws ReflectionException {
        this.assertClazzNotNull(this.clazz);
        this.assertBeanInstanceOfClazz(this.bean);
        Assert.notBlank(fieldName, "Field name must is not blank. ");
        try {
            Field field = ReflectUtils.findField(clazz, fieldName);
            field = ReflectUtils.accessible(field);
            field.set(bean, value);
            return this;
        }
        catch (Exception e) {
            throw new ReflectionException(e);
        }
    }

    public Object callGetter(String methodName) throws ReflectionException {
        Assert.notBlank(methodName, "Method name must is not blank. ");
        if (!methodName.startsWith(STRING_GET)) {
            methodName = STRING_GET + StringUtils.capitalize(methodName);
        }
        return this.call(methodName);
    }

    public Object callSetter(String methodName, Object value) throws ReflectionException {
        Assert.notBlank(methodName, "Method name must is not blank. ");
        if (!methodName.startsWith(STRING_SET)) {
            methodName = STRING_SET + StringUtils.capitalize(methodName);
        }
        return this.call(methodName, value);
    }

    public Object call(String methodName) throws ReflectionException {
        return this.call(methodName, new Object[0]);
    }

    public Object call(String methodName, Object... args) throws ReflectionException {
        this.assertClazzNotNull(this.clazz);
        this.assertBeanInstanceOfClazz(this.bean);
        Assert.notBlank(methodName, "Method name must is not blank. ");
        // You parameters can null, but the args must is not null.
        Assert.notNull(args, "Args must is not null. ");
        try {
            Class<?>[] types = ReflectUtils.findTypes(args);
            // Try invoking the "canonical" method,
            // i.e. the one with exact matching argument types
            try {
                Method method = ReflectUtils.findMethod(clazz, methodName, types);
                method = ReflectUtils.accessible(method);
                return method.invoke(bean, args);
            }
            // If there is no exact match, try to find a method that has a "similar"
            // signature if primitive argument types are converted to their wrappers
            catch (NoSuchMethodException e) {
                Method method = ReflectUtils.findSimilarMethod(clazz, methodName, types);
                method = ReflectUtils.accessible(method);
                return method.invoke(bean, args);
            }
        }
        catch (Exception e) {
            throw new ReflectionException(e);
        }
    }

    @Override
    public String toString() {
        this.assertClazzNotNull(this.clazz);
        this.assertBeanInstanceOfClazz(this.bean);
        return "ReflectUtils{" +
                "clazz=" + clazz.getName() +
                ", bean=" + bean +
                "}";
    }

}
