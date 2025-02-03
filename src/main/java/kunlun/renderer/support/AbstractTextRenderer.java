/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.renderer.support;

import kunlun.core.function.Consumer;
import kunlun.io.util.StringBuilderWriter;
import kunlun.renderer.TextRenderer;
import kunlun.util.Assert;

/**
 * The abstract text renderer.
 * @author Kahle
 */
public abstract class AbstractTextRenderer implements TextRenderer {
    private Consumer<Tpl> templateLoader = new Consumer<Tpl>() {
        @Override
        public void accept(Tpl tpl) {}
    };

    @Override
    public Consumer<Tpl> getTemplateLoader() {

        return templateLoader;
    }

    @Override
    public void setTemplateLoader(Consumer<Tpl> loader) {

        this.templateLoader = Assert.notNull(loader);
    }

    @Override
    public String renderToString(Object template, Object data) {
        StringBuilderWriter writer = new StringBuilderWriter();
        render(template, data, writer);
        return writer.toString();
    }

}
