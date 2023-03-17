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
 * The abstract ai engine provider.
 * @author Kahle
 */
public abstract class AbstractAiProvider implements AiProvider {
    private static final Logger log = LoggerFactory.getLogger(AbstractAiProvider.class);
    protected final Map<String, ArtificialIntelligence> aiEngines;
    protected final Map<String, Object> commonProperties;

    protected AbstractAiProvider(Map<String, Object> commonProperties,
                                 Map<String, ArtificialIntelligence> aiEngines) {
        Assert.notNull(commonProperties, "Parameter \"commonProperties\" must not null. ");
        Assert.notNull(aiEngines, "Parameter \"aiEngines\" must not null. ");
        this.commonProperties = commonProperties;
        this.aiEngines = aiEngines;
    }

    public AbstractAiProvider() {
        this(new ConcurrentHashMap<String, Object>(),
                new ConcurrentHashMap<String, ArtificialIntelligence>());
    }

    protected ArtificialIntelligence getEngineInner(String engineName) {
        ArtificialIntelligence aiEngine = getEngine(engineName);
        Assert.notNull(aiEngine
                , "The corresponding ai engine could not be found by name. ");
        return aiEngine;
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
    public void registerEngine(String engineName, ArtificialIntelligence aiEngine) {
        Assert.notBlank(engineName, "Parameter \"engineName\" must not blank. ");
        Assert.notNull(aiEngine, "Parameter \"aiEngine\" must not null. ");
        String className = aiEngine.getClass().getName();
        aiEngines.put(engineName, aiEngine);
        aiEngine.setCommonProperties(getCommonProperties());
        log.info("Register the ai engine \"{}\" to \"{}\". ", className, engineName);
    }

    @Override
    public void deregisterEngine(String engineName) {
        Assert.notBlank(engineName, "Parameter \"engineName\" must not blank. ");
        ArtificialIntelligence remove = aiEngines.remove(engineName);
        if (remove != null) {
            String className = remove.getClass().getName();
            log.info("Deregister the ai engine \"{}\" from \"{}\". ", className, engineName);
        }
    }

    @Override
    public ArtificialIntelligence getEngine(String engineName) {
        Assert.notBlank(engineName, "Parameter \"engineName\" must not blank. ");
        return aiEngines.get(engineName);
    }

    @Override
    public Object execute(String engineName, Object[] arguments) {
        // Parameter "arguments" usually is: 0 strategy or scene, 1 input, 2 type
        return getEngineInner(engineName).execute(arguments);
    }

    @Override
    public <T> T execute(Object input, String engineName, String strategy, Type type) {

        return ObjectUtils.cast(execute(engineName, new Object[] { strategy, input, type }));
    }

}
