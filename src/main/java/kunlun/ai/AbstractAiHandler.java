/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.ai;

import kunlun.core.ArtificialIntelligence;
import kunlun.core.Handler;
import kunlun.util.Assert;

import java.util.Collections;
import java.util.Map;

/**
 * The abstract AI handler.
 * @author Kahle
 */
public abstract class AbstractAiHandler implements ArtificialIntelligence, Handler {
    private Map<Object, Object> commonProperties = Collections.emptyMap();

    public Map<Object, Object> getCommonProperties() {

        return commonProperties;
    }

    public void setCommonProperties(Map<?, ?> properties) {
        Assert.notNull(properties, "Parameter \"properties\" must not null. ");
        this.commonProperties = Collections.unmodifiableMap(properties);
    }

}
