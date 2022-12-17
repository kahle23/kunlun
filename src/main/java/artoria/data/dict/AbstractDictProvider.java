package artoria.data.dict;

import artoria.util.Assert;
import artoria.util.MapUtils;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * The abstract data dictionary provider.
 * @author Kahle
 */
public abstract class AbstractDictProvider implements DictProvider {
    protected final Map<String, Object> commonProperties;

    protected AbstractDictProvider(Map<String, Object> commonProperties) {
        Assert.notNull(commonProperties, "Parameter \"commonProperties\" must not null. ");
        this.commonProperties = commonProperties;
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
    public void sync(Object strategy, Object data) {

        throw new UnsupportedOperationException();
    }

    @Override
    public Dict findOne(Object dictQuery) {

        throw new UnsupportedOperationException();
    }

    @Override
    public <T> List<T> findMultiple(Object dictQuery, Type type) {

        throw new UnsupportedOperationException();
    }

}
