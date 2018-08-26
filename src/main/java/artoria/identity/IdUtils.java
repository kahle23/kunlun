package artoria.identity;

import artoria.util.Assert;

import java.util.logging.Logger;

/**
 * Id generator tools.
 * @author Kahle
 */
public class IdUtils {
    private static Logger log = Logger.getLogger(IdUtils.class.getName());
    private static IdGenerator idGenerator;

    static {
        IdUtils.setIdGenerator(new SimpleIdGenerator());
    }

    public static IdGenerator getIdGenerator() {
        return idGenerator;
    }

    public static void setIdGenerator(IdGenerator idGenerator) {
        Assert.notNull(idGenerator, "Parameter \"idGenerator\" must not null. ");
        log.info("Set id generator: " + idGenerator.getClass().getName());
        IdUtils.idGenerator = idGenerator;
    }

    public static Number nextNumber() {
        return idGenerator.nextNumber();
    }

    public static String nextString() {
        return idGenerator.nextString();
    }

}
