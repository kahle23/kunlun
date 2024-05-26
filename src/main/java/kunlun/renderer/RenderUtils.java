/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.renderer;

import kunlun.core.Renderer;
import kunlun.logging.Logger;
import kunlun.logging.LoggerFactory;
import kunlun.util.Assert;

/**
 * The render tools.
 * @author Kahle
 */
public class RenderUtils {
    private static final Logger log = LoggerFactory.getLogger(RenderUtils.class);
    private static volatile RendererProvider rendererProvider;

    public static RendererProvider getRendererProvider() {
        if (rendererProvider != null) { return rendererProvider; }
        synchronized (RenderUtils.class) {
            if (rendererProvider != null) { return rendererProvider; }
            RenderUtils.setRendererProvider(new SimpleRendererProvider());
            return rendererProvider;
        }
    }

    public static void setRendererProvider(RendererProvider rendererProvider) {
        Assert.notNull(rendererProvider, "Parameter \"rendererProvider\" must not null. ");
        log.info("Set renderer provider: {}", rendererProvider.getClass().getName());
        RenderUtils.rendererProvider = rendererProvider;
    }

    public static String getDefaultRendererName() {

        return getRendererProvider().getDefaultRendererName();
    }

    public static void setDefaultRendererName(String defaultRendererName) {

        getRendererProvider().setDefaultRendererName(defaultRendererName);
    }

    public static void registerRenderer(String rendererName, Renderer renderer) {

        getRendererProvider().registerRenderer(rendererName, renderer);
    }

    public static void deregisterRenderer(String rendererName) {

        getRendererProvider().deregisterRenderer(rendererName);
    }

    public static Renderer getRenderer(String rendererName) {

        return getRendererProvider().getRenderer(rendererName);
    }

    public static void render(String renderer, Object template, String name, Object data, Object output) {

        getRendererProvider().render(renderer, template, name, data, output);
    }

    public static void render(String renderer, Object template, Object data, Object output) {

        getRendererProvider().render(renderer, template, null, data, output);
    }

    public static void render(Object template, Object data, Object output) {

        getRendererProvider().render(getDefaultRendererName(), template, null, data, output);
    }

    public static byte[] renderToBytes(String renderer, Object template, String name, Object data) {

        return getRendererProvider().renderToBytes(renderer, template, name, data);
    }

    public static byte[] renderToBytes(String renderer, Object template, Object data) {

        return getRendererProvider().renderToBytes(renderer, template, null, data);
    }

    public static byte[] renderToBytes(Object template, Object data) {

        return getRendererProvider().renderToBytes(getDefaultRendererName(), template, null, data);
    }

    public static String renderToString(String renderer, Object template, String name, Object data) {

        return getRendererProvider().renderToString(renderer, template, name, data);
    }

    public static String renderToString(String renderer, Object template, Object data) {

        return getRendererProvider().renderToString(renderer, template, null, data);
    }

    public static String renderToString(Object template, Object data) {

        return getRendererProvider().renderToString(getDefaultRendererName(), template, null, data);
    }

}
