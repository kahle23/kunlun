package artoria.convert.support;

import artoria.convert.ConversionProvider;

public class ObjectToStringConverter extends AbstractClassConverter {

    public ObjectToStringConverter(ConversionProvider conversionProvider) {

        super(conversionProvider, Object.class, String.class);
    }

    @Override
    protected Object convert(Object source, Class<?> sourceClass, Class<?> targetClass) {

        return source.toString();
    }

}
