package artoria.net.http.support;

import artoria.data.tuple.KeyValue;
import artoria.exception.ExceptionUtils;
import artoria.io.util.IOUtils;
import artoria.net.http.HttpMethod;
import artoria.net.http.HttpRequest;
import artoria.net.http.HttpResponse;
import artoria.util.CloseUtils;
import artoria.util.CollectionUtils;
import artoria.util.StringUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.*;
import java.nio.charset.Charset;
import java.util.*;
import java.util.zip.GZIPInputStream;

import static artoria.common.constant.Numbers.ONE;
import static artoria.common.constant.Numbers.ZERO;
import static artoria.common.constant.Symbols.*;
import static artoria.io.util.IOUtils.EOF;
import static artoria.net.http.HttpMethod.HEAD;

public class SimpleHttpClient extends AbstractHttpClient {
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

    private static String createCookiesString(HttpRequest request) {
        /*Map<String, String> cookies = request.getCookies();
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
        return cookiesString;*/
        return null;
    }

    private HttpURLConnection createConnection(SimpleRequest request) throws IOException {
        // No need validate, because validate before invoke.
        String urlStr = request.getUrl();
        URL url = new URL(urlStr);
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
        Integer connectTimeout = request.getConnectTimeout();
        Integer readTimeout = request.getReadTimeout();
        if (connectTimeout != null && connectTimeout >= ZERO) {
            conn.setConnectTimeout(connectTimeout);
        }
        if (readTimeout != null && readTimeout >= ZERO) {
            conn.setReadTimeout(readTimeout);
        }
        // Set SSL and trust hostname.
        /*if (conn instanceof HttpsURLConnection
                && !request.getValidateTLSCertificate()) {
            HttpsURLConnection hsConn = (HttpsURLConnection) conn;
            hsConn.setSSLSocketFactory(buildUnsafeSslSocketFactory());
            hsConn.setHostnameVerifier(buildUnsafeHostnameVerifier());
        }*/
        // Set needs input or output.
        HttpMethod method = request.getMethod();
        conn.setRequestMethod(method.name());
        if (hasBody(method)) {
            conn.setDoOutput(true);
        }
        conn.setDoInput(true);
        // Handle cookies.
        /*Map<String, String> cookies = request.getCookies();
        if (MapUtils.isNotEmpty(cookies)) {
            String cookiesString = createCookiesString(request);
            conn.addRequestProperty(COOKIE, cookiesString);
        }*/
        // Handle headers.
        Map<String, List<String>> headers = request.getHeaders();
        for (Map.Entry<String, List<String>> header : headers.entrySet()) {
            List<String> val = header.getValue();
            String key = header.getKey();
            if (CollectionUtils.isEmpty(val)) { continue; }
            for (String str : val) {
                if (StringUtils.isBlank(str)) { continue; }
                conn.addRequestProperty(key, str);
            }
        }
        // Return.
        return conn;
    }

    private Map<String, List<String>> createHeaders(HttpURLConnection connection) {
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

    private void updateRequestUrl(SimpleRequest request) throws IOException {
        Collection<KeyValue<String, Object>> parameters = request.getParameters();
        String charset = request.getCharset();
        URL oldUrl = new URL(request.getUrl());
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
        for (KeyValue<String, Object> keyValue : parameters) {
            Object value = keyValue.getValue();
            if (needsMultipart(value)) {
                throw new IOException("File not supported in URL query string. ");
            }
            if (first) {
                first = false;
            }
            else {
                urlBuilder.append(AMPERSAND);
            }
            String key = URLEncoder.encode(keyValue.getKey(), charset);
            String val = value != null ? URLEncoder.encode(String.valueOf(value), charset) : EMPTY_STRING;
            urlBuilder.append(key).append(EQUAL).append(val);
        }
        request.setUrl(urlBuilder.toString());
        // Moved into url as get params.
        request.clearParameters();
    }

    private String updateContentType(SimpleRequest request) {
        String mimeBoundary = null, contentType;
        if (StringUtils.isNotBlank(contentType = request.getFirstHeader(CONTENT_TYPE))) {
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
                mimeBoundary = buildMimeBoundary(BOUNDARY_LENGTH);
                if (!contentType.trim().endsWith(SEMICOLON)) {
                    contentType += SEMICOLON;
                    contentType += BLANK_SPACE;
                    contentType += CHARSET_EQUAL;
                    contentType += mimeBoundary;
                    request.addHeader(CONTENT_TYPE, contentType);
                }
            }
        }
        else if (needsMultipart(request.getParameters())) {
            mimeBoundary = buildMimeBoundary(BOUNDARY_LENGTH);
            request.addHeader(CONTENT_TYPE, MULTIPART_FORM_DATA + "; boundary=" + mimeBoundary);
        }
        else {
            request.addHeader(CONTENT_TYPE, FORM_URL_ENCODED + "; charset=" + request.getCharset());
        }
        return mimeBoundary;
    }

