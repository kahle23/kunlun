/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.net.http;

import kunlun.data.tuple.KeyValue;
import kunlun.exception.ExceptionUtils;
import kunlun.io.util.IOUtils;
import kunlun.util.*;

import javax.net.ssl.*;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import static kunlun.common.constant.Numbers.TEN;
import static kunlun.common.constant.Symbols.*;
import static kunlun.io.util.IOUtils.EOF;

/**
 * The abstract http client.
 * @author Kahle
 */
public abstract class AbstractHttpClient implements HttpClient {
    protected static final String CONTENT_ENCODING = "Content-Encoding";
    protected static final String CONTENT_TYPE = "Content-Type";
    protected static final String SET_COOKIE = "Set-Cookie";
    protected static final String LOCATION = "Location";
    protected static final String COOKIE = "Cookie";
    private Map<Object, Object> commonProperties = Collections.emptyMap();

    @Override
    public Map<Object, Object> getCommonProperties() {

        return commonProperties;
    }

    @Override
    public void setCommonProperties(Map<?, ?> properties) {
        Assert.notNull(properties, "Parameter \"properties\" must not null. ");
        this.commonProperties = Collections.unmodifiableMap(properties);
    }

    /**
     * Build the unsafe hostname verifier.
     * @return The unsafe hostname verifier
     */
    protected HostnameVerifier buildUnsafeHostnameVerifier() {
        return new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };
    }

    /**
     * Build the unsafe ssl socket factory.
     * @return The unsafe ssl socket factory
     */
    protected SSLSocketFactory buildUnsafeSslSocketFactory() {
        // Create a trust manager that does not validate certificate chains.
        final TrustManager[] trustAllCerts = new TrustManager[] {new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType) {
            }
            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType) {
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
            // sslContext = SSLContext.getInstance("TLS")
            // sslContext = SSLContext.getInstance("TLS", "SunJSSE")
            sslContext.init(null, trustAllCerts, new SecureRandom());
        }
        catch (GeneralSecurityException e) {
            throw ExceptionUtils.wrap(e);
        }
        // Create a ssl socket factory with our all-trusting manager.
        return sslContext.getSocketFactory();
    }

    /**
     * Check whether the http method supports body.
     * @param method The http method
     * @return The judgment result
     */
    protected boolean hasBody(HttpMethod method) {
        Assert.notNull(method, "Parameter \"method\" must not null. ");
        // https://en.wikipedia.org/wiki/HTTP#Request_methods
        switch (method) {
            case GET:
            case DELETE:
            case HEAD:
            case OPTIONS:
            case TRACE:
            case CONNECT:
                return false;
            case POST:
            case PUT:
            case PATCH:
                return true;
            default: throw new UnsupportedOperationException("Parameter \"method\" is unsupported. ");
        }
    }

    /**
     * Check whether the object is multipart data.
     * @param value The object data
     * @return The judgment result
     */
    protected boolean hasMultipart(Object value) {
        return value instanceof File
//                || value instanceof KeyValue
                ;
    }

    /**
     * Check whether the key-value pair collection contains multipart data.
     * @param parameters The key-value pair collection
     * @return The judgment result
     */
    protected boolean hasMultipart(Collection<KeyValue<String, Object>> parameters) {
        if (CollectionUtils.isEmpty(parameters)) { return false; }
        boolean multipart = false;
        for (KeyValue<String, Object> keyValue : parameters) {
            multipart = hasMultipart(keyValue.getValue());
            if (multipart) { break; }
        }
        return multipart;
    }

    /**
     * Encoding the http URL.
     * @param url The http URL
     * @return The encoded http url
     */
    protected URL encodeUrl(URL url) {
        try {
            // Odd way to encode urls, but it works!
            // URL external form may have spaces which is illegal in new URL() (odd asymmetry)
            String urlEf = url.toExternalForm();
            urlEf = urlEf.replace(" ", "%20");
            final URI uri = new URI(urlEf);
            return new URL(uri.toASCIIString());
        }
        catch (Exception e) {
            // give up and return the original input
            return url;
        }
    }

    /**
     * Encoding the mime name.
     * @param mimeName The mime name
     * @return The encoded mime name
     */
    protected String encodeMimeName(String mimeName) {
        // Encodes \" to %22
        if (mimeName == null) { return null; }
        return StringUtils.replace(mimeName, DOUBLE_QUOTE, "%22");
    }

    /**
     * Obtain the charset name from http content type.
     * @param contentType The http content type
     * @return The http charset name or null
     */
    protected String obtainCharset(String contentType) {
        if (StringUtils.isBlank(contentType)) { return null; }
        contentType = contentType.trim().toLowerCase();
        String charsetEqual = "charset=";
        int begin = contentType.indexOf(charsetEqual);
        if (begin == EOF) { return null; }
        int end = contentType.indexOf(SEMICOLON, begin);
        end = end != EOF ? end : contentType.length();
        begin = begin + charsetEqual.length();
        return contentType.substring(begin, end).trim();
    }

    /**
     * Build the http mime boundary.
     * @param charArray The mime boundary random char array
     * @param length The mime boundary length
     * @return The built http mime boundary
     */
    protected String buildMimeBoundary(char[] charArray, int length) {
        Assert.isTrue(length > TEN, "Parameter \"length\" must greater than 10. ");
        return "----WebKitFormBoundary" + RandomUtils.nextString(charArray, length);
    }

    /**
     * Build the http cookies string.
     * @param cookies The http cookies
     * @param charset The http charset
     * @return The built http cookies string
     */
    protected String buildCookiesString(Map<String, ?> cookies, String charset) {
        if (MapUtils.isEmpty(cookies)) { return EMPTY_STRING; }
        StringBuilder cookiesBuilder = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, ?> cookie : cookies.entrySet()) {
            if (first) { first = false; }
            else { cookiesBuilder.append("; "); }
            cookiesBuilder.append(cookie.getKey());
            cookiesBuilder.append(EQUAL);
            cookiesBuilder.append(cookie.getValue());
        }
        String cookiesString = cookiesBuilder.toString();
        byte[] cookiesStrBytes = cookiesString.getBytes(Charset.forName(charset));
        if (cookiesStrBytes.length != cookiesString.length()) {
            // Spec says only ascii, no escaping / encoding defined.
            // Validate on set? or escape somehow here?
            throw new IllegalArgumentException("Cookies must only ascii! ");
        }
        return cookiesString;
    }

    /**
     * Build the redirect URL (new may not parameters).
     * @param oldUrl The old request URL
     * @param redirectUrl The new request host
     * @return The redirect URL
     * @throws MalformedURLException The error related to URL format
     */
    protected URL buildRedirectUrl(URL oldUrl, String redirectUrl) throws MalformedURLException {
        if (StringUtils.isBlank(redirectUrl)) {
            throw new MalformedURLException("Redirect url must not blank. ");
        }
        String lowerRedirectUrl = redirectUrl.toLowerCase();
        // Define constant.
        String malformedHttp = "http:/", malformedHttps = "https:/"; char slash = '/';
        int httpLength = malformedHttp.length(), httpsLength = malformedHttps.length();
        // Fix broken Location: http:/temp/AAG_New/en/index.php
        if (lowerRedirectUrl.startsWith(malformedHttp) && redirectUrl.charAt(httpLength) != slash) {
            redirectUrl = redirectUrl.substring(httpLength);
        }
        // Fix broken Location: https:/temp/AAG_New/en/index.php
        if (lowerRedirectUrl.startsWith(malformedHttps) && redirectUrl.charAt(httpsLength) != slash) {
            redirectUrl = redirectUrl.substring(httpsLength);
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

    /**
     * Build the request URL.
     * @param u The original http URL
     * @param c The http charset
     * @param p The http parameters
     * @return The built http URL
     * @throws IOException The network IO error
     */
    protected String buildRequestUrl(String u, String c, Collection<KeyValue<String, Object>> p) throws IOException {
        StringBuilder urlBuilder = new StringBuilder();
        URL oldUrl = new URL(u);
        // Reconstitute the query, ready for appends.
        urlBuilder.append(oldUrl.getProtocol())
                .append("://")
                // Includes host, port.
                .append(oldUrl.getAuthority())
                .append(oldUrl.getPath())
                .append(QUESTION_MARK);
        String oldUrlQuery = oldUrl.getQuery();
        boolean first = true;
        if (StringUtils.isNotBlank(oldUrlQuery)) {
            urlBuilder.append(oldUrlQuery);
            first = false;
        }
        for (KeyValue<String, Object> keyValue : p) {
            Object value = keyValue.getValue();
            if (hasMultipart(value)) {
                throw new IOException("File not supported in URL query string. ");
            }
            if (first) { first = false; }
            else { urlBuilder.append(AMPERSAND); }
            String key = URLEncoder.encode(keyValue.getKey(), c);
            String val = value != null ? URLEncoder.encode(String.valueOf(value), c) : EMPTY_STRING;
            urlBuilder.append(key).append(EQUAL).append(val);
        }
        return urlBuilder.toString();
    }

    /**
     * Write multipart data.
     * @param w The http writer
     * @param o The http output stream
     * @param r The http request
     * @param b The http mime boundary
     * @throws IOException The network IO error
     */
    protected void writeMultipart(BufferedWriter w, OutputStream o, HttpRequest r, String b) throws IOException {
        Collection<KeyValue<String, Object>> parameters = r.getParameters();
        for (KeyValue<String, Object> keyValue : parameters) {
            Object val = keyValue.getValue();
            String key = keyValue.getKey();
            w.write("--");
            w.write(b);
            w.write("\r\n");
            w.write("Content-Disposition: form-data; name=\"");
            // encodes " to %22
            w.write(encodeMimeName(key));
            w.write("\"");
            // write value
            InputStream in = null;
            try {
                if (val instanceof File) {
                    File file = (File) val;
                    w.write("; filename=\"");
                    w.write(encodeMimeName(file.getName()));
                    w.write("\"\r\nContent-Type: application/octet-stream\r\n\r\n");
                    // flush
                    w.flush();
                    IOUtils.copyLarge((in = new FileInputStream(file)), o);
                    o.flush();
                }
                else {
                    w.write("\r\n\r\n");
                    if (val != null) {
                        w.write(String.valueOf(val));
                    }
                }
            } finally { CloseUtils.closeQuietly(in); }
            // write new line
            w.write("\r\n");
        }
        w.write("--");
        w.write(b);
        w.write("--");
    }

    /**
     * Write body data.
     * @param w The http writer
     * @param o The http output stream
     * @param r The http request
     * @throws IOException The network IO error
     */
    protected void writeBodyData(BufferedWriter w, OutputStream o, HttpRequest r) throws IOException {
        String charset = r.getCharset();
        Object body = r.getBody();
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
                IOUtils.copyLarge(reader, w);
            }
            else {
                w.write(String.valueOf(body));
            }
            w.flush();
        } finally { CloseUtils.closeQuietly(reader); }
    }

    /**
     * Write form data.
     * @param w The http writer
     * @param o The http output stream
     * @param r The http request
     * @throws IOException The network IO error
     */
    protected void writeFormData(BufferedWriter w, OutputStream o, HttpRequest r) throws IOException {
        Collection<KeyValue<String, Object>> parameters = r.getParameters();
        String charset = r.getCharset();
        boolean first = true;
        for (KeyValue<String, Object> keyValue : parameters) {
            if (first) {
                first = false;
            }
            else {
                w.write(AMPERSAND);
            }
            String key = keyValue.getKey();
            String val = keyValue.getValue() != null ? String.valueOf(keyValue.getValue()) : EMPTY_STRING;
            key = URLEncoder.encode(key, charset);
            val = URLEncoder.encode(val, charset);
            w.write(key);
            w.write(EQUAL);
            w.write(val);
        }
        w.flush();
    }

    /**
     * Write request data.
     * @param request The http request
     * @param output The http output stream
     * @param boundary The http mime boundary
     * @throws IOException The network IO error
     */
    protected void writeRequest(HttpRequest request, OutputStream output, String boundary) throws IOException {
        try {
            String charset = request.getCharset();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(output, charset));
            if (StringUtils.isNotBlank(boundary)) {
                // Boundary will be set if we're in multipart mode.
                writeMultipart(writer, output, request, boundary);
            }
            else if (request.getBody() != null) {
                writeBodyData(writer, output, request);
            }
            else {
                // Regular form data (application/x-www-form-urlencoded).
                writeFormData(writer, output, request);
            }
        } finally { CloseUtils.closeQuietly(output); }
    }

}
