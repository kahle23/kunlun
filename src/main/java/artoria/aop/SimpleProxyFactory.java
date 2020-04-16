package artoria.aop;

import artoria.util.Assert;
import artoria.util.ClassLoaderUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Proxy factory simple implement by jdk.
 * @author Kahle
 */
public class SimpleProxyFactory implements ProxyFactory {
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
    public Object getInstance(Class<?> originalClass, Interceptor interceptor) {
        Assert.notNull(originalClass, "Parameter \"originalClass\" must not null. ");
        Assert.notNull(interceptor, "Parameter \"interceptor\" must not null. ");
        Class<?>[] interfaces = originalClass.isInterface() ?
                new Class[]{ originalClass } : originalClass.getInterfaces();
        InvocationHandler handler = new InvocationHandlerAdapter(interceptor);
        return Proxy.newProxyInstance(CLASS_LOADER, interfaces, handler);
    }

}
