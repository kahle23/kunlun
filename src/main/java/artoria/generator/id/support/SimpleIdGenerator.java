package artoria.generator.id.support;

import java.util.UUID;

import static artoria.common.Constants.EMPTY_STRING;
import static artoria.common.Constants.MINUS;

/**
 * The simple identifier generator based on uuid.
 * @author Kahle
 */
public class SimpleIdGenerator extends AbstractIdGenerator implements StringIdGenerator {
    private boolean isSimple = false;

    public SimpleIdGenerator() {

        this(true);
    }

    public SimpleIdGenerator(boolean isSimple) {

        this.isSimple = isSimple;
    }

    @Override
    public String next(Object... arguments) {
        String uuid = UUID.randomUUID().toString();
        return isSimple ? uuid.replaceAll(MINUS, EMPTY_STRING) : uuid;
    }

}
