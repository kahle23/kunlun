package com.github.kahlkn.artoria.aop;

import com.github.kahlkn.artoria.util.Assert;
import com.github.kahlkn.artoria.util.ClassUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Proxy factory implements by jdk.
 * @author Kahle
 */
public class JdkProxyFactory implements ProxyFactory {
    private static final ClassLoader LOADER = ClassUtils.getDefaultClassLoader();

    private static class InvocationHandlerAdapter implements InvocationHandler {
        private Interceptor interceptor;

        public Interceptor getInterceptor() {
            return interceptor;
        }

        public void setInterceptor(Interceptor interceptor) {
            this.interceptor = interceptor;
        }

        public InvocationHandlerAdapter(Interceptor interceptor) {
            this.interceptor = interceptor;
        }

        @Override
        public Object invoke(Object proxyObject, Method method, Object[] args) throws Throwable {
            return interceptor.intercept(proxyObject, method, args);
        }

    }

    @Override
    public Object getInstance(Class<?> originalClass, Interceptor interceptor) {
        Assert.notNull(originalClass, "Parameter \"originalClass\" must not null. ");
        Assert.notNull(interceptor, "Parameter \"interceptor\" must not null. ");
        Class<?>[] interfaces = originalClass.getInterfaces();
        Assert.notEmpty(interfaces, "Original class \""
                + originalClass.getName() + "\" not implements any interfaces. ");
        InvocationHandler handler = new InvocationHandlerAdapter(interceptor);
        return Proxy.newProxyInstance(LOADER, interfaces, handler);
    }

}
