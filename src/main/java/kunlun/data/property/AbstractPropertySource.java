/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.data.property;

import kunlun.logging.Logger;
import kunlun.logging.LoggerFactory;
import kunlun.util.CollUtil;
import kunlun.util.MapUtil;
import kunlun.util.StrUtil;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * The abstract property source.
 * @author Kahle
 */
public abstract class AbstractPropertySource implements PropertySource {
    private static final Logger log = LoggerFactory.getLogger(AbstractPropertySource.class);

    @Override
    public boolean containsProperty(String name) {

        return getProperty(name) != null;
    }

    @Override
    public Map<String, Object> getProperties() {
        Set<String> names = getPropertyNames();
        if (CollUtil.isEmpty(names)) { return Collections.emptyMap(); }
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        for (String name : names) {
            if (StrUtil.isBlank(name)) { continue; }
            map.put(name, getProperty(name));
        }
        return Collections.unmodifiableMap(map);
    }

    @Override
    public void setProperties(Map<?, ?> properties) {
        if (MapUtil.isEmpty(properties)) { return; }
        for (Map.Entry<?, ?> entry : properties.entrySet()) {
            setProperty(String.valueOf(entry.getKey()), entry.getValue());
        }
    }

}
