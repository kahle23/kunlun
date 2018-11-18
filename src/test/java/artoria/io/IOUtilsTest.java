package artoria.io;

import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

public class IOUtilsTest {
    private static Logger log = LoggerFactory.getLogger(IOUtilsTest.class);

    @Test
    public void test1() throws IOException {
        InputStream in = IOUtils.findClasspath("jdbc.properties");
        log.info(IOUtils.toString(in));
    }

}
