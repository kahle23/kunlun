package artoria.convert.type1.support;

import artoria.convert.type1.ConversionProvider;
import artoria.convert.type1.GenericConverter;
import artoria.util.Assert;

import java.util.Collections;
import java.util.Set;

public abstract class AbstractGenericConverter implements GenericConverter {
    private final ConversionProvider conversionProvider;
    private final Set<ConvertiblePair> convertibleTypes;

    protected AbstractGenericConverter(ConversionProvider conversionProvider,
                                       Class<?> sourceClass, Class<?> targetClass) {
        Assert.notNull(conversionProvider, "Parameter \"conversionProvider\" must not null. ");
        Assert.notNull(sourceClass, "Parameter \"sourceClass\" must not null. ");
        Assert.notNull(targetClass, "Parameter \"targetClass\" must not null. ");
        ConvertiblePair pair = new ConvertiblePair(sourceClass, targetClass);
        this.conversionProvider = conversionProvider;
        this.convertibleTypes = Collections.singleton(pair);
    }

    protected AbstractGenericConverter(ConversionProvider conversionProvider,
                                       Set<ConvertiblePair> convertibleTypes) {
        Assert.notNull(conversionProvider, "Parameter \"conversionProvider\" must not null. ");
        this.conversionProvider = conversionProvider;
        this.convertibleTypes = convertibleTypes;
    }

    protected ConversionProvider getConversionProvider() {

        return conversionProvider;
    }

    @Override
    public Set<ConvertiblePair> getConvertibleTypes() {

        return convertibleTypes;
    }

}
