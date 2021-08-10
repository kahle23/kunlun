package artoria.option;

import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import artoria.util.Assert;
import artoria.util.CollectionUtils;
import artoria.util.StringUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import static artoria.common.Constants.EMPTY_STRING;

/**
 * Simple option provider.
 * @author Kahle
 */
public class SimpleOptionProvider extends AbstractOptionProvider {
    private static Logger log = LoggerFactory.getLogger(SimpleOptionProvider.class);
    private Map<String, List<String>> ownerNameListMap;
    private Map<String, Object> nameValueMap;

    public SimpleOptionProvider() {
        this.ownerNameListMap = new ConcurrentHashMap<String, List<String>>();
        this.nameValueMap = new ConcurrentHashMap<String, Object>();
    }

    @Override
    public boolean containsOption(String owner, String name) {
        Assert.notBlank(name, "Parameter \"name\" must not blank. ");
        if (StringUtils.isBlank(owner)) { owner = EMPTY_STRING; }
        return nameValueMap.containsKey(owner + name);
    }

    @Override
    public Map<String, Object> getOptions(String owner) {
        if (StringUtils.isBlank(owner)) { owner = EMPTY_STRING; }
        List<String> list = ownerNameListMap.get(owner);
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyMap();
        }
        Map<String, Object> result = new HashMap<String, Object>(list.size());
        for (String name : list) {
            result.put(name, nameValueMap.get(owner + name));
        }
        return Collections.unmodifiableMap(result);
    }

    @Override
    public Object getOption(String owner, String name, Object defaultValue) {
        Assert.notBlank(name, "Parameter \"name\" must not blank. ");
        if (StringUtils.isBlank(owner)) { owner = EMPTY_STRING; }
        return nameValueMap.get(owner + name);
    }

    @Override
    public Object setOption(String owner, String name, Object value) {
        Assert.notBlank(name, "Parameter \"name\" must not blank. ");
        Assert.notNull(value, "Parameter \"value\" must not null. ");
        if (StringUtils.isBlank(owner)) { owner = EMPTY_STRING; }
        List<String> list = ownerNameListMap.get(owner);
        if (list == null) {
            list = new CopyOnWriteArrayList<String>();
            ownerNameListMap.put(owner, list);
        }
        list.add(name);
        return nameValueMap.put(owner + name, value);
    }

    @Override
    public Object removeOption(String owner, String name) {
        Assert.notBlank(name, "Parameter \"name\" must not blank. ");
        if (StringUtils.isBlank(owner)) { owner = EMPTY_STRING; }
        List<String> list = ownerNameListMap.get(owner);
        if (list != null) { list.remove(name); }
        return nameValueMap.remove(owner + name);
    }

}
