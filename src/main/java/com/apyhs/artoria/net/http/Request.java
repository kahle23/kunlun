package com.apyhs.artoria.net.http;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.Proxy;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.apyhs.artoria.constant.Const.*;

public class Request {
    private String url;
    private Method method;
    private Map<String, String> headers = new LinkedHashMap<String, String>();
    private Map<String, Object> parameters = new LinkedHashMap<String, Object>();
    private byte[] content;
    private InputStream inputStream;
    private Map<String, String> cookies = new LinkedHashMap<String, String>();
    private int connectTimeout = 19000;
    private int readTimeout = 19000;
    private String charset = DEFAULT_CHARSET_NAME;
    private Proxy proxy;
    private String proxyUser;
    private String proxyPassword;

    public static String parametersToUrlString(Map<String, Object> parameters, String charset)
            throws UnsupportedEncodingException {
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

}
