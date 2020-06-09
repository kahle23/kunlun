package artoria.convert.type;

import artoria.time.DateUtils;
import artoria.util.Assert;
import artoria.util.ClassUtils;
import artoria.util.StringUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

/**
 * String converter.
 * @author Kahle
 */
public class StringConverter implements TypeConverter {

    protected Object stringToDate(Object source, Class<?> target) {
        String srcStr = (String) source;
        if (StringUtils.isNumeric(srcStr)) {
            BigInteger bInt = new BigInteger(srcStr);
            // Maybe is unix timestamp
            // So hand on NumberConverter
            return TypeConvertUtils.convert(bInt, target);
        }
        try {
            Date date = DateUtils.parse(srcStr);
            return TypeConvertUtils.convert(date, target);
        }
        catch (Exception e) {
            // Do nothing.
        }
        return source;
    }

    protected Object stringToBoolean(Object source, Class<?> target) {

        return Boolean.valueOf((String) source);
    }

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
        if (!String.class.isAssignableFrom(clazz)) {
            return source;
        }
        if (StringUtils.isBlank((String) source)) {
            return null;
        }
        if (Number.class.isAssignableFrom(target)) {
            String numString = (String) source;
            numString = numString.trim();
            BigDecimal decimal = new BigDecimal(numString);
            return TypeConvertUtils.convert(decimal, target);
        }
        if (Boolean.class.isAssignableFrom(target)) {
            return stringToBoolean(source, target);
        }
        if (Date.class.isAssignableFrom(target)) {
            return stringToDate(source, target);
        }
        return source;
    }

}
