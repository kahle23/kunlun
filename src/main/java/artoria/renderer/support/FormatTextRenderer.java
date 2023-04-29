package artoria.renderer.support;

import artoria.renderer.TextRenderer;

/**
 * The renderer for handling formatting strings.
 * @author Kahle
 */
public interface FormatTextRenderer extends TextRenderer {

    /**
     * Render the data into text via template.
     * @param template The template to be rendered
     * @param arguments The arguments to use in rendering input template
     * @return The rendered text results
     */
    String render(String template, Object[] arguments);

}
