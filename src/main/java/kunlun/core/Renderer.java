/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.core;

/**
 * The renderer for handling bytes or strings.
 * @see <a href="https://en.wikipedia.org/wiki/Render">Render</a>
 * @see <a href="https://en.wikipedia.org/wiki/Template_processor">Template processor</a>
 * @see <a href="https://en.wikipedia.org/wiki/Rendering_(computer_graphics)">Rendering (computer graphics)</a>
 * @author Kahle
 */
public interface Renderer {

    /**
     * Render the data to the output through the template.
     * @param template The template (stream, reader, string, etc.) to be rendered
     * @param name The template name (most scenarios used for log, nullable)
     * @param data The data to use in rendering input template
     * @param output The output stream or writer in which to render the output
     */
    void render(Object template, String name, Object data, Object output);

}
