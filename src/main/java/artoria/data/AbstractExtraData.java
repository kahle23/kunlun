package artoria.data;

import artoria.beans.BeanUtils;
import artoria.util.MapUtils;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static artoria.common.Constants.TWENTY;

/**
 * Abstract extra data.
 * @author Kahle
 */
public abstract class AbstractExtraData implements ExtraData, Mappable, Beanable, Serializable {
    private Map<String, Object> extraData = new HashMap<String, Object>();

    @Override
    public Object get(String name) {

        return extraData.get(name);
    }

    @Override
    public Object put(String name, Object value) {

        return extraData.put(name, value);
    }

    @Override
    public Object remove(String name) {

        return extraData.remove(name);
    }

    @Override
    public void putAll(Map<String, Object> data) {

        extraData.putAll(data);
    }

    @Override
    public void clear() {

        extraData.clear();
    }

    @Override
    public Map<String, Object> extraData() {

        return Collections.unmodifiableMap(extraData);
    }

    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> resultMap = new HashMap<String, Object>(TWENTY);
        resultMap.putAll(extraData());
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

    @Override
    public <T> T toBean(Class<T> clazz) {
        Map<String, Object> objectMap = toMap();
        return BeanUtils.mapToBean(objectMap, clazz);
    }

    @Override
    public <T> void fromBean(T bean) {
        Map<String, Object> objectMap = BeanUtils.beanToMap(bean);
        fromMap(objectMap);
    }

}
