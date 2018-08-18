package artoria.net;

import java.io.IOException;

/**
 * Http client tools.
 * @author Kahle
 */
public interface HttpClient {

    /**
     * Execute http request and return a response.
     * @param request Http request
     * @return Http response
     * @throws IOException IO exception or subclass
     */
    HttpResponse execute(HttpRequest request) throws IOException;

}
