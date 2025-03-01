/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.renderer.support;

import kunlun.core.function.Consumer;
import kunlun.exception.ExceptionUtil;
import kunlun.util.Assert;

import java.io.IOException;
import java.io.Writer;

/**
 * The abstract format text renderer.
 * @author Kahle
 */
public abstract class AbstractFormatTextRenderer implements FormatTextRenderer {

    @Override
    public Consumer<Tpl> getTemplateLoader() {

        return null;
    }

    @Override
    public void setTemplateLoader(Consumer<Tpl> loader) {

    }

    @Override
    public void render(Object template, Object data, Object output) {
        Object[] arguments = (Object[]) Assert.isInstanceOf(Object[].class, data);
        Writer writer = (Writer) Assert.isInstanceOf(Writer.class, output);
        String tpl = (String) Assert.isInstanceOf(String.class, template);
        try {
            writer.write(render(tpl, arguments));
        } catch (IOException e) { throw ExceptionUtil.wrap(e); }
    }

    @Override
    public String renderToString(Object template, Object data) {

        return render((String) template, (Object[]) data);
    }

}
