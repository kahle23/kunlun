/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.renderer;

import kunlun.core.Renderer;

/**
 * The renderer for handling strings.
 * @author Kahle
 */
public interface TextRenderer extends Renderer {

    /**
     * Render the data into text via template.
     * @param template The template (stream, reader, string, etc.) to be rendered
     * @param name The template name (most scenarios used for log, nullable)
     * @param data The data to use in rendering input template
     * @return The rendered text results
     */
    String renderToString(Object template, String name, Object data);

}
