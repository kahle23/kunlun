package artoria.aop;

import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import artoria.util.Assert;

/**
 * Object enhancer.
 * @author Kahle
 */
public class Enhancer {
    private static final ProxyFactory DEFAULT_PROXY_FACTORY = new DefaultProxyFactory();
    private static Logger log = LoggerFactory.getLogger(Enhancer.class);
    private static ProxyFactory proxyFactory;

    public static ProxyFactory getProxyFactory() {

        return proxyFactory != null ? proxyFactory : DEFAULT_PROXY_FACTORY;
    }

    public static void setProxyFactory(ProxyFactory proxyFactory) {
        Assert.notNull(proxyFactory, "Parameter \"proxyFactory\" must not null. ");
        log.info("Set proxy factory: {}", proxyFactory.getClass().getName());
        Enhancer.proxyFactory = proxyFactory;
    }

    public static Object enhance(Class<?> originalClass, Interceptor interceptor) {

        return getProxyFactory().getInstance(originalClass, interceptor);
    }

    public static Object enhance(Object original, Interceptor interceptor) {
        Assert.notNull(original, "Parameter \"original\" must not null. ");
        return getProxyFactory().getInstance(original.getClass(), interceptor);
    }

}
