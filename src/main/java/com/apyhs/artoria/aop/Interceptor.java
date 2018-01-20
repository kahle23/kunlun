package com.apyhs.artoria.aop;

import java.lang.reflect.Method;

/**
 * Interceptor, can enhance object method.
 * @author Kahle
 */
public interface Interceptor {

    /**
     * Control proxied object method.
     * @param proxyObject Generated proxy object
     * @param method Current method
     * @param args Current method arguments
     * @return method invoke result
     * @throws Throwable Maybe have checked exception
     */
    Object intercept(Object proxyObject, Method method, Object[] args) throws Throwable;

}
