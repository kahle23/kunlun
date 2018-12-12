package artoria.file;

import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static artoria.common.Constants.NEWLINE;

public class FileFactoryTest {
    private static Logger log = LoggerFactory.getLogger(TxtTest.class);
    private static File testGenerated = new File("target\\test-classes\\test_generated.txt");
    private static File testRead = new File("src\\test\\resources\\test_read.txt");

    @Test
    public void test1() throws IOException {
        Txt txt = FileFactory.getInstance(testRead);
        log.info(NEWLINE + txt.writeToString());
        txt = FileFactory.getInstance(Txt.class, testRead, "GB2312");
        log.info(NEWLINE + txt.writeToString());
    }

    @Test
    public void test2() throws IOException {
        PropertiesFile file = FileFactory.getInstance("jdbc.properties");
        file.getProperties().list(System.out);
    }

}
