/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.renderer.support;

import kunlun.data.Dict;
import kunlun.logging.Logger;
import kunlun.logging.LoggerFactory;
import kunlun.renderer.TextRenderer;
import org.junit.Test;

import static kunlun.common.constant.Words.DEFAULT;
import static org.junit.Assert.assertEquals;

public class SimpleTextRendererTest {
    private static final Logger log = LoggerFactory.getLogger(SimpleTextRendererTest.class);
    private static final TextRenderer renderer = new SimpleTextRenderer();

    @Test
    public void test1() {
        String render = renderer.renderToString("Hello, ${arg}!"
                , null, Dict.of("arg", "World"));
        log.info(render);
        assertEquals(render, "Hello, World!");

        render = renderer.renderToString("Hello, ${arg}! ${arg1}"
                , null, Dict.of("arg", "World").set("arg1", "Hi"));
        log.info(render);
        assertEquals(render, "Hello, World! Hi");

        render = renderer.renderToString("Hello, \\${arg}, \\\\${arg1}, ${arg2}! "
                , null, Dict.of("arg", "W1").set("arg1", "W2").set("arg2", "W3"));
        log.info(render);
        assertEquals(render, "Hello, ${arg}, \\\\W2, W3! ");

        render = renderer.renderToString("Hello, \\\\${arg}!"
                , null, Dict.of("arg", "World"));
        log.info(render);
        assertEquals(render, "Hello, \\\\World!");
    }

    @Test
    public void test2() {
        String template = "\nHello, ${param}! \n" +
                "Hello, ${param1}! \n" +
                "Hello, ${param2}! \n" +
                "Hi, ${param}, ${param}, ${param1}, ${param2}.";
        Dict data = Dict.of("param", "World")
//                .set("param1", "Earth")
                .set("param2", new Object());
        log.info(renderer.renderToString(template, DEFAULT, data));
    }

    @Test
    public void test3() {
        String template = "\nHello, ${param}! \n" +
                "Hello, \\${param}filler! \n" +
                "Hello, filler\\${param1}filler${param1}filler! \n" +
                "Hello, \\\\${param}filler! \n" +
                "Hello, \\\\\\${param1}filler! \n" +
                "Hello, filler\\\\${param}filler${param}filler! \n";
        Dict data = Dict.of("param", "World")
                .set("param1", "Earth");
        log.info(renderer.renderToString(template, DEFAULT, data));
    }

}
