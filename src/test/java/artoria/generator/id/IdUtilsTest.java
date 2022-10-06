package artoria.generator.id;

import artoria.generator.id.support.SnowflakeIdGenerator;
import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import org.junit.Test;

import static artoria.common.Constants.*;

public class IdUtilsTest {
    private static Logger log = LoggerFactory.getLogger(IdUtilsTest.class);
    private Integer groupLength = FIVE;
    private Integer count = 100;

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
