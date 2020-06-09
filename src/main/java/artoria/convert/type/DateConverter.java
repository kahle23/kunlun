package artoria.convert.type;

import artoria.reflect.ReflectUtils;
import artoria.time.DateUtils;
import artoria.util.Assert;
import artoria.util.ClassUtils;

import java.util.Date;

import static artoria.common.Constants.DEFAULT_DATETIME_PATTERN;

/**
 * Date converter.
 * @author Kahle
 */
public class DateConverter implements TypeConverter {
    private String datePattern = DEFAULT_DATETIME_PATTERN;

    public String getDatePattern() {

        return datePattern;
    }

    public void setDatePattern(String datePattern) {
        Assert.notBlank(datePattern, "Parameter \"datePattern\" must not blank. ");
        this.datePattern = datePattern;
    }

    protected Object dateToDate(Object source, Class<?> target) {
        long time = ((Date) source).getTime();
        try {
            return ReflectUtils.newInstance(target, time);
        }
        catch (Exception e) {
            return source;
        }
    }

    protected Object dateToString(Object source, Class<?> target) {
        Date date = (Date) source;
        return DateUtils.format(date, datePattern);
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
        if (!Date.class.isAssignableFrom(clazz)) {
            return source;
        }
        if (Date.class.isAssignableFrom(target)) {
            return dateToDate(source, target);
        }
        if (String.class.isAssignableFrom(target)) {
            return dateToString(source, target);
        }
        return source;
    }

}
