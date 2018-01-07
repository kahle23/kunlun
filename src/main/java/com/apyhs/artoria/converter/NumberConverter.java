package com.apyhs.artoria.converter;

import com.apyhs.artoria.exception.ReflectionException;
import com.apyhs.artoria.logging.Logger;
import com.apyhs.artoria.logging.LoggerFactory;
import com.apyhs.artoria.reflect.ReflectUtils;
import com.apyhs.artoria.util.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import static com.apyhs.artoria.util.StringConstant.COLON;

/**
 * Number converter.
 * @author Kahle
 */
public class NumberConverter implements Converter {
    private static final Logger log = LoggerFactory.getLogger(NumberConverter.class);
    private static final String INTEGER = "Integer";
    private static final String VALUE = "Value";
    private static final String INT = "int";

    private Boolean isUnixTimestamp = false;

    public NumberConverter() {
    }

    public NumberConverter(Boolean isUxTp) {
        this.setUnixTimestamp(isUxTp);
    }

    public Boolean getUnixTimestamp() {
        return isUnixTimestamp;
    }

    public void setUnixTimestamp(Boolean unixTimestamp) {
        Assert.notNull(unixTimestamp, "Is Unix timestamp must is not null. ");
        isUnixTimestamp = unixTimestamp;
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
        ReflectUtils ref = ReflectUtils.create(clazz);
        ref.setBean(source);
        try {
            return ref.call(name);
        }
        catch (ReflectionException e) {
            return source;
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
        Assert.notNull(source, "Source must is not null. ");
        Assert.notNull(target, "Target must is not null. ");
        Class<?> clazz = source.getClass();
        log.debug(NumberConverter.class.getSimpleName() + COLON + clazz.getName() + " >> " + target.getName());
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
