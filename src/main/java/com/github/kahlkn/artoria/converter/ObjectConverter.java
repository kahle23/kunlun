package com.github.kahlkn.artoria.converter;

import com.github.kahlkn.artoria.util.Assert;
import com.github.kahlkn.artoria.util.ClassUtils;

/**
 * Simple converter, So is mapping Object.
 * @author Kahle
 */
public class ObjectConverter implements Converter {

    @Override
    public Object convert(Object source, Class<?> target) {
        Assert.notNull(source, "Parameter \"source\" must not null. ");
        Assert.notNull(target, "Parameter \"target\" must not null. ");
        Class<?> clazz = source.getClass();
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
