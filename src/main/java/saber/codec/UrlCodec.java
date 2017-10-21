package saber.codec;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

public class UrlCodec {
    public static final String DEFAULT_KEY_SEPARATOR = "=";
    public static final String DEFAULT_VALUE_SEPARATOR = "&";

    public static final UrlCodec me = UrlCodec.on();

    public static UrlCodec on() {
        UrlCodec urlCodec = new UrlCodec();
        urlCodec.keySeparator = DEFAULT_KEY_SEPARATOR;
        urlCodec.valueSeparator = DEFAULT_VALUE_SEPARATOR;
        return urlCodec;
    }

    public static UrlCodec on(String charset) {
        return new UrlCodec().setCharset(charset);
    }

    public static UrlCodec on(String keySeparator, String valueSeparator) {
        return new UrlCodec()
                .setKeySeparator(keySeparator)
                .setValueSeparator(valueSeparator);
    }

    private String charset = Charset.defaultCharset().name();
    private String keySeparator;
    private String valueSeparator;

    private UrlCodec() {}

    public String getCharset() {
        return charset;
    }

    public UrlCodec setCharset(String charset) {
        this.charset = charset;
        return this;
    }

    public String getKeySeparator() {
        return keySeparator;
    }

    public UrlCodec setKeySeparator(String keySeparator) {
        if (StringUtils.isBlank(keySeparator)) {
            throw new IllegalArgumentException("Key separator is blank. ");
        }
        this.keySeparator = keySeparator;
        return this;
    }

    public String getValueSeparator() {
        return valueSeparator;
    }

    public UrlCodec setValueSeparator(String valueSeparator) {
        if (StringUtils.isBlank(valueSeparator)) {
            throw new IllegalArgumentException("Value separator is blank. ");
        }
        this.valueSeparator = valueSeparator;
        return this;
    }

    public String doEncode(String data) throws UnsupportedEncodingException {
        return URLEncoder.encode(data, charset);
    }

    public String doDecode(String data) throws UnsupportedEncodingException {
        return URLDecoder.decode(data, charset);
    }

    public String encode(Map<?, ?> map) throws UnsupportedEncodingException {
        StringBuilder builder = new StringBuilder();
        if (MapUtils.isEmpty(map))
            return builder.toString();
        for (Map.Entry entry : map.entrySet()) {
            builder.append(entry.getKey());
            builder.append(keySeparator);
            builder.append(URLEncoder.encode(entry.getValue() + "", charset));
            builder.append(valueSeparator);
        }
        int len = builder.length();
        int valLen = valueSeparator.length();
        builder.delete(len - valLen, len);
        return builder.toString();
    }

    public Map<String, String> decode(String data) throws UnsupportedEncodingException {
        Map<String, String> result = new HashMap<>();
        String[] split = data.split(valueSeparator);
        if (split.length <= 0) return result;
        for (String s : split) {
            String[] entry = s.split(keySeparator);
            if (entry.length == 2) {
                result.put(entry[0], URLDecoder.decode(entry[1], charset));
            }
        }
        return result;
    }

}
