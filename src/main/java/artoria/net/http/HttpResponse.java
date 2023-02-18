package artoria.net.http;

import java.io.InputStream;

/**
 * The http response.
 * @author Kahle
 */
public interface HttpResponse extends HttpClient.HttpBase {

    /**
     * Get the http response status code.
     * @return The http response status code
     */
    int getStatusCode();

    /**
     * Get the http response body stream.
     * @return The http response body stream
     */
    InputStream getBodyStream();

    /**
     * Get the http response body as bytes.
     * @return The http response body as bytes
     */
    byte[] getBodyAsBytes();

    /**
     * Get the http response body as string (default utf-8).
     * @return The http response body as string
     */
    String getBodyAsString();

    /**
     * Get the http response body as string.
     * @param charset The charset used to convert string
     * @return The http response body as string
     */
    String getBodyAsString(String charset);

}
