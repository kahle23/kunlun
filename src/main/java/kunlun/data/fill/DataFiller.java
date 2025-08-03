/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.data.fill;

import java.util.Collection;

import static kunlun.data.fill.DataFiller.FillConfig;

/**
 * 数据填充器的抽象定义.
 * <p>
 * [数据填充器] <- [填充配置] <- [多个数据配置] + [被填充的数据]
 * <p>
 * [数据配置] <- [一个数据提供者] + [多个字段配置]
 *
 * @author Zerox
 */
public interface DataFiller<C extends FillConfig> {

    /**
     * Performing data filling.
     * @param config The fill configuration of the data filler
     */
    void fill(C config);


    /**
     * The fill configuration of the data filler.
     * @author Zerox
     */
    interface FillConfig {

        /**
         * Get the data configurations.
         * @return The data configurations
         */
        Collection<? extends DataConfig> getDataConfigs();

        /**
         * Get the data to be filled in.
         * @return The data to be filled in
         */
        Object getData();

    }

    /**
     * The data configuration of the data filler.
     * @author Zerox
     */
    interface DataConfig {

        /**
         * Get the data supplier (query the map or others based on the input collection).
         * @return The data supplier
         */
        DataSupplier getDataSupplier();

        /**
         * Get the field configurations.
         * @return The field configurations
         */
        Collection<? extends FieldConfig> getFieldConfigs();

    }

    /**
     * The field configuration of the data filler.
     * @author Zerox
     */
    interface FieldConfig {

        /**
         * Get the query fields.
         * @return The query fields
         */
        Collection<?> getQueryFields();

        /**
         * Get the fill field.
         * @return The fill field
         */
        Object getFillField();

        /**
         * Get the data field.
         * @return The data field
         */
        Object getDataField();

    }

}
