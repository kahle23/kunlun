package artoria.common;

import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import org.junit.Test;

import static artoria.common.constant.Charsets.STR_DEFAULT_CHARSET;
import static artoria.common.constant.Env.*;
import static artoria.common.constant.Symbols.*;
import static artoria.common.constant.Words.GET;

/**
 * The constants Test.
 * @author Kahle
 */
public class ConstantsTest {
    private static final Logger log = LoggerFactory.getLogger(ConstantsTest.class);

    @Test
    public void test1() {
        log.info(NEWLINE);
        log.info(FILE_SEPARATOR);
        log.info(PATH_SEPARATOR);
        log.info(COMPUTER_NAME);
        log.info(ROOT_PATH);
        log.info(CLASSPATH);
        log.info(HOST_NAME);
        log.info(STR_DEFAULT_CHARSET);
        log.info(GET);
    }

}
