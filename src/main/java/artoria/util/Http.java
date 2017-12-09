package artoria.util;

import artoria.codec.Base64;

import javax.net.ssl.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.*;
import java.nio.charset.Charset;
import java.security.GeneralSecurityException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;

import static artoria.util.StringConstant.*;

/**
 * @author Kahle
 */
public class Http {
    private static final String DEFAULT_USER_AGENT = "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.146 Safari/537.36";
    private static final String DEFAULT_CONTENT_TYPE = "application/x-www-form-urlencoded";
    private static final String DEFAULT_CHARSET_NAME = Charset.defaultCharset().name();
    private static final TrustAnyHostnameVerifier TRUST_ANY_HOSTNAME_VERIFIER = new TrustAnyHostnameVerifier();
    private static final SSLSocketFactory SSL_SOCKET_FACTORY = initSSLSocketFactory();
    private static final String STRING_PROXY_AUTHORIZATION = "Proxy-Authorization";
    private static final String STRING_CONTENT_TYPE = "Content-Type";
    private static final String STRING_USER_AGENT = "User-Agent";
    private static final String GET     = "GET";
    private static final String POST    = "POST";
    private static final String PUT     = "PUT";
    private static final String DELETE  = "DELETE";
    private static final String HEAD    = "HEAD";
    private static final String TRACE   = "TRACE";
    private static final String OPTIONS = "OPTIONS";

    private static class TrustAnyTrustManager implements X509TrustManager {
        // https certificate manager

        @Override
        public X509Certificate[] getAcceptedIssuers() { return null; }

        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {}

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {}
    }

    private static class TrustAnyHostnameVerifier implements HostnameVerifier {
        // https hostname verifier

        @Override
        public boolean verify(String hostname, SSLSession session) { return true; }
    }

    private static SSLSocketFactory initSSLSocketFactory() {
        try {
            TrustManager[] tm = { new TrustAnyTrustManager() };
            // ("TLS", "SunJSSE");
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, tm, new java.security.SecureRandom());
            return sslContext.getSocketFactory();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String buildUrlWithQueryString(String url, Map<String, String> queryParas, String charset)
            throws UnsupportedEncodingException {
        if (MapUtils.isNotEmpty(queryParas)) {
            StringBuilder builder = new StringBuilder(url);
            if (!url.contains(QUESTION_MARK)) {
                builder.append(QUESTION_MARK);
            }
            else {
                builder.append(AMPERSAND);
            }
            for (Map.Entry<String, String> entry : queryParas.entrySet()) {
                builder.append(entry.getKey())
                        .append(EQUAL)
                        .append(URLEncoder.encode(entry.getValue(), charset))
                        .append(AMPERSAND);
            }
            int len = builder.length();
            builder.delete(len - 1, len);
            return builder.toString();
        }
        return url;
    }

    public static Http create() {
        return new Http();
    }

    public static Http create(String url) {
        return new Http().setUrl(url);
    }

    private Map<String, String> parameters = new HashMap<>();
    private Map<String, String> headers = new HashMap<>();
    private int connectTimeout = 19000;
    private int readTimeout = 19000;
    private String proxyUser;
    private String proxyPwd;
    private String charset;
    private String method;
    private Proxy proxy;
    private byte[] data;
    private String url;

    private Http() {}

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public Http setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
        return this;
    }

    public int getReadTimeout() {
        return readTimeout;
    }

