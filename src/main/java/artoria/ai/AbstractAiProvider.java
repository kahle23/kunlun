package artoria.ai;

import artoria.core.ArtificialIntelligence;
import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import artoria.util.Assert;
import artoria.util.MapUtils;
import artoria.util.ObjectUtils;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The abstract artificial intelligence provider.
 * @author Kahle
 */
public abstract class AbstractAiProvider implements AiProvider {
    private static final Logger log = LoggerFactory.getLogger(AbstractAiProvider.class);
    protected final Map<String, ArtificialIntelligence> aiHandlers;
    protected final Map<String, Object> commonProperties;

    protected AbstractAiProvider(Map<String, Object> commonProperties,
                                 Map<String, ArtificialIntelligence> aiHandlers) {
        Assert.notNull(commonProperties, "Parameter \"commonProperties\" must not null. ");
        Assert.notNull(aiHandlers, "Parameter \"aiHandlers\" must not null. ");
        this.commonProperties = commonProperties;
        this.aiHandlers = aiHandlers;
    }

    public AbstractAiProvider() {
        this(new ConcurrentHashMap<String, Object>(),
                new ConcurrentHashMap<String, ArtificialIntelligence>());
    }

    protected ArtificialIntelligence getHandlerInner(String handlerName) {
        ArtificialIntelligence aiHandler = getHandler(handlerName);
        Assert.notNull(aiHandler, "The corresponding ai handler could not be found by name. ");
        return aiHandler;
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
    public void registerHandler(String handlerName, ArtificialIntelligence aiHandler) {
        Assert.notBlank(handlerName, "Parameter \"handlerName\" must not blank. ");
        Assert.notNull(aiHandler, "Parameter \"aiHandler\" must not null. ");
        String className = aiHandler.getClass().getName();
        aiHandlers.put(handlerName, aiHandler);
        if (aiHandler instanceof AbstractAiHandler) {
            ((AbstractAiHandler) aiHandler).setCommonProperties(getCommonProperties());
        }
        log.info("Register the ai handler \"{}\" to \"{}\". ", className, handlerName);
    }

    @Override
    public void deregisterHandler(String handlerName) {
        Assert.notBlank(handlerName, "Parameter \"handlerName\" must not blank. ");
        ArtificialIntelligence remove = aiHandlers.remove(handlerName);
        if (remove != null) {
            String className = remove.getClass().getName();
            log.info("Deregister the ai handler \"{}\" from \"{}\". ", className, handlerName);
        }
    }

    @Override
    public ArtificialIntelligence getHandler(String handlerName) {
        Assert.notBlank(handlerName, "Parameter \"handlerName\" must not blank. ");
        return aiHandlers.get(handlerName);
    }

    @Override
    public Object execute(String handlerName, Object[] arguments) {
        // Parameter "arguments" usually is: 0 strategy or scene, 1 input, 2 type
        return getHandlerInner(handlerName).execute(arguments);
    }

    @Override
    public <T> T execute(String handlerName, Object input, String operation, Type type) {

        return ObjectUtils.cast(execute(handlerName, new Object[] {operation, input, type }));
    }

}
