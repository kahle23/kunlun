package artoria.converter;

import artoria.time.DateUtils;
import artoria.util.Assert;
import artoria.util.ClassUtils;
import artoria.util.StringUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.util.Date;

/**
 * String converter.
 * @author Kahle
 */
public class StringConverter implements TypeConverter {
    private String pattern = DateUtils.DEFAULT_DATE_PATTERN;

    public StringConverter() {
    }

    public StringConverter(String datePattern) {

        this.setPattern(datePattern);
    }

    public String getPattern() {

        return this.pattern;
    }

    public void setPattern(String datePattern) {
        Assert.notBlank(datePattern, "Date pattern must is not blank. ");
        this.pattern = datePattern;
    }

    protected Object stringToDate(Object source, Class<?> target) {
        String srcStr = (String) source;
        if (StringUtils.isNumeric(srcStr)) {
            BigInteger bInt = new BigInteger(srcStr);
            // Maybe is unix timestamp
            // so hand on NumberConverter
            return TypeConvertUtils.convert(bInt, target);
        }
        try {
            Date date = DateUtils.parse(srcStr, this.pattern);
            return TypeConvertUtils.convert(date, target);
        }
        catch (ParseException e) {
            return source;
        }
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
        if (target.isAssignableFrom(clazz)) {
            return source;
        }
        if (!String.class.isAssignableFrom(clazz)) {
            return source;
        }
        if (Number.class.isAssignableFrom(target)) {
            BigDecimal d = new BigDecimal((String) source);
            return TypeConvertUtils.convert(d, target);
        }
        if (Boolean.class.isAssignableFrom(target)) {
            return this.stringToBoolean(source, target);
        }
        if (Date.class.isAssignableFrom(target)) {
            return this.stringToDate(source, target);
        }
        return source;
    }

}
