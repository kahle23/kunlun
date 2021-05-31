package artoria.convert.type1.support;

import artoria.convert.type1.ConversionProvider;

import java.math.BigDecimal;

public class StringToNumberConverter extends AbstractClassConverter {

    public StringToNumberConverter(ConversionProvider conversionProvider) {

        super(conversionProvider, String.class, Number.class);
    }

    @Override
    protected Object convert(Object source, Class<?> sourceClass, Class<?> targetClass) {
        String numString = (String) source;
        numString = numString.trim();
        BigDecimal decimal = new BigDecimal(numString);
        return getConversionProvider().convert(decimal, targetClass);
    }

}
