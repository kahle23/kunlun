package artoria.data.json.support;

import artoria.data.json.JsonHandler;
import artoria.util.Assert;
import artoria.util.StringUtils;

import java.util.Collections;
import java.util.Map;

import static artoria.common.constant.Numbers.ONE;
import static artoria.common.constant.Numbers.ZERO;

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
