/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.data.dict;

import kunlun.common.Page;
import kunlun.common.constant.Nil;
import kunlun.data.dict.support.SimpleDictService;
import kunlun.logging.Logger;
import kunlun.logging.LoggerFactory;
import kunlun.util.Assert;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * The data dictionary tools.
 * @author Kahle
 */
public class DictUtil {
    private static final Logger log = LoggerFactory.getLogger(DictUtil.class);
    private static volatile DictService dictService;

    // region ======== get / set service ========

    public static DictService getDictService() {
        if (dictService != null) { return dictService; }
        synchronized (DictUtil.class) {
            if (dictService != null) { return dictService; }
            DictUtil.setDictService(new SimpleDictService());
            return dictService;
        }
    }

    public static void setDictService(DictService dictService) {
        Assert.notNull(dictService, "Parameter \"dictService\" must not null. ");
        log.debug("Set dict service: {}", dictService.getClass().getName());
        DictUtil.dictService = dictService;
    }
    // endregion ======== get / set service ========


    // region ======== service methods ========

    public static String getDefaultNamespace() {

        return getDictService().getDefaultNamespace();
    }

    public static void setDefaultNamespace(String defaultNamespace) {

        getDictService().setDefaultNamespace(defaultNamespace);
    }

    public static void syncByGroup(Collection<Dict> data) {

        getDictService().syncByGroup(data);
    }

    public static void syncByCode(Collection<Dict> data) {

        getDictService().syncByCode(data);
    }

    public static Dict getByName(String namespace, String groupCode, String name) {

        return getDictService().getByName(namespace, groupCode, name);
    }

    public static Dict getByCode(String namespace, String groupCode, String code) {

        return getDictService().getByCode(namespace, groupCode, code);
    }

    public static Dict getByValue(String namespace, String groupCode, String value) {

        return getDictService().getByValue(namespace, groupCode, value);
    }

    public static Dict getByCondition(DictQuery condition) {

        return getDictService().getByCondition(condition);
    }

    public static List<Dict> listByGroup(String namespace, String groupCode) {

        return getDictService().listByGroup(namespace, groupCode);
    }

    public static Map<String, Dict> mapByGroup(String namespace, String groupCode) {

        return getDictService().mapByGroup(namespace, groupCode);
    }

    public static Page<Dict> listByCondition(boolean paged, DictQuery condition) {

        return getDictService().listByCondition(paged, condition);
    }
    // endregion ======== service methods ========


    // region ======== extended methods ========

    public static Dict getByName(String groupCode, String name) {

        return getByName(Nil.STR, groupCode, name);
    }

    public static Dict getByCode(String groupCode, String code) {

        return getByCode(Nil.STR, groupCode, code);
    }

    public static Dict getByValue(String groupCode, String value) {

        return getByValue(Nil.STR, groupCode, value);
    }

    public static List<Dict> listByGroup(String groupCode) {

        return listByGroup(Nil.STR, groupCode);
    }

    public static Map<String, Dict> mapByGroup(String groupCode) {

        return mapByGroup(Nil.STR, groupCode);
    }
    // endregion ======== extended methods ========

}
