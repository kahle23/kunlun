package artoria.engine.template;

import org.junit.Test;

public class PrintfTemplateEngineTest {
    private static PlainTemplateEngine e = new PrintfTemplateEngine();

    @Test
    public void test1() {
        System.out.println(e.render("Hello, %s!", new Object[]{"World"}));
        System.out.println(e.render("Hello, %s! %s", new Object[]{"World", "Hi"}));
    }

}
