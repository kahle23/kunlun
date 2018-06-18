package artoria.aop;

/**
 * Proxy factory, can generate proxy object.
 * @author Kahle
 */
public interface ProxyFactory {

    /**
     * Get proxy object instance.
     * @param originalClass Be proxied object class
     * @param interceptor Method interceptor
     * @return Proxy object
     */
    Object getInstance(Class<?> originalClass, Interceptor interceptor);

}
