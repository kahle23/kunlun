package artoria.converter;

import artoria.exception.ExceptionUtils;
import artoria.reflect.ReflectUtils;
import artoria.time.DateUtils;
import artoria.util.Assert;
import artoria.util.ClassUtils;
import artoria.util.StringUtils;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

/**
 * Number converter.
 * @author Kahle
 */
public class NumberConverter implements TypeConverter {
    private static final String INTEGER = "Integer";
    private static final String VALUE = "Value";
    private static final String INT = "int";
    private Boolean unixTimestamp = false;

    public NumberConverter() {
    }

    public NumberConverter(Boolean unixTimestamp) {

        setUnixTimestamp(unixTimestamp);
    }

    public Boolean getUnixTimestamp() {

        return unixTimestamp;
    }

    public void setUnixTimestamp(Boolean unixTimestamp) {
        Assert.notNull(unixTimestamp, "Parameter \"unixTimestamp\" must not null. ");
        this.unixTimestamp = unixTimestamp;
    }

    protected Object numberToNumber(Object source, Class<?> target) {
        if (BigInteger.class.isAssignableFrom(target)) {
            return new BigInteger(source.toString());
        }
        if (BigDecimal.class.isAssignableFrom(target)) {
            return new BigDecimal(source.toString());
        }
        // handle : byte, short, int, long, float, double
        Class<?> clazz = source.getClass();
        String name = target.getSimpleName();
        name = INTEGER.equals(name) ? INT : name;
        name = StringUtils.uncapitalize(name);
        name = name + VALUE;
        try {
            Method method = ReflectUtils.findMethod(clazz, name);
            return method.invoke(source);
        }
        catch (Exception e) {
            throw ExceptionUtils.wrap(e);
        }
    }

    protected Object numberToDate(Object source, Class<?> target) {
        Number number = (Number) source;
        long lg = number.longValue();
        lg = unixTimestamp ? lg * 1000L : lg;
        Date date = DateUtils.parse(lg);
        // Maybe target is sql date or timestamp
        return TypeConvertUtils.convert(date, target);
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
        if (!Number.class.isAssignableFrom(clazz)) {
            return source;
        }
        if (String.class.isAssignableFrom(target)) {
            return source.toString();
        }
        if (Date.class.isAssignableFrom(target)) {
            return this.numberToDate(source, target);
        }
        if (!Number.class.isAssignableFrom(target)) {
            return source;
        }
        return this.numberToNumber(source, target);
    }

}
