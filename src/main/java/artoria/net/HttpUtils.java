package artoria.net;

import artoria.exception.ExceptionUtils;
import artoria.util.Assert;
import artoria.util.MapUtils;

import java.util.Map;
import java.util.logging.Logger;

/**
 * Simple http tools.
 * @author Kahle
 */
public class HttpUtils {
    private static Logger log = Logger.getLogger(HttpUtils.class.getName());
    private static HttpClient httpClient;

    static {
        HttpUtils.setHttpClient(new SimpleHttpClient());
    }

    public static HttpClient getHttpClient() {
        return httpClient;
    }

    public static void setHttpClient(HttpClient httpClient) {
        Assert.notNull(httpClient, "Parameter \"httpClient\" must not null. ");
        log.info("Set http client: " + httpClient.getClass().getName());
        HttpUtils.httpClient = httpClient;
    }

    public static String get(String url) {
        return HttpUtils.execute(url, HttpMethod.GET, null);
    }

    public static String get(String url, Map<String, Object> params) {
        return HttpUtils.execute(url, HttpMethod.GET, params);
    }

    public static String post(String url) {
        return HttpUtils.execute(url, HttpMethod.POST, null);
    }

    public static String post(String url, Map<String, Object> params) {
        return HttpUtils.execute(url, HttpMethod.POST, params);
    }

    public static String put(String url) {
        return HttpUtils.execute(url, HttpMethod.PUT, null);
    }

    public static String put(String url, Map<String, Object> params) {
        return HttpUtils.execute(url, HttpMethod.PUT, params);
    }

    public static String delete(String url) {
        return HttpUtils.execute(url, HttpMethod.DELETE, null);
    }

    public static String delete(String url, Map<String, Object> params) {
        return HttpUtils.execute(url, HttpMethod.PUT, params);
    }

    public static String execute(String url, HttpMethod method) {
        return HttpUtils.execute(url, method, null);
    }

    public static String execute(String url, HttpMethod method, Map<String, Object> params) {
        try {
            HttpRequest request = new HttpRequest();
            request.setMethod(method);
            request.setUrl(url);
            if (MapUtils.isNotEmpty(params)) {
                request.addParameters(params);
            }
            HttpResponse response = httpClient.execute(request);
            return response.getBodyAsString();
        }
        catch (Exception e) {
            throw ExceptionUtils.wrap(e);
        }
    }

}
