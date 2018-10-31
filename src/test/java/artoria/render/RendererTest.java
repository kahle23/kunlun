package artoria.render;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class RendererTest {
    private Map<String, Object> data = new HashMap<String, Object>();

    @Before
    public void init() {
        data.put("hello", "world");
        data.put("hello1", new Object());
    }

    @Test
    public void test1() throws Exception {
        String tmp = "hello, ${hello}! \n" +
                "hello, ${hello1}! \n" +
                "hello, ${hello2}! \n" +
                "${hello}${hello}.";
        String result = RenderUtils.renderToString(data, "test1", tmp);
        System.out.println(result);
    }

}
