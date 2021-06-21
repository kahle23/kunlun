package artoria.convert.support;

import artoria.convert.ConversionProvider;
import artoria.util.Assert;
import artoria.util.ClassUtils;
import artoria.util.ObjectUtils;

import java.lang.reflect.Type;

public abstract class AbstractClassConverter extends AbstractGenericConverter {
    private final Class<?> sourceClass;
    private final Class<?> targetClass;

    protected AbstractClassConverter(ConversionProvider conversionProvider,
                                     Class<?> sourceClass, Class<?> targetClass) {
        super(conversionProvider, sourceClass, targetClass);
        this.sourceClass = sourceClass;
        this.targetClass = targetClass;
    }

    protected Class<?> obtainClass(Type type) {
        if (!(type instanceof Class)) {
            throw new IllegalArgumentException("Only 'Class' conversions are supported. ");
        }
        return ObjectUtils.cast(type);
    }

    protected abstract Object convert(Object source, Class<?> sourceClass, Class<?> targetClass);

    @Override
    public Object convert(Object source, Type sourceType, Type targetType) {
        Assert.notNull(targetType, "Parameter \"targetType\" must not null. ");
        Assert.notNull(source, "Parameter \"source\" must not null. ");
        Class<?> sourceClass = sourceType != null ? obtainClass(sourceType) : source.getClass();
        Class<?> targetClass = obtainClass(targetType);
        sourceClass = ClassUtils.getWrapper(sourceClass);
        targetClass = ClassUtils.getWrapper(targetClass);
        if (targetClass.isAssignableFrom(sourceClass)) {
            return source;
        }
        if (!this.sourceClass.isAssignableFrom(sourceClass)) {
            return source;
        }
        if (!this.targetClass.isAssignableFrom(targetClass)) {
            return source;
        }
        return convert(source, sourceClass, targetClass);
    }

}
