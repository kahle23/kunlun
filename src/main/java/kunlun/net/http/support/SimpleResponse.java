/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.net.http.support;

import kunlun.exception.ExceptionUtils;
import kunlun.io.util.IOUtils;
import kunlun.net.http.AbstractHttpBase;
import kunlun.net.http.HttpMethod;
import kunlun.net.http.HttpResponse;
import kunlun.util.CloseUtils;
import kunlun.util.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

/**
 * The simple http response.
 * @author Kahle
 */
public class SimpleResponse extends AbstractHttpBase implements HttpResponse {

    public static SimpleResponse of(HttpMethod method, String url) {

        return new SimpleResponse(method, url);
    }

    public static SimpleResponse of() {

        return new SimpleResponse();
    }


    private InputStream bodyStream;
    private String statusMessage;
    private int statusCode;

    public SimpleResponse(HttpMethod method, String url) {
        this.setMethod(method);
        this.setUrl(url);
    }

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
        finally {
            CloseUtils.closeQuietly(bodyStream);
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
