package artoria.convert.support;

import artoria.convert.ConversionProvider;
import artoria.time.DateUtils;
import artoria.util.StringUtils;

import java.util.Date;

public class StringToDateConverter extends AbstractClassConverter {

    public StringToDateConverter() {

        super(String.class, Date.class);
    }

    public StringToDateConverter(ConversionProvider conversionProvider) {

        super(conversionProvider, String.class, Date.class);
    }

    @Override
    protected Object convert(Object source, Class<?> sourceClass, Class<?> targetClass) {
        String srcStr = (String) source;
        if (StringUtils.isNumeric(srcStr)) {
            Long parseLong = Long.parseLong(srcStr);
            // Maybe is unix timestamp
            // So hand on NumberConverter
            return getConversionProvider().convert(parseLong, targetClass);
        }
        try {
            Date date = DateUtils.parse(srcStr);
            return getConversionProvider().convert(date, targetClass);
        }
        catch (Exception e) {
            // Do nothing.
        }
        return source;
    }

}
