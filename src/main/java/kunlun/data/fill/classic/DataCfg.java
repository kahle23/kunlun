/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.data.fill.classic;

import kunlun.data.fill.DataFiller;
import kunlun.data.fill.DataSupplier;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static kunlun.common.constant.Numbers.ZERO;
import static kunlun.data.fill.classic.ClassicFiller.FieldCfg;
import static kunlun.util.Assert.notEmpty;
import static kunlun.util.Assert.notNull;

/**
 * The data configuration of the classic filler.
 * @author Kahle
 */
public class DataCfg implements DataFiller.DataConfig, Serializable {

    public static DataCfg of(DataSupplier dataSupplier) {

        return new DataCfg(dataSupplier);
    }

    private Collection<FieldCfg> fieldConfigs = new ArrayList<FieldCfg>();
    private final DataSupplier dataSupplier;

    public DataCfg(DataSupplier dataSupplier) {

        this.dataSupplier = notNull(dataSupplier);
    }

    @Override
    public DataSupplier getDataSupplier() {

        return dataSupplier;
    }

    @Override
    public Collection<FieldCfg> getFieldConfigs() {

        return fieldConfigs;
    }

    public void setFieldConfigs(Collection<FieldCfg> fieldConfigs) {

        this.fieldConfigs = notNull(fieldConfigs);
    }

    public DataCfg addFieldConfig(String queryField, String fillField, String dataField) {
        this.fieldConfigs.add(new FieldCfg(queryField, fillField, dataField));
        return this;
    }

    public DataCfg addFieldConfigs(List<FieldCfg> fieldConfigs) {
        this.fieldConfigs.addAll(notEmpty(fieldConfigs));
        return this;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) { return true; }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        DataCfg that = (DataCfg) object;
        if (dataSupplier != null ? !dataSupplier.equals(that.dataSupplier) : that.dataSupplier != null) {
            return false;
        }
        return fieldConfigs != null ? fieldConfigs.equals(that.fieldConfigs) : that.fieldConfigs == null;
    }

    @Override
    public int hashCode() {
        int result = dataSupplier != null ? dataSupplier.hashCode() : ZERO;
        result = 31 * result + (fieldConfigs != null ? fieldConfigs.hashCode() : ZERO);
        return result;
    }

}
