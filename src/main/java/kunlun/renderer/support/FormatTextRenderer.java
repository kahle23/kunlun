/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.renderer.support;

import kunlun.renderer.TextRenderer;

/**
 * The renderer for handling formatting strings.
 * @author Kahle
 */
public interface FormatTextRenderer extends TextRenderer {

    /**
     * Render the data into text via template.
     * @param template The template to be rendered
     * @param arguments The arguments to use in rendering input template
     * @return The rendered text results
     */
    String render(String template, Object[] arguments);

}
