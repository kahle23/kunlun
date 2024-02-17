/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.aop.support;

import kunlun.aop.AbstractProxyHandler;
import kunlun.aop.Interceptor;
import kunlun.util.Assert;
import kunlun.util.ClassLoaderUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * The proxy handler simple implement by jdk.
 * @author Kahle
 */
public class SimpleProxyHandler extends AbstractProxyHandler {
    private static final ClassLoader CLASS_LOADER = ClassLoaderUtils.getDefaultClassLoader();

    private static class InvocationHandlerAdapter implements InvocationHandler {
        private final Interceptor interceptor;

        public InvocationHandlerAdapter(Interceptor interceptor) {
            Assert.notNull(interceptor, "Parameter \"interceptor\" must not null. ");
            this.interceptor = interceptor;
        }

        @Override
        public Object invoke(Object proxyObject, Method method, Object[] args) throws Throwable {

            return interceptor.intercept(proxyObject, method, args);
        }

    }

    @Override
    public Object proxy(Class<?> originalClass, Interceptor interceptor) {
        Assert.notNull(originalClass, "Parameter \"originalClass\" must not null. ");
        Assert.notNull(interceptor, "Parameter \"interceptor\" must not null. ");
        Class<?>[] interfaces = originalClass.isInterface() ?
                new Class[]{ originalClass } : originalClass.getInterfaces();
        InvocationHandler handler = new InvocationHandlerAdapter(interceptor);
        return Proxy.newProxyInstance(CLASS_LOADER, interfaces, handler);
    }

}
