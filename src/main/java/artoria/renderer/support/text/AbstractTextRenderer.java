package artoria.renderer.support.text;

import artoria.data.bean.BeanUtils;
import artoria.util.Assert;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Map;

public abstract class AbstractTextRenderer implements TextRenderer {

    protected Charset getEncoding(Object data) {
        Assert.notNull(data, "Parameter \"data\" must not null. ");
        Map map = data instanceof Map ? (Map) data : BeanUtils.createBeanMap(data);
        Object encodingObj = map.get("encoding");
        if (encodingObj instanceof Charset) {
            return (Charset) encodingObj;
        }
        if (encodingObj instanceof String) {
            return Charset.forName((String) encodingObj);
        }
        Object charsetObj = map.get("charset");
        if (charsetObj instanceof Charset) {
            return (Charset) charsetObj;
        }
        if (charsetObj instanceof String) {
            return Charset.forName((String) charsetObj);
        }
        throw new IllegalArgumentException("Parameter \"data\" must contain encoding information. ");
    }

    @Override
    public void render(Object data, OutputStream output, String tag, InputStream input) {
        Assert.notNull(output, "Parameter \"output\" must not null. ");
        Assert.notNull(input, "Parameter \"input\" must not null. ");
        Assert.notNull(data, "Parameter \"data\" must not null. ");
        Charset encoding = getEncoding(data);
        Writer writer = new OutputStreamWriter(output, encoding);
        Reader reader = new InputStreamReader(input, encoding);
        render(data, writer, tag, reader);
    }

}
