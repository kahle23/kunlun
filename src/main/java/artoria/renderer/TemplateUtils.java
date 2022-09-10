package artoria.renderer;

import artoria.io.StringBuilderWriter;
import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import artoria.renderer.support.text.NormalTextRenderer;
import artoria.renderer.support.text.SimpleTextRenderer;
import artoria.util.Assert;

import java.io.Reader;
import java.io.Writer;

/**
 * Template render tools.
 * @author Kahle
 */
public class TemplateUtils {
    private static Logger log = LoggerFactory.getLogger(TemplateUtils.class);
    private static volatile NormalTextRenderer templateEngine;

    public static NormalTextRenderer getTemplateEngine() {
        if (templateEngine != null) { return templateEngine; }
        synchronized (TemplateUtils.class) {
            if (templateEngine != null) { return templateEngine; }
            TemplateUtils.setTemplateEngine(new SimpleTextRenderer());
            return templateEngine;
        }
    }

    public static void setTemplateEngine(NormalTextRenderer templateEngine) {
        Assert.notNull(templateEngine, "Parameter \"templateEngine\" must not null. ");
        log.info("Set template engine: {}", templateEngine.getClass().getName());
        TemplateUtils.templateEngine = templateEngine;
    }

    public static void render(String name, String encoding, Object data, Writer output) {

        getTemplateEngine().render(name, encoding, data, output);
    }

    public static void render(Object data, Writer output, String logTag, Reader reader) {

        getTemplateEngine().render(data, output, logTag, reader);
    }

    public static void render(Object data, Writer output, String logTag, String template) {

        getTemplateEngine().render(data, output, logTag, template);
    }

    public static String renderToString(String name, String encoding, Object data) {
        StringBuilderWriter writer = new StringBuilderWriter();
        getTemplateEngine().render(name, encoding, data, writer);
        return writer.toString();
    }

    public static String renderToString(Object data, String logTag, Reader reader) {
        StringBuilderWriter writer = new StringBuilderWriter();
        getTemplateEngine().render(data, writer, logTag, reader);
        return writer.toString();
    }

    public static String renderToString(Object data, String logTag, String template) {
        StringBuilderWriter writer = new StringBuilderWriter();
        getTemplateEngine().render(data, writer, logTag, template);
        return writer.toString();
    }

}
