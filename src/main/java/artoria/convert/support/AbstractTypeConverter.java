package artoria.convert.support;

import artoria.convert.ConversionService;

import java.util.Set;

public abstract class AbstractTypeConverter extends AbstractGenericConverter {

    protected AbstractTypeConverter(Class<?> sourceClass, Class<?> targetClass) {

        super(sourceClass, targetClass);
    }

    protected AbstractTypeConverter(ConversionService conversionService,
                                    Class<?> sourceClass, Class<?> targetClass) {

        super(conversionService, sourceClass, targetClass);
    }

    protected AbstractTypeConverter(Set<ConvertiblePair> convertibleTypes) {

        super(convertibleTypes);
    }

    protected AbstractTypeConverter(ConversionService conversionService,
                                    Set<ConvertiblePair> convertibleTypes) {

        super(conversionService, convertibleTypes);
    }

}
