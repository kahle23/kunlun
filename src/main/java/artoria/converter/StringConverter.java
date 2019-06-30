package artoria.converter;

import artoria.time.DateUtils;
import artoria.util.Assert;
import artoria.util.ClassUtils;
import artoria.util.StringUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.util.*;

import static artoria.common.Constants.DEFAULT_DATE_PATTERN;
import static artoria.common.Constants.FILLED_DATE_PATTERN;

/**
 * String converter.
 * @author Kahle
 */
public class StringConverter implements TypeConverter {
    private Set<String> datePatterns = new HashSet<String>();

    public StringConverter() {
        datePatterns.addAll(Arrays.asList(
                DEFAULT_DATE_PATTERN,
                "yyyy-MM-dd HH:mm:ss",
                "yyyy-MM-dd HH:mm",
                "yyyy-MM-dd",
                "yyyy/MM/dd HH:mm:ss",
                "yyyy/MM/dd HH:mm",
                "yyyy/MM/dd",
                FILLED_DATE_PATTERN
        ));
    }

    public Set<String> getDatePatterns() {

        return Collections.unmodifiableSet(datePatterns);
    }

    public void addDatePatterns(String... datePatterns) {
        Assert.notEmpty(datePatterns, "Parameter \"datePatterns\" must not empty. ");
        this.datePatterns.addAll(Arrays.asList(datePatterns));
    }

    public void addDatePatterns(Collection<String> datePatterns) {
        Assert.notEmpty(datePatterns, "Parameter \"datePatterns\" must not empty. ");
        this.datePatterns.addAll(datePatterns);
    }

    protected Object stringToDate(Object source, Class<?> target) {
        String srcStr = (String) source;
        if (StringUtils.isNumeric(srcStr)) {
            BigInteger bInt = new BigInteger(srcStr);
            // Maybe is unix timestamp
            // So hand on NumberConverter
            return TypeConvertUtils.convert(bInt, target);
        }
        for (String datePattern : datePatterns) {
            try {
                Date date = DateUtils.parse(srcStr, datePattern);
                return TypeConvertUtils.convert(date, target);
            }
            catch (ParseException e) {
            }
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
            return this.stringToBoolean(source, target);
        }
        if (Date.class.isAssignableFrom(target)) {
            return this.stringToDate(source, target);
        }
        return source;
    }

}
