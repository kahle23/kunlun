package artoria.net.http;

import java.util.List;
import java.util.Map;

/**
 * Provide the highest level of abstraction for http client.
 * @author Kahle
 */
public interface HttpClient {

    /**
     * Get the common properties of the settings.
     * @return The common properties that is set
     */
    Map<Object, Object> getCommonProperties();

    /**
     * Set common properties for the http client.
     * @param properties The properties to be set
     */
    void setCommonProperties(Map<?, ?> properties);

    /**
     * Execute http request and return a response.
     * @param request The http request
     * @return The http response
     */
    HttpResponse execute(HttpRequest request);

    /**
     * The base http information.
     * @author Kahle
     */
    interface HttpBase {

        /**
         * Get the http method.
         * @return The http method
         */
        HttpMethod getMethod();

        /**
         * Get the http url.
         * @return The http url
         */
        String     getUrl();

        /**
         * Get the http charset.
         * @return The http charset
         */
        String     getCharset();

        /**
         * Get the http headers.
         * @return The http headers
         */
        Map<String, List<String>> getHeaders();

    }

}
