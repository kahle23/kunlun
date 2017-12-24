package apyh.artoria.converter;

import apyh.artoria.logging.Logger;
import apyh.artoria.logging.LoggerFactory;
import apyh.artoria.util.Assert;
import apyh.artoria.util.ClassUtils;

import static apyh.artoria.util.StringConstant.COLON;
import static apyh.artoria.util.StringConstant.MINUS;

/**
 * Simple converter, So is mapping Object.
 * @author Kahle
 */
public class ObjectConverter implements Converter {
    private static final Logger log = LoggerFactory.getLogger(ObjectConverter.class);

    @Override
    public Object convert(Object source, Class<?> target) {
        Assert.notNull(source, "Source must is not null. ");
        Assert.notNull(target, "Target must is not null. ");
        Class<?> clazz = source.getClass();
        log.debug(ObjectConverter.class.getSimpleName() + COLON + clazz.getName() + MINUS + target.getName());
        target = ClassUtils.getWrapper(target);
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
