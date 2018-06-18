package artoria.net;

import artoria.codec.Base64;
import artoria.exception.ExceptionUtils;
import artoria.io.IOUtils;
import artoria.util.ArrayUtils;
import artoria.util.Assert;
import artoria.util.MapUtils;
import artoria.util.StringUtils;

import javax.net.ssl.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.*;
import java.nio.charset.Charset;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.LinkedHashMap;
import java.util.Map;

import static artoria.util.Const.*;

/**
 * Simple http tools.
 * @author Kahle
 */
public class HttpUtils {
    private static final String DEFAULT_USER_AGENT = "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.146 Safari/537.36";
    private static final String DEFAULT_CONTENT_TYPE = "application/x-www-form-urlencoded";
    private static final TrustAnyHostnameVerifier TRUST_ANY_HOSTNAME_VERIFIER = new TrustAnyHostnameVerifier();
    private static final SSLSocketFactory SSL_SOCKET_FACTORY = HttpUtils.initSSLSocketFactory();
    private static final String PROXY_AUTHORIZATION = "Proxy-Authorization";
    private static final String CONTENT_TYPE = "Content-Type";
    private static final String USER_AGENT = "User-Agent";
    private static final String BASIC = "Basic ";

    /**
     * Https certificate manager.
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
     * Https hostname verifier.
     */
    private static class TrustAnyHostnameVerifier implements HostnameVerifier {

