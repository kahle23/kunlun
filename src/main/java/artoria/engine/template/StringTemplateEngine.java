package artoria.engine.template;

import java.io.Reader;
import java.io.Writer;

/**
 * The template engine for handling strings.
 * @author Kahle
 */
public interface StringTemplateEngine extends TemplateEngine {

    /**
     * Renders the input reader using the data into the output writer.
     * @param data The data to use in rendering input template
     * @param writer The writer in which to render the output
     * @param tag The string to be used as the template name
     * @param reader The reader to be rendered
     */
    void render(Object data, Writer writer, String tag, Reader reader);

}
