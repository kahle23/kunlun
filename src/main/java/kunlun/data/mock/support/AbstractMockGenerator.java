/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.data.mock.support;

import kunlun.data.mock.MockGenerator;
import kunlun.util.Assert;

import java.util.Collections;
import java.util.Map;

/**
 * The abstract data mock generator.
 * @author Kahle
 */
public abstract class AbstractMockGenerator implements MockGenerator {
    private Map<Object, Object> commonProperties = Collections.emptyMap();

    public Map<Object, Object> getCommonProperties() {

        return commonProperties;
    }

    public void setCommonProperties(Map<?, ?> properties) {
        Assert.notNull(properties, "Parameter \"properties\" must not null. ");
        this.commonProperties = Collections.unmodifiableMap(properties);
    }

}
