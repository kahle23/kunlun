package com.github.kahlkn.artoria.reflect;

import com.github.kahlkn.artoria.aop.Enhancer;
import com.github.kahlkn.artoria.aop.Interceptor;
import com.github.kahlkn.artoria.cache.CacheUtils;
import com.github.kahlkn.artoria.cache.DataLoader;
import com.github.kahlkn.artoria.exception.ExceptionUtils;
import com.github.kahlkn.artoria.util.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.*;
import java.util.*;

/**
 * Reflect tools.
 * @author Kahle
 */
public class ReflectUtils {
    private static Logger log = LoggerFactory.getLogger(ReflectUtils.class);
    private static CacheUtils cacheUtils;
    private static Reflecter reflecter;

    static {
        ReflectUtils.setCacheUtils(new CacheUtils());
        ReflectUtils.setReflecter(new JdkReflecter());
    }

    public static CacheUtils getCacheUtils() {
        return cacheUtils;
    }

    public static void setCacheUtils(CacheUtils cacheUtils) {
        Assert.notNull(cacheUtils, "Parameter \"cacheUtils\" must not null. ");
        ReflectUtils.cacheUtils = cacheUtils;
    }

    public static Reflecter getReflecter() {
        return reflecter;
    }

    public static void setReflecter(Reflecter reflecter) {
        Assert.notNull(reflecter, "Parameter \"reflecter\" must not null. ");
        reflecter = CacheEnhancer.getInstance(reflecter);
        log.info("Set reflecter: {}", reflecter.getClass().getName());
        ReflectUtils.reflecter = reflecter;
    }

    public static Object newInstance(String className, Object... args) throws ClassNotFoundException
            , InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        Class<?> clazz = reflecter.forName(className);
        return args.length == 0 ? clazz.newInstance() : ReflectUtils.newInstance(clazz, args);
    }

    public static Object newInstance(Class<?> clazz, Object... args) throws InvocationTargetException
            , NoSuchMethodException, InstantiationException, IllegalAccessException {
        Assert.notNull(clazz, "Parameter \"clazz\" must not null. ");
        Class<?>[] types = reflecter.findParameterTypes(args);
        Constructor<?> constructor = reflecter.findConstructor(clazz, types);
        reflecter.makeAccessible(constructor);
        return constructor.newInstance(args);
    }

    public static Class<?>[] findParameterTypes(Object... values) {
        return reflecter.findParameterTypes(values);
    }

    public static boolean matchParameterTypes(Class<?>[] declaredTypes, Class<?>[] actualTypes) {
        return reflecter.matchParameterTypes(declaredTypes, actualTypes);
    }

    public static <T extends AccessibleObject> boolean checkAccessible(T accessible) {
        return reflecter.checkAccessible(accessible);
    }

    public static <T extends AccessibleObject> void makeAccessible(T accessible) {
        reflecter.makeAccessible(accessible);
    }

    public static Constructor<?>[] findConstructors(Class<?> clazz) {
        return reflecter.findConstructors(clazz);
    }

    public static Constructor<?> findConstructor(Class<?> clazz, Class<?>... parameterTypes) throws NoSuchMethodException {
        return reflecter.findConstructor(clazz, parameterTypes);
    }

    public static Field[] findFields(Class<?> clazz) {
        return reflecter.findFields(clazz);
    }

    public static Field[] findDeclaredFields(Class<?> clazz) {
        return reflecter.findDeclaredFields(clazz);
    }

    public static Field[] findAccessFields(Class<?> clazz) {
        return reflecter.findAccessFields(clazz);
    }

    public static Field findField(Class<?> clazz, String fieldName) throws NoSuchFieldException {
        return reflecter.findField(clazz, fieldName);
    }

    public static Method[] findMethods(Class<?> clazz) {
        return reflecter.findMethods(clazz);
    }

    public static Method[] findDeclaredMethods(Class<?> clazz) {
        return reflecter.findDeclaredMethods(clazz);
    }

    public static Method[] findAccessMethods(Class<?> clazz) {
        return reflecter.findAccessMethods(clazz);
    }

    public static Map<String, Method> findReadMethods(Class<?> clazz) {
        return reflecter.findReadMethods(clazz);
    }

    public static Map<String, Method> findWriteMethods(Class<?> clazz) {
        return reflecter.findWriteMethods(clazz);
    }

    public static Method findMethod(Class<?> clazz, String methodName, Class<?>... parameterTypes) throws NoSuchMethodException {
        return reflecter.findMethod(clazz, methodName, parameterTypes);
    }

    public static Method findSimilarMethod(Class<?> clazz, String methodName, Class<?>... parameterTypes) throws NoSuchMethodException {
        return reflecter.findSimilarMethod(clazz, methodName, parameterTypes);
    }

    private static class CacheEnhancer {

        public static Reflecter getInstance(Reflecter reflecter) {
            Assert.notNull(reflecter, "Parameter \"reflecter\" must not null. ");
            ReflecterInterceptor intr = new ReflecterInterceptor(reflecter);
            return (Reflecter) Enhancer.enhance(reflecter, intr);
        }

        private static class DataLoaderImpl implements DataLoader {
            private Object object;
            private Method method;
            private Object[] args;

            public DataLoaderImpl(Object object, Method method, Object[] args) {
                this.object = object;
                this.method = method;
                this.args = args;
            }

            @Override
            public Object load() {
                try {
                    return method.invoke(object, args);
                }
                catch (Exception e) {
                    throw ExceptionUtils.wrap(e);
                }
            }

        }

        private static class ReflecterInterceptor implements Interceptor {
            private static final List<String> METHOD_NAMES;
            private Reflecter original;

            static {
                List<String> list = new ArrayList<String>();
                Collections.addAll(list, "forName"
                        , "findConstructors", "findConstructor"
                        , "findFields", "findDeclaredFields"
                        , "findAccessFields", "findField"
                        , "findMethods", "findDeclaredMethods"
                        , "findAccessMethods", "findReadMethods"
                        , "findWriteMethods", "findMethod"
                        , "findSimilarMethod");
                METHOD_NAMES = Collections.unmodifiableList(list);
            }

            ReflecterInterceptor(Reflecter original) {
                this.original = original;
            }

            @Override
            public Object intercept(Object proxyObject, Method method, Object[] args) throws Throwable {
                if (METHOD_NAMES.contains(method.getName())) {
                    DataLoader loader = new DataLoaderImpl(original, method, args);
                    String key = method.getName() + Arrays.toString(args);
                    return cacheUtils.get(key, loader);
                }
                else {
                    return method.invoke(original, args);
                }
            }

        }

    }

}
