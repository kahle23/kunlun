package saber.util.codec;

import org.apache.commons.collections.MapUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public abstract class UrlUtils {
    private static final String EQUAL_SIGN = "=";
    private static final String AND_SIGN = "&";

    public static String doEncode(String data, String encoding)
            throws UnsupportedEncodingException {
        return URLEncoder.encode(data, encoding);
    }

    public static String doDecode(String data, String encoding)
            throws UnsupportedEncodingException {
        return URLDecoder.decode(data, encoding);
    }

    public static String encode(Map<?, ?> map, String encoding)
            throws UnsupportedEncodingException {
        return encode(map, EQUAL_SIGN, AND_SIGN, encoding);
    }

    public static Map<String, String> decode(String data, String encoding)
            throws UnsupportedEncodingException {
        return decode(data, EQUAL_SIGN, AND_SIGN, encoding);
    }

    public static String encode(Map<?, ?> map, String keySeparator, String valueSeparator, String encoding)
            throws UnsupportedEncodingException {
        StringBuilder builder = new StringBuilder();
        if (MapUtils.isEmpty(map))
            return builder.toString();
        for (Map.Entry entry : map.entrySet()) {
            builder.append(entry.getKey());
            builder.append(keySeparator);
            builder.append(URLEncoder.encode(entry.getValue() + "", encoding));
            builder.append(valueSeparator);
        }
        int len = builder.length();
        int valLen = valueSeparator.length();
        builder.delete(len - valLen, len);
        return builder.toString();
    }

    public static Map<String, String> decode(String data, String keySeparator, String valueSeparator, String encoding)
            throws UnsupportedEncodingException {
        Map<String, String> result = new HashMap<>();
        String[] split = data.split(valueSeparator);
        if (split.length <= 0) return result;
        for (String s : split) {
            String[] entry = s.split(keySeparator);
            if (entry.length == 2) {
                result.put(entry[0], URLDecoder.decode(entry[1], encoding));
            }
        }
        return result;
    }

}
