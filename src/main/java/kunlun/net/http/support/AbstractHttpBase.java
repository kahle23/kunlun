/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.net.http.support;

import kunlun.net.http.HttpClient;
import kunlun.net.http.HttpMethod;
import kunlun.util.Assert;
import kunlun.util.CollectionUtils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static kunlun.common.constant.Charsets.STR_UTF_8;
import static kunlun.common.constant.Numbers.ZERO;

/**
 * The abstract base http information.
 * @author Kahle
 */
public abstract class AbstractHttpBase implements HttpClient.HttpBase {
    private Map<String, List<String>> headers = new LinkedHashMap<String, List<String>>();
    private HttpMethod method;
    private String charset = STR_UTF_8;
    private String url;

    @Override
    public HttpMethod getMethod() {

        return method;
    }

    public void setMethod(HttpMethod method) {
        Assert.notNull(method, "Parameter \"method\" must not null. ");
        this.method = method;
    }

    @Override
    public String getUrl() {

        return url;
    }

    public void setUrl(String url) {
        Assert.notBlank(url, "Parameter \"url\" must not blank. ");
        this.url = url;
    }

    @Override
    public String getCharset() {

        return charset;
    }

    public void setCharset(String charset) {
        Assert.notBlank(charset, "Parameter \"charset\" must not blank. ");
        this.charset = charset;
    }

    public void addHeader(String name, String value) {
        List<String> list = headers.get(name);
        if (list == null) {
            headers.put(name, list = new ArrayList<String>());
        }
        list.add(value);
    }

    public void addHeader(String name, List<String> values) {
        List<String> list = headers.get(name);
        if (list == null) {
            headers.put(name, list = new ArrayList<String>());
        }
        if (CollectionUtils.isNotEmpty(values)) {
            list.addAll(values);
        }
    }

    public void addHeaders(Map<String, List<String>> headers) {
        Assert.notEmpty(headers, "Parameter \"headers\" must not empty. ");
        for (Map.Entry<String, List<String>> entry : headers.entrySet()) {
            addHeader(entry.getKey(), entry.getValue());
        }
    }

    public void removeHeader(String name, String value) {
        if (!headers.containsKey(name)) { return; }
        List<String> list = headers.get(name);
        if (CollectionUtils.isEmpty(list)) {
            headers.remove(name); return;
        }
        list.remove(value);
    }

    public void removeHeader(String name) {

        headers.remove(name);
    }

    public void clearHeaders() {

        headers.clear();
    }

    public List<String> getHeader(String name) {

        return headers.get(name);
    }

    public boolean containsHeader(String name) {

        return headers.containsKey(name);
    }

    public String getFirstHeader(String name) {
        List<String> list = headers.get(name);
        return CollectionUtils.isNotEmpty(list) ? list.get(ZERO) : null;
    }

    @Override
    public Map<String, List<String>> getHeaders() {

        return headers;
    }

    public void setHeaders(Map<String, List<String>> headers) {
        Assert.notNull(headers, "Parameter \"headers\" must not null. ");
        this.headers = headers;
    }

}
