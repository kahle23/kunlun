/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.renderer;

import kunlun.core.Renderer;
import kunlun.core.function.Consumer;
import kunlun.logging.Logger;
import kunlun.logging.LoggerFactory;
import kunlun.util.Assert;

import java.io.ByteArrayOutputStream;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static kunlun.core.Renderer.Tpl;

/**
 * The simple renderer provider.
 * @author Kahle
 */
public class SimpleRendererProvider implements RendererProvider {
    private static final Logger log = LoggerFactory.getLogger(SimpleRendererProvider.class);
    protected final Map<String, Renderer> renderers;
    private String defaultRendererName = "default";

    protected SimpleRendererProvider(Map<String, Renderer> renderers) {

        this.renderers = Assert.notNull(renderers);
    }

    public SimpleRendererProvider() {

        this(new ConcurrentHashMap<String, Renderer>());
    }

    @Override
    public String getDefaultRendererName() {

        return defaultRendererName;
    }

    @Override
    public void setDefaultRendererName(String defaultRendererName) {
        Assert.notBlank(defaultRendererName, "Parameter \"defaultRendererName\" must not blank. ");
        this.defaultRendererName = defaultRendererName;
    }

    @Override
    public void registerRenderer(String rendererName, Renderer renderer) {
        Assert.notBlank(rendererName, "Parameter \"rendererName\" must not blank. ");
        Assert.notNull(renderer, "Parameter \"renderer\" must not null. ");
        String className = renderer.getClass().getName();
        renderers.put(rendererName, renderer);
        log.debug("Register the renderer \"{}\" to \"{}\". ", className, rendererName);
    }

    @Override
    public void deregisterRenderer(String rendererName) {
        Assert.notBlank(rendererName, "Parameter \"rendererName\" must not blank. ");
        Renderer remove = renderers.remove(rendererName);
        if (remove != null) {
            String className = remove.getClass().getName();
            log.debug("Deregister the renderer \"{}\" from \"{}\". ", className, rendererName);
        }
    }

    @Override
    public Renderer getRenderer(String rendererName) {
        Assert.notBlank(rendererName, "Parameter \"rendererName\" must not blank. ");
        Renderer renderer = renderers.get(rendererName);
        Assert.notNull(renderer
                , "The corresponding renderer could not be found by name. ");
        return renderer;
    }

    @Override
    public Consumer<Tpl> getTemplateLoader(String rendererName) {

        return getRenderer(rendererName).getTemplateLoader();
    }

    @Override
    public void setTemplateLoader(String rendererName, Consumer<Tpl> loader) {

        getRenderer(rendererName).setTemplateLoader(loader);
    }

    @Override
    public void render(String rendererName, Object template, Object data, Object output) {

        getRenderer(rendererName).render(template, data, output);
    }

    @Override
    public byte[] renderToBytes(String rendererName, Object template, Object data) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        render(rendererName, template, data, output);
        return output.toByteArray();
    }

    @Override
    public String renderToString(String rendererName, Object template, Object data) {

        return ((TextRenderer) getRenderer(rendererName)).renderToString(template, data);
    }

}
