package artoria.renderer.support;

import artoria.io.util.StringBuilderWriter;
import artoria.renderer.AbstractRenderer;
import artoria.renderer.TextRenderer;

/**
 * The abstract text renderer.
 * @author Kahle
 */
public abstract class AbstractTextRenderer extends AbstractRenderer implements TextRenderer {

    @Override
    public String renderToString(Object template, String name, Object data) {
        StringBuilderWriter writer = new StringBuilderWriter();
        render(template, name, data, writer);
        return writer.toString();
    }

}
