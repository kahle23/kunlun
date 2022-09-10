package artoria.renderer.support.text;

import java.io.Writer;

public interface NormalTextRenderer extends TextRenderer {

    /**
     * Renders the input template using the data into the output writer.
     * @param data The data to use in rendering input template
     * @param writer The writer in which to render the output
     * @param tag The string to be used as the template name
     * @param template The input template to be rendered
     */
    void render(Object data, Writer writer, String tag, String template);

    void render(String name, String encoding, Object data, Writer writer);

}
