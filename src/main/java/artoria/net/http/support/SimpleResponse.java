package artoria.net.http.support;

import artoria.exception.ExceptionUtils;
import artoria.io.IOUtils;
import artoria.net.http.HttpResponse;
import artoria.util.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

/**
 * The simple http response.
 * @author Kahle
 */
public class SimpleResponse extends AbstractHttpBase implements HttpResponse {
    private InputStream bodyStream;
    private String statusMessage;
    private int statusCode;

    public SimpleResponse() {

    }

    @Override
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

    @Override
    public InputStream getBodyStream() {

        return bodyStream;
    }

    public void setBodyStream(InputStream bodyStream) {

        this.bodyStream = bodyStream;
    }

    @Override
    public byte[] getBodyAsBytes() {
        if (bodyStream == null) { return null; }
        try {
            return IOUtils.toByteArray(bodyStream);
        }
        catch (IOException e) {
            throw ExceptionUtils.wrap(e);
        }
    }

    @Override
    public String getBodyAsString() {

        return getBodyAsString(getCharset());
    }

    @Override
    public String getBodyAsString(String charset) {
        if (bodyStream == null) { return null; }
        if (StringUtils.isBlank(charset)) { charset = getCharset(); }
        return new String(getBodyAsBytes(), Charset.forName(charset));
    }

}
