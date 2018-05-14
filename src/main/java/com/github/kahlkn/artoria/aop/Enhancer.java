package com.github.kahlkn.artoria.aop;

import com.github.kahlkn.artoria.logging.Logger;
import com.github.kahlkn.artoria.logging.LoggerFactory;
import com.github.kahlkn.artoria.util.Assert;

/**
 * Object enhancer.
 * @author Kahle
 */
public class Enhancer {
    private static Logger log = LoggerFactory.getLogger(Enhancer.class);
    private static ProxyFactory proxyFactory;

    static {
        Enhancer.setProxyFactory(new JdkProxyFactory());
    }

    public static ProxyFactory getProxyFactory() {
        return proxyFactory;
    }

    public static void setProxyFactory(ProxyFactory proxyFactory) {
        Assert.notNull(proxyFactory, "Parameter \"proxyFactory\" must not null. ");
        log.info("Set proxy factory: " + proxyFactory.getClass().getName());
        Enhancer.proxyFactory = proxyFactory;
    }

    public static Object enhance(Class<?> originalClass, Interceptor interceptor) {
        return proxyFactory.getInstance(originalClass, interceptor);
    }

    public static Object enhance(Object original, Interceptor interceptor) {
        Assert.notNull(original, "Parameter \"original\" must not null. ");
        return proxyFactory.getInstance(original.getClass(), interceptor);
    }

}
