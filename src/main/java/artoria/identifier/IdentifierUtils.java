package artoria.identifier;

import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import artoria.util.Assert;

/**
 * Identifier tools.
 * @author Kahle
 */
public class IdentifierUtils {
    private static Logger log = LoggerFactory.getLogger(IdentifierUtils.class);
    private static StringIdentifierGenerator stringIdGenerator;
    private static LongIdentifierGenerator longIdGenerator;

    public static StringIdentifierGenerator getStringIdGenerator() {
        if (stringIdGenerator != null) { return stringIdGenerator; }
        synchronized (IdentifierUtils.class) {
            if (stringIdGenerator != null) { return stringIdGenerator; }
            IdentifierUtils.setStringIdGenerator(new SimpleIdGenerator());
            return stringIdGenerator;
        }
    }

    public static void setStringIdGenerator(StringIdentifierGenerator idGenerator) {
        Assert.notNull(idGenerator, "Parameter \"idGenerator\" must not null. ");
        log.info("Set string id generator: {}", idGenerator.getClass().getName());
        IdentifierUtils.stringIdGenerator = idGenerator;
    }

    public static LongIdentifierGenerator getLongIdGenerator() {
        if (longIdGenerator != null) { return longIdGenerator; }
        synchronized (IdentifierUtils.class) {
            if (longIdGenerator != null) { return longIdGenerator; }
            IdentifierUtils.setLongIdGenerator(new SnowFlakeIdGenerator());
            return longIdGenerator;
        }
    }

    public static void setLongIdGenerator(LongIdentifierGenerator idGenerator) {
        Assert.notNull(idGenerator, "Parameter \"idGenerator\" must not null. ");
        log.info("Set long id generator: {}", idGenerator.getClass().getName());
        IdentifierUtils.longIdGenerator = idGenerator;
    }

    public static String nextStringIdentifier() {

        return getStringIdGenerator().nextStringIdentifier();
    }

    public static Long nextLongIdentifier() {

        return getLongIdGenerator().nextLongIdentifier();
    }

}
