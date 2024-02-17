/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.renderer.support;

/**
 * The printf format text renderer.
 * @author Kahle
 */
public class PrintfTextRenderer extends AbstractFormatTextRenderer {

    @Override
    public String render(String template, Object[] arguments) {

        return String.format(template, arguments);
    }

}