    public Http setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
        return this;
    }

    public String getCharset() {
        return charset;
    }

    public Http setCharset(String charset) {
        this.charset = charset;
        return this;
    }

    public String getMethod() {
        return method;
    }

    public Http setMethod(String method) {
        this.method = method;
        return this;
    }

    public byte[] getData() {
        return data;
    }

    public Http setData(byte[] data) {
        this.data = data;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public Http setUrl(String url) {
        this.url = url;
        return this;
    }

    public String getProxyAuth() {
        return this.proxyUser + ":" + this.proxyPwd;
    }

    public Http setProxyAuth(String username, String password) {
        this.proxyUser = username;
        this.proxyPwd = password;
        return this;
    }

    public String getProxyUser() {
        return proxyUser;
    }

    public Http setProxyUser(String proxyUser) {
        this.proxyUser = proxyUser;
        return this;
    }

    public String getProxyPwd() {
        return proxyPwd;
    }

    public Http setProxyPwd(String proxyPwd) {
        this.proxyPwd = proxyPwd;
        return this;
    }

    public Proxy getProxy() {
        return proxy;
    }

    public Http setProxy(Proxy proxy) {
        this.proxy = proxy;
        return this;
    }

    public Http setHttpProxy(String hostname, int port) {
        this.proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(hostname, port));
        return this;
    }

    public String getHeader(String headerName) {
        return headers.get(headerName);
    }

    public Http addHeader(String headerName, String headerValue) {
        headers.put(headerName, headerValue);
        return this;
    }

    public Http removeHeader(String headerName) {
        headers.remove(headerName);
        return this;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public Http clearHeaders() {
        headers.clear();
        return this;
    }

    public Http addHeaders(Map<String, String> headers) {
        this.headers.putAll(headers);
        return this;
    }

    public String getParameter(String paraName) {
        return parameters.get(paraName);
    }

    public Http addParameter(String paraName, String paraValue) {
        parameters.put(paraName, paraValue);
        return this;
    }

    public Http removeParameter(String paraName) {
        parameters.remove(paraName);
        return this;
    }

    public Map<String, String> getParameters() {
        return parameters;
    }

    public Http clearParameters() {
        parameters.clear();
        return this;
    }

    public Http addParameters(Map<String, String> parameters) {
        this.parameters.putAll(parameters);
        return this;
    }

    public Http setContentType(String contentType) {
        headers.put(STRING_CONTENT_TYPE, contentType);
        return this;
    }

    public Http setUserAgent(String userAgent) {
        headers.put(STRING_USER_AGENT, userAgent);
        return this;
    }

    public HttpURLConnection connect() throws IOException, GeneralSecurityException {
        if (StringUtils.isBlank(url)) {
            throw new IOException("Url is blank. ");
        }
        if (StringUtils.isBlank(method)) {
            throw new IOException("Method is blank. ");
        }
        if (StringUtils.isBlank(charset)) {
            charset = DEFAULT_CHARSET_NAME;
        }

        URL urlObj = new URL(buildUrlWithQueryString(url, parameters, charset));
        HttpURLConnection conn = (HttpURLConnection) (proxy != null ? urlObj.openConnection(proxy) : urlObj.openConnection());
        if (conn instanceof HttpsURLConnection) {
            ((HttpsURLConnection)conn).setSSLSocketFactory(SSL_SOCKET_FACTORY);
            ((HttpsURLConnection)conn).setHostnameVerifier(TRUST_ANY_HOSTNAME_VERIFIER);
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
            String auth = proxyUser + COLON + (StringUtils.isNotBlank(proxyPwd) ? proxyPwd : EMPTY_STRING);
            auth = "Basic " + Base64.encodeToString(auth.getBytes(DEFAULT_CHARSET_NAME));
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
            conn = connect();
            if (data != null && data.length > 0) {
                out = conn.getOutputStream();
                out.write(data);
                out.flush();
            }
            in = conn.getInputStream();
            return IOUtils.toString(in, charset);
        } finally {
            IOUtils.closeQuietly(out);
            IOUtils.closeQuietly(in);
            IOUtils.close(conn);
        }
    }

    public String send(String url, String method) throws IOException, GeneralSecurityException {
        this.url = url;
        this.method = method;
        return send();
    }

    public String get() throws IOException, GeneralSecurityException {
        method = GET;
        return send();
    }

    public String get(String url) throws IOException, GeneralSecurityException {
        this.url = url;
        method = GET;
        return send();
    }

    public String post() throws IOException, GeneralSecurityException {
        method = POST;
        return send();
    }

    public String post(String url) throws IOException, GeneralSecurityException {
        this.url = url;
        method = POST;
        return send();
    }

    public String put() throws IOException, GeneralSecurityException {
        method = PUT;
        return send();
    }

    public String put(String url) throws IOException, GeneralSecurityException {
        this.url = url;
        method = PUT;
        return send();
    }

    public String delete() throws IOException, GeneralSecurityException {
        method = DELETE;
        return send();
    }

    public String delete(String url) throws IOException, GeneralSecurityException {
        this.url = url;
        method = DELETE;
        return send();
    }

    public String head() throws IOException, GeneralSecurityException {
        method = HEAD;
        return send();
    }

    public String head(String url) throws IOException, GeneralSecurityException {
        this.url = url;
        method = HEAD;
        return send();
    }

    public String trace() throws IOException, GeneralSecurityException {
        method = TRACE;
        return send();
    }

    public String trace(String url) throws IOException, GeneralSecurityException {
        this.url = url;
        method = TRACE;
        return send();
    }

    public String options() throws IOException, GeneralSecurityException {
        method = OPTIONS;
        return send();
    }

    public String options(String url) throws IOException, GeneralSecurityException {
        this.url = url;
        method = OPTIONS;
        return send();
    }

}
