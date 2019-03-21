package artoria.generator;

import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import artoria.time.Clock;
import artoria.time.SystemClock;
import artoria.util.Assert;

/**
 * Id generator tools.
 * @author Kahle
 */
public class IdUtils {
    private static final IdGenerator<String> DEFAULT_STRING_ID_GENERATOR;
    private static final IdGenerator<Long> DEFAULT_NUMBER_ID_GENERATOR;
    private static Logger log = LoggerFactory.getLogger(IdUtils.class);
    private static IdGenerator<String> stringIdGenerator;
    private static IdGenerator<Long> numberIdGenerator;

    static {
        Clock clock = SystemClock.getInstance();
        long defaultId = 1L;
        DEFAULT_NUMBER_ID_GENERATOR = new SnowFlakeIdGenerator(defaultId, defaultId, clock);
        DEFAULT_STRING_ID_GENERATOR = new SimpleIdGenerator();
    }

    public static IdGenerator<String> getStringIdGenerator() {

        return stringIdGenerator != null ? stringIdGenerator : DEFAULT_STRING_ID_GENERATOR;
    }

    public static void setStringIdGenerator(IdGenerator<String> stringIdGenerator) {
        Assert.notNull(stringIdGenerator, "Parameter \"stringIdGenerator\" must not null. ");
        log.info("Set id generator: {}", stringIdGenerator.getClass().getName());
        IdUtils.stringIdGenerator = stringIdGenerator;
    }

    public static IdGenerator<Long> getNumberIdGenerator() {

        return numberIdGenerator != null ? numberIdGenerator : DEFAULT_NUMBER_ID_GENERATOR;
    }

    public static void setNumberIdGenerator(IdGenerator<Long> numberIdGenerator) {
        Assert.notNull(numberIdGenerator, "Parameter \"numberIdGenerator\" must not null. ");
        log.info("Set id generator: {}", numberIdGenerator.getClass().getName());
        IdUtils.numberIdGenerator = numberIdGenerator;
    }

    public static String nextString() {

        return getStringIdGenerator().generate();
    }

    public static Long nextNumber() {

        return getNumberIdGenerator().generate();
    }

}
