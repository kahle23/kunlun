package artoria.convert.support;

import artoria.convert.ConversionService;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;

public class DateToDateConverter extends AbstractClassConverter {

    public DateToDateConverter() {

        super(Date.class, Date.class);
    }

    public DateToDateConverter(ConversionService conversionService) {

        super(conversionService, Date.class, Date.class);
    }

    @Override
    protected Object convert(Object source, Class<?> sourceClass, Class<?> targetClass) {
        long time = ((Date) source).getTime();
        if (java.sql.Date.class.isAssignableFrom(targetClass)) {
            return new java.sql.Date(time);
        }
        if (Timestamp.class.isAssignableFrom(targetClass)) {
            return new Timestamp(time);
        }
        if (Time.class.isAssignableFrom(targetClass)) {
            return new Time(time);
        }
        return source;
    }

}
