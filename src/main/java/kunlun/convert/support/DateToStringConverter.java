/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.convert.support;

import kunlun.time.DateUtils;
import kunlun.util.Assert;

import java.util.Date;

import static kunlun.common.constant.TimePatterns.DEFAULT_DATETIME;

public class DateToStringConverter extends AbstractClassConverter {
    private String dateToStringPattern = DEFAULT_DATETIME;

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
