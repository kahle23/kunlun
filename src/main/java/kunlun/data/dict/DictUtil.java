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
    // endregion


    // region ======== service methods ========

    public static String getDefaultNamespace() {

        return getDictService().getDefaultNamespace();
    }

    public static void setDefaultNamespace(String defaultNamespace) {

        getDictService().setDefaultNamespace(defaultNamespace);
    }

    public static void syncByGroup(Collection<DataDict> data) {

        getDictService().syncByGroup(data);
    }

    public static void syncByCode(Collection<DataDict> data) {

        getDictService().syncByCode(data);
    }

    public static DataDict getByName(String namespace, String groupCode, String name) {

        return getDictService().getByName(namespace, groupCode, name);
    }

    public static DataDict getByCode(String namespace, String groupCode, String code) {

        return getDictService().getByCode(namespace, groupCode, code);
    }

    public static DataDict getByValue(String namespace, String groupCode, String value) {

        return getDictService().getByValue(namespace, groupCode, value);
    }

    public static DataDict getByCondition(DictQuery condition) {

        return getDictService().getByCondition(condition);
    }

    public static List<DataDict> listByGroup(String namespace, String groupCode) {

        return getDictService().listByGroup(namespace, groupCode);
    }

    public static Map<String, DataDict> mapByGroup(String namespace, String groupCode) {

        return getDictService().mapByGroup(namespace, groupCode);
    }

    public static Page<DataDict> listByCondition(boolean paged, DictQuery condition) {

        return getDictService().listByCondition(paged, condition);
    }
    // endregion


    // region ======== extended methods ========

    public static DataDict getByName(String groupCode, String name) {

        return getByName(Nil.STR, groupCode, name);
    }

    public static DataDict getByCode(String groupCode, String code) {

        return getByCode(Nil.STR, groupCode, code);
    }

    public static DataDict getByValue(String groupCode, String value) {

        return getByValue(Nil.STR, groupCode, value);
    }

    public static List<DataDict> listByGroup(String groupCode) {

        return listByGroup(Nil.STR, groupCode);
    }

    public static Map<String, DataDict> mapByGroup(String groupCode) {

        return mapByGroup(Nil.STR, groupCode);
    }
    // endregion


    // region ======== extended methods 1 ========

    public static String getValueByName(String namespace, String groupCode, String name) {
        DataDict dataDict = getByName(namespace, groupCode, name);
        if (dataDict == null) { return null; }
        return dataDict.getValue();
    }

    public static String getValueByName(String groupCode, String name) {

        return getValueByName(Nil.STR, groupCode, name);
    }

    public static String getNameByValue(String namespace, String groupCode, String value) {
        DataDict dataDict = getByValue(namespace, groupCode, value);
        if (dataDict == null) { return null; }
        return dataDict.getName();
    }

    public static String getNameByValue(String groupCode, String value) {

        return getNameByValue(Nil.STR, groupCode, value);
    }
    // endregion

}
