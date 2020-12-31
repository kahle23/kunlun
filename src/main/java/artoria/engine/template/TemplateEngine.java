package artoria.engine.template;

import java.io.Reader;
import java.io.Writer;

/**
 * Template renderer.
 */
public interface TemplateEngine {

    void render(String name, String encoding, Object data, Writer output);

    void render(Object data, Writer output, String logTag, Reader reader);

    /**
     * Template rendering.
     * @param data The data to be populated into the template
     * @param output The output stream of rendered result
     * @param logTag String to be used as the template name for log messages in case of error
     * @param template The template string
     */
    void render(Object data, Writer output, String logTag, String template);

}
