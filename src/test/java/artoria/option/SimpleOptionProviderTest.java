package artoria.option;

import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import org.junit.Test;

public class SimpleOptionProviderTest {
    private static Logger log = LoggerFactory.getLogger(SimpleOptionProviderTest.class);

    @Test
    public void test1() {
        OptionUtils.setOption("test_key", true);
        log.info("Test set: {}", OptionUtils.getOption("test_key"));
    }

}
