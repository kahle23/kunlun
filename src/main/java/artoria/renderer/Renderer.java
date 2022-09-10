package artoria.renderer;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * The renderer for handling bytes or strings.
 * @see <a href="https://en.wikipedia.org/wiki/Render">Render</a>
 * @see <a href="https://en.wikipedia.org/wiki/Template_processor">Template processor</a>
 * @see <a href="https://en.wikipedia.org/wiki/Rendering_(computer_graphics)">Rendering (computer graphics)</a>
 * @author Kahle
 */
public interface Renderer {

    /**
     * Renders the input stream using the data into the output stream.
     * @param data The data to use in rendering input template
     * @param output The stream in which to render the output
     * @param tag The string to be used as the template name
     * @param input The input stream to be rendered
     */
    void render(Object data, OutputStream output, String tag, InputStream input);

}
