package artoria.generator;

import artoria.util.Assert;
import artoria.util.StringUtils;

import java.util.UUID;

import static artoria.common.Constants.MINUS;

/**
 * Id generator simple implement by uuid.
 * @author Kahle
 */
public class SimpleIdGenerator implements IdGenerator<String> {
    private String separator;
    private boolean needReplace;

    public SimpleIdGenerator() {
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
    public String generate() {
        String uuid = UUID.randomUUID().toString();
        return needReplace ? StringUtils.replace(uuid, MINUS, separator) : uuid;
    }

}
