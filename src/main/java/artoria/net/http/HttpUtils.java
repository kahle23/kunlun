package artoria.net.http;

import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import artoria.util.Assert;

/**
 * The http tools.
 * @author Kahle
 */
public class HttpUtils {
    private static final Logger log = LoggerFactory.getLogger(HttpUtils.class);
    private static volatile HttpProvider httpProvider;

    public static HttpProvider getHttpProvider() {
        if (httpProvider != null) { return httpProvider; }
        synchronized (HttpUtils.class) {
            if (httpProvider != null) { return httpProvider; }
            HttpUtils.setHttpProvider(new SimpleHttpProvider());
            return httpProvider;
        }
    }

    public static void setHttpProvider(HttpProvider httpProvider) {
        Assert.notNull(httpProvider, "Parameter \"httpProvider\" must not null. ");
        log.info("Set http provider: {}", httpProvider.getClass().getName());
        HttpUtils.httpProvider = httpProvider;
    }

    public static String getDefaultClientName() {

        return getHttpProvider().getDefaultClientName();
    }

    public static void setDefaultClientName(String defaultClientName) {

        getHttpProvider().setDefaultClientName(defaultClientName);
    }

    public static void registerClient(String name, HttpClient httpClient) {

        getHttpProvider().registerClient(name, httpClient);
    }

    public static void deregisterClient(String name) {

        getHttpProvider().deregisterClient(name);
    }

    public static HttpClient getHttpClient(String name) {

        return getHttpProvider().getHttpClient(name);
    }

    public static HttpResponse execute(HttpRequest request) {

        return getHttpProvider().execute(getDefaultClientName(), request);
    }

    public static HttpResponse execute(String name, HttpRequest request) {

        return getHttpProvider().execute(name, request);
    }

}
