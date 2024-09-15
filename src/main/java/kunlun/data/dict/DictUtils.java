/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.data.dict;

import kunlun.data.dict.support.SimpleDictService;
import kunlun.logging.Logger;
import kunlun.logging.LoggerFactory;
import kunlun.util.Assert;

import java.util.Collection;

import static kunlun.data.dict.DictService.DictQuery;

/**
 * The data dictionary tools.
 * @author Kahle
 */
public class DictUtils {
    private static final Logger log = LoggerFactory.getLogger(DictUtils.class);
    private static volatile DictService dictService;

    public static DictService getDictService() {
        if (dictService != null) { return dictService; }
        synchronized (DictUtils.class) {
            if (dictService != null) { return dictService; }
            DictUtils.setDictService(new SimpleDictService());
            return dictService;
        }
    }

    public static void setDictService(DictService dictService) {
        Assert.notNull(dictService, "Parameter \"dictProvider\" must not null. ");
        log.info("Set dict service: {}", dictService.getClass().getName());
        DictUtils.dictService = dictService;
    }

    public static void sync(Object strategy, Object data) {

        getDictService().sync(strategy, data);
    }

    public static Dict getByName(String group, String name) {

        return getDictService().getByName(group, name);
    }

    public static Dict getByCode(String group, String code) {

        return getDictService().getByCode(group, code);
    }

    public static Dict getByValue(String group, String value) {

        return getDictService().getByValue(group, value);
    }

    public static Dict getByCondition(DictQuery condition) {

        return getDictService().getByCondition(condition);
    }

    public static Collection<Dict> listByGroup(String group) {

        return getDictService().listByGroup(group);
    }

    public static Collection<Dict> listByCondition(DictQuery condition) {

        return getDictService().listByCondition(condition);
    }

}
