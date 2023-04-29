package artoria.renderer.support;

import artoria.exception.ExceptionUtils;
import artoria.renderer.AbstractRenderer;
import artoria.util.Assert;

import java.io.IOException;
import java.io.Writer;

/**
 * The abstract format text renderer.
 * @author Kahle
 */
public abstract class AbstractFormatTextRenderer extends AbstractRenderer implements FormatTextRenderer {

    @Override
    public void render(Object template, String name, Object data, Object output) {
        Assert.isInstanceOf(String.class, template, "Parameter \"template\" must instance of String. ");
        Assert.isInstanceOf(Writer.class, output, "Parameter \"output\" must instance of Writer. ");
        Assert.isInstanceOf(Object[].class, data, "Parameter \"data\" must instance of Object[]. ");
        Object[] arguments = (Object[]) data;
        Writer writer = (Writer) output;
        try {
            String render = render((String) template, arguments);
            writer.write(render);
        }
        catch (IOException e) {
            throw ExceptionUtils.wrap(e);
        }
    }

    @Override
    public String renderToString(Object template, String name, Object data) {

        return render((String) template, (Object[]) data);
    }

}
