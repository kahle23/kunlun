package artoria.template;

import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static artoria.common.Constants.DEFAULT;

public class RendererTest {
    private static Logger log = LoggerFactory.getLogger(RendererTest.class);
    private Map<String, Object> data = new HashMap<String, Object>();

    @Before
    public void init() {
        data.put("hello", "world");
        data.put("hello1", new Object());
    }

    @Test
    public void test1() {
        String tmp = "hello, ${hello}! \n" +
                "hello, ${hello1}! \n" +
                "hello, ${hello2}! \n" +
                "${hello}${hello}.";
        log.info(RenderUtils.renderToString(data, DEFAULT, tmp));
    }

    @Test
    public void test2() {
        log.info(RenderUtils.renderToString(data, DEFAULT, "\\${hello}abc"));
        log.info(RenderUtils.renderToString(data, DEFAULT, "abc\\${hello}abc${hello}abc"));
        log.info(RenderUtils.renderToString(data, DEFAULT, "\\\\${hello}abc"));
        log.info(RenderUtils.renderToString(data, DEFAULT, "\\\\\\${hello}abc"));
        log.info(RenderUtils.renderToString(data, DEFAULT, "abc\\\\${hello}abc${hello}abc"));
    }

}
