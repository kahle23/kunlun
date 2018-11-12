package artoria.render;

import java.io.Reader;
import java.io.Writer;

/**
 * String template renderer.
 * @author Kahle
 */
public interface StringRenderer {

    /**
     * Rendering template by name.
     * @param name Template name
     * @param data Template filled model
     * @param writer Render result
     * @throws Exception Have more different exception
     */
    void render(String name, Object data, Writer writer) throws Exception;

    /**
     * Rendering template by name.
     * @param name Template name
     * @param encoding Template content charset
     * @param data Template filled model
     * @param writer Render result
     * @throws Exception Have more different exception
     */
    void render(String name, String encoding, Object data, Writer writer) throws Exception;

    /**
     * Rendering template by content.
     * @param data Template filled model
     * @param writer Render result
     * @param logTag Log messages name
     * @param template Template string
     * @throws Exception Have more different exception
     */
    void render(Object data, Writer writer, String logTag, String template) throws Exception;

    /**
     * Rendering template by content.
     * @param data Template filled model
     * @param writer Render result
     * @param logTag Log messages name
     * @param reader Template input stream
     * @throws Exception Have more different exception
     */
    void render(Object data, Writer writer, String logTag, Reader reader) throws Exception;

    /**
     * Rendering template by name.
     * @param name Template name
     * @param data Template filled model
     * @return Render result
     * @throws Exception Have more different exception
     */
    String renderToString(String name, Object data) throws Exception;

    /**
     * Rendering template by name.
     * @param name Template name
     * @param encoding Template content charset
     * @param data Template filled model
     * @return Render result
     * @throws Exception Have more different exception
     */
    String renderToString(String name, String encoding, Object data) throws Exception;

    /**
     * Rendering template by content.
     * @param data Template filled model
     * @param logTag Log messages name
     * @param template Template string
     * @return Render result
     * @throws Exception Have more different exception
     */
    String renderToString(Object data, String logTag, String template) throws Exception;

    /**
     * Rendering template by content.
     * @param data Template filled model
     * @param logTag Log messages name
     * @param reader Template input stream
     * @return Render result
     * @throws Exception Have more different exception
     */
    String renderToString(Object data, String logTag, Reader reader) throws Exception;

}
