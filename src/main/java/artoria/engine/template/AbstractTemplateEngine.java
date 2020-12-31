package artoria.engine.template;

import artoria.io.IOUtils;
import artoria.util.Assert;
import artoria.util.ClassLoaderUtils;

import java.io.*;
import java.nio.charset.Charset;

public abstract class AbstractTemplateEngine implements TemplateEngine {

    @Override
    public void render(String name, String encoding, Object data, Writer output) {
        Assert.notBlank(name, "Parameter \"name\" must not blank. ");
        InputStream inputStream = ClassLoaderUtils.getResourceAsStream(name, getClass());
        if (inputStream == null) {
            throw new RenderException("Can not find template by \"" + name + "\" in classpath. ");
        }
        Charset charset = Charset.forName(encoding);
        render(data, output, name, new InputStreamReader(inputStream, charset));
    }

    @Override
    public void render(Object data, Writer output, String logTag, Reader reader) {
        Assert.notNull(reader, "Parameter \"reader\" must not null. ");
        String input;
        try {
            if (reader instanceof BufferedReader) {
                reader = new BufferedReader(reader);
            }
            input = IOUtils.toString(reader);
        }
        catch (IOException e) {
            throw new RenderException(e);
        }
        render(data, output, logTag, input);
    }

    @Override
    public void render(Object data, Writer output, String logTag, String template) {

        throw new UnsupportedOperationException();
    }

}
