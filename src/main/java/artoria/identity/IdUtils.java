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

    public static IdGenerator getIdGenerator() {
        if (idGenerator != null) {
            return idGenerator;
        }
        synchronized (IdGenerator.class) {
            if (idGenerator != null) {
                return idGenerator;
            }
            setIdGenerator(new SimpleIdGenerator());
            return idGenerator;
        }
    }

    public static void setIdGenerator(IdGenerator idGenerator) {
        Assert.notNull(idGenerator, "Parameter \"idGenerator\" must not null. ");
        synchronized (IdGenerator.class) {
            log.info("Set id generator: " + idGenerator.getClass().getName());
            IdUtils.idGenerator = idGenerator;
        }
    }

    public static Number nextNumber() {

        return getIdGenerator().nextNumber();
    }

    public static String nextString() {

        return getIdGenerator().nextString();
    }

}
