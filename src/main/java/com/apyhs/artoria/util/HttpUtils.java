package com.apyhs.artoria.util;

import com.apyhs.artoria.codec.Base64;
import com.apyhs.artoria.exception.UncheckedException;

import javax.net.ssl.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.*;
import java.nio.charset.Charset;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;

import static com.apyhs.artoria.util.Const.*;

/**
 * @author Kahle
 */
public class HttpUtils {
    private static final String DEFAULT_USER_AGENT = "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.146 Safari/537.36";
    private static final String DEFAULT_CONTENT_TYPE = "application/x-www-form-urlencoded";
    private static final TrustAnyHostnameVerifier TRUST_ANY_HOSTNAME_VERIFIER = new TrustAnyHostnameVerifier();
    private static final SSLSocketFactory SSL_SOCKET_FACTORY = HttpUtils.initSSLSocketFactory();
    private static final String STRING_PROXY_AUTHORIZATION = "Proxy-Authorization";
    private static final String STRING_CONTENT_TYPE = "Content-Type";
    private static final String STRING_USER_AGENT = "User-Agent";
    private static final String BASIC = "Basic ";
    private static final String GET     = "GET";
    private static final String POST    = "POST";
    private static final String PUT     = "PUT";
    private static final String DELETE  = "DELETE";
    private static final String HEAD    = "HEAD";
    private static final String TRACE   = "TRACE";
    private static final String OPTIONS = "OPTIONS";

