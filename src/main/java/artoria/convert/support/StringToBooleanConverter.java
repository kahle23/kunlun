package artoria.convert.support;

public class StringToBooleanConverter extends AbstractClassConverter {

    public StringToBooleanConverter() {

        super(String.class, Boolean.class);
    }

    @Override
    protected Object convert(Object source, Class<?> sourceClass, Class<?> targetClass) {

        return Boolean.valueOf((String) source);
    }

}
