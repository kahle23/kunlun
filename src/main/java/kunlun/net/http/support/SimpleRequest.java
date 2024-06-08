/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.net.http.support;

import kunlun.data.tuple.KeyValue;
import kunlun.data.tuple.KeyValueImpl;
import kunlun.net.http.AbstractHttpBase;
import kunlun.net.http.HttpMethod;
import kunlun.net.http.HttpRequest;
import kunlun.util.Assert;
import kunlun.util.MapUtils;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import static kunlun.common.constant.Numbers.ZERO;

/**
 * The simple http request.
 * @author Kahle
 */
public class SimpleRequest extends AbstractHttpBase implements HttpRequest {

    public static SimpleRequest of(HttpMethod method,
                                   String url,
                                   Map<String, ?> headers,
                                   Map<String, ?> parameters,
                                   Object body) {
        SimpleRequest request = new SimpleRequest(method, url);
        if (MapUtils.isNotEmpty(headers)) {
            for (Map.Entry<String, ?> entry : headers.entrySet()) {
                String value = entry.getValue() != null ? String.valueOf(entry.getValue()) : null;
                request.addHeader(entry.getKey(), value);
            }
        }
        if (MapUtils.isNotEmpty(parameters)) {
            request.addParameters(parameters);
        }
        if (body != null) { request.setBody(body); }
        return request;
    }

    public static SimpleRequest of(HttpMethod method, String url) {

        return new SimpleRequest(method, url);
    }

    public static SimpleRequest of() {

        return new SimpleRequest();
    }


    /**
     * The network proxy information.
     */
    private Proxy proxy;
    /**
     * The connect timeout in milliseconds.
     */
    private Integer connectTimeout = 19000;
    /**
     * The read timeout in milliseconds.
     */
    private Integer readTimeout = 19000;
    /**
     * Validate https certificate (default validate, false unsafe).
     */
    private Boolean validateCertificate;
    /**
     * Whether to follow the redirect (default false).
     */
    private Boolean followRedirect;
    /**
     * Whether to ignore http errors (default true).
     */
    private Boolean ignoreHttpErrors;
    /**
     * The http response stream will be the original stream (default false).
     */
    private Boolean stream;
    /**
     * The http request parameters.
     */
    private final Collection<KeyValue<String, Object>> parameters = new ArrayList<KeyValue<String, Object>>();
    /**
     * The http request body.
     */
    private Object body;

    public SimpleRequest(HttpMethod method, String url) {
        this.setMethod(method);
        this.setUrl(url);
    }

    public SimpleRequest() {

    }

    public Proxy getProxy() {

        return proxy;
    }

    public void setProxy(Proxy proxy) {
        Assert.notNull(proxy, "Parameter \"proxy\" must not null. ");
        this.proxy = proxy;
    }

    public void setProxy(String hostname, int port) {
        Assert.notBlank(hostname, "Parameter \"hostname\" must not blank. ");
        Assert.isTrue(port > ZERO, "Parameter \"port\" must greater than 0. ");
        SocketAddress address = new InetSocketAddress(hostname, port);
        this.proxy = new Proxy(Proxy.Type.HTTP, address);
    }

    public Integer getConnectTimeout() {

        return connectTimeout;
    }

    public void setConnectTimeout(Integer connectTimeout) {
        Assert.notNull(connectTimeout, "Parameter \"connectTimeout\" must not null. ");
        Assert.isTrue(connectTimeout > ZERO, "Parameter \"connectTimeout\" must greater than 0. ");
        this.connectTimeout = connectTimeout;
    }

    public Integer getReadTimeout() {

        return readTimeout;
    }

    public void setReadTimeout(Integer readTimeout) {
        Assert.notNull(readTimeout, "Parameter \"readTimeout\" must not null. ");
        Assert.isTrue(readTimeout > ZERO, "Parameter \"readTimeout\" must greater than 0. ");
        this.readTimeout = readTimeout;
    }

    public Boolean getValidateCertificate() {

        return validateCertificate;
    }

    public void setValidateCertificate(Boolean validateCertificate) {
        Assert.notNull(validateCertificate, "Parameter \"validateCertificate\" must not null. ");
        this.validateCertificate = validateCertificate;
    }

    public Boolean getFollowRedirect() {

        return followRedirect;
    }

    public void setFollowRedirect(Boolean followRedirect) {
        Assert.notNull(followRedirect, "Parameter \"followRedirect\" must not null. ");
        this.followRedirect = followRedirect;
    }

    public Boolean getIgnoreHttpErrors() {

        return ignoreHttpErrors;
    }

    public void setIgnoreHttpErrors(Boolean ignoreHttpErrors) {
        Assert.notNull(ignoreHttpErrors, "Parameter \"ignoreHttpErrors\" must not null. ");
        this.ignoreHttpErrors = ignoreHttpErrors;
    }

    public Boolean getStream() {

        return stream;
    }

    public void setStream(Boolean stream) {
        Assert.notNull(stream, "Parameter \"stream\" must not null. ");
        this.stream = stream;
    }

    public Object getParameter(String paramName) {
        Assert.notBlank(paramName, "Parameter \"paramName\" must not blank. ");
        for (KeyValue<String, Object> parameter : parameters) {
            if (paramName.equals(parameter.getKey())) {
                return parameter.getValue();
            }
        }
        return null;
    }

    public void addParameter(String paramName, Object paraValue) {
        Assert.notBlank(paramName, "Parameter \"paramName\" must not blank. ");
        parameters.add(new KeyValueImpl<String, Object>(paramName, paraValue));
    }

    public void addParameters(Collection<KeyValue<String, Object>> parameters) {
        Assert.notEmpty(parameters, "Parameter \"parameters\" must not empty. ");
        this.parameters.addAll(parameters);
    }

    public void addParameters(Map<?, ?> parameters) {
        Assert.notEmpty(parameters, "Parameter \"parameters\" must not empty. ");
        for (Map.Entry<?, ?> entry : parameters.entrySet()) {
            String key   = String.valueOf(entry.getKey());
            Object value = entry.getValue();
            this.parameters.add(new KeyValueImpl<String, Object>(key, value));
        }
    }

    @Override
    public Collection<KeyValue<String, Object>> getParameters() {

        return Collections.unmodifiableCollection(parameters);
    }

    public void clearParameters() {

        parameters.clear();
    }

    @Override
    public Object getBody() {

        return body;
    }

    public void setBody(Object body) {

        this.body = body;
    }

}