    private void fillResponseCharset(SimpleResponse response) {
        String contentTypeArray = response.getFirstHeader(CONTENT_TYPE);
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

    private void fillResponseCookies(SimpleResponse response, List<String> cookies) {
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
            /*if (StringUtils.isNotBlank(cookieName)) {
                response.addCookie(cookieName, cookieValue);
            }*/
        }
    }

    private void fillResponseHeaders(SimpleResponse response, Map<String, List<String>> headers) {
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

    private void setupFromConnection(SimpleResponse response, SimpleResponse previousResponse, HttpURLConnection connection) throws IOException {
        response.setUrl(String.valueOf(connection.getURL()));
        response.setMethod(HttpMethod.valueOf(connection.getRequestMethod()));
        response.setStatusCode(connection.getResponseCode());
        response.setStatusMessage(connection.getResponseMessage());

        Map<String, List<String>> headers = createHeaders(connection);
        fillResponseHeaders(response, headers);
        fillResponseCharset(response);

        // If from a redirect, map previous response cookies into this response
        if (previousResponse == null) { return; }
        /*Map<String, String> previousCookies = previousResponse.getCookies();
        for (Map.Entry<String, String> prevCookie : previousCookies.entrySet()) {
            if (response.containsCookie(prevCookie.getKey())) { continue; }
            response.addCookie(prevCookie.getKey(), prevCookie.getValue());
        }*/
    }

    private HttpResponse responseRedirect(SimpleResponse response, SimpleRequest request) throws IOException {
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
        String location = response.getFirstHeader(LOCATION);
        URL redirectUrl = createRedirectUrl(new URL(request.getUrl()), location);
        request.setUrl(String.valueOf(encodeUrl(redirectUrl)));
        // Add response cookies to request (for e.g. login posts).
//        request.addCookies(response.getCookies());
        return execute(request, response);
    }

    private void fillResponseBody(SimpleResponse response, SimpleRequest request, HttpURLConnection connection) throws IOException {
        // -1 means unknown, chunked.
        if (connection.getContentLength() != ZERO && request.getMethod() != HEAD) {
            // Sun throws an IO exception on 500 response with no content when trying to read body.
            InputStream errorStream = connection.getErrorStream();
            InputStream bodyStream =
                    errorStream != null ? errorStream : connection.getInputStream();
            if (response.containsHeader(CONTENT_ENCODING)
                    && GZIP.equalsIgnoreCase(response.getFirstHeader(CONTENT_ENCODING))) {
                bodyStream = new GZIPInputStream(bodyStream);
            }
            byte[] body = IOUtils.toByteArray(bodyStream);
            response.setBodyStream(new ByteArrayInputStream(body));
        }
    }

    private HttpResponse execute(SimpleRequest request, SimpleResponse previousResponse) throws IOException {
        validate(request);
        HttpMethod method = request.getMethod();
        boolean haveBody = hasBody(method);
        String mimeBoundary = null;
        if (!haveBody && CollectionUtils.isNotEmpty(request.getParameters())) {
            updateRequestUrl(request);
        }
        else if (haveBody) {
            mimeBoundary = updateContentType(request);
        }
        HttpURLConnection connection = createConnection(request);
        SimpleResponse response;
        try {
            connection.connect();
            if (connection.getDoOutput()) {
                writeRequestData(request, connection.getOutputStream(), mimeBoundary);
            }
            response = new SimpleResponse();
            setupFromConnection(response, previousResponse, connection);
            // Redirect if there's a location header (from 3xx, or 201 etc).
            /*if (response.containsHeader(LOCATION) && request.getFollowRedirect()) {
                return responseRedirect(response, request);
            }*/
            /*int status = response.getStatusCode();
            boolean isErrorStatus = status < 200 || status >= 400;
            if (isErrorStatus && !request.getIgnoreHttpError()) {
                String url = request.getUrl();
                throw new IOException("HTTP error " + status + " fetching URL \"" + url.toString() + "\". ");
            }*/
            fillResponseBody(response, request, connection);
        }
        finally {
            CloseUtils.closeQuietly(connection);
        }
        return response;
    }

    @Override
    public HttpResponse execute(HttpRequest request) {
        try {
            return execute((SimpleRequest) request, null);
        }
        catch (Exception e) {
            throw ExceptionUtils.wrap(e);
        }
    }

}
