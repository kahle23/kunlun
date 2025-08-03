/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.data.fill.classic;

import kunlun.convert.ConversionUtil;
import kunlun.core.function.Function;
import kunlun.data.Array;
import kunlun.data.bean.support.FieldBasedBeanMap;
import kunlun.data.fill.DataFiller;
import kunlun.util.CollUtil;
import kunlun.util.MapUtil;
import kunlun.util.ObjUtil;

import java.io.Serializable;
import java.util.*;

import static java.util.Collections.singletonList;
import static kunlun.common.constant.Numbers.ZERO;
import static kunlun.util.Assert.*;

/**
 * The classic data filler.
 * <p>
 * Data filling process:
 *  1. Convert data to map (bean map).
 *  2. Extract the data to be queried.
 *  3. Query data.
 *  4. Fill data.
 * <p>
 * Field example:
 *  queryField = createId  , projectId
 *  fillField  = createName, projectName
 *  dataField  = nickName  , name
 *
 * @author Kahle
 */
public class ClassicFiller implements DataFiller<FillCfg> {

    @Override
    public void fill(FillCfg config) {
        notNull(config, "Parameter \"config\" must not null. ");
        // If data is null or field configs is empty, logical end.
        if (CollUtil.isEmpty(config.getDataConfigs())) { return; }
        if (ObjUtil.isEmpty(config.getData())) { return; }
        // Convert the data.
        Collection<Map<String, Object>> data =
                notNull(config.getDataConverter()).apply(config.getData());
        if (CollUtil.isEmpty(data)) { return; }
        // Extract the data to be queried.
        Map<DataCfg, Collection<Object>> conditionsMap =
                new LinkedHashMap<DataCfg, Collection<Object>>();
        for (Map<String, Object> datum : data) {
            for (DataCfg dataConfig : config.getDataConfigs()) {
                for (FieldCfg fieldConfig : dataConfig.getFieldConfigs()) {
                    if (fieldConfig == null) { continue; }
                    Object value = datum.get(CollUtil.getFirst(fieldConfig.getQueryFields()));
                    if (value == null) { continue; }
                    Collection<Object> coll = conditionsMap.get(dataConfig);
                    if (coll == null) {
                        conditionsMap.put(dataConfig, coll = new Array());
                    }
                    coll.add(value);
                }
            }
        }
        // Query data.
        for (Map.Entry<DataCfg, Collection<Object>> entry : conditionsMap.entrySet()) {
            Collection<Object> conditions = entry.getValue();
            DataCfg dataConfig = entry.getKey();
            // Fill data.
            doFill(dataConfig, data, dataConfig.getDataSupplier().acquire(conditions));
        }
    }

    /**
     * Data filling is carried out after the data preparation is completed.
     * @param cfg The data configuration
     * @param inputs The input data
     * @param data The retrieved data
     */
    protected void doFill(DataCfg cfg, Collection<Map<String, Object>> inputs,
                          Map<String, Map<String, Object>> data) {
        // data validation.
        if (CollUtil.isEmpty(inputs)) { return; }
        if (MapUtil.isEmpty(data)) { return; }
        // fill data.
        for (Map<String, Object> inputMap : inputs) {
            for (FieldCfg fieldCfg : cfg.getFieldConfigs()) {
                Object value = inputMap.get(CollUtil.getFirst(fieldCfg.getQueryFields()));
                if (value == null) { continue; }
                Map<String, Object> objMap = data.get(String.valueOf(value));
                if (objMap == null) { continue; }
                Object dataVal = objMap.get(fieldCfg.getDataField());
                if (dataVal == null) { continue; }
                inputMap.put(fieldCfg.getFillField(), dataVal);
            }
        }
    }


    /**
     * The data converter (convert the input object into the collection of bean map).
     * @author Kahle
     */
    public static class DataConverter implements Function<Object, Collection<Map<String, Object>>> {
        @Override
        public Collection<Map<String, Object>> apply(Object data) {
            List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
            // if the data is not a collection.
            if (!(data instanceof Collection)) {
                dataList.add(data instanceof Map ? ObjUtil.<Map<String, Object>>cast(data)
                        : ObjUtil.<Map<String, Object>>cast(new FieldBasedBeanMap(ConversionUtil.getConversionService(), data)));
                return dataList;
            }
            // if the data is a collection.
            for (Object datum : ObjUtil.<Collection<Object>>cast(data)) {
                if (datum == null) { continue; }
                dataList.add(datum instanceof Map ? ObjUtil.<Map<String, Object>>cast(datum)
                        : ObjUtil.<Map<String, Object>>cast(new FieldBasedBeanMap(ConversionUtil.getConversionService(), datum)));
            }
            return dataList;
        }
    }


    /**
     * The data filling field configuration.
     * @author Kahle
     */
    public static class FieldCfg implements DataFiller.FieldConfig, Serializable {
        /**
         * The query field.
         */
        private Collection<String> queryFields;
        /**
         * The fill field.
         */
        private String fillField;
        /**
         * The data field.
         */
        private String dataField;

        public FieldCfg(Collection<String> queryFields, String fillField, String dataField) {
            this.setQueryFields(queryFields);
            this.setFillField(fillField);
            this.setDataField(dataField);
        }

        public FieldCfg(String queryField, String fillField, String dataField) {
            this.setQueryFields(singletonList(notBlank(queryField)));
            this.setFillField(fillField);
            this.setDataField(dataField);
        }

        public FieldCfg() {

        }

        @Override
        public Collection<String> getQueryFields() {

            return queryFields;
        }

        public void setQueryFields(Collection<String> queryFields) {

            this.queryFields = notEmpty(queryFields);
        }

        @Override
        public String getFillField() {

            return fillField;
        }

        public void setFillField(String fillField) {

            this.fillField = notBlank(fillField);
        }

        @Override
        public String getDataField() {

            return dataField;
        }

        public void setDataField(String dataField) {

            this.dataField = notBlank(dataField);
        }

        @Override
        public boolean equals(Object object) {
            if (this == object) { return true; }
            if (object == null || getClass() != object.getClass()) {
                return false;
            }
            FieldCfg that = (FieldCfg) object;
            if (queryFields != null ? !queryFields.equals(that.queryFields) : that.queryFields != null) {
                return false;
            }
            if (fillField != null ? !fillField.equals(that.fillField) : that.fillField != null) {
                return false;
            }
            return dataField != null ? dataField.equals(that.dataField) : that.dataField == null;
        }

        @Override
        public int hashCode() {
            int result = queryFields != null ? queryFields.hashCode() : ZERO;
            result = 31 * result + (fillField != null ? fillField.hashCode() : ZERO);
            result = 31 * result + (dataField != null ? dataField.hashCode() : ZERO);
            return result;
        }
    }

}
