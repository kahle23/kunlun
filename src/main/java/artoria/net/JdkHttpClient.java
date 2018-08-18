package artoria.net;

import artoria.io.FileUtils;
import artoria.io.IOUtils;
import artoria.util.Assert;
import artoria.util.MapUtils;
import artoria.util.StringUtils;

import javax.net.ssl.*;
import java.io.*;
import java.net.*;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.*;
import java.util.zip.GZIPInputStream;

import static artoria.common.Constants.*;
import static artoria.io.IOUtils.EOF;
import static artoria.net.HttpMessage.CONTENT_TYPE;
import static artoria.net.HttpMethod.HEAD;

/**
 * Http client tools implements by jdk.
 * @author Kahle
 */
public class JdkHttpClient implements HttpClient {
    private static final char[] MIME_BOUNDARY_CHARS = "-_1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
    private static final char SLASH_CHAR = '/';
    private static final String FORM_URL_ENCODED = "application/x-www-form-urlencoded";
    private static final String MULTIPART_FORM_DATA = "multipart/form-data";
    private static final String CONTENT_ENCODING = "Content-Encoding";
    private static final String BOUNDARY_EQUAL = "boundary=";
    private static final String MALFORMED_HTTPS = "https:/";
    private static final String MALFORMED_HTTP = "http:/";
    private static final String CHARSET_EQUAL = "charset=";
    private static final String SET_COOKIE = "Set-Cookie";
    private static final String LOCATION = "Location";
    private static final String MINUS_MINUS = "--";
    private static final String COOKIE = "Cookie";
    private static final String HTTPS = "https";
    private static final String HTTP = "http";
    private static final String GZIP = "gzip";
    private static final int MALFORMED_HTTP_LENGTH = MALFORMED_HTTP.length();
    private static final int MALFORMED_HTTPS_LENGTH = MALFORMED_HTTPS.length();
    private static final int CHARSET_EQUAL_LENGTH = CHARSET_EQUAL.length();
    private static final int BOUNDARY_LENGTH = 32;
    private static final int HTTP_TEMP_REDIR = 307;
    private static SSLSocketFactory sslSocketFactory;

    private URL encodeUrl(URL url) {
        try {
            // Odd way to encode urls, but it works!
            final URI uri = new URI(url.toExternalForm());
            return new URL(uri.toASCIIString());
        }
        catch (Exception e) {
            return url;
        }
    }

    private String encodeMimeName(String name) {
        // Encodes \" to %22
        if (name == null) { return null; }
        return StringUtils.replace(name, QUOTE_MARK, "%22");
    }

    private String generateMimeBoundary() {
        StringBuilder mimeBoundary = new StringBuilder(BOUNDARY_LENGTH);
        Random random = new Random();
        for (int i = 0; i < BOUNDARY_LENGTH; i++) {
            int nextInt = random.nextInt(MIME_BOUNDARY_CHARS.length);
            mimeBoundary.append(MIME_BOUNDARY_CHARS[nextInt]);
        }
        return mimeBoundary.toString();
    }

    private boolean needsMultipart(Object value) {
        return value instanceof File
//                || value instanceof InputStream
//                || value instanceof Reader
                ;
    }

    private boolean needsMultipart(HttpRequest request) {
        Map<String, Object> parameters = request.getParameters();
        boolean needsMulti = false;
        for (Map.Entry<String, Object> entry : parameters.entrySet()) {
            Object value = entry.getValue();
            needsMulti = this.needsMultipart(value);
            if (needsMulti) { break; }
        }
        return needsMulti;
    }

