package artoria.identifier;

import artoria.util.Assert;
import artoria.util.StringUtils;

import java.util.UUID;

import static artoria.common.Constants.EMPTY_STRING;
import static artoria.common.Constants.MINUS;

/**
 * Id generator simple implement by uuid.
 * @author Kahle
 */
public class SimpleIdGenerator implements StringIdentifierGenerator {
    private boolean needReplace = false;
    private String separator;

    public SimpleIdGenerator() {

        this(EMPTY_STRING);
    }

    public SimpleIdGenerator(String separator) {

        this.setSeparator(separator);
    }

    public String getSeparator() {

        return this.separator;
    }

    public void setSeparator(String separator) {
        Assert.notNull(separator, "Parameter \"separator\" must not null. ");
        this.separator = separator;
        this.needReplace = !MINUS.equals(separator);
    }

    @Override
    public Object nextIdentifier() {

        return nextStringIdentifier();
    }

    @Override
    public String nextStringIdentifier() {
        String uuid = UUID.randomUUID().toString();
        return needReplace ? StringUtils.replace(uuid, MINUS, separator) : uuid;
    }

}
