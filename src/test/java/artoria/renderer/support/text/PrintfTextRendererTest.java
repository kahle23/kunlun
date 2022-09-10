package artoria.renderer.support.text;

import org.junit.Test;

public class PrintfTextRendererTest {
    private static FormatTextRenderer r = new PrintfTextRenderer();

    @Test
    public void test1() {
        System.out.println(r.render("Hello, %s!", new Object[]{"World"}));
        System.out.println(r.render("Hello, %s! %s", new Object[]{"World", "Hi"}));
    }

}
