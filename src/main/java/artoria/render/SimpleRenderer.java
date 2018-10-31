package artoria.render;

import artoria.io.IOUtils;
import artoria.io.StringBuilderWriter;
import artoria.util.Assert;
import artoria.util.StringUtils;

import java.io.*;
import java.text.ParseException;
import java.util.Map;

import static artoria.common.Constants.*;
import static artoria.io.IOUtils.EOF;

/**
 * Template renderer simple implement by jdk.
 * @author Kahle
 */
public class SimpleRenderer implements Renderer {

    @Override
    public void render(String name, Object data, Writer writer) throws Exception {

        this.render(name, DEFAULT_CHARSET_NAME, data, writer);
    }

    @Override
    public void render(String name, String encoding, Object data, Writer writer) throws Exception {
        Assert.notBlank(name, "Parameter \"name\" must not blank. ");
        InputStream in = IOUtils.findClasspath(name);
        if (in == null) {
            throw new IOException("Can not find template by \"" + name + "\" in classpath. ");
        }
        Reader reader = new InputStreamReader(in, encoding);
        this.render(data, writer, name, reader);
    }

    @Override
    public void render(Object data, Writer writer, String logTag, String template) throws Exception {
        Assert.notBlank(template, "Parameter \"template\" must not blank. ");
        StringReader reader = new StringReader(template);
        this.render(data, writer, logTag, reader);
    }

    @Override
    public void render(Object data, Writer writer, String logTag, Reader reader) throws Exception {
        Assert.notNull(reader, "Parameter \"reader\" must not null. ");
        String template = IOUtils.toString(reader);
        this.render(data, writer, template);
    }

    @Override
    public String renderToString(String name, Object data) throws Exception {

        return this.renderToString(name, DEFAULT_CHARSET_NAME, data);
    }

    @Override
    public String renderToString(String name, String encoding, Object data) throws Exception {
        StringBuilderWriter writer = new StringBuilderWriter();
        this.render(name, encoding, data, writer);
        return writer.toString();
    }

    @Override
    public String renderToString(Object data, String logTag, String template) throws Exception {
        Assert.notBlank(template, "Parameter \"template\" must not blank. ");
        StringReader reader = new StringReader(template);
        return this.renderToString(data, logTag, reader);
    }

    @Override
    public String renderToString(Object data, String logTag, Reader reader) throws Exception {
        StringBuilderWriter writer = new StringBuilderWriter();
        this.render(data, writer, logTag, reader);
        return writer.toString();
    }

    private void render(Object data, Writer writer, String template) throws Exception {
        Assert.notNull(data, "Parameter \"data\" must not null. ");
        Assert.isInstanceOf(Map.class, data, "Parameter \"data\" must instance of \"Map.class\". ");
        Assert.notNull(writer, "Parameter \"writer\" must not null. ");
        if (StringUtils.isBlank(template)) { writer.write(template); return; }
        Map dataMap = (Map) data;
        for (int finish = template.length(), begin = 0, end = 0; end != finish; ) {
            end = template.indexOf(DOLLAR_MARK + LEFT_CURLY_BRACKET, begin);
            end = end == EOF ? finish : end;
            writer.write(template.substring(begin, end));
            if (end == finish) { continue; }
            end = template.indexOf(RIGHT_CURLY_BRACKET, (begin = end + 2));
            if (end == EOF) {
                throw new ParseException("After \"${\" must be \"}\" in index \"" + begin + "\". ", begin);
            }
            Object obj = dataMap.get(template.substring(begin, end));
            writer.write(obj != null ? obj.toString() : EMPTY_STRING);
            begin = ++end;
        }
    }

}
