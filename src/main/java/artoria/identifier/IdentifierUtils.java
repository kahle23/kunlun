package artoria.identifier;

import artoria.generator.id.IdGenerator;
import artoria.generator.id.IdUtils;
import artoria.generator.id.support.SnowflakeIdGenerator;
import artoria.logging.Logger;
import artoria.logging.LoggerFactory;

/**
 * Identifier tools.
 * @author Kahle
 */
@Deprecated // TODO: 2023/3/7 Deletable
public class IdentifierUtils {
    private static Logger log = LoggerFactory.getLogger(IdentifierUtils.class);

    public static IdGenerator getStringIdGenerator() {

        return IdUtils.getIdGenerator("uuid");
    }

    public static void setStringIdGenerator(IdGenerator idGenerator) {

        throw new UnsupportedOperationException();
    }

    public static IdGenerator getLongIdGenerator() {
        String name = "snowflake";
        if (IdUtils.getIdGenerator(name) != null) {
            return IdUtils.getIdGenerator(name);
        }
        synchronized (IdentifierUtils.class) {
            if (IdUtils.getIdGenerator(name) != null) {
                return IdUtils.getIdGenerator(name);
            }
            IdUtils.registerGenerator(name, new SnowflakeIdGenerator());
            return IdUtils.getIdGenerator(name);
        }
    }

    public static void setLongIdGenerator(IdGenerator idGenerator) {

        throw new UnsupportedOperationException();
    }

    public static String nextStringIdentifier() {

        return (String) getStringIdGenerator().next();
    }

    public static Long nextLongIdentifier() {

        return (Long) getLongIdGenerator().next();
    }

}
