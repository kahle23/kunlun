/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.data.dict.support;

import kunlun.common.Page;
import kunlun.data.dict.AbstractDictService;
import kunlun.data.dict.Dict;
import kunlun.data.dict.DictQuery;

/**
 * The simple data dictionary service.
 * @author Kahle
 */
public class SimpleDictService extends AbstractDictService {

    @Override
    public Page<Dict> listByCondition(boolean paged, DictQuery condition) {

        throw new UnsupportedOperationException();
    }

}
