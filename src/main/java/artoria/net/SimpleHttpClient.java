package artoria.net;

import artoria.exception.ExceptionUtils;
import artoria.file.FileEntity;
import artoria.file.FileUtils;
import artoria.io.IOUtils;
import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import artoria.random.RandomUtils;
import artoria.util.Assert;
import artoria.util.CloseUtils;
import artoria.util.MapUtils;
import artoria.util.StringUtils;

import javax.net.ssl.*;
import java.io.*;
import java.net.*;
import java.nio.charset.Charset;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;

import static artoria.common.Constants.*;
import static artoria.io.IOUtils.EOF;
import static artoria.net.HttpMethod.HEAD;

/**
 * Http client simple implement by jdk.
 * @author Kahle
 */
public class SimpleHttpClient implements HttpClient {
    private static final char[] MIME_BOUNDARY_CHARS = "1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
    private static final char SLASH_CHAR = '/';
    private static final String FORM_URL_ENCODED = "application/x-www-form-urlencoded";
    private static final String MULTIPART_FORM_DATA = "multipart/form-data";
    private static final String CONTENT_ENCODING = "Content-Encoding";
    private static final String CONTENT_TYPE = "Content-Type";
    private static final String BOUNDARY_EQUAL = "boundary=";
    private static final String MALFORMED_HTTPS = "https:/";
    private static final String MALFORMED_HTTP = "http:/";
    private static final String CHARSET_EQUAL = "charset=";
    private static final String SET_COOKIE = "Set-Cookie";
    private static final String LOCATION = "Location";
    private static final String COOKIE = "Cookie";
    private static final String HTTPS = "https";
    private static final String HTTP = "http";
    private static final String GZIP = "gzip";
    private static final int MALFORMED_HTTP_LENGTH = MALFORMED_HTTP.length();
    private static final int MALFORMED_HTTPS_LENGTH = MALFORMED_HTTPS.length();
    private static final int CHARSET_EQUAL_LENGTH = CHARSET_EQUAL.length();
    private static final int BOUNDARY_LENGTH = 16;
    private static final int HTTP_TEMP_REDIRECT = 307;
    private static final SSLSocketFactory UNSAFE_SSL_SOCKET_FACTORY = createUnsafeSSLSocketFactory();
    private static final HostnameVerifier UNSAFE_HOSTNAME_VERIFIER = createUnsafeHostnameVerifier();
    private static Logger log = LoggerFactory.getLogger(SimpleHttpClient.class);