    private HostnameVerifier takeUnSecureHostnameVerifier() throws IOException {
        return new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };
    }

    private SSLSocketFactory takeUnSecureSSLSocketFactory() throws IOException {
        if (sslSocketFactory != null) { return sslSocketFactory; }
        synchronized (this) {
            if (sslSocketFactory != null) { return sslSocketFactory; }
            // Create a trust manager that does not validate certificate chains.
            final TrustManager[] trustAllCerts = new TrustManager[]{ new X509TrustManager() {

                @Override
                public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

            }};
            // Install the all-trusting trust manager.
            final SSLContext sslContext;
            try {
                sslContext = SSLContext.getInstance("SSL");
                // sslContext = SSLContext.getInstance("TLS");
                // sslContext = SSLContext.getInstance("TLS", "SunJSSE");
                sslContext.init(null, trustAllCerts, new SecureRandom());
            }
            catch (NoSuchAlgorithmException e) {
                throw new IOException("Can't create unsecure trust manager. ", e);
            }
            catch (KeyManagementException e) {
                throw new IOException("Can't create unsecure trust manager. ", e);
            }
            // Create an ssl socket factory with our all-trusting manager.
            sslSocketFactory = sslContext.getSocketFactory();
            return sslSocketFactory;
        }
    }

    private Map<String, List<String>> takeHeaders(HttpURLConnection conn) {
        // The default sun impl of conn.getHeaderFields() returns header values out of order
        final LinkedHashMap<String, List<String>> headers = new LinkedHashMap<String, List<String>>();
        int i = 0;
        while (true) {
            String key = conn.getHeaderFieldKey(i);
            String val = conn.getHeaderField(i);
            if (key == null && val == null) { break; }
            i++;
            // Skip http1.1 line
            if (key == null || val == null) { continue; }
            if (headers.containsKey(key)) {
                headers.get(key).add(val);
            }
            else {
                List<String> vals = new ArrayList<String>();
                headers.put(key, vals);
                vals.add(val);
            }
        }
        return headers;
    }

    private void handleResponseCharset(HttpResponse response) {
        String contentType = response.getHeader(CONTENT_TYPE);
        if (StringUtils.isNotBlank(contentType)) {
            int begin = contentType.indexOf(CHARSET_EQUAL);
            if (begin == EOF) { return; }
            int end = contentType.indexOf(SEMICOLON, begin);
            end = end != EOF ? end : contentType.length();
            begin = begin + CHARSET_EQUAL_LENGTH;
            String charsetName = contentType.substring(begin, end).trim();
            response.setCharset(Charset.forName(charsetName).name());
        }
    }

    private void handleResponseCookies(HttpResponse response, List<String> cookies) {
        for (String cookie : cookies) {
            if (cookie == null) { continue; }
            int beginIndex = cookie.indexOf(EQUAL);
            if (beginIndex == EOF) { continue; }
            String cookieName =
                    cookie.substring(0, beginIndex).trim();
            int endIndex = cookie.indexOf(SEMICOLON);
            if (endIndex == EOF) { continue; }
            String cookieValue =
                    cookie.substring(beginIndex + 1, endIndex).trim();
            // Ignores path, date, domain, validateTLSCertificates et al. req'd?
            // Name not blank, value not null.
            if (StringUtils.isNotBlank(cookieName)) {
                response.addCookie(cookieName, cookieValue);
            }
        }
    }

    private void handleResponseHeaders(HttpResponse response, Map<String, List<String>> headers) {
        for (Map.Entry<String, List<String>> entry : headers.entrySet()) {
            String name = entry.getKey();
            // Http/1.1 line
            if (name == null) { continue; }
            List<String> values = entry.getValue();
            if (SET_COOKIE.equalsIgnoreCase(name)) {
                this.handleResponseCookies(response, values);
                continue;
            }
            // Combine same header names with comma: http://www.w3.org/Protocols/rfc2616/rfc2616-sec4.html#sec4.2
            if (values.size() == 1) {
                response.addHeader(name, values.get(0));
            }
            else if (values.size() > 1) {
                StringBuilder valueBuilder = new StringBuilder();
                for (int i = 0; i < values.size(); i++) {
                    final String val = values.get(i);
                    if (i != 0) {
                        valueBuilder.append(COMMA).append(BLANK_SPACE);
                    }
                    valueBuilder.append(val);
                }
                response.addHeader(name, valueBuilder.toString());
            }
        }
    }

    private void handleResponseHeaders(HttpResponse response, HttpResponse previous, HttpURLConnection conn) throws IOException {
        response.setUrl(conn.getURL());
        response.setMethod(HttpMethod.valueOf(conn.getRequestMethod()));
        response.setStatusCode(conn.getResponseCode());
        response.setStatusMessage(conn.getResponseMessage());

        Map<String, List<String>> headers = this.takeHeaders(conn);
        this.handleResponseHeaders(response, headers);
        this.handleResponseCharset(response);

        // If from a redirect, map previous response cookies into this response
        if (previous == null) { return; }
        Map<String, String> previousCookies = previous.getCookies();
        for (Map.Entry<String, String> prevCookie : previousCookies.entrySet()) {
            if (response.containsCookie(prevCookie.getKey())) { continue; }
            response.addCookie(prevCookie.getKey(), prevCookie.getValue());
        }
    }

    private void handleResponseBody(HttpResponse response, HttpRequest request, HttpURLConnection conn) throws IOException {
        // -1 means unknown, chunked.
        if (conn.getContentLength() != 0 && request.getMethod() != HEAD) {
            // Sun throws an IO exception on 500 response with no content when trying to read body.
            InputStream errorStream = conn.getErrorStream();
            InputStream bodyStream =
                    errorStream != null ? errorStream : conn.getInputStream();
            if (response.containsHeader(CONTENT_ENCODING)
                    && GZIP.equalsIgnoreCase(response.getHeader(CONTENT_ENCODING))) {
                bodyStream = new GZIPInputStream(bodyStream);
            }
            OutputStream bodyToStream;
            Writer bodyToWriter;
            File bodyToFile;
            if ((bodyToFile = request.getResponseBodyToFile()) != null) {
                FileUtils.write(bodyStream, bodyToFile);
            }
            else if ((bodyToStream = request.getResponseBodyToStream()) != null) {
                IOUtils.copyLarge(bodyStream, bodyToStream);
            }
            else if ((bodyToWriter = request.getResponseBodyToWriter()) != null) {
                String charset = request.getCharset();
                Reader reader = new InputStreamReader(bodyStream, charset);
                reader = new BufferedReader(reader);
                boolean isBw = bodyToWriter instanceof BufferedWriter;
                Writer writer = isBw ? bodyToWriter : new BufferedWriter(bodyToWriter);
                IOUtils.copyLarge(reader, writer);
            }
            else {
                byte[] body = IOUtils.toByteArray(bodyStream);
                response.setBody(body);
            }
        }
    }

    private URL handleRedirectUrl(URL oldUrl, String redirectUrl) throws MalformedURLException {
        if (StringUtils.isBlank(redirectUrl)) {
            throw new MalformedURLException("Redirect url must not blank. ");
        }
        // Fix broken Location: http:/temp/AAG_New/en/index.php
        if (redirectUrl.startsWith(MALFORMED_HTTP) && redirectUrl.charAt(MALFORMED_HTTP_LENGTH) != SLASH_CHAR) {
            redirectUrl = redirectUrl.substring(MALFORMED_HTTP_LENGTH);
        }
        // Fix broken Location: https:/temp/AAG_New/en/index.php
        if (redirectUrl.startsWith(MALFORMED_HTTPS) && redirectUrl.charAt(MALFORMED_HTTPS_LENGTH) != SLASH_CHAR) {
            redirectUrl = redirectUrl.substring(MALFORMED_HTTPS_LENGTH);
        }
        // Workaround: java resolves '//path/file + ?foo' to '//path/?foo', not '//path/file?foo' as desired
        if (redirectUrl.startsWith(QUESTION_MARK)) {
            redirectUrl = oldUrl.getPath() + redirectUrl;
        }
        // Workaround: //example.com + ./foo = //example.com/./foo, not //example.com/foo
        if (redirectUrl.startsWith(DOT) && !oldUrl.getFile().startsWith(SLASH)) {
            oldUrl = new URL(oldUrl.getProtocol(), oldUrl.getHost(), oldUrl.getPort(), SLASH + oldUrl.getFile());
        }
        return new URL(oldUrl, redirectUrl);
    }

    private HttpResponse handleResponseRedirect(HttpResponse response, HttpRequest request, int status) throws IOException {
        // Http/1.1 temporary redirect, not in Java's set.
        if (status != HTTP_TEMP_REDIR) {
            // Always redirect with a get.
            // Any data param from original req are dropped.
            request.setMethod(HttpMethod.GET);
            request.clearParameters();
            request.setBody(null);
            request.removeHeader(CONTENT_TYPE);
        }
        String location = response.getHeader(LOCATION);
        URL redirectUrl = this.handleRedirectUrl(request.getUrl(), location);
        request.setUrl(this.encodeUrl(redirectUrl));
        // Add response cookies to request (for e.g. login posts).
        request.addCookies(response.getCookies());
        return this.execute(request, response);
    }

    private String handleContentType(HttpRequest request) {
        String boundary = null;
        if (request.containsHeader(CONTENT_TYPE)) {
            // If content type already set, try add charset or boundary if those aren't included.
            String contentType = request.getHeader(CONTENT_TYPE);
            if (StringUtils.isBlank(contentType)) {
                request.removeHeader(CONTENT_TYPE);
                return this.handleContentType(request);
            }
            boolean isMultipart = contentType.toLowerCase().startsWith(MULTIPART_FORM_DATA);
            if (!isMultipart && !contentType.contains(CHARSET_EQUAL)) {
                if (!contentType.trim().endsWith(SEMICOLON)) {
                    contentType += SEMICOLON;
                }
                contentType += BLANK_SPACE;
                contentType += CHARSET_EQUAL;
                contentType += request.getCharset().toLowerCase();
                request.addHeader(CONTENT_TYPE, contentType);
            }
            if (isMultipart && !contentType.contains(BOUNDARY_EQUAL)) {
                boundary = this.generateMimeBoundary();
                if (!contentType.trim().endsWith(SEMICOLON)) {
                    contentType += SEMICOLON;
                    contentType += BLANK_SPACE;
                    contentType += CHARSET_EQUAL;
                    contentType += boundary;
                    request.addHeader(CONTENT_TYPE, contentType);
                }
            }
        }
        else if (needsMultipart(request)) {
            boundary = this.generateMimeBoundary();
            request.addHeader(CONTENT_TYPE, MULTIPART_FORM_DATA + "; boundary=" + boundary);
        }
        else {
            request.addHeader(CONTENT_TYPE, FORM_URL_ENCODED + "; charset=" + request.getCharset());
        }
        return boundary;
    }

    private void buildUrlQueryString(HttpRequest request) throws IOException {
        Map<String, Object> parameters = request.getParameters();
        String charset = request.getCharset();
        URL oldUrl = request.getUrl();
        StringBuilder urlBuilder = new StringBuilder();
        boolean first = true;
        // Reconstitute the query, ready for appends.
        urlBuilder.append(oldUrl.getProtocol())
                .append("://")
                // Includes host, port.
                .append(oldUrl.getAuthority())
                .append(oldUrl.getPath())
                .append(QUESTION_MARK);
        if (oldUrl.getQuery() != null) {
            urlBuilder.append(oldUrl.getQuery());
            first = false;
        }
        for (Map.Entry<String, Object> entry : parameters.entrySet()) {
            Object value = entry.getValue();
            if (this.needsMultipart(value)) {
                throw new IOException("File not supported in URL query string. ");
            }
            if (first) {
                first = false;
            }
            else {
                urlBuilder.append(AMPERSAND);
            }
            String key = URLEncoder.encode(entry.getKey(), charset);
            String val = URLEncoder.encode(value + EMPTY_STRING, charset);
            urlBuilder.append(key).append(EQUAL).append(val);
        }
        URL url = new URL(urlBuilder.toString());
        request.setUrl(url);
        // Moved into url as get params.
        request.clearParameters();
    }

    private String buildCookiesString(HttpRequest request) throws IOException {
        Map<String, String> cookies = request.getCookies();
        StringBuilder cookiesBuilder = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, String> cookie : cookies.entrySet()) {
            if (!first) {
                cookiesBuilder
                        .append(SEMICOLON)
                        .append(BLANK_SPACE);
            }
            else {
                first = false;
            }
            cookiesBuilder
                    .append(cookie.getKey())
                    .append(EQUAL)
                    .append(cookie.getValue());
        }
        String cookiesStr = cookiesBuilder.toString();
        String charset = request.getCharset();
        byte[] cookiesStrBytes = cookiesStr.getBytes(charset);
        if (cookiesStrBytes.length != cookiesStr.length()) {
            // Spec says only ascii, no escaping / encoding defined.
            // Validate on set? or escape somehow here?
            throw new IOException("Cookies must only ascii! ");
        }
        return cookiesStr;
    }

    private HttpURLConnection buildConnection(HttpRequest request) throws IOException {
        // No need validate, because validate before invoke.
        URL url = request.getUrl();
        Proxy proxy = request.getProxy();
        HttpURLConnection conn = (HttpURLConnection) (
                proxy == null
                        ? url.openConnection()
                        : url.openConnection(proxy)
        );
        conn.setRequestMethod(request.getMethod().name());
        // Don't rely on native redirection support.
        // Because if native, maybe only redirect once.
        conn.setInstanceFollowRedirects(false);
        conn.setConnectTimeout(request.getConnectTimeout());
        conn.setReadTimeout(request.getReadTimeout());
        // Set SSL and trust hostname.
        if (conn instanceof HttpsURLConnection
                && !request.getValidateTLSCertificate()) {
            HttpsURLConnection hsConn = (HttpsURLConnection) conn;
            SSLSocketFactory sf = this.takeUnSecureSSLSocketFactory();
            HostnameVerifier hv = this.takeUnSecureHostnameVerifier();
            hsConn.setSSLSocketFactory(sf);
            hsConn.setHostnameVerifier(hv);
        }
        // Set needs input or output.
        HttpMethod method = request.getMethod();
        if (method.getHasBody()) {
            conn.setDoOutput(true);
        }
        conn.setDoInput(true);
        // Handle cookies.
        Map<String, String> cookies = request.getCookies();
        if (MapUtils.isNotEmpty(cookies)) {
            String cookiesStr = this.buildCookiesString(request);
            conn.addRequestProperty(COOKIE, cookiesStr);
        }
        // Handle headers.
        Map<String, String> headers = request.getHeaders();
        for (Map.Entry<String, String> header : headers.entrySet()) {
            String key = header.getKey();
            String val = header.getValue();
            conn.addRequestProperty(key, val);
        }
        // Return.
        return conn;
    }

    private void writeFormData(HttpRequest request, BufferedWriter writer) throws IOException {
        Map<String, Object> parameters = request.getParameters();
        String charset = request.getCharset();
        boolean first = true;
        for (Map.Entry<String, Object> entry : parameters.entrySet()) {
            if (first) {
                first = false;
            }
            else {
                writer.write(AMPERSAND);
            }
            String key = entry.getKey();
            String val = entry.getValue() + EMPTY_STRING;
            key = URLEncoder.encode(key, charset);
            val = URLEncoder.encode(val, charset);
            writer.write(key);
            writer.write(EQUAL);
            writer.write(val);
        }
    }

    private void writeRequestBody(HttpRequest request, BufferedWriter writer) throws IOException {
        String charset = request.getCharset();
        Object body = request.getBody();
        boolean needsClose = false;
        Reader reader = null;
        try {
            if (body instanceof File) {
                InputStream in = new FileInputStream((File) body);
                reader = new InputStreamReader(in, charset);
                reader = new BufferedReader(reader);
                needsClose = true;
            }
            else if (body instanceof InputStream) {
                reader = new InputStreamReader((InputStream) body, charset);
                reader = new BufferedReader(reader);
            }
            else if (body instanceof Reader) {
                boolean isBr = body instanceof BufferedReader;
                reader = isBr ? (Reader) body : new BufferedReader((Reader) body);
            }
            if (reader != null) {
                IOUtils.copyLarge(reader, writer);
                writer.flush();
            }
            else {
                writer.write(body + EMPTY_STRING);
            }
        }
        finally {
            if (needsClose) {
                IOUtils.closeQuietly(reader);
            }
        }
    }

    private void writeMultipartData(HttpRequest request, String boundary, BufferedWriter writer) throws IOException {
        Map<String, Object> parameters = request.getParameters();
        String charset = request.getCharset();
        for (Map.Entry<String, Object> entry : parameters.entrySet()) {
            String key = entry.getKey();
            Object val = entry.getValue();
            writer.write(MINUS_MINUS);
            writer.write(boundary);
            writer.newLine();
            // writer.write("\r\n");
            writer.write("Content-Disposition: form-data; name=\"");
            writer.write(this.encodeMimeName(key));
            writer.write(QUOTE_MARK);
            boolean needsClose = false;
            String fileName = null;
            Reader reader = null;
            try {
                if (val instanceof File) {
                    File file = (File) val;
                    fileName = file.getName();
                    InputStream in = new FileInputStream(file);
                    reader = new InputStreamReader(in, charset);
                    reader = new BufferedReader(reader);
                    needsClose = true;
                }
                else if (val instanceof InputStream) {
                    // Todo:
                    throw new UnsupportedOperationException("Because no fileName! ");
                }
                else if (val instanceof Reader) {
                    // Todo:
                    throw new UnsupportedOperationException("Because no fileName! ");
                }
                if (reader != null && StringUtils.isNotBlank(fileName)) {
                    writer.write("; filename=\"");
                    writer.write(this.encodeMimeName(fileName));
                    writer.write(QUOTE_MARK);
                    writer.newLine();
                    // writer.write("\r\n");
                    writer.write("Content-Type: application/octet-stream");
                    writer.newLine();
                    writer.newLine();
                    // writer.write("\r\n\r\n");
                    writer.flush();
                    IOUtils.copyLarge(reader, writer);
                    writer.flush();
                }
                else {
                    writer.newLine();
                    writer.newLine();
                    // writer.write("\r\n\r\n");
                    writer.write(val + EMPTY_STRING);
                }
                writer.newLine();
                // writer.write("\r\n");
            }
            finally {
                if (needsClose) {
                    IOUtils.closeQuietly(reader);
                }
            }
        }
        writer.write(MINUS_MINUS);
        writer.write(boundary);
        writer.write(MINUS_MINUS);
    }

    private void writeRequestContent(HttpRequest request, String boundary, OutputStream outputStream) throws IOException {
        try {
            Writer osw = new OutputStreamWriter(outputStream, request.getCharset());
            BufferedWriter writer = new BufferedWriter(osw);
            if (boundary != null) {
                // Boundary will be set if we're in multipart mode.
                this.writeMultipartData(request, boundary, writer);
            }
            else if (request.getBody() != null) {
                this.writeRequestBody(request, writer);
            }
            else {
                // Regular form data (application/x-www-form-urlencoded).
                this.writeFormData(request, writer);
            }
        }
        finally {
            IOUtils.closeQuietly(outputStream);
        }
    }

    private void validateRequest(HttpRequest request) throws IOException {
        Assert.notNull(request, "Parameter \"request\" must not null. ");
        Assert.notNull(request.getMethod(), "Request method must not null. ");
        Assert.notNull(request.getUrl(), "Request url must not null. ");
        HttpMethod method = request.getMethod();
        Object body = request.getBody();
        URL url = request.getUrl();
        String protocol = url.getProtocol();
        if (!HTTP.equals(protocol) && !HTTPS.equals(protocol)) {
            throw new MalformedURLException("Only http & https protocols supported. ");
        }
        boolean methodHasBody = method.getHasBody();
        boolean hasRequestBody = body != null;
        if (!methodHasBody) {
            Assert.isFalse(hasRequestBody, "Cannot set a request body for HTTP method " + method.name() + ". ");
        }
    }

    private HttpResponse execute(HttpRequest request, HttpResponse previous) throws IOException {
        this.validateRequest(request);
        HttpMethod method = request.getMethod();
        boolean methodHasBody = method.getHasBody();
        String mimeBoundary = null;
        if (!methodHasBody && MapUtils.isNotEmpty(request.getParameters())) {
            this.buildUrlQueryString(request);
        }
        else if (methodHasBody) {
            mimeBoundary = this.handleContentType(request);
        }
        HttpURLConnection conn = this.buildConnection(request);
        HttpResponse response;
        try {
            conn.connect();
            if (conn.getDoOutput()) {
                OutputStream out = conn.getOutputStream();
                this.writeRequestContent(request, mimeBoundary, out);
            }
            response = new HttpResponse(previous);
            response.setRequest(request);
            this.handleResponseHeaders(response, previous, conn);
            int status = conn.getResponseCode();
            // Redirect if there's a location header (from 3xx, or 201 etc).
            if (response.containsHeader(LOCATION) && request.getFollowRedirect()) {
                return this.handleResponseRedirect(response, request, status);
            }
            boolean isErrorStatus = status < 200 || status >= 400;
            if (isErrorStatus && !request.getIgnoreHttpError()) {
                URL url = request.getUrl();
                throw new IOException("HTTP error " + status + " fetching URL \"" + url.toString() + "\". ");
            }
            this.handleResponseBody(response, request, conn);
        }
        finally {
            IOUtils.closeQuietly(conn);
        }
        return response;
    }

    @Override
    public HttpResponse execute(HttpRequest request) throws IOException {
        return this.execute(request, null);
    }

}
