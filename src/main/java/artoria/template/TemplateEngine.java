package artoria.template;

import java.io.InputStream;
import java.io.Reader;
import java.io.Writer;

/**
 * Template renderer.
 */
public interface TemplateEngine {

    /**
     * Template rendering.
     * @param data The data to be populated into the template
     * @param output The output stream of rendered result
     * @param logTag String to be used as the template name for log messages in case of error
     * @param template The template string
     */
    void render(Object data, Writer output, String logTag, String template);

    void render(Object data, Writer output, String logTag, Reader reader);

    /**
     * Template rendering.
     * @param data The data to be populated into the template
     * @param output The output stream of rendered result
     * @param logTag String to be used as the template name for log messages in case of error
     * @param inputStream The input stream of the template
     * @param encoding The character encoding to use
     */
    void render(Object data, Writer output, String logTag, InputStream inputStream, String encoding);

    void render(String name, String encoding, Object data, Writer output);

    String renderToString(Object data, String logTag, String template);

    String renderToString(Object data, String logTag, Reader reader);

    String renderToString(Object data, String logTag, InputStream inputStream, String encoding);

    String renderToString(String name, String encoding, Object data);

}