    /**
     * Https certificate manager
     */
    private static class TrustAnyTrustManager implements X509TrustManager {

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }

        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

    }

    /**
     * Https hostname verifier
     */
    private static class TrustAnyHostnameVerifier implements HostnameVerifier {

        @Override
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }

    }

    private static SSLSocketFactory initSSLSocketFactory() {
        try {
            TrustManager[] tm = { new TrustAnyTrustManager() };
            // ("TLS", "SunJSSE");
            SSLContext sslContext = SSLContext.getInstance("TLS");
            SecureRandom random = new SecureRandom();
            sslContext.init(null, tm, random);
            return sslContext.getSocketFactory();
        }
        catch (Exception e) {
            throw new UncheckedException(e);
        }
    }

    public static HttpUtils create() {
        return new HttpUtils();
    }

    public static HttpUtils create(String url) {
        return new HttpUtils().setUrl(url);
    }

    private Map<String, String> parameters = new HashMap<String, String>();
    private Map<String, String> headers = new HashMap<String, String>();
    private int connectTimeout = 19000;
    private int readTimeout = 19000;
    private String proxyUser;
    private String proxyPassword;
    private String charset = DEFAULT_CHARSET_NAME;
    private String method;
    private Proxy proxy;
    private byte[] data;
    private String url;

    private HttpUtils() {}

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public HttpUtils setConnectTimeout(int connectTimeout) {
        boolean b = connectTimeout > 0;
        Assert.state(b, "Connect timeout must greater than 0. ");
        this.connectTimeout = connectTimeout;
        return this;
    }

    public int getReadTimeout() {
        return readTimeout;
    }

    public HttpUtils setReadTimeout(int readTimeout) {
        boolean b = readTimeout > 0;
        Assert.state(b, "Read timeout must greater than 0. ");
        this.readTimeout = readTimeout;
        return this;
    }

    public String getCharset() {
        return charset;
    }

    public HttpUtils setCharset(String charset) {
        Assert.notBlank(charset, "Charset must is not blank. ");
        this.charset = charset;
        return this;
    }

    public String getMethod() {
        return method;
    }

    public HttpUtils setMethod(String method) {
        Assert.notBlank(method, "Method must is not blank. ");
        this.method = method.toUpperCase();
        return this;
    }

    public byte[] getData() {
        return data;
    }

    public HttpUtils setData(byte[] data) {
        this.data = data;
        return this;
    }

    public HttpUtils setData(String data) {
        Assert.notBlank(data, "Data must is not blank. ");
        Charset encoding = Charset.forName(this.charset);
        this.data = data.getBytes(encoding);
        return this;
    }

    public String getUrl() {
        return url;
    }

    public HttpUtils setUrl(String url) {
        Assert.notBlank(url, "Url must is not blank. ");
        this.url = url;
        return this;
    }

    public String getProxyAuth() {
        return this.proxyUser + COLON + this.proxyPassword;
    }

    public HttpUtils setProxyAuth(String username, String password) {
        Assert.notBlank(username, "Username must is not blank. ");
        Assert.notBlank(password, "Password must is not blank. ");
        this.proxyUser = username;
        this.proxyPassword = password;
        return this;
    }

    public String getProxyUser() {
        return proxyUser;
    }

    public HttpUtils setProxyUser(String proxyUser) {
        Assert.notBlank(proxyUser, "Username must is not blank. ");
        this.proxyUser = proxyUser;
        return this;
    }

    public String getProxyPassword() {
        return proxyPassword;
    }

    public HttpUtils setProxyPassword(String proxyPassword) {
        Assert.notBlank(proxyPassword, "Password must is not blank. ");
        this.proxyPassword = proxyPassword;
        return this;
    }

    public Proxy getProxy() {
        return proxy;
    }

    public HttpUtils setProxy(Proxy proxy) {
        Assert.notNull(proxy, "Proxy must is not null. ");
        this.proxy = proxy;
        return this;
    }

    public HttpUtils setHttpProxy(String hostname, int port) {
        Assert.notBlank(hostname, "Hostname must is not blank. ");
        Assert.state(port > 0, "Port must greater than 0. ");
        SocketAddress address = new InetSocketAddress(hostname, port);
        this.proxy = new Proxy(Proxy.Type.HTTP, address);
        return this;
    }

    public String getHeader(String headerName) {
        return headers.get(headerName);
    }

    public HttpUtils addHeader(String headerName, String headerValue) {
        Assert.notBlank(headerName, "Header name must is not blank. ");
        headers.put(headerName, headerValue);
        return this;
    }

    public HttpUtils removeHeader(String headerName) {
        Assert.notBlank(headerName, "Header name must is not blank. ");
        headers.remove(headerName);
        return this;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public HttpUtils clearHeaders() {
        headers.clear();
        return this;
    }

    public HttpUtils addHeaders(Map<String, String> headers) {
        Assert.notNull(headers, "Headers must is not null. ");
        this.headers.putAll(headers);
        return this;
    }

    public String getParameter(String paraName) {
        return parameters.get(paraName);
    }

    public HttpUtils addParameter(String paraName, String paraValue) {
        Assert.notBlank(paraName, "Parameter name must is not blank. ");
        parameters.put(paraName, paraValue);
        return this;
    }

    public HttpUtils removeParameter(String paraName) {
        Assert.notBlank(paraName, "Parameter name must is not blank. ");
        parameters.remove(paraName);
        return this;
    }

    public Map<String, String> getParameters() {
        return parameters;
    }

    public HttpUtils clearParameters() {
        parameters.clear();
        return this;
    }

    public HttpUtils addParameters(Map<String, String> parameters) {
        Assert.notNull(parameters, "Parameters must is not null. ");
        this.parameters.putAll(parameters);
        return this;
    }

    public HttpUtils setContentType(String contentType) {
        Assert.notBlank(contentType, "Content type must is not blank. ");
        headers.put(STRING_CONTENT_TYPE, contentType);
        return this;
    }

    public HttpUtils setUserAgent(String userAgent) {
        Assert.notBlank(userAgent, "User agent must is not blank. ");
        headers.put(STRING_USER_AGENT, userAgent);
        return this;
    }

    public String buildUrl() throws UnsupportedEncodingException {
        Assert.notBlank(url, "Url must is not blank. ");
        Assert.notBlank(charset, "Charset must is not blank. ");
        if (MapUtils.isEmpty(parameters)) { return url; }
        StringBuilder builder = new StringBuilder(url);
        if (!url.contains(QUESTION_MARK)) {
            builder.append(QUESTION_MARK);
        }
        else {
            builder.append(AMPERSAND);
        }
        for (Map.Entry<String, String> entry : parameters.entrySet()) {
            builder.append(entry.getKey())
                    .append(EQUAL)
                    .append(URLEncoder.encode(entry.getValue(), charset))
                    .append(AMPERSAND);
        }
        int len = builder.length();
        builder.delete(len - 1, len);
        return builder.toString();
    }

    public HttpURLConnection connect() throws IOException, GeneralSecurityException {
        Assert.notBlank(url, "Url must is not blank. ");
        Assert.notBlank(method, "Method must is not blank. ");
        Assert.notBlank(charset, "Charset must is not blank. ");

        String buildUrl = this.buildUrl();
        URL urlObj = new URL(buildUrl);
        boolean hasProxy = proxy != null;
        URLConnection c = hasProxy ? urlObj.openConnection(proxy) : urlObj.openConnection();
        HttpURLConnection conn = (HttpURLConnection) c;

        if (conn instanceof HttpsURLConnection) {
            ((HttpsURLConnection) conn).setSSLSocketFactory(SSL_SOCKET_FACTORY);
            ((HttpsURLConnection) conn).setHostnameVerifier(TRUST_ANY_HOSTNAME_VERIFIER);
        }

        conn.setRequestMethod(method);
        conn.setDoOutput(true);
        conn.setDoInput(true);

        conn.setConnectTimeout(connectTimeout);
        conn.setReadTimeout(readTimeout);

        if (MapUtils.isEmpty(headers) || !headers.containsKey(STRING_CONTENT_TYPE)) {
            conn.setRequestProperty(STRING_CONTENT_TYPE, DEFAULT_CONTENT_TYPE);
        }
        if (MapUtils.isEmpty(headers) || !headers.containsKey(STRING_USER_AGENT)) {
            conn.setRequestProperty(STRING_USER_AGENT, DEFAULT_USER_AGENT);
        }
        if (MapUtils.isNotEmpty(headers)) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                conn.setRequestProperty(entry.getKey(), entry.getValue());
            }
        }
        if (StringUtils.isNotBlank(proxyUser)) {
            boolean hasPwd = StringUtils.isNotBlank(proxyPassword);
            proxyPassword = hasPwd ? proxyPassword : EMPTY_STRING;
            String auth = proxyUser + COLON + proxyPassword;
            Charset encoding = Charset.forName(this.charset);
            byte[] bytes = auth.getBytes(encoding);
            auth = BASIC + Base64.encodeToString(bytes);
            conn.setRequestProperty(STRING_PROXY_AUTHORIZATION, auth);
        }

        conn.connect();
        return conn;
    }

    public String send() throws IOException, GeneralSecurityException {
        HttpURLConnection conn = null;
        OutputStream out = null;
        InputStream in = null;
        try {
            conn = this.connect();
            if (ArrayUtils.isNotEmpty(data)) {
                out = conn.getOutputStream();
                out.write(data);
                out.flush();
            }
            in = conn.getInputStream();
            return IOUtils.toString(in, charset);
        }
        finally {
            IOUtils.closeQuietly(out);
            IOUtils.closeQuietly(in);
            IOUtils.closeQuietly(conn);
        }
    }

    public String send(String url, String method) throws IOException, GeneralSecurityException {
        this.url = url;
        this.method = method;
        return this.send();
    }

    public String get() throws IOException, GeneralSecurityException {
        this.method = GET;
        return send();
    }

    public String get(String url) throws IOException, GeneralSecurityException {
        this.url = url;
        this.method = GET;
        return send();
    }

    public String post() throws IOException, GeneralSecurityException {
        this.method = POST;
        return send();
    }

    public String post(String url) throws IOException, GeneralSecurityException {
        this.url = url;
        this.method = POST;
        return send();
    }

    public String put() throws IOException, GeneralSecurityException {
        this.method = PUT;
        return send();
    }

    public String put(String url) throws IOException, GeneralSecurityException {
        this.url = url;
        this.method = PUT;
        return send();
    }

    public String delete() throws IOException, GeneralSecurityException {
        this.method = DELETE;
        return send();
    }

    public String delete(String url) throws IOException, GeneralSecurityException {
        this.url = url;
        this.method = DELETE;
        return send();
    }

    public String head() throws IOException, GeneralSecurityException {
        this.method = HEAD;
        return send();
    }

    public String head(String url) throws IOException, GeneralSecurityException {
        this.url = url;
        this.method = HEAD;
        return send();
    }

    public String trace() throws IOException, GeneralSecurityException {
        this.method = TRACE;
        return send();
    }

    public String trace(String url) throws IOException, GeneralSecurityException {
        this.url = url;
        this.method = TRACE;
        return send();
    }

    public String options() throws IOException, GeneralSecurityException {
        this.method = OPTIONS;
        return send();
    }

    public String options(String url) throws IOException, GeneralSecurityException {
        this.url = url;
        this.method = OPTIONS;
        return send();
    }

}
