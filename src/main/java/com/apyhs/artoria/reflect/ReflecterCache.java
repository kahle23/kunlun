package com.apyhs.artoria.reflect;

import com.apyhs.artoria.aop.Enhancer;
import com.apyhs.artoria.aop.Interceptor;
import com.apyhs.artoria.cache.CacheUtils;
import com.apyhs.artoria.cache.DataLoader;
import com.apyhs.artoria.exception.UncheckedException;
import com.apyhs.artoria.util.Assert;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ReflecterCache {

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
                throw new UncheckedException(e);
            }
        }

    }

    private static class ReflecterInterceptor implements Interceptor {
        private static final List<String> METHOD_NAMES;
        private Reflecter original;
        private String className;

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

        public ReflecterInterceptor(Reflecter original) {
            this.original = original;
            this.className = original.getClass().getName();
        }

        @Override
        public Object intercept(Object proxyObject, Method method, Object[] args) throws Throwable {
            if (METHOD_NAMES.contains(method.getName())) {
                DataLoader loader = new DataLoaderImpl(original, method, args);
                String key = className + method.getName() + Arrays.toString(args);
                return CacheUtils.get(key, loader);
            }
            else {
                return method.invoke(original, args);
            }
        }

    }

}
