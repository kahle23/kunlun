/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.util.handler;

/**
 * The template rendering tool.
 * @author Kahle
 */
@Deprecated
public interface RenderHandler {

    /**
     * Render the data into text via template.
     * @param rendererName The renderer name
     * @param template The template (stream, reader, string, etc.) to be rendered
     * @param name The template name (most scenarios used for log, nullable)
     * @param data The data to use in rendering input template
     * @return The rendered text results
     */
    String render(String rendererName, Object template, String name, Object data);

}
