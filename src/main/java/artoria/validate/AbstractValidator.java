package artoria.validate;

import artoria.util.Assert;

/**
 * The abstract validator.
 * @author Kahle
 */
public abstract class AbstractValidator implements Validator {
    private final String name;

    public AbstractValidator(String name) {
        Assert.notBlank(name, "Parameter \"name\" must not blank. ");
        this.name = name;
    }

    @Override
    public String getName() {

        return name;
    }

}
