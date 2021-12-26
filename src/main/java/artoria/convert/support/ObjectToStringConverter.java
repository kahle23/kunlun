package artoria.convert.support;

public class ObjectToStringConverter extends AbstractClassConverter {

    public ObjectToStringConverter() {

        super(Object.class, String.class);
    }

    @Override
    protected Object convert(Object source, Class<?> sourceClass, Class<?> targetClass) {

        return source.toString();
    }

}
