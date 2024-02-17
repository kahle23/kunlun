/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.data.bean.support;

import kunlun.convert.ConversionService;
import kunlun.convert.ConversionUtils;
import kunlun.data.bean.BeanMap;
import kunlun.data.bean.BeanMapFactory;
import kunlun.util.Assert;

/**
 * The bean map factory simple implement by jdk.
 * @author Kahle
 */
public class SimpleBeanMapFactory implements BeanMapFactory {
    private final ConversionService conversionService;

    public SimpleBeanMapFactory() {

        this(ConversionUtils.getConversionService());
    }

    public SimpleBeanMapFactory(ConversionService conversionService) {
        Assert.notNull(conversionService, "Parameter \"conversionService\" must not null. ");
        this.conversionService = conversionService;
    }

    @Override
    public BeanMap getInstance(Object bean) {
        SimpleBeanMap beanMap = new SimpleBeanMap(conversionService);
        if (bean != null) { beanMap.setBean(bean); }
        return beanMap;
    }

}
