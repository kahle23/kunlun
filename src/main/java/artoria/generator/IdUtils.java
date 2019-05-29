package artoria.generator;

import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import artoria.util.Assert;

import static artoria.common.Constants.EMPTY_STRING;

/**
 * Id generator tools.
 * @author Kahle
 */
public class IdUtils {
    private static final IdGenerator<String> DEFAULT_GENERATOR = new SimpleIdGenerator(EMPTY_STRING);
    private static Logger log = LoggerFactory.getLogger(IdUtils.class);
    private static IdGenerator<String> stringIdGenerator;
    private static IdGenerator<Long> numberIdGenerator;

    public static IdGenerator<String> getStringIdGenerator() {

        return stringIdGenerator != null ? stringIdGenerator : DEFAULT_GENERATOR;
    }

    public static void setStringIdGenerator(IdGenerator<String> stringIdGenerator) {
        Assert.notNull(stringIdGenerator, "Parameter \"stringIdGenerator\" must not null. ");
        log.info("Set string id generator: {}", stringIdGenerator.getClass().getName());
        IdUtils.stringIdGenerator = stringIdGenerator;
    }

    public static IdGenerator<Long> getNumberIdGenerator() {

        return numberIdGenerator;
    }

    public static void setNumberIdGenerator(IdGenerator<Long> numberIdGenerator) {
        Assert.notNull(numberIdGenerator, "Parameter \"numberIdGenerator\" must not null. ");
        log.info("Set number id generator: {}", numberIdGenerator.getClass().getName());
        IdUtils.numberIdGenerator = numberIdGenerator;
    }

    public static String nextString() {

        return getStringIdGenerator().generate();
    }

    public static Long nextNumber() {

        return getNumberIdGenerator().generate();
    }

}
