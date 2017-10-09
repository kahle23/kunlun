package saber.codec;

import saber.CollectionUtils;
import saber.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Map;

@SuppressWarnings("unchecked")
public abstract class UrlUtils {

    public static String encode(String data, String encoding)
            throws UnsupportedEncodingException {
        return URLEncoder.encode(data, encoding);
    }

    public static String decode(String data, String encoding)
            throws UnsupportedEncodingException {
        return URLDecoder.decode(data, encoding);
    }

    public static void encode(Map map, String encoding)
            throws UnsupportedEncodingException {
        if (CollectionUtils.isEmpty(map)) return;
        for (Object key : map.keySet()) {
            String value = map.get(key) != null
                    ? map.get(key).toString() : null;
            if (StringUtils.hasText(value)) {
                map.put(key, value != null ?
                        URLEncoder.encode(value, encoding) : null);
            }
        }
    }

    public static void decode(Map map, String encoding)
            throws UnsupportedEncodingException {
        if (CollectionUtils.isEmpty(map)) return;
        for (Object key : map.keySet()) {
            String value = map.get(key) != null
                    ? map.get(key).toString() : null;
            if (StringUtils.hasText(value)) {
                map.put(key, value != null ?
                        URLEncoder.encode(value, encoding) : null);
            }
        }
    }

}
