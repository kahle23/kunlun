package artoria.net;

import java.io.IOException;
import java.nio.charset.Charset;

import static artoria.common.Constants.*;

/**
 * Http response message.
 * @author Kahle
 * TODO: 2023/6/2 Deletable
 */
@Deprecated
public class HttpResponse extends HttpMessage {
    private static final int MAX_REDIRECTS = TWENTY;
    private int numRedirects = ZERO;
    private HttpRequest request;
    private int statusCode;
    private String statusMessage;
    private byte[] body;

    public HttpResponse() {
    }

    public HttpResponse(HttpResponse previousResponse) throws IOException {
        if (previousResponse == null) { return; }
        numRedirects = previousResponse.getNumRedirects() + ONE;
        if (numRedirects >= MAX_REDIRECTS) {
            throw new IOException(
                    "Too many redirects occurred trying to load URL \"" + previousResponse.getUrl() + "\". "
            );
        }
    }

    public int getNumRedirects() {

        return numRedirects;
    }

    public HttpRequest getRequest() {

        return request;
    }

    public void setRequest(HttpRequest request) {

        this.request = request;
    }

    public int getStatusCode() {

        return statusCode;
    }

    public void setStatusCode(int statusCode) {

        this.statusCode = statusCode;
    }

    public String getStatusMessage() {

        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {

        this.statusMessage = statusMessage;
    }

    public byte[] getBody() {

        return body;
    }

    public void setBody(byte[] body) {

        this.body = body;
    }

    public String getBodyAsString() {
        String encoding = getCharset();
        encoding = encoding != null ? encoding : DEFAULT_CHARSET_NAME;
        return getBodyAsString(encoding);
    }

    public String getBodyAsString(String encoding) {
        if (body == null) { return null; }
        Charset charset = Charset.forName(encoding);
        return new String(body, charset);
    }

}
