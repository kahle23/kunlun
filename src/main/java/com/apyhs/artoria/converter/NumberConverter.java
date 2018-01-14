package com.apyhs.artoria.converter;

import com.apyhs.artoria.exception.UncheckedException;
import com.apyhs.artoria.reflect.ReflectUtils;
import com.apyhs.artoria.util.Assert;
import com.apyhs.artoria.util.ClassUtils;
import com.apyhs.artoria.util.DateUtils;
import com.apyhs.artoria.util.StringUtils;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

/**
 * Number converter.
 * @author Kahle
 */
public class NumberConverter implements Converter {
    private static final String INTEGER = "Integer";
    private static final String VALUE = "Value";
    private static final String INT = "int";

    private Boolean isUnixTimestamp = false;

    public NumberConverter() {}

    public NumberConverter(Boolean isUxTp) {
        this.setIsUnixTimestamp(isUxTp);
    }

    public Boolean getIsUnixTimestamp() {
        return isUnixTimestamp;
    }

    public void setIsUnixTimestamp(Boolean isUnixTimestamp) {
        Assert.notNull(isUnixTimestamp, "Parameter \"isUnixTimestamp\" must not null. ");
        this.isUnixTimestamp = isUnixTimestamp;
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
            throw new UncheckedException(e);
        }
    }

    protected Object numberToDate(Object source, Class<?> target) {
        Number number = (Number) source;
        long lg = number.longValue();
        lg = isUnixTimestamp ? lg * 1000L : lg;
        Date date = DateUtils.parse(lg);
        // Maybe target is sql date or timestamp
        return ConvertUtils.convert(date, target);
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
