package artoria.render;

import artoria.util.Assert;

import java.io.Reader;
import java.io.Writer;
import java.util.logging.Logger;

/**
 * String template render tools.
 * @author Kahle
 */
public class StringRenderUtils {
    private static final StringRenderer DEFAULT_STRING_RENDERER = new SimpleStringRenderer();
    private static Logger log = Logger.getLogger(StringRenderUtils.class.getName());
    private static StringRenderer stringRenderer;

    public static StringRenderer getStringRenderer() {
        return stringRenderer != null
                ? stringRenderer : DEFAULT_STRING_RENDERER;
    }

    public static void setStringRenderer(StringRenderer stringRenderer) {
        Assert.notNull(stringRenderer, "Parameter \"stringRenderer\" must not null. ");
        log.info("Set string template renderer: " + stringRenderer.getClass().getName());
        StringRenderUtils.stringRenderer = stringRenderer;
    }

    public static void render(String name, Object data, Writer writer) throws Exception {

        getStringRenderer().render(name, data, writer);
    }

    public static void render(String name, String encoding, Object data, Writer writer) throws Exception {

        getStringRenderer().render(name, encoding, data, writer);
    }

    public static void render(Object data, Writer writer, String logTag, String template) throws Exception {

        getStringRenderer().render(data, writer, logTag, template);
    }

    public static void render(Object data, Writer writer, String logTag, Reader reader) throws Exception {

        getStringRenderer().render(data, writer, logTag, reader);
    }

    public static String renderToString(String name, Object data) throws Exception {

        return getStringRenderer().renderToString(name, data);
    }

    public static String renderToString(String name, String encoding, Object data) throws Exception {

        return getStringRenderer().renderToString(name, encoding, data);
    }

    public static String renderToString(Object data, String logTag, String template) throws Exception {

        return getStringRenderer().renderToString(data, logTag, template);
    }

    public static String renderToString(Object data, String logTag, Reader reader) throws Exception {

        return getStringRenderer().renderToString(data, logTag, reader);
    }

}
