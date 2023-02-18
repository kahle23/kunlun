package artoria.net.http.support;

import artoria.data.KeyValue;
import artoria.exception.ExceptionUtils;
import artoria.io.IOUtils;
import artoria.net.http.HttpClient;
import artoria.net.http.HttpMethod;
import artoria.net.http.HttpRequest;
import artoria.util.*;

import javax.net.ssl.*;
import java.io.*;
import java.net.URLEncoder;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import static artoria.common.Constants.*;

/**
 * The abstract http client.
 * @author Kahle
 */
public abstract class AbstractHttpClient implements HttpClient {
    private static final char[] MIME_BOUNDARY_CHARS =
            "1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
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

    protected HostnameVerifier buildUnsafeHostnameVerifier() {

        return new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };
    }

    protected SSLSocketFactory buildUnsafeSslSocketFactory() {
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

    protected String buildMimeBoundary(int length) {
        Assert.isTrue(length > FIVE, "Parameter \"length\" must greater than 5. ");
        String mimeBoundary = "----WebKitFormBoundary";
        mimeBoundary +=
                RandomUtils.nextString(MIME_BOUNDARY_CHARS, length);
        return mimeBoundary;
    }

    protected void validate(HttpRequest request) {
        Assert.notNull(request, "Parameter \"request\" must not null. ");
        HttpMethod method = request.getMethod();
        String url = request.getUrl();
        Assert.notNull(method, "The request method must not null. ");
        Assert.notBlank(url, "The request url must not blank. ");
    }

    protected boolean hasBody(HttpMethod method) {
        Assert.notNull(method, "Parameter \"method\" must not null. ");
        switch (method) {
            case GET:     return false;
            case POST:    return true;
            case PUT:     return true;
            case DELETE:  return false;
            case HEAD:    return false;
            case OPTIONS: return false;
            case TRACE:   return false;
//            case CONNECT: return false;
//            case PATCH:   return false;
            default: throw new UnsupportedOperationException("Parameter \"method\" is unsupported. ");
        }
    }

    protected boolean needsMultipart(Object value) {
        return value instanceof File
//                || value instanceof KeyValue
                ;
    }

    protected boolean needsMultipart(Collection<KeyValue<String, Object>> parameters) {
        if (CollectionUtils.isEmpty(parameters)) { return false; }
        boolean needsMultipart = false;
        for (KeyValue<String, Object> keyValue : parameters) {
            Object value = keyValue.getValue();
            needsMultipart = needsMultipart(value);
            if (needsMultipart) { break; }
        }
        return needsMultipart;
    }

    protected void writeFormData(BufferedWriter writer, OutputStream output, HttpRequest request) throws IOException {
        Collection<KeyValue<String, Object>> parameters = request.getParameters();
        String charset = request.getCharset();
        boolean first = true;
        for (KeyValue<String, Object> keyValue : parameters) {
            if (first) {
                first = false;
            }
            else {
                writer.write(AMPERSAND);
            }
            String key = keyValue.getKey();
            String val = keyValue.getValue() != null ? String.valueOf(keyValue.getValue()) : EMPTY_STRING;
            key = URLEncoder.encode(key, charset);
            val = URLEncoder.encode(val, charset);
            writer.write(key);
            writer.write(EQUAL);
            writer.write(val);
        }
        writer.flush();
    }

    protected void writeBodyData(BufferedWriter writer, OutputStream output, HttpRequest request) throws IOException {
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

    protected String encodeMimeName(String name) {
        // Encodes \" to %22
        if (name == null) { return null; }
        return StringUtils.replace(name, DOUBLE_QUOTE, "%22");
    }

    protected void writeString(BufferedWriter writer, String string) throws IOException {
        writer.write("\r\n\r\n");
        writer.write(string);
    }

    protected void writeFile(BufferedWriter writer, OutputStream output, String filename, InputStream fileStream) throws IOException {
        writer.write("; filename=\"");
        writer.write(encodeMimeName(filename));
        writer.write("\"\r\nContent-Type: application/octet-stream\r\n\r\n");
        // flush
        writer.flush();
        IOUtils.copyLarge(fileStream, output);
        output.flush();
    }

    protected void writeValue(BufferedWriter writer, OutputStream output, Object value) throws IOException {
        InputStream in = null;
        try {
            if (value instanceof File) {
                File file = (File) value;
                in = new FileInputStream(file);
                writeFile(writer, output, file.getName(), in);
            }
            else {
                writeString(writer, value != null ? String.valueOf(value) : null);
            }
        }
        finally {
            CloseUtils.closeQuietly(in);
        }
    }

    protected void writeMultipartData(BufferedWriter writer, OutputStream output, HttpRequest request, String mimeBoundary) throws IOException {
        Collection<KeyValue<String, Object>> parameters = request.getParameters();
        for (KeyValue<String, Object> keyValue : parameters) {
            Object val = keyValue.getValue();
            String key = keyValue.getKey();
            writer.write("--");
            writer.write(mimeBoundary);
            writer.write("\r\n");
            writer.write("Content-Disposition: form-data; name=\"");
            // encodes " to %22
            writer.write(encodeMimeName(key));
            writer.write("\"");
            writeValue(writer, output, val);
            writer.write("\r\n");
        }
        writer.write("--");
        writer.write(mimeBoundary);
        writer.write("--");
    }

    protected void writeRequestData(HttpRequest request, OutputStream output, String mimeBoundary) throws IOException {
        try {
            String charset = request.getCharset();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(output, charset));
            if (mimeBoundary != null) {
                // Boundary will be set if we're in multipart mode.
                writeMultipartData(writer, output, request, mimeBoundary);
            }
            else if (request.getBody() != null) {
                writeBodyData(writer, output, request);
            }
            else {
                // Regular form data (application/x-www-form-urlencoded).
                writeFormData(writer, output, request);
            }
        }
        finally {
            CloseUtils.closeQuietly(output);
        }
    }

}
