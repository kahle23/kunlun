/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.data.fill;

import kunlun.core.function.Function;

import java.util.Collection;

import static kunlun.data.fill.DataFiller.FillConfig;

/**
 * The abstract definition of the data filler.
 * @author Kahle
 */
public interface DataFiller<C extends FillConfig> {

    /**
     * Performing data filling.
     * @param config The fill configuration of the data filler
     */
    void fill(C config);


    /**
     * The fill configuration of the data filler.
     * @author Kahle
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
     * @author Kahle
     */
    interface DataConfig {

        /**
         * Get the data supplier (query the map or others based on the input collection).
         * @return The data supplier
         */
        Function<Collection<?>, ?> getDataSupplier();

        /**
         * Get the field configurations.
         * @return The field configurations
         */
        Collection<? extends FieldConfig> getFieldConfigs();

    }


    /**
     * The field configuration of the data filler.
     * @author Kahle
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
