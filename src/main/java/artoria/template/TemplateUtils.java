package artoria.template;

import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import artoria.util.Assert;

import java.io.InputStream;
import java.io.Reader;
import java.io.Writer;

/**
 * Template render tools.
 * @author Kahle
 */
public class TemplateUtils {
    private static Logger log = LoggerFactory.getLogger(TemplateUtils.class);
    private static TemplateEngine templateEngine;

    public static TemplateEngine getTemplateEngine() {
        if (templateEngine != null) { return templateEngine; }
        synchronized (TemplateUtils.class) {
            if (templateEngine != null) { return templateEngine; }
            TemplateUtils.setTemplateEngine(new SimpleTemplateEngine());
            return templateEngine;
        }
    }

    public static void setTemplateEngine(TemplateEngine templateEngine) {
        Assert.notNull(templateEngine, "Parameter \"templateEngine\" must not null. ");
        log.info("Set template engine: {}", templateEngine.getClass().getName());
        TemplateUtils.templateEngine = templateEngine;
    }

    public static void render(Object data, Writer output, String logTag, String template) {

        getTemplateEngine().render(data, output, logTag, template);
    }

    public static void render(Object data, Writer output, String logTag, Reader reader) {

        getTemplateEngine().render(data, output, logTag, reader);
    }

    public static void render(Object data, Writer output, String logTag, InputStream inputStream, String encoding) {

        getTemplateEngine().render(data, output, logTag, inputStream, encoding);
    }

    public static void render(String name, String encoding, Object data, Writer output) {

        getTemplateEngine().render(name, encoding, data, output);
    }

    public static String renderToString(Object data, String logTag, String template) {

        return getTemplateEngine().renderToString(data, logTag, template);
    }

    public static String renderToString(Object data, String logTag, Reader reader) {

        return getTemplateEngine().renderToString(data, logTag, reader);
    }

    public static String renderToString(Object data, String logTag, InputStream inputStream, String encoding) {

        return getTemplateEngine().renderToString(data, logTag, inputStream, encoding);
    }

    public static String renderToString(String name, String encoding, Object data) {

        return getTemplateEngine().renderToString(name, encoding, data);
    }

}
