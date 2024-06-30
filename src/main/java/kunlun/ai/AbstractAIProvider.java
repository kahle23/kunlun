/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.ai;

import kunlun.core.ArtificialIntelligence;
import kunlun.logging.Logger;
import kunlun.logging.LoggerFactory;
import kunlun.util.Assert;
import kunlun.util.MapUtils;
import kunlun.util.ObjectUtils;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The abstract artificial intelligence provider.
 * @author Kahle
 */
public abstract class AbstractAIProvider implements AIProvider {
    private static final Logger log = LoggerFactory.getLogger(AbstractAIProvider.class);
    protected final Map<String, ArtificialIntelligence> aiHandlers;
    protected final Map<String, Object> commonProperties;

    protected AbstractAIProvider(Map<String, Object> commonProperties,
                                 Map<String, ArtificialIntelligence> aiHandlers) {
        Assert.notNull(commonProperties, "Parameter \"commonProperties\" must not null. ");
        Assert.notNull(aiHandlers, "Parameter \"aiHandlers\" must not null. ");
        this.commonProperties = commonProperties;
        this.aiHandlers = aiHandlers;
    }

    public AbstractAIProvider() {
        this(new ConcurrentHashMap<String, Object>(),
                new ConcurrentHashMap<String, ArtificialIntelligence>());
    }

    protected ArtificialIntelligence getHandlerInner(String handlerName) {
        ArtificialIntelligence aiHandler = getHandler(handlerName);
        Assert.notNull(aiHandler, "The corresponding AI handler could not be found by name. ");
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
        if (aiHandler instanceof AbstractAIHandler) {
            ((AbstractAIHandler) aiHandler).setCommonProperties(getCommonProperties());
        }
        log.info("Register the AI handler \"{}\" to \"{}\". ", className, handlerName);
    }

    @Override
    public void deregisterHandler(String handlerName) {
        Assert.notBlank(handlerName, "Parameter \"handlerName\" must not blank. ");
        ArtificialIntelligence remove = aiHandlers.remove(handlerName);
        if (remove != null) {
            String className = remove.getClass().getName();
            log.info("Deregister the AI handler \"{}\" from \"{}\". ", className, handlerName);
        }
    }

    @Override
    public ArtificialIntelligence getHandler(String handlerName) {
        Assert.notBlank(handlerName, "Parameter \"handlerName\" must not blank. ");
        return aiHandlers.get(handlerName);
    }

    @Override
    public Object execute(String handlerName, Object[] arguments) {
        // Parameter "arguments" is usually: 0 strategy or operation, 1 input, 2 type
        return getHandlerInner(handlerName).execute(arguments);
    }

    @Override
    public <T> T execute(String handlerName, Object input, String operation, Type type) {

        return ObjectUtils.cast(execute(handlerName, new Object[] { operation, input, type }));
    }

}
