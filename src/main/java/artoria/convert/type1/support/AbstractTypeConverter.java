package artoria.convert.type1.support;

import artoria.convert.type1.ConversionProvider;

import java.util.Set;

public abstract class AbstractTypeConverter extends AbstractGenericConverter {

    protected AbstractTypeConverter(ConversionProvider conversionProvider,
                                    Class<?> sourceClass, Class<?> targetClass) {

        super(conversionProvider, sourceClass, targetClass);
    }

    protected AbstractTypeConverter(ConversionProvider conversionProvider,
                                    Set<ConvertiblePair> convertibleTypes) {

        super(conversionProvider, convertibleTypes);
    }

}
