package artoria.template;

import artoria.io.StringBuilderWriter;
import artoria.util.Assert;

import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;

import static artoria.util.Const.DEFAULT_CHARSET_NAME;

/**
 * Template renderer by jdk.
 * @author Kahle
 */
public class JdkRenderer implements Renderer {

    @Override
    public void render(String name, Object data, Writer writer) throws Exception {
        this.render(name, DEFAULT_CHARSET_NAME, data, writer);
    }

    @Override
    public void render(String name, String encoding, Object data, Writer writer) throws Exception {
        throw new UnsupportedOperationException();
    }

    @Override
    public void render(Object data, Writer writer, String logTag, String template) throws Exception {
        Assert.notBlank(template, "Parameter \"template\" must not blank. ");
        StringReader reader = new StringReader(template);
        this.render(data, writer, logTag, reader);
    }

    @Override
    public void render(Object data, Writer writer, String logTag, Reader reader) throws Exception {
        throw new UnsupportedOperationException();
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

}
