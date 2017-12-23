package apyh.artoria.converter;

import apyh.artoria.exception.ReflectionException;
import apyh.artoria.logging.Logger;
import apyh.artoria.logging.LoggerFactory;
import apyh.artoria.util.ClassUtils;
import apyh.artoria.util.ReflectUtils;

import java.util.Date;

/**
 * Simple converter
 * @author Kahle
 */
public class SimpleConverter implements Converter {
    private static final Logger log = LoggerFactory.getLogger(SimpleConverter.class);

    @Override
    public Object convert(Object source, Class<?> target) {
        Class<?> clazz = source.getClass();
        log.debug(SimpleConverter.class.getSimpleName() + ": " + clazz.getName() + " To " + target.getName());
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
        if (Date.class.isAssignableFrom(clazz)
                && Date.class.isAssignableFrom(target)) {
            try {
                long time = ((Date) source).getTime();
                return ReflectUtils.newInstance(target, time);
            }
            catch (ReflectionException e) {
                // ignore
            }
        }
        return source;
    }

}
