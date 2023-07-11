package artoria.data;

import artoria.data.bean.BeanUtils;
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
@Deprecated // TODO: 2023/3/7 Deletable
public abstract class AbstractExtraData implements ExtraData, RawData, Serializable {
    private Map<String, Object> extraData = new HashMap<String, Object>();
    private Object rawData;

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
    public Object rawData() {

        return rawData;
    }

    @Override
    public void rawData(Object rawData) {

        this.rawData = rawData;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> resultMap = new HashMap<String, Object>(TWENTY);
        resultMap.putAll(extraData());
        Map<String, Object> beanToMap = BeanUtils.beanToMap(this);
        resultMap.putAll(beanToMap);
        return resultMap;
    }

    public void fromMap(Map<String, Object> map) {
        if (MapUtils.isEmpty(map)) { return; }
        BeanUtils.copy(map, this);
        putAll(map);
    }

}
