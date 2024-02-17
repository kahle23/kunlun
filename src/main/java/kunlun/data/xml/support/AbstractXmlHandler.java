/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.data.xml.support;

import kunlun.data.xml.XmlHandler;
import kunlun.util.Assert;

import java.util.Collections;
import java.util.Map;

/**
 * The abstract xml conversion handler.
 * @author Kahle
 */
public abstract class AbstractXmlHandler implements XmlHandler {
    private Map<Object, Object> commonProperties = Collections.emptyMap();

    @Override
    public Map<Object, Object> getCommonProperties() {

        return commonProperties;
    }

    @Override
    public void setCommonProperties(Map<?, ?> properties) {
        Assert.notNull(properties, "Parameter \"properties\" must not null. ");
        this.commonProperties = Collections.unmodifiableMap(properties);
    }

}
