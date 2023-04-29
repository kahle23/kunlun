package artoria.renderer;

import artoria.core.Renderer;
import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import artoria.util.Assert;
import artoria.util.MapUtils;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The simple renderer provider.
 * @author Kahle
 */
public class SimpleRendererProvider implements RendererProvider {
    private static final Logger log = LoggerFactory.getLogger(SimpleRendererProvider.class);
    protected final Map<String, Renderer> renderers;
    protected final Map<String, Object> commonProperties;

    protected SimpleRendererProvider(Map<String, Object> commonProperties,
                                     Map<String, Renderer> renderers) {
        Assert.notNull(commonProperties, "Parameter \"commonProperties\" must not null. ");
        Assert.notNull(renderers, "Parameter \"renderers\" must not null. ");
        this.commonProperties = commonProperties;
        this.renderers = renderers;
    }

    public SimpleRendererProvider() {
        this(new ConcurrentHashMap<String, Object>(),
                new ConcurrentHashMap<String, Renderer>());
    }

    @Override
    public void registerCommonProperties(Map<?, ?> commonProperties) {
        if (MapUtils.isEmpty(commonProperties)) { return; }
        for (Map.Entry<?, ?> entry : commonProperties.entrySet()) {
            String keyStr = String.valueOf(entry.getKey());
            this.commonProperties.put(keyStr, entry.getValue());
        }
    }

    @Override
    public void clearCommonProperties() {

        this.commonProperties.clear();
    }

    @Override
    public Map<String, Object> getCommonProperties() {

        return Collections.unmodifiableMap(commonProperties);
    }

    @Override
    public void registerRenderer(String rendererName, Renderer renderer) {
        Assert.notBlank(rendererName, "Parameter \"rendererName\" must not blank. ");
        Assert.notNull(renderer, "Parameter \"renderer\" must not null. ");
        String className = renderer.getClass().getName();
        if (renderer instanceof AbstractRenderer) {
            ((AbstractRenderer) renderer).setCommonProperties(getCommonProperties());
        }
        renderers.put(rendererName, renderer);
        log.info("Register the renderer \"{}\" to \"{}\". ", className, rendererName);
    }

    @Override
    public void deregisterRenderer(String rendererName) {
        Assert.notBlank(rendererName, "Parameter \"rendererName\" must not blank. ");
        Renderer remove = renderers.remove(rendererName);
        if (remove != null) {
            String className = remove.getClass().getName();
            log.info("Deregister the renderer \"{}\" from \"{}\". ", className, rendererName);
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
    public void render(String rendererName, Object template, String name, Object data, Object output) {

        getRenderer(rendererName).render(template, name, data, output);
    }

    @Override
    public byte[] renderToBytes(String rendererName, Object template, String name, Object data) {

        throw new UnsupportedOperationException();
    }

    @Override
    public String renderToString(String rendererName, Object template, String name, Object data) {

        return ((TextRenderer) getRenderer(rendererName)).renderToString(template, name, data);
    }

}
