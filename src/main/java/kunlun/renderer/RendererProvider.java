/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.renderer;

import kunlun.core.Renderer;
import kunlun.core.function.Consumer;

import static kunlun.core.Renderer.Tpl;

/**
 * The renderer provider.
 * @author Kahle
 */
public interface RendererProvider {

    /**
     * Get the default renderer name.
     * @return The default renderer name
     */
    String getDefaultRendererName();

    /**
     * Set the default renderer name.
     * Depending on the implementation class, this method may throw an error
     *  (i.e. it does not allow the modification of the default renderer name).
     * @param defaultRendererName The default renderer name
     */
    void setDefaultRendererName(String defaultRendererName);

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
     * Get the template loader.
     * @param rendererName The renderer name
     * @return The template loader
     */
    Consumer<Tpl> getTemplateLoader(String rendererName);

    /**
     * Set the template loader.
     * @param rendererName The renderer name
     * @param loader The template loader
     */
    void setTemplateLoader(String rendererName, Consumer<Tpl> loader);

    /**
     * Render the data to the output through the template.
     * @param rendererName The renderer name
     * @param template The template (stream, reader, string, etc.) to be rendered
     * @param data The data to use in rendering input template
     * @param output The output stream or writer in which to render the output
     */
    void render(String rendererName, Object template, Object data, Object output);

    /**
     * Render the data into binary via template.
     * @param rendererName The renderer name
     * @param template The template (stream, reader, string, etc.) to be rendered
     * @param data The data to use in rendering input template
     * @return The rendered text results
     */
    byte[] renderToBytes(String rendererName, Object template, Object data);

    /**
     * Render the data into text via template.
     * @param rendererName The renderer name
     * @param template The template (stream, reader, string, etc.) to be rendered
     * @param data The data to use in rendering input template
     * @return The rendered text results
     */
    String renderToString(String rendererName, Object template, Object data);

}
