package com.github.kahlkn.artoria.converter;

import com.github.kahlkn.artoria.reflect.ReflectUtils;
import com.github.kahlkn.artoria.util.Assert;
import com.github.kahlkn.artoria.util.ClassUtils;

import java.util.Date;

/**
 * Date converter.
 * @author Kahle
 */
public class DateConverter implements Converter {

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
