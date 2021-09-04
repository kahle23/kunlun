package artoria.beans;

import artoria.convert.ConversionProvider;
import artoria.convert.ConversionUtils;
import artoria.util.Assert;

/**
 * The bean map factory simple implement by jdk.
 * @author Kahle
 */
public class SimpleMapFactory implements MapFactory {
    private final ConversionProvider conversionProvider;

    public SimpleMapFactory() {

        this(ConversionUtils.getConversionProvider());
    }

    public SimpleMapFactory(ConversionProvider conversionProvider) {
        Assert.notNull(conversionProvider, "Parameter \"conversionProvider\" must not null. ");
        this.conversionProvider = conversionProvider;
    }

    @Override
    public BeanMap getInstance() {

        return new SimpleBeanMap(conversionProvider);
    }

    @Override
    public BeanMap getInstance(Object bean) {

        return new SimpleBeanMap(conversionProvider, bean);
    }

}
