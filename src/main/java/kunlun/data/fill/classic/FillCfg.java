/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.data.fill.classic;

import kunlun.core.function.Function;
import kunlun.data.fill.DataFiller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import static kunlun.data.fill.classic.ClassicFiller.DataConverter;
import static kunlun.util.Assert.notEmpty;
import static kunlun.util.Assert.notNull;

/**
 * The filling configuration of the classic filler.
 * @author Kahle
 */
public class FillCfg implements DataFiller.FillConfig {
    protected static final ClassicFiller FILLER = new ClassicFiller();

    public static FillCfg of(Object data) {

        return of().setData(data);
    }

    public static FillCfg of() {

        return new FillCfg();
    }

    private Function<Object, Collection<Map<String, Object>>> dataConverter = new DataConverter();
    private final Collection<DataCfg> dataConfigs = new ArrayList<DataCfg>();
    private Object data;

    @Override
    public Collection<DataCfg> getDataConfigs() {

        return dataConfigs;
    }

    public FillCfg addDataConfigs(Collection<DataCfg> dataConfigs) {
        this.dataConfigs.addAll(notEmpty(dataConfigs));
        return this;
    }

    public FillCfg addDataConfig(DataCfg dataConfig) {
        this.dataConfigs.add(notNull(dataConfig));
        return this;
    }

    public FillCfg clearDataConfigs() {
        this.dataConfigs.clear();
        return this;
    }

    @Override
    public Object getData() {

        return data;
    }

    public FillCfg setData(Object data) {
        this.data = notNull(data);
        return this;
    }

    public Function<Object, Collection<Map<String, Object>>> getDataConverter() {

        return dataConverter;
    }

    public FillCfg setDataConverter(Function<Object, Collection<Map<String, Object>>> dataConverter) {
        this.dataConverter = notNull(dataConverter);
        return this;
    }

    public void fill() {

        FILLER.fill(this);
    }

}
