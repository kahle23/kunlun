package artoria.generator;

import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import org.junit.Test;

import static artoria.common.Constants.NEWLINE;

public class IdUtilsTest {
    private static Logger log = LoggerFactory.getLogger(IdUtilsTest.class);
    private Integer groupLength = 5;
    private Integer count = 100;

    @Test
    public void test1() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < count; i++) {
            Long number = IdUtils.nextNumber();
            builder.append(number)
                    .append("(")
                    .append(number.toString().length())
                    .append(")")
                    .append(" ");
            if (i % groupLength == 0) {
                builder.append(NEWLINE);
            }
        }
        log.info(builder.toString());
    }

    @Test
    public void test2() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < count; i++) {
            String string = IdUtils.nextString();
            builder.append(string)
                    .append("(")
                    .append(string.length())
                    .append(")")
                    .append(" ");
            if (i % groupLength == 0) {
                builder.append(NEWLINE);
            }
        }
        log.info(builder.toString());
    }

}
