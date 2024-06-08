/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.net.http.support;

import kunlun.exception.ExceptionUtils;
import kunlun.io.util.IOUtils;
import kunlun.net.http.AbstractHttpClient;
import kunlun.net.http.HttpMethod;
import kunlun.net.http.HttpRequest;
import kunlun.net.http.HttpResponse;
import kunlun.util.Assert;
import kunlun.util.CloseUtils;
import kunlun.util.CollectionUtils;
import kunlun.util.StringUtils;

import javax.net.ssl.HttpsURLConnection;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;

import static kunlun.common.constant.Numbers.ONE;
import static kunlun.common.constant.Numbers.ZERO;
import static kunlun.common.constant.Symbols.*;
import static kunlun.io.util.IOUtils.EOF;
import static kunlun.net.http.HttpMethod.HEAD;

/**
 * The http client based on jdk simple implementation.
 * @author Kahle
 */
public class SimpleHttpClient extends AbstractHttpClient {
    protected static final String FORM_URL_ENCODED = "application/x-www-form-urlencoded";
    protected static final String MULTIPART_FORM_DATA = "multipart/form-data";
    protected static final String HTTPS = "https";
    protected static final String HTTP = "http";
    protected static final String GZIP = "gzip";

    /**
     * Obtains the headers from the URL connection.
     * @param connection The URL connection
     * @return The headers
     */
    protected Map<String, List<String>> obtainHeaders(URLConnection connection) {
        // The default sun impl of connection.getHeaderFields() returns header values out of order
        Map<String, List<String>> headers = new LinkedHashMap<String, List<String>>();
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

    /**
     * Resets the http content type of the http request.
     * @param request The http request
     * @return The http mime boundary or null
     */
    protected String resetContentType(SimpleRequest request) {
        char[] boundaryChars = "123456789abcdefghijkmnpqrstuvwxyzABCDEFGHIJKLMNPQRSTUVWXYZ".toCharArray();
        String boundaryEqual = "boundary=", charsetEqual = "charset=", mimeBoundary = null, contentType;
        int boundaryLength = 16;
        if (StringUtils.isNotBlank(contentType = request.getFirstHeader(CONTENT_TYPE))) {
            // If content type already set, try add charset or boundary if those aren't included.
            String lowerContentType = contentType.toLowerCase();
            boolean isMultipart = lowerContentType.startsWith(MULTIPART_FORM_DATA);
            if (!isMultipart && !contentType.contains(charsetEqual)) {
                if (!contentType.trim().endsWith(SEMICOLON)) {
                    contentType += SEMICOLON;
                }
                contentType += BLANK_SPACE;
                contentType += charsetEqual;
                contentType += request.getCharset().toLowerCase();
                request.setHeader(CONTENT_TYPE, contentType);
            }
            if (isMultipart && !lowerContentType.contains(boundaryEqual)) {
                mimeBoundary = buildMimeBoundary(boundaryChars, boundaryLength);
                if (!contentType.trim().endsWith(SEMICOLON)) {
                    contentType += SEMICOLON;
                    contentType += BLANK_SPACE;
                    contentType += charsetEqual;
                    contentType += mimeBoundary;
                    request.setHeader(CONTENT_TYPE, contentType);
                }
            }
        }
        else if (hasMultipart(request.getParameters())) {
            mimeBoundary = buildMimeBoundary(boundaryChars, boundaryLength);
            request.setHeader(CONTENT_TYPE, MULTIPART_FORM_DATA + "; boundary=" + mimeBoundary);
        }
        else {
            request.setHeader(CONTENT_TYPE, FORM_URL_ENCODED + "; charset=" + request.getCharset());
        }
        return mimeBoundary;
    }

    /**
     * Create an http URL connection based on an http request.
     * @param request The http request
     * @return The http URL connection
     * @throws IOException The network IO error
     */
    protected HttpURLConnection createConnection(SimpleRequest request) throws IOException {
        // No need validate, because validate before invoke.
        URL url = new URL(request.getUrl());
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
        // Set timeout.
        Integer connectTimeout = request.getConnectTimeout();
        Integer readTimeout = request.getReadTimeout();
        if (connectTimeout != null && connectTimeout >= ZERO) {
            conn.setConnectTimeout(connectTimeout);
        }
        if (readTimeout != null && readTimeout >= ZERO) {
            conn.setReadTimeout(readTimeout);
        }
        // Set SSL and trust hostname.
        if (conn instanceof HttpsURLConnection
                && request.getValidateCertificate() != null
                && !request.getValidateCertificate()) {
            HttpsURLConnection hsConn = (HttpsURLConnection) conn;
            hsConn.setSSLSocketFactory(buildUnsafeSslSocketFactory());
            hsConn.setHostnameVerifier(buildUnsafeHostnameVerifier());
        }
        // Set needs input or output.
        if (hasBody(request.getMethod())) {
            conn.setDoOutput(true);
        }
        conn.setDoInput(true);
        // Handle cookies.
        /*Map<String, String> cookies = request.getCookies();
        if (MapUtils.isNotEmpty(cookies)) {
            conn.addRequestProperty(COOKIE, buildCookiesString(cookies, request.getCharset()));
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

    /**
     * Process the http response cookies.
     * @param response The http response
     * @param cookies The http response cookies
     */
    protected void processResponseCookies(SimpleResponse response, List<String> cookies) {
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

    /**
     * Process the http response headers.
     * @param response The http response
     * @param headers The http response headers
     */
    protected void processResponseHeaders(SimpleResponse response, Map<String, List<String>> headers) {
        for (Map.Entry<String, List<String>> entry : headers.entrySet()) {
            String name = entry.getKey();
            // Http/1.1 line
            if (name == null) { continue; }
            List<String> values = entry.getValue();
            if (SET_COOKIE.equalsIgnoreCase(name)) {
                processResponseCookies(response, values);
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

    /**
     * Process the http response body.
     * @param r The http response
     * @param c The http URL connection
     * @param s Whether to enable streaming
     * @throws IOException The network IO error
     */
    protected void processResponseBody(SimpleResponse r, HttpURLConnection c, Boolean s) throws IOException {
        // -1 means unknown, chunked.
        if (c.getContentLength() != ZERO && r.getMethod() != HEAD) {
            // Sun throws an IO exception on 500 response with no content when trying to read body.
            InputStream errorStream = c.getErrorStream();
            InputStream bodyStream =
                    errorStream != null ? errorStream : c.getInputStream();
            if (r.containsHeader(CONTENT_ENCODING)
                    && GZIP.equalsIgnoreCase(r.getFirstHeader(CONTENT_ENCODING))) {
                bodyStream = new GZIPInputStream(bodyStream);
            }
            if (s != null && s) {
                r.setBodyStream(new ConnectionInputStream(c, bodyStream));
            }
            else {
                byte[] body = IOUtils.toByteArray(bodyStream);
                r.setBodyStream(new ByteArrayInputStream(body));
            }
        }
    }

    /**
     * Process the http response redirect.
     * @param res The http response
     * @param req The http request
     * @return The http response after redirection
     * @throws IOException The network IO error
     */
    protected HttpResponse processResponseRedirect(SimpleResponse res, SimpleRequest req) throws IOException {
        // Http/1.1 temporary redirect, not in Java's set.
        int httpTempRedirect = 307;
        if (res.getStatusCode() != httpTempRedirect) {
            // Always redirect with a get.
            // Any data param from original req are dropped.
            req.setMethod(HttpMethod.GET);
            req.clearParameters();
            req.setBody(null);
            req.removeHeader(CONTENT_TYPE);
        }
        String location = res.getFirstHeader(LOCATION);
        //Assert.notNull(location);
        URL redirectUrl = buildRedirectUrl(new URL(req.getUrl()), location);
        req.setUrl(String.valueOf(encodeUrl(redirectUrl)));
        // Add response cookies to request (for e.g. login posts).
        //req.addCookies(resp.getCookies());
        return execute(req, res);
    }

    /**
     * Build the http response based on the http URL connection.
     * @param prevResp The previous http response
     * @param c The http URL connection
     * @return The built http response
     * @throws IOException The network IO error
     */
    protected SimpleResponse buildResponse(SimpleResponse prevResp, HttpURLConnection c) throws IOException {
        SimpleResponse resp = SimpleResponse.of(
                HttpMethod.valueOf(c.getRequestMethod()), String.valueOf(c.getURL()));
        resp.setStatusCode(c.getResponseCode());
        resp.setStatusMessage(c.getResponseMessage());
        processResponseHeaders(resp, obtainHeaders(c));
        // Set charset
        String contentType = resp.getFirstHeader(CONTENT_TYPE);
        String charsetName = obtainCharset(contentType);
        if (StringUtils.isNotBlank(charsetName)) {
            resp.setCharset(Charset.forName(charsetName).name());
        }
        // If from a redirect, map previous response cookies into this response
        if (prevResp == null) { return resp; }
        /*Map<String, String> prevCookies = prevResp.getCookies();
        for (Map.Entry<String, String> prevCookie : prevCookies.entrySet()) {
            if (response.containsCookie(prevCookie.getKey())) { continue; }
            resp.addCookie(prevCookie.getKey(), prevCookie.getValue());
        }*/
        return resp;
    }

    /**
     * Execute http request and return a response.
     * @param request The http request
     * @param previousResponse The previous http response
     * @return The http response
     * @throws IOException The network IO error
     */
    protected HttpResponse execute(SimpleRequest request, SimpleResponse previousResponse) throws IOException {
        // Validate request.
        Assert.notNull(request, "Parameter \"request\" must not null. ");
        Assert.notNull(request.getMethod(), "The request method must not null. ");
        Assert.notBlank(request.getUrl(), "The request url must not blank. ");
        // Handle has body.
        boolean hasBody = hasBody(request.getMethod());
        String mimeBoundary = null;
        if (!hasBody && CollectionUtils.isNotEmpty(request.getParameters())) {
            // Moved into url as get params.
            request.setUrl(buildRequestUrl(request.getUrl(), request.getCharset(), request.getParameters()));
            request.clearParameters();
        }
        else if (hasBody) {
            mimeBoundary = resetContentType(request);
        }
        // Create connection.
        HttpURLConnection connection = createConnection(request);
        try {
            connection.connect();
            // Write request data.
            if (connection.getDoOutput()) {
                writeRequest(request, connection.getOutputStream(), mimeBoundary);
            }
            // Build response by connection.
            SimpleResponse response = buildResponse(previousResponse, connection);
            // Redirect if there's a location header (from 3xx, or 201 etc).
            boolean redirect = request.getFollowRedirect() != null && request.getFollowRedirect();
            if (response.containsHeader(LOCATION) && redirect) {
                return processResponseRedirect(response, request);
            }
            // Http status code.
            boolean isErrorStatus = response.getStatusCode() < 200 || response.getStatusCode() >= 400;
            boolean ignoreErrors = request.getIgnoreHttpErrors() == null || request.getIgnoreHttpErrors();
            if (isErrorStatus && !ignoreErrors) {
                throw new IOException(String.format("HTTP error fetching URL. Status=%s, URL=[%s]"
                        , response.getStatusCode(), request.getUrl()));
            }
            // Process response body.
            processResponseBody(response, connection, request.getStream());
            // End.
            return response;
        }
        finally {
            if (request.getStream() == null || !request.getStream()) {
                CloseUtils.closeQuietly(connection);
            }
        }
    }

    @Override
    public HttpResponse execute(HttpRequest request) {
        try {
            return execute((SimpleRequest) request, null);
        } catch (Exception e) { throw ExceptionUtils.wrap(e); }
    }

    /**
     * The input stream that contains an HttpURLConnection.
     * @author Kahle
     */
    protected static class ConnectionInputStream extends InputStream {
        private final HttpURLConnection connection;
        private final InputStream inputStream;

        public ConnectionInputStream(HttpURLConnection connection, InputStream inputStream) {
            this.inputStream = inputStream;
            this.connection = connection;
        }

        @Override
        public int read() throws IOException {

            return inputStream.read();
        }

        @Override
        public int read(byte[] b) throws IOException {

            return inputStream.read(b);
        }

        @Override
        public int read(byte[] b, int off, int len) throws IOException {

            return inputStream.read(b, off, len);
        }

        @Override
        public long skip(long n) throws IOException {

            return inputStream.skip(n);
        }

        @Override
        public int available() throws IOException {

            return inputStream.available();
        }

        @Override
        public void close() throws IOException {
            try { inputStream.close(); }
            finally { connection.disconnect(); }
        }

        @Override
        public synchronized void mark(int readLimit) {

            inputStream.mark(readLimit);
        }

        @Override
        public synchronized void reset() throws IOException {

            inputStream.reset();
        }

        @Override
        public boolean markSupported() {

            return inputStream.markSupported();
        }
    }

}