        @Override
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }

    }

    /**
     * Http method.
     */
    public enum Method {

        /**
         * Http get method.
         */
        GET(     "GET",     false),

        /**
         * Http post method.
         */
        POST(    "POST",    true),

        /**
         * Http put method.
         */
        PUT(     "PUT",     true),

        /**
         * Http delete method.
         */
        DELETE(  "DELETE",  false),

        /**
         * Http head method.
         */
        HEAD(    "HEAD",    false),

        /**
         * Http options method.
         */
        OPTIONS( "OPTIONS", false),

        /**
         * Http trace method.
         */
        TRACE(   "TRACE",   false),
        ;

        private String name;
        private boolean hasBody;

        Method(String name, boolean hasBody) {
            this.name = name;
            this.hasBody = hasBody;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public boolean getHasBody() {
            return hasBody;
        }

        public void setHasBody(boolean hasBody) {
            this.hasBody = hasBody;
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
            throw ExceptionUtils.wrap(e);
        }
    }

    public static HttpUtils create() {
        return new HttpUtils();
    }

    public static HttpUtils create(String url) {
        return new HttpUtils().setUrl(url);
    }

    private String charset = DEFAULT_CHARSET_NAME;
    private int connectTimeout = 19000;
    private int readTimeout = 19000;
    private String url;
    private Method method;
    private Map<String, String> headers = new LinkedHashMap<String, String>();
    private Map<String, Object> parameters = new LinkedHashMap<String, Object>();
    private byte[] data;
    private Proxy proxy;
    private String proxyUser;
    private String proxyPassword;

    private HttpUtils() {}

    private String urlCodec() throws IOException {
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<String, Object> entry : parameters.entrySet()) {
            builder.append(entry.getKey())
                    .append(EQUAL)
                    .append(URLEncoder.encode(entry.getValue().toString(), charset))
                    .append(AMPERSAND);
        }
        int len = builder.length();
        builder.delete(len - 1, len);
        return builder.toString();
    }

    public HttpURLConnection connect() throws IOException {
        // No need validate, because validate in setter.
        // Handle url.
        String handleUrl = url;
        if (!method.getHasBody() && MapUtils.isNotEmpty(parameters)) {
            String params = this.urlCodec();
            handleUrl += url.contains(QUESTION_MARK) ? AMPERSAND : QUESTION_MARK;
            handleUrl = handleUrl + params;
            parameters.clear();
        }

        // Build URLConnection.
        URL urlObj = new URL(handleUrl);
        boolean hasProxy = proxy != null;
        URLConnection c = hasProxy ? urlObj.openConnection(proxy) : urlObj.openConnection();
        HttpURLConnection conn = (HttpURLConnection) c;

        // Set SSL and trust hostname.
        if (conn instanceof HttpsURLConnection) {
            ((HttpsURLConnection) conn).setSSLSocketFactory(SSL_SOCKET_FACTORY);
            ((HttpsURLConnection) conn).setHostnameVerifier(TRUST_ANY_HOSTNAME_VERIFIER);
        }

        // Set method.
        conn.setRequestMethod(method.getName());
        conn.setDoOutput(true);
        conn.setDoInput(true);

        // Set timeout.
        conn.setConnectTimeout(connectTimeout);
        conn.setReadTimeout(readTimeout);

        // Set default content-type.
        if (MapUtils.isEmpty(headers) || !headers.containsKey(CONTENT_TYPE)) {
            conn.setRequestProperty(CONTENT_TYPE, DEFAULT_CONTENT_TYPE + ";charset=" + charset);
        }

        // Set default user-agent.
        if (MapUtils.isEmpty(headers) || !headers.containsKey(USER_AGENT)) {
            conn.setRequestProperty(USER_AGENT, DEFAULT_USER_AGENT);
        }

        // Set headers.
        if (MapUtils.isNotEmpty(headers)) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                conn.setRequestProperty(entry.getKey(), entry.getValue());
            }
        }

        // Set proxy auth.
        if (StringUtils.isNotBlank(proxyUser)) {
            boolean hasPwd = StringUtils.isNotBlank(proxyPassword);
            proxyPassword = hasPwd ? proxyPassword : EMPTY_STRING;
            String auth = proxyUser + COLON + proxyPassword;
            Charset encoding = Charset.forName(this.charset);
            byte[] bytes = auth.getBytes(encoding);
            auth = BASIC + Base64.encodeToString(bytes);
            conn.setRequestProperty(PROXY_AUTHORIZATION, auth);
        }

        conn.connect();
        return conn;
    }

    public String execute() throws IOException {
        HttpURLConnection conn = null;
        OutputStream out = null;
        InputStream in = null;
        try {
            conn = this.connect();

            boolean hasParams = method.getHasBody() && MapUtils.isNotEmpty(parameters);
            boolean hasData = ArrayUtils.isNotEmpty(data);
            boolean needOutput = hasParams || hasData;

            out = needOutput ? conn.getOutputStream() : null;
            if (hasParams) {
                String params = this.urlCodec();
                Charset encoding = Charset.forName(this.charset);
                out.write(params.getBytes(encoding));
            }
            if (hasData) {
                out.write(data);
            }
            if (needOutput) { out.flush(); }

            in = conn.getInputStream();
            return IOUtils.toString(in, charset);
        }
        finally {
            IOUtils.closeQuietly(out);
            IOUtils.closeQuietly(in);
            IOUtils.closeQuietly(conn);
        }
    }

    public String execute(String url, Method method) throws IOException {
        this.setUrl(url);
        this.setMethod(method);
        return this.execute();
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public HttpUtils setConnectTimeout(int connectTimeout) {
        boolean b = connectTimeout > 0;
        Assert.state(b, "Parameter \"connectTimeout\" must greater than 0. ");
        this.connectTimeout = connectTimeout;
        return this;
    }

    public int getReadTimeout() {
        return readTimeout;
    }

    public HttpUtils setReadTimeout(int readTimeout) {
        boolean b = readTimeout > 0;
        Assert.state(b, "Parameter \"readTimeout\" must greater than 0. ");
        this.readTimeout = readTimeout;
        return this;
    }

    public String getCharset() {
        return charset;
    }

    public HttpUtils setCharset(String charset) {
        Assert.notBlank(charset, "Parameter \"charset\" must not blank. ");
        this.charset = charset;
        return this;
    }

    public Method getMethod() {
        return method;
    }

    public HttpUtils setMethod(Method method) {
        Assert.notNull(method, "Parameter \"method\" must not null. ");
        this.method = method;
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
        Assert.notBlank(data, "Parameter \"data\" must not blank. ");
        Charset encoding = Charset.forName(this.charset);
        this.data = data.getBytes(encoding);
        return this;
    }

    public String getUrl() {
        return url;
    }

    public HttpUtils setUrl(String url) {
        Assert.notBlank(url, "Parameter \"url\" must not blank. ");
        this.url = url;
        return this;
    }

    public String getProxyAuth() {
        return this.proxyUser + COLON + this.proxyPassword;
    }

    public HttpUtils setProxyAuth(String proxyUser, String proxyPassword) {
        this.setProxyUser(proxyUser);
        this.setProxyPassword(proxyPassword);
        return this;
    }

    public String getProxyUser() {
        return proxyUser;
    }

    public HttpUtils setProxyUser(String proxyUser) {
        Assert.notBlank(proxyUser, "Parameter \"proxyUser\" must not blank. ");
        this.proxyUser = proxyUser;
        return this;
    }

    public String getProxyPassword() {
        return proxyPassword;
    }

    public HttpUtils setProxyPassword(String proxyPassword) {
        Assert.notBlank(proxyPassword, "Parameter \"proxyPassword\" must not blank. ");
        this.proxyPassword = proxyPassword;
        return this;
    }

    public Proxy getProxy() {
        return proxy;
    }

    public HttpUtils setProxy(Proxy proxy) {
        Assert.notNull(proxy, "Parameter \"proxy\" must not null. ");
        this.proxy = proxy;
        return this;
    }

    public HttpUtils setHttpProxy(String hostname, int port) {
        Assert.notBlank(hostname, "Parameter \"hostname\" must not blank. ");
        Assert.state(port > 0, "Parameter \"port\" must greater than 0. ");
        SocketAddress address = new InetSocketAddress(hostname, port);
        this.proxy = new Proxy(Proxy.Type.HTTP, address);
        return this;
    }

    public String getHeader(String headerName) {
        return headers.get(headerName);
    }

    public HttpUtils addHeader(String headerName, String headerValue) {
        Assert.notBlank(headerName, "Parameter \"headerName\" must not blank. ");
        headers.put(headerName, headerValue);
        return this;
    }

    public HttpUtils removeHeader(String headerName) {
        Assert.notBlank(headerName, "Parameter \"headerName\" must not blank. ");
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
        Assert.notNull(headers, "Parameter \"headers\" must not null. ");
        this.headers.putAll(headers);
        return this;
    }

    public Object getParameter(String paramName) {
        return parameters.get(paramName);
    }

    public HttpUtils addParameter(String paramName, Object paraValue) {
        Assert.notBlank(paramName, "Parameter \"paramName\" must not blank. ");
        parameters.put(paramName, paraValue);
        return this;
    }

    public HttpUtils removeParameter(String paramName) {
        Assert.notBlank(paramName, "Parameter \"paramName\" must not blank. ");
        parameters.remove(paramName);
        return this;
    }

    public Map<String, Object> getParameters() {
        return parameters;
    }

    public HttpUtils clearParameters() {
        parameters.clear();
        return this;
    }

    public HttpUtils addParameters(Map<String, String> parameters) {
        Assert.notNull(parameters, "Parameter \"parameters\" must not null. ");
        this.parameters.putAll(parameters);
        return this;
    }

    public HttpUtils setContentType(String contentType) {
        Assert.notBlank(contentType, "Parameter \"contentType\" must not blank. ");
        headers.put(CONTENT_TYPE, contentType);
        return this;
    }

    public HttpUtils setUserAgent(String userAgent) {
        Assert.notBlank(userAgent, "Parameter \"userAgent\" must not blank. ");
        headers.put(USER_AGENT, userAgent);
        return this;
    }

    public String get() throws IOException {
        this.method = Method.GET;
        return this.execute();
    }

    public String get(String url) throws IOException {
        this.url = url;
        return this.get();
    }

    public String post() throws IOException {
        this.method = Method.POST;
        return this.execute();
    }

    public String post(String url) throws IOException {
        this.url = url;
        return this.post();
    }

    public String put() throws IOException {
        this.method = Method.PUT;
        return this.execute();
    }

    public String put(String url) throws IOException {
        this.url = url;
        return this.put();
    }

    public String delete() throws IOException {
        this.method = Method.DELETE;
        return this.execute();
    }

    public String delete(String url) throws IOException {
        this.url = url;
        return this.delete();
    }

    public String head() throws IOException {
        this.method = Method.HEAD;
        return this.execute();
    }

    public String head(String url) throws IOException {
        this.url = url;
        return this.head();
    }

    public String options() throws IOException {
        this.method = Method.OPTIONS;
        return this.execute();
    }

    public String options(String url) throws IOException {
        this.url = url;
        return this.options();
    }

    public String trace() throws IOException {
        this.method = Method.TRACE;
        return this.execute();
    }

    public String trace(String url) throws IOException {
        this.url = url;
        return this.trace();
    }

}
