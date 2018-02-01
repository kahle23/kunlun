package com.apyhs.artoria.net.http;

import java.io.InputStream;
import java.net.Proxy;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.apyhs.artoria.constant.Const.DEFAULT_CHARSET_NAME;

public class Request {
    private URL url;
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

}
