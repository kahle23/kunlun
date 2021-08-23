package artoria.engine.template;

import artoria.io.StringBuilderWriter;
import artoria.lang.Dict;
import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import org.junit.Test;

import static artoria.common.Constants.DEFAULT;

public class SimpleTemplateEngineTest {
    private static Logger log = LoggerFactory.getLogger(SimpleTemplateEngineTest.class);
    private static RichTemplateEngine engine = new SimpleTemplateEngine();

    @Test
    public void test1() {
        String template = "\nHello, ${param}! \n" +
                "Hello, ${param1}! \n" +
                "Hello, ${param2}! \n" +
                "Hi, ${param}, ${param}, ${param1}, ${param2}.";
        Dict data = Dict.of("param", "World")
//                .set("param1", "Earth")
                .set("param2", new Object());
        StringBuilderWriter writer = new StringBuilderWriter();
        engine.render(data, writer, DEFAULT, template);
        log.info(writer.toString());
    }

    @Test
    public void test2() {
        String template = "\nHello, ${param}! \n" +
                "Hello, \\${param}filler! \n" +
                "Hello, filler\\${param1}filler${param1}filler! \n" +
                "Hello, \\\\${param}filler! \n" +
                "Hello, \\\\\\${param1}filler! \n" +
                "Hello, filler\\\\${param}filler${param}filler! \n";
        Dict data = Dict.of("param", "World")
                .set("param1", "Earth");
        StringBuilderWriter writer = new StringBuilderWriter();
        engine.render(data, writer, DEFAULT, template);
        log.info(writer.toString());
    }

}
