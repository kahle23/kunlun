package artoria.convert.support;

import artoria.convert.ConversionProvider;

public class StringToBooleanConverter extends AbstractClassConverter {

    public StringToBooleanConverter(ConversionProvider conversionProvider) {

        super(conversionProvider, String.class, Boolean.class);
    }

    @Override
    protected Object convert(Object source, Class<?> sourceClass, Class<?> targetClass) {

        return Boolean.valueOf((String) source);
    }

}
