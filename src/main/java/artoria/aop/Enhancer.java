package artoria.aop;

import artoria.util.Assert;

import java.util.logging.Logger;

/**
 * Object enhancer.
 * @author Kahle
 */
public class Enhancer {
    private static Logger log = Logger.getLogger(Enhancer.class.getName());
    private static ProxyFactory proxyFactory;

    public static ProxyFactory getProxyFactory() {
        if (proxyFactory != null) {
            return proxyFactory;
        }
        synchronized (ProxyFactory.class) {
            if (proxyFactory != null) {
                return proxyFactory;
            }
            setProxyFactory(new SimpleProxyFactory());
            return proxyFactory;
        }
    }

    public static void setProxyFactory(ProxyFactory proxyFactory) {
        Assert.notNull(proxyFactory, "Parameter \"proxyFactory\" must not null. ");
        synchronized (ProxyFactory.class) {
            log.info("Set proxy factory: " + proxyFactory.getClass().getName());
            Enhancer.proxyFactory = proxyFactory;
        }
    }

    public static Object enhance(Class<?> originalClass, Interceptor interceptor) {

        return getProxyFactory().getInstance(originalClass, interceptor);
    }

    public static Object enhance(Object original, Interceptor interceptor) {
        Assert.notNull(original, "Parameter \"original\" must not null. ");
        return getProxyFactory().getInstance(original.getClass(), interceptor);
    }

}
