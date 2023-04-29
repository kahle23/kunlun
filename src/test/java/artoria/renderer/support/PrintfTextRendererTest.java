package artoria.renderer.support;

import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PrintfTextRendererTest {
    private static final Logger log = LoggerFactory.getLogger(PrintfTextRendererTest.class);
    private static final FormatTextRenderer renderer = new PrintfTextRenderer();

    @Test
    public void test1() {
        String render = renderer.render("Hello, %s!", new Object[]{"World"});
        log.info(render);
        assertEquals(render, "Hello, World!");

        render = renderer.render("Hello, %s! %s", new Object[]{"World", "Hi"});
        log.info(render);
        assertEquals(render, "Hello, World! Hi");
    }

}
