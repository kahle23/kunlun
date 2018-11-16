package artoria.common;

import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import org.junit.Test;

import static artoria.common.Constants.*;

public class ConstantsTest {
    private static Logger log = LoggerFactory.getLogger(ConstantsTest.class);

    @Test
    public void test1() {
        log.info(CLASSPATH);
        log.info(ROOT_PATH);
        log.info(FILE_SEPARATOR);
        log.info(PATH_SEPARATOR);
        log.info(DEFAULT_CHARSET_NAME);
        log.info(NEWLINE);
        log.info(GET);
    }

}
