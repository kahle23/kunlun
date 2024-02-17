/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.renderer.support;

import kunlun.logging.Logger;
import kunlun.logging.LoggerFactory;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class LoggerTextRendererTest {
    private static final Logger log = LoggerFactory.getLogger(LoggerTextRendererTest.class);
    private static final FormatTextRenderer renderer = new LoggerTextRenderer();

    @Test
    public void test1() {
        String render = renderer.render("Hello, {}!", new Object[]{"World"});
        log.info(render);
        assertEquals(render, "Hello, World!");

        render = renderer.render("Hello, {}! {}", new Object[]{"World", "Hi"});
        log.info(render);
        assertEquals(render, "Hello, World! Hi");

        render = renderer.render("Hello, \\{}, \\\\{}, {}! ", new Object[]{"W1", "W2", "W3"});
        log.info(render);
        assertEquals(render, "Hello, {}, \\\\W1, W2! ");

        render = renderer.render("Hello, \\\\{}!", new Object[]{"World"});
        log.info(render);
        assertEquals(render, "Hello, \\\\World!");
    }

}