    private static HostnameVerifier createUnsafeHostnameVerifier() {

        return new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };
    }

    private static SSLSocketFactory createUnsafeSSLSocketFactory() {
        // Create a trust manager that does not validate certificate chains.
        final TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {

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
        catch (GeneralSecurityException e) {
            throw ExceptionUtils.wrap(e);
        }
        // Create an ssl socket factory with our all-trusting manager.
        return sslContext.getSocketFactory();
    }

    private static String generateMimeBoundary() {
        String mimeBoundary = "----WebKitFormBoundary";
        mimeBoundary +=
                RandomUtils.nextString(MIME_BOUNDARY_CHARS, BOUNDARY_LENGTH);
        return mimeBoundary;
    }

    private static boolean needsMultipart(Object value) {
        return value instanceof File
                || value instanceof FileEntity
                ;
    }

    private static boolean needsMultipart(HttpRequest request) {
        Map<String, Object> parameters = request.getParameters();
        boolean needsMultipart = false;
        for (Map.Entry<String, Object> entry : parameters.entrySet()) {
            Object value = entry.getValue();
            needsMultipart = needsMultipart(value);
            if (needsMultipart) { break; }
        }
        return needsMultipart;
    }

    private static String createCookiesString(HttpRequest request) {
        Map<String, String> cookies = request.getCookies();
        StringBuilder cookiesBuilder = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, String> cookie : cookies.entrySet()) {
            if (!first) {
                cookiesBuilder.append(SEMICOLON);
                cookiesBuilder.append(BLANK_SPACE);
            }
            else {
                first = false;
            }
            cookiesBuilder.append(cookie.getKey());
            cookiesBuilder.append(EQUAL);
            cookiesBuilder.append(cookie.getValue());
        }
        String cookiesString = cookiesBuilder.toString();
        String charset = request.getCharset();
        Charset forName = Charset.forName(charset);
        byte[] cookiesStrBytes = cookiesString.getBytes(forName);
        if (cookiesStrBytes.length != cookiesString.length()) {
            // Spec says only ascii, no escaping / encoding defined.
            // Validate on set? or escape somehow here?
            throw new IllegalArgumentException("Cookies must only ascii! ");
        }
        return cookiesString;
    }

    private static HttpURLConnection createConnection(HttpRequest request) throws IOException {
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
            hsConn.setSSLSocketFactory(UNSAFE_SSL_SOCKET_FACTORY);
            hsConn.setHostnameVerifier(UNSAFE_HOSTNAME_VERIFIER);
        }
        // Set needs input or output.
        HttpMethod method = request.getMethod();
        if (method.hasBody()) {
            conn.setDoOutput(true);
        }
        conn.setDoInput(true);
        // Handle cookies.
        Map<String, String> cookies = request.getCookies();
        if (MapUtils.isNotEmpty(cookies)) {
            String cookiesString = createCookiesString(request);
            conn.addRequestProperty(COOKIE, cookiesString);
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

    private static String encodeMimeName(String name) {
        // Encodes \" to %22
        if (name == null) { return null; }
        return StringUtils.replace(name, DOUBLE_QUOTE, "%22");
    }

    private static Map<String, List<String>> createHeaderMap(HttpURLConnection connection) {
        // The default sun impl of connection.getHeaderFields() returns header values out of order
        final LinkedHashMap<String, List<String>> headers = new LinkedHashMap<String, List<String>>();
        int i = ZERO;
        while (true) {
            String val = connection.getHeaderField(i);
            String key = connection.getHeaderFieldKey(i);
            if (key == null && val == null) { break; }
            i++;
            // Skip http1.1 line
            if (key == null || val == null) { continue; }
            if (headers.containsKey(key)) {
                headers.get(key).add(val);
            }
            else {
                List<String> list = new ArrayList<String>();
                headers.put(key, list);
                list.add(val);
            }
        }
        return headers;
    }

    private static URL encodeUrl(URL url) {
        try {
            // Odd way to encode urls, but it works!
            final URI uri = new URI(url.toExternalForm());
            return new URL(uri.toASCIIString());
        }
        catch (Exception e) {
            return url;
        }
    }

    private static URL createRedirectUrl(URL oldUrl, String redirectUrl) throws MalformedURLException {
        if (StringUtils.isBlank(redirectUrl)) {
            throw new MalformedURLException("Redirect url must not blank. ");
        }
        String lowerRedirectUrl = redirectUrl.toLowerCase();
        // Fix broken Location: http:/temp/AAG_New/en/index.php
        if (lowerRedirectUrl.startsWith(MALFORMED_HTTP) && redirectUrl.charAt(MALFORMED_HTTP_LENGTH) != SLASH_CHAR) {
            redirectUrl = redirectUrl.substring(MALFORMED_HTTP_LENGTH);
        }
        // Fix broken Location: https:/temp/AAG_New/en/index.php
        if (lowerRedirectUrl.startsWith(MALFORMED_HTTPS) && redirectUrl.charAt(MALFORMED_HTTPS_LENGTH) != SLASH_CHAR) {
            redirectUrl = redirectUrl.substring(MALFORMED_HTTPS_LENGTH);
        }
        // Workaround: java resolves '//path/file + ?foo' to '//path/?foo', not '//path/file?foo' as desired
        if (redirectUrl.startsWith(QUESTION_MARK)) {
            redirectUrl = oldUrl.getPath() + redirectUrl;
        }
        // Workaround: //example.com + ./foo = //example.com/./foo, not //example.com/foo
        String oldUrlFile = oldUrl.getFile();
        if (redirectUrl.startsWith(DOT) && !oldUrlFile.startsWith(SLASH)) {
            oldUrl = new URL(oldUrl.getProtocol(), oldUrl.getHost(), oldUrl.getPort(), SLASH + oldUrlFile);
        }
        return new URL(oldUrl, redirectUrl);
    }

    private void validate(HttpRequest request) throws IOException {
        Assert.notNull(request, "Parameter \"request\" must not null. ");
        HttpMethod method = request.getMethod();
        URL url = request.getUrl();
        Object body = request.getBody();
        Assert.notNull(method, "Request method must not null. ");
        Assert.notNull(url, "Request url must not null. ");
        String protocol = url.getProtocol();
        if (!HTTP.equals(protocol) && !HTTPS.equals(protocol)) {
            throw new MalformedURLException("Only http & https protocols supported. ");
        }
        boolean hasBody = method.hasBody();
        if (!hasBody && body != null) {
            throw new IllegalStateException("Cannot set a request body for HTTP method " + method.name() + ". ");
        }
    }

    private void updateRequestUrl(HttpRequest request) throws IOException {
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
        String oldUrlQuery = oldUrl.getQuery();
        if (oldUrlQuery != null) {
            urlBuilder.append(oldUrlQuery);
            first = false;
        }
        for (Map.Entry<String, Object> entry : parameters.entrySet()) {
            Object value = entry.getValue();
            if (needsMultipart(value)) {
                throw new IOException("File not supported in URL query string. ");
            }
            if (first) {
                first = false;
            }
            else {
                urlBuilder.append(AMPERSAND);
            }
            String key = URLEncoder.encode(entry.getKey(), charset);
            String val = URLEncoder.encode(String.valueOf(value), charset);
            urlBuilder.append(key).append(EQUAL).append(val);
        }
        URL url = new URL(urlBuilder.toString());
        request.setUrl(url);
        // Moved into url as get params.
        request.clearParameters();
    }

    private String updateContentType(HttpRequest request) {
        String mimeBoundary = null, contentType;
        if (StringUtils.isNotBlank(contentType = request.getHeader(CONTENT_TYPE))) {
            // If content type already set, try add charset or boundary if those aren't included.
            String lowerContentType = contentType.toLowerCase();
            boolean isMultipart = lowerContentType.startsWith(MULTIPART_FORM_DATA);
            if (!isMultipart && !contentType.contains(CHARSET_EQUAL)) {
                if (!contentType.trim().endsWith(SEMICOLON)) {
                    contentType += SEMICOLON;
                }
                contentType += BLANK_SPACE;
                contentType += CHARSET_EQUAL;
                contentType += request.getCharset().toLowerCase();
                request.addHeader(CONTENT_TYPE, contentType);
            }
            if (isMultipart && !lowerContentType.contains(BOUNDARY_EQUAL)) {
                mimeBoundary = generateMimeBoundary();
                if (!contentType.trim().endsWith(SEMICOLON)) {
                    contentType += SEMICOLON;
                    contentType += BLANK_SPACE;
                    contentType += CHARSET_EQUAL;
                    contentType += mimeBoundary;
                    request.addHeader(CONTENT_TYPE, contentType);
                }
            }
        }
        else if (needsMultipart(request)) {
            mimeBoundary = generateMimeBoundary();
            request.addHeader(CONTENT_TYPE, MULTIPART_FORM_DATA + "; boundary=" + mimeBoundary);
        }
        else {
            request.addHeader(CONTENT_TYPE, FORM_URL_ENCODED + "; charset=" + request.getCharset());
        }
        return mimeBoundary;
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
            String val = String.valueOf(entry.getValue());
            key = URLEncoder.encode(key, charset);
            val = URLEncoder.encode(val, charset);
            writer.write(key);
            writer.write(EQUAL);
            writer.write(val);
        }
        writer.flush();
    }

    private void writeBodyData(HttpRequest request, BufferedWriter writer) throws IOException {
        String charset = request.getCharset();
        Object body = request.getBody();
        Reader reader = null;
        try {
            if (body instanceof File) {
                InputStream in = new FileInputStream((File) body);
                reader = new InputStreamReader(in, charset);
                reader = new BufferedReader(reader);
            }
            else if (body instanceof InputStream) {
                reader = new InputStreamReader((InputStream) body, charset);
                reader = new BufferedReader(reader);
            }
            else if (body instanceof Reader) {
                boolean isBr = body instanceof BufferedReader;
                reader = isBr ? (Reader) body : new BufferedReader((Reader) body);
            }
            //else {
            //}
            if (reader != null) {
                IOUtils.copyLarge(reader, writer);
            }
            else {
                writer.write(String.valueOf(body));
            }
            writer.flush();
        }
        finally {
            CloseUtils.closeQuietly(reader);
        }
    }

    private void writeMultipartData(HttpRequest request, String mimeBoundary, BufferedWriter writer) throws IOException {
        Map<String, Object> parameters = request.getParameters();
        String charset = request.getCharset();
        boolean first = true;
        for (Map.Entry<String, Object> entry : parameters.entrySet()) {
            String key = entry.getKey();
            Object val = entry.getValue();
            if (first) { first = false; }
            else { writer.append(NEWLINE); }
            writer.append(DOUBLE_MINUS).append(mimeBoundary).append(NEWLINE);
            writer.append("Content-Disposition: form-data; name=\"");
            writer.append(encodeMimeName(key)).append(DOUBLE_QUOTE);
            Reader reader = null;
            try {
                String fileName;
                if (val instanceof FileEntity) {
                    FileEntity fileEntity = (FileEntity) val;
                    fileName = fileEntity.getName();
                    InputStream in = fileEntity.getInputStream();
                    reader = new InputStreamReader(in, charset);
                    reader = new BufferedReader(reader);
                }
                else if (val instanceof File) {
                    File file = (File) val;
                    fileName = file.getName();
                    InputStream in = new FileInputStream(file);
                    reader = new InputStreamReader(in, charset);
                    reader = new BufferedReader(reader);
                }
                else {
                    fileName = null;
                }
                if (StringUtils.isNotBlank(fileName)) {
                    writer.append("; filename=\"");
                    writer.append(encodeMimeName(fileName));
                    writer.append(DOUBLE_QUOTE).append(NEWLINE);
                    writer.append("Content-Type: ");
                    writer.append("application/octet-stream");
                }
                writer.append(NEWLINE).append(NEWLINE);
                if (reader != null) {
                    IOUtils.copyLarge(reader, writer);
                }
                //else if (val instanceof) {
                //}
                else {
                    writer.append(String.valueOf(val));
                }
                writer.flush();
            }
            finally {
                CloseUtils.closeQuietly(reader);
            }
        }
        writer.append(NEWLINE).append(DOUBLE_MINUS);
        writer.append(mimeBoundary).append(DOUBLE_MINUS);
        writer.append(NEWLINE).flush();
    }

    private void writeRequestObject(HttpRequest request, String mimeBoundary, HttpURLConnection connection) throws IOException {
        OutputStream outputStream = null;
        try {
            String charset = request.getCharset();
            outputStream = connection.getOutputStream();
            Writer osw = new OutputStreamWriter(outputStream, charset);
            BufferedWriter writer = new BufferedWriter(osw);
            if (mimeBoundary != null) {
                // Boundary will be set if we're in multipart mode.
                writeMultipartData(request, mimeBoundary, writer);
            }
            else if (request.getBody() != null) {
                writeBodyData(request, writer);
            }
            else {
                // Regular form data (application/x-www-form-urlencoded).
                writeFormData(request, writer);
            }
        }
        finally {
            CloseUtils.closeQuietly(outputStream);
        }
    }

    private void fillResponseCharset(HttpResponse response) {
        String contentTypeArray = response.getHeader(CONTENT_TYPE);
        if (StringUtils.isNotBlank(contentTypeArray)) {
            String[] split = contentTypeArray.split(COMMA);
            for (String contentType : split) {
                if (StringUtils.isBlank(contentType)) { continue; }
                contentType = contentType.trim().toLowerCase();
                int begin = contentType.indexOf(CHARSET_EQUAL);
                if (begin == EOF) { continue; }
                int end = contentType.indexOf(SEMICOLON, begin);
                end = end != EOF ? end : contentType.length();
                begin = begin + CHARSET_EQUAL_LENGTH;
                String charsetName = contentType.substring(begin, end).trim();
                if (StringUtils.isBlank(charsetName)) { continue; }
                response.setCharset(Charset.forName(charsetName).name());
                return;
            }
        }
    }

    private void fillResponseCookies(HttpResponse response, List<String> cookies) {
        for (String cookie : cookies) {
            if (cookie == null) { continue; }
            int beginIndex = cookie.indexOf(EQUAL);
            if (beginIndex == EOF) { continue; }
            String cookieName =
                    cookie.substring(ZERO, beginIndex).trim();
            int endIndex = cookie.indexOf(SEMICOLON);
            if (endIndex == EOF) { continue; }
            String cookieValue =
                    cookie.substring(beginIndex + ONE, endIndex).trim();
            // Ignores path, date, domain, validateTLSCertificates et al. req'd?
            // Name not blank, value not null.
            if (StringUtils.isNotBlank(cookieName)) {
                response.addCookie(cookieName, cookieValue);
            }
        }
    }

    private void fillResponseHeaders(HttpResponse response, Map<String, List<String>> headers) {
        for (Map.Entry<String, List<String>> entry : headers.entrySet()) {
            String name = entry.getKey();
            // Http/1.1 line
            if (name == null) { continue; }
            List<String> values = entry.getValue();
            if (SET_COOKIE.equalsIgnoreCase(name)) {
                fillResponseCookies(response, values);
                continue;
            }
            // Combine same header names with comma: http://www.w3.org/Protocols/rfc2616/rfc2616-sec4.html#sec4.2
            if (values.size() == ONE) {
                response.addHeader(name, values.get(ZERO));
            }
            else if (values.size() > ONE) {
                StringBuilder valueBuilder = new StringBuilder();
                for (int i = ZERO; i < values.size(); i++) {
                    final String val = values.get(i);
                    if (i != ZERO) {
                        valueBuilder.append(COMMA).append(BLANK_SPACE);
                    }
                    valueBuilder.append(val);
                }
                response.addHeader(name, valueBuilder.toString());
            }
        }
    }

    private void setupFromConnection(HttpResponse response, HttpResponse previousResponse, HttpURLConnection connection) throws IOException {
        response.setUrl(connection.getURL());
        response.setMethod(HttpMethod.valueOf(connection.getRequestMethod()));
        response.setStatusCode(connection.getResponseCode());
        response.setStatusMessage(connection.getResponseMessage());

        Map<String, List<String>> headers = createHeaderMap(connection);
        fillResponseHeaders(response, headers);
        fillResponseCharset(response);

        // If from a redirect, map previous response cookies into this response
        if (previousResponse == null) { return; }
        Map<String, String> previousCookies = previousResponse.getCookies();
        for (Map.Entry<String, String> prevCookie : previousCookies.entrySet()) {
            if (response.containsCookie(prevCookie.getKey())) { continue; }
            response.addCookie(prevCookie.getKey(), prevCookie.getValue());
        }
    }

    private HttpResponse responseRedirect(HttpResponse response, HttpRequest request) throws IOException {
        int status = response.getStatusCode();
        // Http/1.1 temporary redirect, not in Java's set.
        if (status != HTTP_TEMP_REDIRECT) {
            // Always redirect with a get.
            // Any data param from original req are dropped.
            request.setMethod(HttpMethod.GET);
            request.clearParameters();
            request.setBody(null);
            request.removeHeader(CONTENT_TYPE);
        }
        String location = response.getHeader(LOCATION);
        URL redirectUrl = createRedirectUrl(request.getUrl(), location);
        request.setUrl(encodeUrl(redirectUrl));
        // Add response cookies to request (for e.g. login posts).
        request.addCookies(response.getCookies());
        return execute(request, response);
    }

    private void fillResponseBody(HttpResponse response, HttpRequest request, HttpURLConnection connection) throws IOException {
        // -1 means unknown, chunked.
        if (connection.getContentLength() != ZERO && request.getMethod() != HEAD) {
            // Sun throws an IO exception on 500 response with no content when trying to read body.
            InputStream errorStream = connection.getErrorStream();
            InputStream bodyStream =
                    errorStream != null ? errorStream : connection.getInputStream();
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

    private HttpResponse execute(HttpRequest request, HttpResponse previousResponse) throws IOException {
        validate(request);
        HttpMethod method = request.getMethod();
        boolean hasBody = method.hasBody();
        String mimeBoundary = null;
        if (!hasBody && MapUtils.isNotEmpty(request.getParameters())) {
            updateRequestUrl(request);
        }
        else if (hasBody) {
            mimeBoundary = updateContentType(request);
        }
        HttpURLConnection connection = createConnection(request);
        HttpResponse response;
        try {
            connection.connect();
            if (connection.getDoOutput()) {
                writeRequestObject(request, mimeBoundary, connection);
            }
            response = new HttpResponse(previousResponse);
            response.setRequest(request);
            setupFromConnection(response, previousResponse, connection);
            // Redirect if there's a location header (from 3xx, or 201 etc).
            if (response.containsHeader(LOCATION) && request.getFollowRedirect()) {
                return responseRedirect(response, request);
            }
            int status = response.getStatusCode();
            boolean isErrorStatus = status < 200 || status >= 400;
            if (isErrorStatus && !request.getIgnoreHttpError()) {
                URL url = request.getUrl();
                throw new IOException("HTTP error " + status + " fetching URL \"" + url.toString() + "\". ");
            }
            fillResponseBody(response, request, connection);
        }
        finally {
            CloseUtils.closeQuietly(connection);
        }
        return response;
    }

    @Override
    public HttpResponse execute(HttpRequest request) throws IOException {

        return execute(request, null);
    }

}
