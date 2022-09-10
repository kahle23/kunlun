package artoria.renderer.support.text;

import org.junit.Test;

public class LoggerTextRendererTest {
    private static FormatTextRenderer r = new LoggerTextRenderer();

    @Test
    public void test1() {
        System.out.println(r.render("Hello, {}!", new Object[]{"World"}));
        System.out.println(r.render("Hello, {}! {}", new Object[]{"World", "Hi"}));
        System.out.println(r.render("Hello, \\{}, \\\\{}, {}! ", new Object[]{"W1", "W2", "W3"}));
        System.out.println(r.render("Hello, \\\\{}!", new Object[]{"World"}));
    }

}
