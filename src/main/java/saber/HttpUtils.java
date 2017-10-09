package saber;

import saber.codec.MapUtils;
import saber.codec.UrlUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.util.Map;

public class HttpUtils {

    public static String buildUrlWithQueryString(String url, Map<String, String> queryParas, String charset)
            throws UnsupportedEncodingException {
        if (CollectionUtils.isNotEmpty(queryParas)) {
            if (!url.contains("?")) url += "?";
            else url += "&";
            UrlUtils.encode(queryParas, charset);
            url += MapUtils.encode(queryParas);
        }
        return url;
    }

    public static String get(String url, Map<String, String> queryParas, Map<String, String> headers)
            throws IOException, GeneralSecurityException {
        return new Http(url).addParameters(queryParas).addHeaders(headers).get();
    }

    public static String get(String url, Map<String, String> queryParas)
            throws IOException, GeneralSecurityException {
        return new Http(url).addParameters(queryParas).get();
    }

    public static String get(String url)
            throws IOException, GeneralSecurityException {
        return new Http(url).get();
    }

    public static String post(String url, Map<String, String> queryParas, Map<String, String> headers, String data)
            throws IOException, GeneralSecurityException {
        return new Http(url).setData(!StringUtils.hasText(data) ? null : data.getBytes())
                .addParameters(queryParas).addHeaders(headers).post();
    }

    public static String post(String url, Map<String, String> headers, String data)
            throws IOException, GeneralSecurityException {
        return new Http(url).setData(!StringUtils.hasText(data) ? null : data.getBytes())
                .addHeaders(headers).post();
    }

    public static String post(String url, String data)
            throws IOException, GeneralSecurityException {
        return new Http(url).setData(!StringUtils.hasText(data) ? null : data.getBytes()).post();
    }

    public static String post(String url, Map<String, String> queryParas)
            throws IOException, GeneralSecurityException {
        return new Http(url).addParameters(queryParas).post();
    }

}
