package artoria.renderer.support.text;

import artoria.exception.ExceptionUtils;
import artoria.io.IOUtils;
import artoria.util.Assert;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

public abstract class AbstractFormatTextRenderer extends AbstractTextRenderer implements FormatTextRenderer {

    @Override
    public void render(Object data, Writer writer, String tag, Reader reader) {
        Assert.notNull(writer, "Parameter \"writer\" must not null. ");
        Assert.notNull(reader, "Parameter \"reader\" must not null. ");
        if (!(data == null || data instanceof Object[])) {
            throw new IllegalArgumentException("Parameter \"data\" must instance of Object[]. ");
        }
        Object[] arguments = (Object[]) data;
        try {
            reader = reader instanceof BufferedReader
                    ? reader : new BufferedReader(reader);
            String template = IOUtils.toString(reader);
            String render = render(template, arguments);
            writer.write(render);
        }
        catch (IOException e) {
            throw ExceptionUtils.wrap(e);
        }
    }

    @Override
    public String render(String template, Object[] arguments) {

        throw new UnsupportedOperationException();
    }

}
