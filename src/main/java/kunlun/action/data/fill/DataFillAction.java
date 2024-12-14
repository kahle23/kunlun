/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.action.data.fill;

import kunlun.core.Action;
import kunlun.util.Assert;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import static java.util.Collections.singletonList;

/**
 * The data filling action.
 *
 * Data filling process:
 *  1. Convert data to map (bean map).
 *  2. Extract the data to be queried.
 *  3. Query data.
 *  4. Fill data.
 *
 * Field example:
 *  queryField = createId  , projectId
 *  fillField  = createName, projectName
 *  dataField  = nickName  , name
 *
 * @author Kahle
 */
public interface DataFillAction extends Action {

    /**
     * Acquire data based on field configuration and data to be queried.
     *
     * Example:
     *  cfg if createId, query by account id.
     *  cfg if projectId, query by project id.
     *
     * @param cfg The data filling field configuration
     * @param coll The data to be queried
     * @return The data that was queried
     */
    Map<String, Map<String, Object>> acquire(FieldConfig cfg, Collection<?> coll);

    /**
     * Convert the data into the bean map collection.
     * @param data The data to be processed
     * @return The converted data
     */
    Collection<Map<String, Object>> convert(Object data);

    /**
     * Performing data filling.
     * @param cfg The data filling field configuration
     * @param map The data that was queried
     * @param data The data to be filled in
     */
    void fill(FieldConfig cfg, Map<String, Map<String, Object>> map, Collection<Map<String, Object>> data);

    /**
     * The data filling configuration.
     * @author Kahle
     */
    interface Config {

        /**
         * Get the specific fill configurations.
         * @return The specific fill configurations
         */
        Collection<? extends FieldConfig> getFieldConfigs();

        /**
         * Get the data to be filled in.
         * @return The data to be filled in
         */
        Object getData();

    }

    /**
     * The data filling field configuration.
     * @author Kahle
     */
    interface FieldConfig {

        /**
         * Get the query fields.
         * "Collection" is used to support multi-data query scenarios.
         * @return The query fields
         */
        Collection<String> getQueryFields();

        /**
         * Get the fill fields.
         * Support multi-field filling scenario, and the following "dataFields" one-to-one mapping.
         * @return The fill fields
         */
        Collection<String> getFillFields();

        /**
         * Get the data fields.
         * @return The data fields
         */
        Collection<String> getDataFields();

    }

    /**
     * @author Kahle
     */
    class FieldConfigImpl implements FieldConfig {
        private List<String> queryFields;
        private List<String> fillFields;
        private List<String> dataFields;

        public FieldConfigImpl(List<String> queryFields, List<String> fillFields, List<String> dataFields) {
            this.setQueryFields(queryFields);
            this.setFillFields(fillFields);
            this.setDataFields(dataFields);
        }

        public FieldConfigImpl(String queryField, String fillField, String dataField) {
            this.setQueryFields(singletonList(queryField));
            this.setFillFields(singletonList(fillField));
            this.setDataFields(singletonList(dataField));
        }

        @Override
        public List<String> getQueryFields() {

            return queryFields;
        }

        public void setQueryFields(List<String> queryFields) {
            Assert.notEmpty(queryFields, "Parameter \"queryFields\" must not empty. ");
            this.queryFields = queryFields;
        }

        @Override
        public List<String> getFillFields() {

            return fillFields;
        }

        public void setFillFields(List<String> fillFields) {
            Assert.notEmpty(fillFields, "Parameter \"fillFields\" must not empty. ");
            this.fillFields = fillFields;
        }

        @Override
        public List<String> getDataFields() {

            return dataFields;
        }

        public void setDataFields(List<String> dataFields) {
            Assert.notEmpty(dataFields, "Parameter \"dataFields\" must not empty. ");
            this.dataFields = dataFields;
        }
    }

}
