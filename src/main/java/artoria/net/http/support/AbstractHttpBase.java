package artoria.net.http.support;

import artoria.net.http.HttpClient;
import artoria.net.http.HttpMethod;
import artoria.util.Assert;
import artoria.util.CollectionUtils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static artoria.common.Constants.UTF_8;
import static artoria.common.Constants.ZERO;

/**
 * The abstract base http information.
 * @author Kahle
 */
public abstract class AbstractHttpBase implements HttpClient.HttpBase {
    private Map<String, List<String>> headers = new LinkedHashMap<String, List<String>>();
    private HttpMethod method;
    private String charset = UTF_8;
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
        Assert.notNull(value, "Parameter \"value\" must not null. ");
        Assert.notBlank(name, "Parameter \"name\" must not blank. ");
        List<String> list = headers.get(name);
        if (list == null) {
            headers.put(name, list = new ArrayList<String>());
        }
        list.add(value);
    }

    public void addHeader(String name, List<String> values) {
        Assert.notEmpty(values, "Parameter \"values\" must not empty. ");
        Assert.notBlank(name, "Parameter \"name\" must not blank. ");
        List<String> list = headers.get(name);
        if (list == null) {
            headers.put(name, list = new ArrayList<String>());
        }
        list.addAll(values);
    }

    public void addHeaders(Map<String, List<String>> headers) {
        Assert.notEmpty(headers, "Parameter \"headers\" must not empty. ");
        for (Map.Entry<String, List<String>> entry : headers.entrySet()) {
            addHeader(entry.getKey(), entry.getValue());
        }
    }

    public void removeHeader(String name, String value) {
        Assert.notNull(value, "Parameter \"value\" must not null. ");
        Assert.notBlank(name, "Parameter \"name\" must not blank. ");
        if (!headers.containsKey(name)) { return; }
        List<String> list = headers.get(name);
        if (CollectionUtils.isEmpty(list)) {
            headers.remove(name); return;
        }
        list.remove(value);
    }

    public void removeHeader(String name) {
        Assert.notBlank(name, "Parameter \"name\" must not blank. ");
        headers.remove(name);
    }

    public void clearHeaders() {

        headers.clear();
    }

    public List<String> getHeader(String name) {
        Assert.notBlank(name, "Parameter \"name\" must not blank. ");
        return headers.get(name);
    }

    public boolean containsHeader(String name) {
        Assert.notBlank(name, "Parameter \"name\" must not blank. ");
        return headers.containsKey(name);
    }

    public String getFirstHeader(String name) {
        Assert.notBlank(name, "Parameter \"name\" must not blank. ");
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
