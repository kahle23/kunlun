/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.convert.support;

import kunlun.convert.ConversionService;
import kunlun.exception.ExceptionUtils;
import kunlun.reflect.ReflectUtils;
import kunlun.util.StringUtils;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;

public class NumberToNumberConverter extends AbstractClassConverter {
    private static final String INTEGER = "Integer";
    private static final String VALUE = "Value";
    private static final String INT = "int";

    public NumberToNumberConverter() {

        super(Number.class, Number.class);
    }

    public NumberToNumberConverter(ConversionService conversionService) {

        super(conversionService, Number.class, Number.class);
    }

    @Override
    protected Object convert(Object source, Class<?> sourceClass, Class<?> targetClass) {
        if (BigInteger.class.isAssignableFrom(targetClass)) {
            return new BigInteger(source.toString());
        }
        if (BigDecimal.class.isAssignableFrom(targetClass)) {
            return new BigDecimal(source.toString());
        }
        // handle : byte, short, int, long, float, double
        Class<?> clazz = source.getClass();
        String name = targetClass.getSimpleName();
        name = INTEGER.equals(name) ? INT : name;
        name = StringUtils.uncapitalize(name);
        name = name + VALUE;
        try {
            Method method = ReflectUtils.getMethod(clazz, name);
            return method.invoke(source);
        }
        catch (Exception e) {
            throw ExceptionUtils.wrap(e);
        }
    }

}
