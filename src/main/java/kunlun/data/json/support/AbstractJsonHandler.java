/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.data.json.support;

import kunlun.data.json.JsonHandler;
import kunlun.util.Assert;
import kunlun.util.StringUtils;

import java.util.Collections;
import java.util.Map;

import static kunlun.common.constant.Numbers.ONE;
import static kunlun.common.constant.Numbers.ZERO;

/**
 * The abstract json conversion handler.
 * @author Kahle
 */
public abstract class AbstractJsonHandler implements JsonHandler {
    private Map<Object, Object> commonProperties = Collections.emptyMap();

    protected boolean isWrap(CharSequence sequence, char prefixChar, char suffixChar) {
        if (sequence == null) { return false; }
        int endIndex = sequence.length() - ONE;
        return sequence.charAt(ZERO) == prefixChar && sequence.charAt(endIndex) == suffixChar;
    }

    @Override
    public Map<Object, Object> getCommonProperties() {

        return commonProperties;
    }

    @Override
    public void setCommonProperties(Map<?, ?> properties) {
        Assert.notNull(properties, "Parameter \"properties\" must not null. ");
        this.commonProperties = Collections.unmodifiableMap(properties);
    }

    @Override
    public boolean isJsonObject(String jsonString) {
        if (StringUtils.isBlank(jsonString)) { return false; }
        return isWrap(jsonString.trim(), '{', '}');
    }

    @Override
    public boolean isJsonArray(String jsonString) {
        if (StringUtils.isBlank(jsonString)) { return false; }
        return isWrap(jsonString.trim(), '[', ']');
    }

}
