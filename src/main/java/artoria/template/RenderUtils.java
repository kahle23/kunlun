package artoria.template;

import artoria.util.Assert;

import java.io.Reader;
import java.io.Writer;
import java.util.logging.Logger;

/**
 * Template render tools.
 * @author Kahle
 */
public class RenderUtils {
    private static Logger log = Logger.getLogger(RenderUtils.class.getName());
    private static Renderer renderer;

    static {
        RenderUtils.setRenderer(new JdkRenderer());
    }

    public static Renderer getRenderer() {
        return renderer;
    }

    public static void setRenderer(Renderer renderer) {
        Assert.notNull(renderer, "Parameter \"renderer\" must not null. ");
        RenderUtils.renderer = renderer;
        log.info("Set template renderer: " + renderer.getClass().getName());
    }

    public static void render(String name, Object data, Writer writer) throws Exception {
        renderer.render(name, data, writer);
    }

    public static void render(String name, String encoding, Object data, Writer writer) throws Exception {
        renderer.render(name, encoding, data, writer);
    }

    public static void render(Object data, Writer writer, String logTag, String template) throws Exception {
        renderer.render(data, writer, logTag, template);
    }

    public static void render(Object data, Writer writer, String logTag, Reader reader) throws Exception {
        renderer.render(data, writer, logTag, reader);
    }

    public static String renderToString(String name, Object data) throws Exception {
        return renderer.renderToString(name, data);
    }

    public static String renderToString(String name, String encoding, Object data) throws Exception {
        return renderer.renderToString(name, encoding, data);
    }

    public static String renderToString(Object data, String logTag, String template) throws Exception {
        return renderer.renderToString(data, logTag, template);
    }

    public static String renderToString(Object data, String logTag, Reader reader) throws Exception {
        return renderer.renderToString(data, logTag, reader);
    }

}
