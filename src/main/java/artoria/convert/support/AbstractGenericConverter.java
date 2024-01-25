package artoria.convert.support;

import artoria.convert.ConversionService;
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
    private final ConversionService conversionService;
    private final Set<ConvertiblePair> convertibleTypes;

    protected AbstractGenericConverter(Class<?> sourceClass, Class<?> targetClass) {

        this(new ProxyConversionService(), sourceClass, targetClass);
    }

    protected AbstractGenericConverter(ConversionService conversionService,
                                       Class<?> sourceClass, Class<?> targetClass) {
        Assert.notNull(conversionService, "Parameter \"conversionService\" must not null. ");
        Assert.notNull(sourceClass, "Parameter \"sourceClass\" must not null. ");
        Assert.notNull(targetClass, "Parameter \"targetClass\" must not null. ");
        ConvertiblePair pair = new ConvertiblePair(sourceClass, targetClass);
        this.conversionService = conversionService;
        this.convertibleTypes = Collections.singleton(pair);
    }

    protected AbstractGenericConverter(Set<ConvertiblePair> convertibleTypes) {

        this(new ProxyConversionService(), convertibleTypes);
    }

    protected AbstractGenericConverter(ConversionService conversionService,
                                       Set<ConvertiblePair> convertibleTypes) {
        Assert.notNull(conversionService, "Parameter \"conversionService\" must not null. ");
        this.conversionService = conversionService;
        this.convertibleTypes = convertibleTypes;
    }

    protected ConversionService getConversionService() {

        return conversionService;
    }

    @Override
    public Set<ConvertiblePair> getConvertibleTypes() {

        return convertibleTypes;
    }

    /**
     * The static proxy class for the conversion tools.
     * @author Kahle
     */
    private static class ProxyConversionService implements ConversionService {

        @Override
        public void registerConverter(GenericConverter converter) {

            ConversionUtils.registerConverter(converter);
        }

        @Override
        public void deregisterConverter(GenericConverter converter) {

            ConversionUtils.deregisterConverter(converter);
        }

        @Override
        public GenericConverter getConverter(Type sourceType, Type targetType) {
            ConversionService conversionService = ConversionUtils.getConversionService();
            return conversionService.getConverter(sourceType, targetType);
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
