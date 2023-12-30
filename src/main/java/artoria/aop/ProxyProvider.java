package artoria.aop;

import java.util.Map;

/**
 * The proxy provider for generate proxy object.
 * @author Kahle
 */
public interface ProxyProvider {

    /**
     * Register common properties' information.
     * @param commonProperties The common properties
     */
    void registerCommonProperties(Map<?, ?> commonProperties);

    /**
     * Clears common properties' information.
     */
    void clearCommonProperties();

    /**
     * Get common properties' information.
     * @return The common properties
     */
    Map<String, Object> getCommonProperties();

    /**
     * Get the default handler name.
     * @return The default handler name
     */
    String getDefaultHandlerName();

    /**
     * Set the default handler name.
     * Depending on the implementation class, this method may throw an error
     *  (i.e. it does not allow the modification of the default handler name).
     * @param defaultHandlerName The default handler name
     */
    void setDefaultHandlerName(String defaultHandlerName);

    /**
     * Register the proxy handler.
     * @param name The proxy handler name
     * @param proxyHandler The proxy handler
     */
    void registerHandler(String name, ProxyHandler proxyHandler);

    /**
     * Deregister the proxy handler.
     * @param name The proxy handler name
     */
    void deregisterHandler(String name);

    /**
     * Get the proxy handler by name.
     * @param name The proxy handler name
     * @return The proxy handler
     */
    ProxyHandler getProxyHandler(String name);

    /**
     * Get proxy object instance.
     * @param handlerName The proxy handler name
     * @param originalClass Be proxied object class
     * @param interceptor Method interceptor
     * @return Proxy object
     */
    Object proxy(String handlerName, Class<?> originalClass, Interceptor interceptor);

    /**
     * Get proxy object instance.
     * @param handlerName The proxy handler name
     * @param interceptor Method interceptor
     * @return Proxy object
     */
    <T> T proxy(String handlerName, AbstractInterceptor<T> interceptor);

}
