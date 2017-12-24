package apyh.artoria.converter;

import apyh.artoria.exception.ReflectionException;
import apyh.artoria.logging.Logger;
import apyh.artoria.logging.LoggerFactory;
import apyh.artoria.util.Assert;
import apyh.artoria.util.ClassUtils;
import apyh.artoria.util.ReflectUtils;

import java.util.Date;

import static apyh.artoria.util.StringConstant.COLON;
import static apyh.artoria.util.StringConstant.MINUS;

public class DateConverter implements Converter {
    private static final Logger log = LoggerFactory.getLogger(DateConverter.class);

    protected Object dateToDate(Object source, Class<?> target) {
        try {
            long time = ((Date) source).getTime();
            return ReflectUtils.newInstance(target, time);
        }
        catch (ReflectionException e) {
            return source;
        }
    }

    @Override
    public Object convert(Object source, Class<?> target) {
        Assert.notNull(source, "Source must is not null. ");
        Assert.notNull(target, "Target must is not null. ");
        Class<?> clazz = source.getClass();
        log.debug(DateConverter.class.getSimpleName() + COLON + clazz.getName() + MINUS + target.getName());
        target = ClassUtils.getWrapper(target);
        if (target.isAssignableFrom(clazz)) {
            return source;
        }
        if (Date.class.isAssignableFrom(clazz)
                && Date.class.isAssignableFrom(target)) {
            return this.dateToDate(source, target);
        }
        return source;
    }

}
