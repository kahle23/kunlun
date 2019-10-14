package artoria.net;

import artoria.exception.ExceptionUtils;
import artoria.util.Assert;
import artoria.util.StringUtils;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import static artoria.common.Constants.*;

/**
 * Abstract http message body.
 * @author Kahle
 */
public abstract class HttpMessage {
    private Map<String, String> headers = new LinkedHashMap<String, String>();
    private Map<String, String> cookies = new LinkedHashMap<String, String>();
    private String charset = DEFAULT_CHARSET_NAME;
    private HttpMethod method;
    private URL url;

    public HttpMessage() {
    }

    public URL getUrl() {

        return url;
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

        return method;
    }

    public void setMethod(HttpMethod method) {
        Assert.notNull(method, "Parameter \"method\" must not null. ");
        this.method = method;
    }

    public String getCharset() {

        return charset;
    }

    public void setCharset(String charset) {
        Assert.notBlank(charset, "Parameter \"charset\" must not blank. ");
        this.charset = charset;
    }

    public String getHeader(String headerName) {
        Assert.notBlank(headerName, "Parameter \"headerName\" must not blank. ");
        return headers.get(headerName);
    }

    public void addHeader(String headerName, String headerValue) {
        Assert.notBlank(headerName, "Parameter \"headerName\" must not blank. ");
        Assert.notNull(headerValue, "Parameter \"headerValue\" must not null. ");
        headers.put(headerName, headerValue);
    }

    public void addHeaders(Map<String, String> headers) {
        Assert.notNull(headers, "Parameter \"headers\" must not null. ");
        this.headers.putAll(headers);
    }

    public boolean containsHeader(String headerName) {
        Assert.notBlank(headerName, "Parameter \"headerName\" must not blank. ");
        return headers.containsKey(headerName);
    }

    public void removeHeader(String headerName) {
        Assert.notBlank(headerName, "Parameter \"headerName\" must not blank. ");
        headers.remove(headerName);
    }

    public Map<String, String> getHeaders() {

        return Collections.unmodifiableMap(headers);
    }

    public void clearHeaders() {

        headers.clear();
    }

    public String getCookie(String cookieName) {
        Assert.notBlank(cookieName, "Parameter \"cookieName\" must not blank. ");
        return cookies.get(cookieName);
    }

    public void addCookie(String cookieName, String cookieValue) {
        Assert.notBlank(cookieName, "Parameter \"cookieName\" must not blank. ");
        Assert.notNull(cookieValue, "Parameter \"cookieValue\" must not null. ");
        cookies.put(cookieName, cookieValue);
    }

    public void addCookies(Map<String, String> cookies) {
        Assert.notNull(cookies, "Parameter \"cookies\" must not null. ");
        this.cookies.putAll(cookies);
    }

    public void addCookies(String cookieString) {
        Assert.notBlank(cookieString, "Parameter \"cookieString\" must not blank. ");
        Map<String, String> cookiesMap = new LinkedHashMap<String, String>();
        String[] cookieEntryArray = cookieString.split(SEMICOLON);
        for (String cookieEntry : cookieEntryArray) {
            if (StringUtils.isBlank(cookieEntry)) {
                continue;
            }
            cookieEntry = cookieEntry.trim();
            int index = cookieEntry.indexOf(EQUAL);
            if (index == MINUS_ONE || index == ZERO) { continue; }
            String key = cookieEntry.substring(ZERO, index);
            String val = cookieEntry.substring(index + ONE);
            cookiesMap.put(key.trim(), val.trim());
        }
        cookies.putAll(cookiesMap);
    }

    public boolean containsCookie(String cookieName) {
        Assert.notBlank(cookieName, "Parameter \"cookieName\" must not blank. ");
        return cookies.containsKey(cookieName);
    }

    public void removeCookie(String cookieName) {
        Assert.notBlank(cookieName, "Parameter \"cookieName\" must not blank. ");
        cookies.remove(cookieName);
    }

    public Map<String, String> getCookies() {

        return Collections.unmodifiableMap(cookies);
    }

    public void clearCookies() {

        cookies.clear();
    }

}
