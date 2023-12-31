package artoria.data.bean;

import artoria.convert.ConversionService;
import artoria.convert.ConversionUtils;
import artoria.util.Assert;

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
