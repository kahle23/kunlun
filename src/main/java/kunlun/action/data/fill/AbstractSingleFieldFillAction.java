/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.action.data.fill;

import kunlun.action.AbstractAction;
import kunlun.convert.ConversionUtils;
import kunlun.data.Array;
import kunlun.data.bean.support.FieldBasedBeanMap;
import kunlun.logging.Logger;
import kunlun.logging.LoggerFactory;
import kunlun.util.*;

import java.util.*;

/**
 * The abstract single field fill action.
 * @author Kahle
 */
public abstract class AbstractSingleFieldFillAction extends AbstractAction implements DataFillAction {
    private static final Logger log = LoggerFactory.getLogger(AbstractSingleFieldFillAction.class);

    @Override
    public Collection<Map<String, Object>> convert(Object data) {
        List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();

        if (!(data instanceof Collection)) {
                dataList.add(data instanceof Map ? (Map<String, Object>) data
                        : ObjectUtils.<Map<String, Object>>cast(new FieldBasedBeanMap(ConversionUtils.getConversionService(), data)));
            return dataList;
        }

        for (Object datum : (Collection) data) {
            if (datum == null) { continue; }
                dataList.add(datum instanceof Map ? (Map<String, Object>) datum
                        : ObjectUtils.<Map<String, Object>>cast(new FieldBasedBeanMap(ConversionUtils.getConversionService(), datum)));
        }
        return dataList;
    }

    @Override
    public void fill(FieldConfig cfg, Map<String, Map<String, Object>> map, Collection<Map<String, Object>> data) {
        // data validation.
        if (CollectionUtils.isEmpty(data)) { return; }
        if (MapUtils.isEmpty(map)) { return; }
        // get field config.
        String queryField = IteratorUtils.getFirst(cfg.getQueryFields());
        String fillField = IteratorUtils.getFirst(cfg.getFillFields());
        String dataField = IteratorUtils.getFirst(cfg.getDataFields());
        // fill data.
        for (Map<String, Object> dataMap : data) {
            Object value = dataMap.get(queryField);
            if (value == null) { continue; }
            Map<String, Object> objMap = map.get(String.valueOf(value));
            if (objMap == null) { continue; }
            Object dataVal = objMap.get(dataField);
            if (dataVal == null) { continue; }
            dataMap.put(fillField, dataVal);
        }
    }

    @Override
    public Object execute(String strategy, Object input, Object[] arguments) {
        // Validation.
        Assert.notNull(input, "Parameter \"input\" must not null. ");
        Assert.isInstanceOf(Config.class, input
                , "Parameter \"input\" must instance of \"Config\". ");
        // Get field configs and data.
        Config config = (Config) input;
        Collection<? extends FieldConfig> fieldConfigs = config.getFieldConfigs();
        Object data = config.getData();
        // If data is null or field configs is empty, logical end.
        if (data == null) { return null; }
        if (CollectionUtils.isEmpty(fieldConfigs)) { return null; }
        // Convert the data.
        Collection<Map<String, Object>> dataList = convert(data);
        if (CollectionUtils.isEmpty(dataList)) { return null; }
        // Extract the data to be queried.
        Map<FieldConfig, Collection<Object>> queryFieldMap = new LinkedHashMap<FieldConfig, Collection<Object>>();
        for (Map<String, Object> dataMap : dataList) {
            for (FieldConfig fieldConfig : fieldConfigs) {
                Object value = dataMap.get(IteratorUtils.getFirst(fieldConfig.getQueryFields()));
                if (value == null) { continue; }
                Collection<Object> coll = queryFieldMap.get(fieldConfig);
                if (coll == null) {
                    queryFieldMap.put(fieldConfig, coll = new Array());
                }
                coll.add(value);
            }
        }
        // Query data.
        for (Map.Entry<FieldConfig, Collection<Object>> entry : queryFieldMap.entrySet()) {
            Collection<Object> objectList = entry.getValue();
            FieldConfig fieldConfig = entry.getKey();
            // Fill data.
            fill(fieldConfig, acquire(fieldConfig, objectList), dataList);
        }
        return null;
    }

}
