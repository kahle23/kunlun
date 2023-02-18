package artoria.net.http;

import java.util.Map;

/**
 * The http provider.
 * @author Kahle
 */
public interface HttpProvider {

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
     * Register the http client.
     * @param name The http client name
     * @param httpClient The http client
     */
    void registerClient(String name, HttpClient httpClient);

    /**
     * Deregister the http client.
     * @param name The http client name
     */
    void deregisterClient(String name);

    /**
     * Get the http client by name.
     * @param name The http client name
     * @return The http client
     */
    HttpClient getHttpClient(String name);

    /**
     * Execute http request and return a response.
     * @param name The http client name
     * @param request Http request
     * @return Http response
     */
    HttpResponse execute(String name, HttpRequest request);

}
