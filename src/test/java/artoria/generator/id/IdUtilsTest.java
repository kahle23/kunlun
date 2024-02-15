package artoria.generator.id;

import artoria.generator.id.support.SnowflakeIdGenerator;
import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import org.junit.Test;

import static artoria.common.constant.Numbers.FIVE;
import static artoria.common.constant.Numbers.ZERO;
import static artoria.common.constant.Symbols.NEWLINE;

/**
 * The id tools Test.
 * @author Kahle
 */
public class IdUtilsTest {
    private static final Logger log = LoggerFactory.getLogger(IdUtilsTest.class);
    private final Integer groupLength = FIVE;
    private final Integer count = 100;

    @Test
    public void test1() {
        IdUtils.registerGenerator("snowflake", new SnowflakeIdGenerator());
        StringBuilder builder = new StringBuilder();
        for (int i = ZERO; i < count; i++) {
            Long number = IdUtils.nextLong("snowflake");
            builder.append(number)
                    .append("(")
                    .append(number.toString().length())
                    .append(")")
                    .append(" ");
            if (i % groupLength == ZERO) {
                builder.append(NEWLINE);
            }
        }
        log.info(builder.toString());
    }

    @Test
    public void test2() {
        StringBuilder builder = new StringBuilder();
        for (int i = ZERO; i < count; i++) {
            String string = IdUtils.nextString("uuid");
            builder.append(string)
                    .append("(")
                    .append(string.length())
                    .append(")")
                    .append(" ");
            if (i % groupLength == ZERO) {
                builder.append(NEWLINE);
            }
        }
        log.info(builder.toString());
    }

}
