package artoria.template;

import artoria.io.IOUtils;
import artoria.io.StringBuilderWriter;
import artoria.util.Assert;
import artoria.util.ClassLoaderUtils;
import artoria.util.StringUtils;

import java.io.*;
import java.nio.charset.Charset;

import static artoria.common.Constants.DEFAULT_ENCODING_NAME;

public abstract class AbstractTemplateEngine implements TemplateEngine {

    @Override
    public void render(Object data, Writer output, String logTag, Reader reader) {
        Assert.notNull(reader, "Parameter \"reader\" must not null. ");
        String input;
        try {
            input = IOUtils.toString(reader);
        }
        catch (IOException e) {
            throw new RenderException(e);
        }
        render(data, output, logTag, input);
    }

    @Override
    public void render(Object data, Writer output, String logTag, InputStream inputStream, String encoding) {
        Assert.notNull(inputStream, "Parameter \"inputStream\" must not null. ");
        if (StringUtils.isBlank(encoding)) { encoding = DEFAULT_ENCODING_NAME; }
        Charset charset = Charset.forName(encoding);
        Reader reader = new BufferedReader(new InputStreamReader(inputStream, charset));
        render(data, output, logTag, reader);
    }

    @Override
    public void render(String name, String encoding, Object data, Writer output) {
        Assert.notBlank(name, "Parameter \"name\" must not blank. ");
        InputStream inputStream = ClassLoaderUtils.getResourceAsStream(name, getClass());
        if (inputStream == null) {
            throw new RenderException("Can not find template by \"" + name + "\" in classpath. ");
        }
        render(data, output, name, inputStream, encoding);
    }

    @Override
    public String renderToString(Object data, String logTag, String template) {
        StringBuilderWriter writer = new StringBuilderWriter();
        render(data, writer, logTag, template);
        return writer.toString();
    }

    @Override
    public String renderToString(Object data, String logTag, Reader reader) {
        StringBuilderWriter writer = new StringBuilderWriter();
        render(data, writer, logTag, reader);
        return writer.toString();
    }

    @Override
    public String renderToString(Object data, String logTag, InputStream inputStream, String encoding) {
        StringBuilderWriter writer = new StringBuilderWriter();
        render(data, writer, logTag, inputStream, encoding);
        return writer.toString();
    }

    @Override
    public String renderToString(String name, String encoding, Object data) {
        StringBuilderWriter writer = new StringBuilderWriter();
        render(name, encoding, data, writer);
        return writer.toString();
    }

}
