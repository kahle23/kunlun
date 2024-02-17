/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.renderer.support;

import kunlun.io.util.StringBuilderWriter;
import kunlun.renderer.AbstractRenderer;
import kunlun.renderer.TextRenderer;

/**
 * The abstract text renderer.
 * @author Kahle
 */
public abstract class AbstractTextRenderer extends AbstractRenderer implements TextRenderer {

    @Override
    public String renderToString(Object template, String name, Object data) {
        StringBuilderWriter writer = new StringBuilderWriter();
        render(template, name, data, writer);
        return writer.toString();
    }

}
