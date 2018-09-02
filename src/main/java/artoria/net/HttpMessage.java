package artoria.net;

import artoria.exception.ExceptionUtils;
import artoria.util.Assert;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import static artoria.common.Constants.DEFAULT_CHARSET_NAME;

/**
 * Abstract http message body.
 * @author Kahle
 */
public abstract class HttpMessage {
    public static final String CONTENT_TYPE = "Content-Type";
    private URL url;
    private HttpMethod method;
    private String charset = DEFAULT_CHARSET_NAME;
    private Map<String, String> headers = new LinkedHashMap<String, String>();
    private Map<String, String> cookies = new LinkedHashMap<String, String>();

    public HttpMessage() {
    }

    public URL getUrl() {

        return this.url;
    }

    public void setUrl(URL url) {
        Assert.notNull(url, "Parameter \"url\" must not null. ");
        this.url = url;
    }

    public void setUrl(String url) {
        Assert.notBlank(url, "Parameter \"url\" must not blank. ");
        try {
            this.url = new URL(url);
        }
        catch (MalformedURLException e) {
            throw ExceptionUtils.wrap(e);
        }
    }

    public HttpMethod getMethod() {

        return this.method;
    }

    public void setMethod(HttpMethod method) {
        Assert.notNull(method, "Parameter \"method\" must not null. ");
        this.method = method;
    }

    public String getCharset() {

        return this.charset;
    }

    public void setCharset(String charset) {
        Assert.notBlank(charset, "Parameter \"charset\" must not blank. ");
        this.charset = charset;
    }

    public String getHeader(String headerName) {
        Assert.notBlank(headerName, "Parameter \"headerName\" must not blank. ");
        return this.headers.get(headerName);
    }

    public void addHeader(String headerName, String headerValue) {
        Assert.notBlank(headerName, "Parameter \"headerName\" must not blank. ");
        Assert.notNull(headerValue, "Parameter \"headerValue\" must not null. ");
        this.headers.put(headerName, headerValue);
    }

    public void addHeaders(Map<String, String> headers) {
        Assert.notNull(headers, "Parameter \"headers\" must not null. ");
        this.headers.putAll(headers);
    }

    public boolean containsHeader(String headerName) {
        Assert.notBlank(headerName, "Parameter \"headerName\" must not blank. ");
        return this.headers.containsKey(headerName);
    }

    public void removeHeader(String headerName) {
        Assert.notBlank(headerName, "Parameter \"headerName\" must not blank. ");
        this.headers.remove(headerName);
    }

    public Map<String, String> getHeaders() {

        return Collections.unmodifiableMap(this.headers);
    }

    public void clearHeaders() {

        this.headers.clear();
    }

    public String getCookie(String cookieName) {
        Assert.notBlank(cookieName, "Parameter \"cookieName\" must not blank. ");
        return this.cookies.get(cookieName);
    }

    public void addCookie(String cookieName, String cookieValue) {
        Assert.notBlank(cookieName, "Parameter \"cookieName\" must not blank. ");
        Assert.notNull(cookieValue, "Parameter \"cookieValue\" must not null. ");
        this.cookies.put(cookieName, cookieValue);
    }

    public void addCookies(Map<String, String> cookies) {
        Assert.notNull(cookies, "Parameter \"cookies\" must not null. ");
        this.cookies.putAll(cookies);
    }

    public boolean containsCookie(String cookieName) {
        Assert.notBlank(cookieName, "Parameter \"cookieName\" must not blank. ");
        return this.cookies.containsKey(cookieName);
    }

    public void removeCookie(String cookieName) {
        Assert.notBlank(cookieName, "Parameter \"cookieName\" must not blank. ");
        this.cookies.remove(cookieName);
    }

    public Map<String, String> getCookies() {

        return Collections.unmodifiableMap(this.cookies);
    }

    public void clearCookies() {

        this.cookies.clear();
    }

}
