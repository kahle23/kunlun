package artoria.convert.type1.support;

import artoria.convert.type1.ConversionProvider;

public class StringToBooleanConverter extends AbstractClassConverter {

    public StringToBooleanConverter(ConversionProvider conversionProvider) {

        super(conversionProvider, String.class, Boolean.class);
    }

    @Override
    protected Object convert(Object source, Class<?> sourceClass, Class<?> targetClass) {

        return Boolean.valueOf((String) source);
    }

}
