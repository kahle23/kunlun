package artoria.engine.template;

import org.junit.Test;

public class LoggerTemplateEngineTest {
    private static PlainTemplateEngine e = new LoggerTemplateEngine();

    @Test
    public void test1() {
        System.out.println(e.render("Hello, {}!", new Object[]{"World"}));
        System.out.println(e.render("Hello, {}! {}", new Object[]{"World", "Hi"}));
        System.out.println(e.render("Hello, \\{}, \\\\{}, {}! ", new Object[]{"W1", "W2", "W3"}));
        System.out.println(e.render("Hello, \\\\{}!", new Object[]{"World"}));
    }

}
