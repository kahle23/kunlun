package artoria.codec;

import artoria.util.Assert;
import artoria.util.MapUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import static artoria.util.StringConstant.AMPERSAND;
import static artoria.util.StringConstant.EQUAL;

/**
 * Url codec, and map to string tools.
 * @author Kahle
 */
public class UrlCodec {

    public static final UrlCodec ME = UrlCodec.create();

    public static UrlCodec create() {
        UrlCodec urlCodec = new UrlCodec();
        urlCodec.keySeparator = EQUAL;
        urlCodec.valueSeparator = AMPERSAND;
        return urlCodec;
    }

    public static UrlCodec create(String charset) {
        return UrlCodec.create()
                .setCharset(charset);
    }

    public static UrlCodec create(String keySeparator, String valueSeparator) {
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
        Assert.notBlank(charset, "Charset must is not blank. ");
        this.charset = charset;
        return this;
    }

    public String getKeySeparator() {
        return keySeparator;
    }

    public UrlCodec setKeySeparator(String keySeparator) {
        Assert.notBlank(keySeparator, "Key separator must is not blank. ");
        this.keySeparator = keySeparator;
        return this;
    }

    public String getValueSeparator() {
        return valueSeparator;
    }

    public UrlCodec setValueSeparator(String valueSeparator) {
        Assert.notBlank(valueSeparator, "Value separator must is not blank. ");
        this.valueSeparator = valueSeparator;
        return this;
    }

    public String encode(String data) throws UnsupportedEncodingException {
        Assert.notNull(data, "Data must is not null. ");
        return URLEncoder.encode(data, charset);
    }

    public String decode(String data) throws UnsupportedEncodingException {
        Assert.notNull(data, "Data must is not null. ");
        return URLDecoder.decode(data, charset);
    }

    public String encodeToString(Map<?, ?> map) throws UnsupportedEncodingException {
        Assert.notNull(map, "Map must is not null. ");
        StringBuilder builder = new StringBuilder();
        if (MapUtils.isEmpty(map)) {
            return builder.toString();
        }
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

    public Map<String, String> decodeFromString(String data) throws UnsupportedEncodingException {
        Assert.notNull(data, "Data must is not null. ");
        String[] split = data.split(valueSeparator);
        Map<String, String> result = new HashMap<>(split.length);
        if (split.length <= 0) {
            return result;
        }
        for (String s : split) {
            String[] entry = s.split(keySeparator);
            if (entry.length == 2) {
                result.put(entry[0], URLDecoder.decode(entry[1], charset));
            }
        }
        return result;
    }

}
