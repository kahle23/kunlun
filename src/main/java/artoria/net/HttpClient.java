package artoria.net;

import java.io.IOException;

/**
 * Http client tools.
 * @author Kahle
 * TODO: 2023/6/2 Deletable
 */
@Deprecated
public interface HttpClient {

    /**
     * Execute http request and return a response.
     * @param request Http request
     * @return Http response
     * @throws IOException IO exception or subclass
     */
    HttpResponse execute(HttpRequest request) throws IOException;

}
