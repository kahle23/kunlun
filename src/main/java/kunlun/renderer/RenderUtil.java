/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.renderer;

import kunlun.core.Renderer;
import kunlun.core.function.Consumer;
import kunlun.logging.Logger;
import kunlun.logging.LoggerFactory;
import kunlun.renderer.support.SimpleTextRenderer;
import kunlun.util.Assert;

import static kunlun.core.Renderer.Tpl;

/**
 * The render tools.
 * @author Kahle
 */
public class RenderUtil {
    private static final Logger log = LoggerFactory.getLogger(RenderUtil.class);
    private static volatile RendererProvider rendererProvider;

    public static RendererProvider getRendererProvider() {
        if (rendererProvider != null) { return rendererProvider; }
        synchronized (RenderUtil.class) {
            if (rendererProvider != null) { return rendererProvider; }
            RenderUtil.setRendererProvider(new SimpleRendererProvider());
            // Register the default renderer.
            registerRenderer(getDefaultRendererName(), new SimpleTextRenderer());
            return rendererProvider;
        }
    }

    public static void setRendererProvider(RendererProvider rendererProvider) {
        Assert.notNull(rendererProvider, "Parameter \"rendererProvider\" must not null. ");
        log.debug("Set renderer provider: {}", rendererProvider.getClass().getName());
        RenderUtil.rendererProvider = rendererProvider;
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

    public static Consumer<Tpl> getTemplateLoader(String rendererName) {

        return getRenderer(rendererName).getTemplateLoader();
    }

    public static void setTemplateLoader(String rendererName, Consumer<Tpl> loader) {

        getRenderer(rendererName).setTemplateLoader(loader);
    }

    public static void render(String renderer, Object template, Object data, Object output) {

        getRendererProvider().render(renderer, template, data, output);
    }

    public static void render(Object template, Object data, Object output) {

        getRendererProvider().render(getDefaultRendererName(), template, data, output);
    }

    public static byte[] renderToBytes(String renderer, Object template, Object data) {

        return getRendererProvider().renderToBytes(renderer, template, data);
    }

    public static byte[] renderToBytes(Object template, Object data) {

        return getRendererProvider().renderToBytes(getDefaultRendererName(), template, data);
    }

    public static String renderToString(String renderer, Object template, Object data) {

        return getRendererProvider().renderToString(renderer, template, data);
    }

    public static String renderToString(Object template, Object data) {

        return getRendererProvider().renderToString(getDefaultRendererName(), template, data);
    }

}
