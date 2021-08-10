package artoria.option;

import artoria.convert.ConversionUtils;
import artoria.util.Assert;
import artoria.util.ObjectUtils;

public abstract class AbstractOptionProvider implements OptionProvider {

    @Override
    public <T> T getRequiredOption(String owner, String name, Class<T> type) {
        Assert.notNull(type, "Parameter \"type\" must not null. ");
        T value = getOption(owner, name, type, null);
        Assert.state(value != null, "The option value is null. ");
        return value;
    }

    @Override
    public <T> T getOption(String owner, String name, Class<T> type, T defaultValue) {
        Assert.notNull(type, "Parameter \"type\" must not null. ");
        Object value = getOption(owner, name, defaultValue);
        if (value == null) { return defaultValue; }
        value = ConversionUtils.convert(value, type);
        return ObjectUtils.cast(value, type);
    }

}
