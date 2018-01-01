package com.apyhs.artoria.converter;

import com.apyhs.artoria.logging.Logger;
import com.apyhs.artoria.logging.LoggerFactory;
import com.apyhs.artoria.util.Assert;
import com.apyhs.artoria.util.ClassUtils;
import com.apyhs.artoria.util.DateUtils;
import com.apyhs.artoria.util.StringUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.util.Date;

import static com.apyhs.artoria.util.DateUtils.DEFAULT_DATE_PATTERN;
import static com.apyhs.artoria.util.StringConstant.*;

/**
 * String converter.
 * @author Kahle
 */
public class StringConverter implements Converter {
    private static final Logger log = LoggerFactory.getLogger(StringConverter.class);

    private String pattern = DEFAULT_DATE_PATTERN;

    public StringConverter() {
    }

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

    @Override
    public Object convert(Object source, Class<?> target) {
        Assert.notNull(source, "Source must is not null. ");
        Assert.notNull(target, "Target must is not null. ");
        Class<?> clazz = source.getClass();
        log.debug(StringConverter.class.getSimpleName() + COLON + clazz.getName() + " >> " + target.getName());
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
