package artoria.identity;

import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import org.junit.Test;

import static artoria.common.Constants.EMPTY_STRING;

public class IdUtilsTest {
    private static Logger log = LoggerFactory.getLogger(IdUtilsTest.class);

    @Test
    public void test1() {
        for (int i = 0; i < 100; i++) {
            Long number = IdUtils.nextNumber();
            log.info(number + "  " + number.toString().length());
            log.info("");
        }
    }

    @Test
    public void test2() {
        SimpleIdGenerator idGenerator = new SimpleIdGenerator(EMPTY_STRING);
        IdUtils.setStringIdGenerator(idGenerator);
        for (int i = 0; i < 100; i++) {
            String string = IdUtils.nextString();
            log.info(string + "  " + string.length());
            log.info("");
        }
    }

}
