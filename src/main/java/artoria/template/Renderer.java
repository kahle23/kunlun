package artoria.template;

/**
 * Template renderer.
 * @author Kahle
 */
public interface Renderer {

    /**
     * Rendering template by name or by content.
     * @param data Template filled model
     * @param output Render result output stream
     * @param name Template name
     * @param input Template input stream
     * @param charset Template content charset name
     * @throws RenderException An exception occurred while rendering
     */
    void render(Object data, Object output, String name, Object input, String charset) throws RenderException;

}
