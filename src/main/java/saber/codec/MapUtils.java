package saber.codec;

import saber.CollectionUtils;

import java.util.HashMap;
import java.util.Map;

public abstract class MapUtils {
    private static final String EQUAL_SIGN = "=";
    private static final String AND_SIGN = "&";

    public static String encode(Map<?, ?> map) {
        return encode(map, EQUAL_SIGN, AND_SIGN);
    }

    public static Map<String, String> decode(String data) {
        return decode(data, EQUAL_SIGN, AND_SIGN);
    }

    public static String encode(Map<?, ?> map, String keySeparator, String valueSeparator) {
        StringBuilder builder = new StringBuilder();
        if (CollectionUtils.isEmpty(map))
            return builder.toString();
        for (Map.Entry entry : map.entrySet()) {
            builder.append(entry.getKey());
            builder.append(keySeparator);
            builder.append(entry.getValue());
            builder.append(valueSeparator);
        }
        int len = builder.length();
        int valLen = valueSeparator.length();
        builder.delete(len - valLen, len);
        return builder.toString();
    }

    public static Map<String, String> decode(String data, String keySeparator, String valueSeparator) {
        Map<String, String> result = new HashMap<>();
        String[] split = data.split(valueSeparator);
        if (split.length <= 0) return result;
        for (String s : split) {
            String[] entry = s.split(keySeparator);
            if (entry.length == 2) {
                result.put(entry[0], entry[1]);
            }
        }
        return result;
    }

}
