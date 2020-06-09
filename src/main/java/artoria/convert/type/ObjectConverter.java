package artoria.convert.type;

import artoria.util.Assert;
import artoria.util.ClassUtils;

/**
 * Simple converter, so is mapping object.
 * @author Kahle
 */
public class ObjectConverter implements TypeConverter {

    @Override
    public Object convert(Object source, Class<?> target) {
        Assert.notNull(source, "Parameter \"source\" must not null. ");
        Assert.notNull(target, "Parameter \"target\" must not null. ");
        Class<?> clazz = source.getClass();
        target = ClassUtils.getWrapper(target);
        clazz = ClassUtils.getWrapper(clazz);
        if (target.isAssignableFrom(clazz)) {
            return source;
        }
        boolean tgIsStr = String.class.isAssignableFrom(target);
        if (Boolean.class.isAssignableFrom(clazz) && tgIsStr) {
            return source.toString();
        }
        if (Character.class.isAssignableFrom(clazz) && tgIsStr) {
            return source.toString();
        }
        return source;
    }

}
