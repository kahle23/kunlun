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
     * @param charsetName Template content charset name
     * @throws Exception Have more different exception
     */
    void render(Object data, Object output, String name, Object input, String charsetName) throws Exception;

}
