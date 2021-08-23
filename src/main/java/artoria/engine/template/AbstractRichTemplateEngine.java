package artoria.engine.template;

import artoria.io.IOUtils;
import artoria.util.Assert;
import artoria.util.ClassLoaderUtils;
import artoria.util.StringUtils;

import java.io.*;
import java.nio.charset.Charset;

import static artoria.common.Constants.DEFAULT_ENCODING_NAME;

public abstract class AbstractRichTemplateEngine extends AbstractStringTemplateEngine implements RichTemplateEngine {

    @Override
    public void render(Object data, Writer writer, String tag, Reader reader) {
        Assert.notNull(reader, "Parameter \"reader\" must not null. ");
        String template;
        try {
            reader = reader instanceof BufferedReader
                    ? reader : new BufferedReader(reader);
            template = IOUtils.toString(reader);
        }
        catch (IOException e) {
            throw new RenderException(e);
        }
        render(data, writer, tag, template);
    }

    @Override
    public void render(Object data, Writer writer, String tag, String template) {

        throw new UnsupportedOperationException();
    }

    @Override
    public void render(String name, String encoding, Object data, Writer writer) {
        Assert.notBlank(name, "Parameter \"name\" must not blank. ");
        InputStream inputStream = ClassLoaderUtils.getResourceAsStream(name, getClass());
        if (inputStream == null) {
            throw new RenderException("Can not find template by \"" + name + "\" in classpath. ");
        }
        if (StringUtils.isBlank(encoding)) { encoding = DEFAULT_ENCODING_NAME; }
        Charset charset = Charset.forName(encoding);
        render(data, writer, name, new InputStreamReader(inputStream, charset));
    }

}
