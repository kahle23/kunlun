package artoria.common;

import artoria.beans.BeanUtils;
import artoria.util.MapUtils;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static artoria.common.Constants.TWENTY;

/**
 * Abstract extended data.
 * @author Kahle
 */
public abstract class AbstractExtendedData implements ExtendedData, Mappable, Serializable {
    private Map<String, Object> extendedData = new HashMap<String, Object>();

    @Override
    public Object get(String name) {

        return extendedData.get(name);
    }

    @Override
    public Object put(String name, Object value) {

        return extendedData.put(name, value);
    }

    @Override
    public Object remove(String name) {

        return extendedData.remove(name);
    }

    @Override
    public void putAll(Map<String, Object> data) {

        extendedData.putAll(data);
    }

    @Override
    public void clear() {

        extendedData.clear();
    }

    @Override
    public Map<String, Object> extendedData() {

        return Collections.unmodifiableMap(extendedData);
    }

    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> resultMap = new HashMap<String, Object>(TWENTY);
        resultMap.putAll(extendedData());
        Map<String, Object> beanToMap = BeanUtils.beanToMap(this);
        resultMap.putAll(beanToMap);
        return resultMap;
    }

    @Override
    public void fromMap(Map<String, Object> map) {
        if (MapUtils.isEmpty(map)) { return; }
        BeanUtils.copy(map, this);
        putAll(map);
    }

}
