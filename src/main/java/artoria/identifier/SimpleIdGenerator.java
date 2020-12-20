package artoria.identifier;

import java.util.UUID;

import static artoria.common.Constants.EMPTY_STRING;
import static artoria.common.Constants.MINUS;

/**
 * Id generator simple implement by uuid.
 * @author Kahle
 */
public class SimpleIdGenerator implements StringIdentifierGenerator {
    private boolean isSimple = false;

    public SimpleIdGenerator() {

        this(true);
    }

    public SimpleIdGenerator(boolean isSimple) {

        this.isSimple = isSimple;
    }

    @Override
    public Object nextIdentifier() {

        return nextStringIdentifier();
    }

    @Override
    public String nextStringIdentifier() {
        String uuid = UUID.randomUUID().toString();
        return isSimple ? uuid.replaceAll(MINUS, EMPTY_STRING) : uuid;
    }

}
