package artoria.net;

import java.io.IOException;
import java.nio.charset.Charset;

import static artoria.common.Constants.DEFAULT_CHARSET_NAME;

/**
 * Http response message.
 * @author Kahle
 */
public class HttpResponse extends HttpMessage {
    private static final int MAX_REDIRECTS = 20;
    private int numRedirects = 0;
    private HttpRequest request;
    private int statusCode;
    private String statusMessage;
    private byte[] body;

    public HttpResponse() {
    }

    public HttpResponse(HttpResponse previous) throws IOException {
        if (previous == null) { return; }
        this.numRedirects = previous.getNumRedirects() + 1;
        if (this.numRedirects >= MAX_REDIRECTS) {
            throw new IOException("Too many redirects occurred trying to load URL \"" + previous.getUrl() + "\". ");
        }
    }

    public int getNumRedirects() {

        return this.numRedirects;
    }

    public HttpRequest getRequest() {

        return this.request;
    }

    public void setRequest(HttpRequest request) {

        this.request = request;
    }

    public int getStatusCode() {

        return this.statusCode;
    }

    public void setStatusCode(int statusCode) {

        this.statusCode = statusCode;
    }

    public String getStatusMessage() {

        return this.statusMessage;
    }

    public void setStatusMessage(String statusMessage) {

        this.statusMessage = statusMessage;
    }

    public byte[] getBody() {

        return this.body;
    }

    public void setBody(byte[] body) {

        this.body = body;
    }

    public String getBodyAsString() {
        String encoding = this.getCharset();
        encoding = encoding != null ? encoding : DEFAULT_CHARSET_NAME;
        return this.getBodyAsString(encoding);
    }

    public String getBodyAsString(String encoding) {
        if (this.body == null) { return null; }
        Charset charset = Charset.forName(encoding);
        return new String(this.body, charset);
    }

}
