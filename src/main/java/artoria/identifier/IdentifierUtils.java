package artoria.identifier;

import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import artoria.util.Assert;

/**
 * Identifier generator tools.
 * @author Kahle
 */
public class IdentifierUtils {
    private static final StringIdentifierGenerator STRING_IDENTIFIER_GENERATOR = new SimpleIdGenerator();
    private static final LongIdentifierGenerator LONG_IDENTIFIER_GENERATOR = new SnowFlakeIdGenerator();
    private static Logger log = LoggerFactory.getLogger(IdentifierUtils.class);
    private static StringIdentifierGenerator stringIdentifierGenerator;
    private static LongIdentifierGenerator longIdentifierGenerator;

    public static StringIdentifierGenerator getStringIdentifierGenerator() {

        return stringIdentifierGenerator != null ? stringIdentifierGenerator : STRING_IDENTIFIER_GENERATOR;
    }

    public static void setStringIdentifierGenerator(StringIdentifierGenerator stringIdentifierGenerator) {
        Assert.notNull(stringIdentifierGenerator, "Parameter \"stringIdentifierGenerator\" must not null. ");
        String generatorClassName = stringIdentifierGenerator.getClass().getName();
        log.info("Set string identifier generator: {}", generatorClassName);
        IdentifierUtils.stringIdentifierGenerator = stringIdentifierGenerator;
    }

    public static LongIdentifierGenerator getLongIdentifierGenerator() {

        return longIdentifierGenerator != null ? longIdentifierGenerator : LONG_IDENTIFIER_GENERATOR;
    }

    public static void setLongIdentifierGenerator(LongIdentifierGenerator longIdentifierGenerator) {
        Assert.notNull(longIdentifierGenerator, "Parameter \"longIdentifierGenerator\" must not null. ");
        String generatorClassName = longIdentifierGenerator.getClass().getName();
        log.info("Set long identifier generator: {}", generatorClassName);
        IdentifierUtils.longIdentifierGenerator = longIdentifierGenerator;
    }

    public static String nextStringIdentifier() {

        return getStringIdentifierGenerator().nextStringIdentifier();
    }

    public static Long nextLongIdentifier() {

        return getLongIdentifierGenerator().nextLongIdentifier();
    }

}
