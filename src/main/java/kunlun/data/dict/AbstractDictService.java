/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.data.dict;

import kunlun.common.Page;
import kunlun.common.constant.Symbols;
import kunlun.util.CollUtil;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static java.lang.Boolean.FALSE;
import static kunlun.util.Assert.notBlank;
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

        this.defaultNamespace = notBlank(defaultNamespace);
    }

    @Override
    public void syncByGroup(Collection<DataDict> data) {

        throw new UnsupportedOperationException();
    }

    @Override
    public void syncByCode(Collection<DataDict> data) {

        throw new UnsupportedOperationException();
    }

    @Override
    public DataDict getByName(String namespace, String groupCode, String name) {
        DictQuery condition = new DictQuery(namespace, notNull(groupCode));
        condition.setName(notNull(name));
        return getByCondition(condition);
    }

    @Override
    public DataDict getByCode(String namespace, String groupCode, String code) {
        DictQuery condition = new DictQuery(namespace, notNull(groupCode));
        condition.setCode(notNull(code));
        return getByCondition(condition);
    }

    @Override
    public DataDict getByValue(String namespace, String groupCode, String value) {
        DictQuery condition = new DictQuery(namespace, notNull(groupCode));
        condition.setValue(notNull(value));
        return getByCondition(condition);
    }

    @Override
    public DataDict getByCondition(DictQuery condition) {
        Page<DataDict> page = listByCondition(FALSE, notNull(condition));
        return CollUtil.getFirst(page.getData());
    }

    @Override
    public List<DataDict> listByGroup(String namespace, String groupCode) {
        Page<DataDict> page = listByCondition(FALSE, new DictQuery(namespace, notNull(groupCode)));
        return page.getData();
    }

    @Override
    public Map<String, DataDict> mapByGroup(String namespace, String groupCode) {
        Map<String, DataDict> result = new LinkedHashMap<String, DataDict>();
        for (DataDict dataDict : listByGroup(namespace, groupCode)) {
            if (dataDict == null) { continue; }
            result.put(dataDict.getValue(), dataDict);
        }
        return result;
    }

}
