package sabertest.engine.template;

import freemarker.cache.MultiTemplateLoader;
import freemarker.cache.StringTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import org.junit.Before;
import org.junit.Test;
import saber.engine.template.FreeMarker;

import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

public class FreeMarkerTest {
    private Map data = new HashMap<>();
    private Configuration cfg = new Configuration();
    private StringTemplateLoader strTempLoader = new StringTemplateLoader();

    @Before
    public void init() {
        data.put("str", "hello world!");
        cfg.setObjectWrapper(new DefaultObjectWrapper());
    }

    @Test
    public void test1() throws Exception {
        FreeMarker.me.setConfiguration(cfg);

        StringReader reader = new StringReader("aa${str} vvvv\n ${str} aa");
        Template template = new Template("123", reader, cfg);

        strTempLoader.putTemplate("123", "aa${str} vvvv\n ${str} aa");
        MultiTemplateLoader loader = new MultiTemplateLoader(new TemplateLoader[]{strTempLoader});
        cfg.setTemplateLoader(loader);

        System.out.println(FreeMarker.me.processString("123", data));
        System.out.println(FreeMarker.me.processString(template, data));
    }

    @Test
    public void test2() throws Exception {
        String tempSource = "aa${str} vvvv\n ${str} aa";
        System.out.println(FreeMarker.me
                .addTemplate("123", tempSource)
                .processString("123", data));
    }

    @Test
    public void test3() throws Exception {
        String tempSource = "aa${str} vvvv\n ${str} aa";
        System.out.println(FreeMarker.me.handleString(tempSource, data));
    }

    @Test
    public void test4() throws Exception {
        FreeMarker.me = FreeMarker.on(new Configuration());
        System.out.println(FreeMarker.me.handleString("aa${str} vvvv\n ${str} aa", data));
    }

}
