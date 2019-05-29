package artoria.common;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Abstract raw data.
 * @author Kahle
 */
public abstract class AbstractRawData implements RawData, Serializable {
    private Map<String, Object> rawData = new HashMap<String, Object>();

    @Override
    public Object get(String name) {

        return rawData.get(name);
    }

    @Override
    public Object put(String name, Object value) {

        return rawData.put(name, value);
    }

    @Override
    public Object remove(String name) {

        return rawData.remove(name);
    }

    @Override
    public void putAll(Map<String, Object> data) {

        rawData.putAll(data);
    }

    @Override
    public void clear() {

        rawData.clear();
    }

    @Override
    public Map<String, Object> rawData() {

        return Collections.unmodifiableMap(rawData);
    }

}
