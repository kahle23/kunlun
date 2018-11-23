package artoria.template;

import artoria.io.StringBuilderWriter;
import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import artoria.util.Assert;

/**
 * Template render tools.
 * @author Kahle
 */
public class RenderUtils {
    private static final Renderer DEFAULT_STRING_RENDERER = new SimpleRenderer();
    private static Logger log = LoggerFactory.getLogger(RenderUtils.class);
    private static Renderer renderer;

    public static Renderer getRenderer() {
        return renderer != null
                ? renderer : DEFAULT_STRING_RENDERER;
    }

    public static void setRenderer(Renderer renderer) {
        Assert.notNull(renderer, "Parameter \"renderer\" must not null. ");
        log.info("Set template renderer: " + renderer.getClass().getName());
        RenderUtils.renderer = renderer;
    }

    public static void render(Object data, Object output, String name) throws Exception {

        getRenderer().render(data, output, name, null, null);
    }

    public static void render(Object data, Object output, String name, Object input) throws Exception {

        getRenderer().render(data, output, name, input, null);
    }

    public static void render(Object data, Object output, String name, Object input, String charsetName) throws Exception {

        getRenderer().render(data, output, name, input, charsetName);
    }

    public static String renderToString(Object data, String name) throws Exception {
        StringBuilderWriter output = new StringBuilderWriter();
        getRenderer().render(data, output, name, null, null);
        return output.toString();
    }

    public static String renderToString(Object data, String name, Object input) throws Exception {
        StringBuilderWriter output = new StringBuilderWriter();
        getRenderer().render(data, output, name, input, null);
        return output.toString();
    }

    public static String renderToString(Object data, String name, Object input, String charsetName) throws Exception {
        StringBuilderWriter output = new StringBuilderWriter();
        getRenderer().render(data, output, name, input, charsetName);
        return output.toString();
    }

}
