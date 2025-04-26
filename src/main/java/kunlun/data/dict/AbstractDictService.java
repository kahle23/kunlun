/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.data.dict;

import kunlun.common.Page;
import kunlun.common.constant.Symbols;
import kunlun.util.Assert;
import kunlun.util.CollUtil;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static java.lang.Boolean.FALSE;
import static kunlun.util.Assert.notNull;

/**
 * The abstract data dictionary service.
 * @author Kahle
 */
public abstract class AbstractDictService implements DictService {
    private String defaultNamespace = Symbols.EMPTY_STRING;

    @Override
    public String getDefaultNamespace() {

        return defaultNamespace;
    }

    @Override
    public void setDefaultNamespace(String defaultNamespace) {

        this.defaultNamespace = Assert.notBlank(defaultNamespace);
    }

    @Override
    public void syncByGroup(Collection<Dict> data) {

        throw new UnsupportedOperationException();
    }

    @Override
    public void syncByCode(Collection<Dict> data) {

        throw new UnsupportedOperationException();
    }

    @Override
    public Dict getByName(String namespace, String groupCode, String name) {
        DictQuery condition = new DictQuery(namespace, notNull(groupCode));
        condition.setName(notNull(name));
        return getByCondition(condition);
    }

    @Override
    public Dict getByCode(String namespace, String groupCode, String code) {
        DictQuery condition = new DictQuery(namespace, notNull(groupCode));
        condition.setCode(notNull(code));
        return getByCondition(condition);
    }

    @Override
    public Dict getByValue(String namespace, String groupCode, String value) {
        DictQuery condition = new DictQuery(namespace, notNull(groupCode));
        condition.setValue(notNull(value));
        return getByCondition(condition);
    }

    @Override
    public Dict getByCondition(DictQuery condition) {
        Page<Dict> page = listByCondition(FALSE, notNull(condition));
        return CollUtil.getFirst(page.getData());
    }

    @Override
    public List<Dict> listByGroup(String namespace, String groupCode) {
        Page<Dict> page = listByCondition(FALSE, new DictQuery(namespace, notNull(groupCode)));
        return page.getData();
    }

    @Override
    public Map<String, Dict> mapByGroup(String namespace, String groupCode) {
        Map<String, Dict> result = new LinkedHashMap<String, Dict>();
        for (Dict dict : listByGroup(namespace, groupCode)) {
            if (dict == null) { continue; }
            result.put(dict.getValue(), dict);
        }
        return result;
    }

}
