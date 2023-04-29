package artoria.renderer;

import artoria.core.Renderer;

import java.util.Map;

/**
 * The renderer provider.
 * @author Kahle
 */
public interface RendererProvider {

    /**
     * Register common properties' information.
     * @param commonProperties The common properties
     */
    void registerCommonProperties(Map<?, ?> commonProperties);

    /**
     * Clears common properties' information.
     */
    void clearCommonProperties();

    /**
     * Get common properties' information.
     * @return The common properties
     */
    Map<String, Object> getCommonProperties();

    /**
     * Register the renderer.
     * @param rendererName The renderer name
     * @param renderer The renderer
     */
    void registerRenderer(String rendererName, Renderer renderer);

    /**
     * Deregister the renderer.
     * @param rendererName The renderer name
     */
    void deregisterRenderer(String rendererName);

    /**
     * Get the renderer by name.
     * @param rendererName The renderer name
     * @return The renderer
     */
    Renderer getRenderer(String rendererName);

    /**
     * Render the data to the output through the template.
     * @param rendererName The renderer name
     * @param template The template (stream, reader, string, etc) to be rendered
     * @param name The template name (most scenarios used for log, nullable)
     * @param data The data to use in rendering input template
     * @param output The output stream or writer in which to render the output
     */
    void render(String rendererName, Object template, String name, Object data, Object output);

    /**
     * Render the data into binary via template.
     * @param rendererName The renderer name
     * @param template The template (stream, reader, string, etc) to be rendered
     * @param name The template name (most scenarios used for log, nullable)
     * @param data The data to use in rendering input template
     * @return The rendered text results
     */
    byte[] renderToBytes(String rendererName, Object template, String name, Object data);

    /**
     * Render the data into text via template.
     * @param rendererName The renderer name
     * @param template The template (stream, reader, string, etc) to be rendered
     * @param name The template name (most scenarios used for log, nullable)
     * @param data The data to use in rendering input template
     * @return The rendered text results
     */
    String renderToString(String rendererName, Object template, String name, Object data);

}
