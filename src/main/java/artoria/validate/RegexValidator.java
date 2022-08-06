package artoria.validate;

import artoria.util.Assert;

import java.util.regex.Pattern;

/**
 * The regex validator.
 * @author Kahle
 */
public class RegexValidator implements Validator {
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
