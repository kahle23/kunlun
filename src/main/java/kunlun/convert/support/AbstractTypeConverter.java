/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.convert.support;

import kunlun.convert.ConversionService;

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
