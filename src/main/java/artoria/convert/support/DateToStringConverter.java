package artoria.convert.support;

import artoria.time.DateUtils;
import artoria.util.Assert;

import java.util.Date;

import static artoria.common.Constants.DEFAULT_DATETIME_PATTERN;

public class DateToStringConverter extends AbstractClassConverter {
    private String dateToStringPattern = DEFAULT_DATETIME_PATTERN;

    public DateToStringConverter() {

        super(Date.class, String.class);
    }

    public String getDateToStringPattern() {

        return dateToStringPattern;
    }

    public void setDateToStringPattern(String dateToStringPattern) {
        Assert.notBlank(dateToStringPattern, "Parameter \"dateToStringPattern\" must not blank. ");
        this.dateToStringPattern = dateToStringPattern;
    }

    @Override
    protected Object convert(Object source, Class<?> sourceClass, Class<?> targetClass) {
        Date date = (Date) source;
        return DateUtils.format(date, dateToStringPattern);
    }

}
