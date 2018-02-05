package com.apyhs.artoria.converter;

import com.apyhs.artoria.time.DateUtils;
import com.apyhs.artoria.util.Assert;
import com.apyhs.artoria.util.ClassUtils;
import com.apyhs.artoria.util.StringUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.util.Date;

import static com.apyhs.artoria.time.DateUtils.DEFAULT_DATE_PATTERN;
import static com.apyhs.artoria.util.Const.FALSE;
import static com.apyhs.artoria.util.Const.TRUE;

/**
 * String converter.
 * @author Kahle
 */
public class StringConverter implements Converter {

    private String pattern = DEFAULT_DATE_PATTERN;

    public StringConverter() {}

    public StringConverter(String datePattern) {
        this.setPattern(datePattern);
    }

    public String getPattern() {
        return pattern;
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
            return ConvertUtils.convert(bInt, target);
        }
        try {
            Date date = DateUtils.parse(srcStr, pattern);
            return ConvertUtils.convert(date, target);
        }
        catch (ParseException e) {
            return source;
        }
    }

    protected Object stringToBoolean(Object source, Class<?> target) {
        if (TRUE.equalsIgnoreCase((String) source)) {
            return true;
        }
        else if (FALSE.equalsIgnoreCase((String) source)) {
            return false;
        }
        else {
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
        if (!String.class.isAssignableFrom(clazz)) {
            return source;
        }
        if (Number.class.isAssignableFrom(target)) {
            BigDecimal d = new BigDecimal((String) source);
            return ConvertUtils.convert(d, target);
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
