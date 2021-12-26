package artoria.convert.support;

import artoria.convert.ConversionProvider;
import artoria.convert.ConversionUtils;
import artoria.convert.GenericConverter;
import artoria.util.Assert;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Set;

/**
 * The abstract generic converter.
 * @author Kahle
 */
public abstract class AbstractGenericConverter implements GenericConverter {
    private final ConversionProvider conversionProvider;
    private final Set<ConvertiblePair> convertibleTypes;

    protected AbstractGenericConverter(Class<?> sourceClass, Class<?> targetClass) {

        this(new ProxyConversionProvider(), sourceClass, targetClass);
    }

    protected AbstractGenericConverter(ConversionProvider conversionProvider,
                                       Class<?> sourceClass, Class<?> targetClass) {
        Assert.notNull(conversionProvider, "Parameter \"conversionProvider\" must not null. ");
        Assert.notNull(sourceClass, "Parameter \"sourceClass\" must not null. ");
        Assert.notNull(targetClass, "Parameter \"targetClass\" must not null. ");
        ConvertiblePair pair = new ConvertiblePair(sourceClass, targetClass);
        this.conversionProvider = conversionProvider;
        this.convertibleTypes = Collections.singleton(pair);
    }

    protected AbstractGenericConverter(Set<ConvertiblePair> convertibleTypes) {

        this(new ProxyConversionProvider(), convertibleTypes);
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

    /**
     * The static proxy class for the conversion tools.
     */
    private static class ProxyConversionProvider implements ConversionProvider {

        @Override
        public void addConverter(GenericConverter converter) {

            ConversionUtils.register(converter);
        }

        @Override
        public void removeConverter(GenericConverter converter) {

            ConversionUtils.deregister(converter);
        }

        @Override
        public GenericConverter getConverter(Type sourceType, Type targetType) {
            ConversionProvider conversionProvider = ConversionUtils.getConversionProvider();
            return conversionProvider.getConverter(sourceType, targetType);
        }

        @Override
        public boolean canConvert(Type sourceType, Type targetType) {

            return ConversionUtils.canConvert(sourceType, targetType);
        }

        @Override
        public Object convert(Object source, Type targetType) {

            return ConversionUtils.convert(source, targetType);
        }

        @Override
        public Object convert(Object source, Type sourceType, Type targetType) {

            return ConversionUtils.convert(source, sourceType, targetType);
        }

    }

}
