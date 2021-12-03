package artoria.aop;

import java.lang.reflect.Method;

/**
 * The interceptor for enhance object method.
 * @author Kahle
 */
public interface Interceptor {

    /**
     * Processes a method invocation on a proxy instance and returns the result.
     * @param proxyObject The proxy instance that the method was invoked on
     * @param method The method instance
     * @param args The method arguments
     * @return The value to return from the method invocation on the proxy instance
     * @throws Throwable The exception to throw from the method invocation on the proxy instance
     */
    Object intercept(Object proxyObject, Method method, Object[] args) throws Throwable;

}
