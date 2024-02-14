package artoria.renderer;

import artoria.data.tuple.PairImpl;
import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import artoria.renderer.support.SimpleTextRenderer;
import artoria.util.Assert;

import java.io.Reader;
import java.io.Writer;

/**
 * Template render tools.
 * @author Kahle
 */
@Deprecated // TODO: 2023/11/13 can delete
public class TemplateUtils {
    private static Logger log = LoggerFactory.getLogger(TemplateUtils.class);
    private static volatile TextRenderer templateEngine;

    public static TextRenderer getTemplateEngine() {
        if (templateEngine != null) { return templateEngine; }
        synchronized (TemplateUtils.class) {
            if (templateEngine != null) { return templateEngine; }
            TemplateUtils.setTemplateEngine(new SimpleTextRenderer());
            return templateEngine;
        }
    }

    public static void setTemplateEngine(TextRenderer templateEngine) {
        Assert.notNull(templateEngine, "Parameter \"templateEngine\" must not null. ");
        log.info("Set template engine: {}", templateEngine.getClass().getName());
        TemplateUtils.templateEngine = templateEngine;
    }

    public static void render(String name, String encoding, Object data, Writer output) {

        getTemplateEngine().render(name, encoding, data, output);
    }

    public static void render(Object data, Writer output, String logTag, Reader reader) {

        getTemplateEngine().render(reader, logTag, data, output);
    }

    public static void render(Object data, Writer output, String logTag, String template) {

        getTemplateEngine().render((Object) template, logTag, data, output);
    }

    public static String renderToString(String name, String encoding, Object data) {

        return getTemplateEngine().renderToString(new PairImpl<String, String>(name, encoding), name, data);
    }

    public static String renderToString(Object data, String logTag, Reader reader) {

        return getTemplateEngine().renderToString(reader, logTag, data);
    }

    public static String renderToString(Object data, String logTag, String template) {

        return getTemplateEngine().renderToString(template, logTag, data);
    }

}
