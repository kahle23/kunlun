package apyh.artoria.converter;

import apyh.artoria.logging.Logger;
import apyh.artoria.logging.LoggerFactory;
import apyh.artoria.util.ClassUtils;
import apyh.artoria.util.DateUtils;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;

import static apyh.artoria.util.StringConstant.STRING_FALSE;
import static apyh.artoria.util.StringConstant.STRING_TRUE;

/**
 * String converter
 * @author Kahle
 */
public class StringConverter implements Converter {
    private static final Logger log = LoggerFactory.getLogger(StringConverter.class);

    @Override
    public Object convert(Object source, Class<?> target) {
        Class<?> clazz = source.getClass();
        log.debug(StringConverter.class.getSimpleName() + ": " + clazz.getName() + " To " + target.getName());
        target = ClassUtils.getWrapper(target);
        if (target.isAssignableFrom(clazz)) {
            return source;
        }
        if (!String.class.isAssignableFrom(clazz)) {
            return source;
        }
        if (Number.class.isAssignableFrom(target)) {
            BigDecimal d = new BigDecimal((String) source);
            return TypeUtils.convert(d, target);
        }
        if (Boolean.class.isAssignableFrom(target)) {
            return this.stringToBoolean(source);
        }
        if (CharSequence.class.isAssignableFrom(target)) {
            return source;
        }
        if (Date.class.isAssignableFrom(target)) {
            try {
                Date date = DateUtils.parse((String) source);
                return TypeUtils.convert(date, target);
            }
            catch (ParseException e) {
                return source;
            }
        }
        return source;
    }

    private Object stringToBoolean(Object source) {
        if (STRING_TRUE.equalsIgnoreCase((String) source)) {
            return true;
        }
        else if (STRING_FALSE.equalsIgnoreCase((String) source)) {
            return false;
        }
        else {
            return source;
        }
    }

}
