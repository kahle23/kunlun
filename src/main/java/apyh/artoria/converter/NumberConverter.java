package apyh.artoria.converter;

import apyh.artoria.exception.ReflectionException;
import apyh.artoria.logging.Logger;
import apyh.artoria.logging.LoggerFactory;
import apyh.artoria.util.ClassUtils;
import apyh.artoria.util.ReflectUtils;
import apyh.artoria.util.StringUtils;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Number converter
 * @author Kahle
 */
public class NumberConverter implements Converter {
    private static final Logger log = LoggerFactory.getLogger(NumberConverter.class);

    @Override
    public Object convert(Object source, Class<?> target) {
        Class<?> clazz = source.getClass();
        log.debug(NumberConverter.class.getSimpleName() + ": " + clazz.getName() + " To " + target.getName());
        target = ClassUtils.getWrapper(target);
        if (target.isAssignableFrom(clazz)) {
            return source;
        }
        if (!Number.class.isAssignableFrom(clazz)) {
            return source;
        }
        if (String.class.isAssignableFrom(target)) {
            return source.toString();
        }
        if (!Number.class.isAssignableFrom(target)) {
            return source;
        }
        if (BigInteger.class.isAssignableFrom(target)) {
            return new BigInteger(source.toString());
        }
        if (BigDecimal.class.isAssignableFrom(target)) {
            return new BigDecimal(source.toString());
        }
        // handle : byte, short, int, long, float, double
        String name = target.getSimpleName();
        name = "Integer".equals(name) ? "int" : name;
        name = StringUtils.uncapitalize(name);
        name = name + "Value";
        ReflectUtils ref = ReflectUtils.create(clazz).setBean(source);
        try {
            return ref.call(name);
        }
        catch (ReflectionException e) {
            return source;
        }
    }

}
