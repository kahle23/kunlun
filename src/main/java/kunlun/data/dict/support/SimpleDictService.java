/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.data.dict.support;

import kunlun.data.dict.AbstractDictService;
import kunlun.data.dict.Dict;

import java.lang.reflect.Type;
import java.util.Collection;

/**
 * The simple data dictionary provider.
 * @author Kahle
 */
public class SimpleDictService extends AbstractDictService {

    @Override
    protected Dict getDict(String group, String name, String code, String value) {

        throw new UnsupportedOperationException();
    }

    @Override
    public Collection<Dict> listByGroup(String group) {

        throw new UnsupportedOperationException();
    }

    @Override
    public Collection<Dict> listByCondition(DictQuery dictQuery, Type type) {

        throw new UnsupportedOperationException();
    }

}
