/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.data.validation.support;

import kunlun.data.validation.BooleanValidator;
import kunlun.util.Assert;

import java.util.regex.Pattern;

/**
 * The regex validator.
 * @author Kahle
 */
public class RegexValidator implements BooleanValidator {
    private final Pattern pattern;
    private final String regex;

    public RegexValidator(String regex) {
        Assert.notBlank(regex, "Parameter \"regex\" must not blank. ");
        this.pattern = Pattern.compile(regex);
        this.regex = regex;
    }

    public String getRegex() {

        return regex;
    }

    @Override
    public Boolean validate(Object target) {
        Assert.isInstanceOf(CharSequence.class, target
                , "The argument must be of type char sequence. ");
        return pattern.matcher((CharSequence) target).matches();
    }

}
