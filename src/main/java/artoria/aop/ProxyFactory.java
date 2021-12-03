package artoria.aop;

/**
 * The proxy factory for generate proxy object.
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
