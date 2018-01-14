package com.apyhs.artoria.converter;

import com.apyhs.artoria.logging.Logger;
import com.apyhs.artoria.logging.LoggerFactory;
import com.apyhs.artoria.reflect.ReflectUtils;
import com.apyhs.artoria.util.Assert;
import com.apyhs.artoria.util.ClassUtils;

import java.util.Date;

import static com.apyhs.artoria.util.Const.COLON;

/**
 * Date converter.
 * @author Kahle
 */
public class DateConverter implements Converter {
    private static final Logger log = LoggerFactory.getLogger(DateConverter.class);

    protected Object dateToDate(Object source, Class<?> target) {
        long time = ((Date) source).getTime();
        try {
            return ReflectUtils.newInstance(target, time);
        }
        catch (Exception e) {
            return source;
        }
    }

    @Override
    public Object convert(Object source, Class<?> target) {
        Assert.notNull(source, "Parameter \"source\" must not null. ");
        Assert.notNull(target, "Parameter \"target\" must not null. ");
        Class<?> clazz = source.getClass();
        log.debug(DateConverter.class.getSimpleName() + COLON + clazz.getName() + " >> " + target.getName());
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
