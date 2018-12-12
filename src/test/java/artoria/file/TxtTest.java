package artoria.file;

import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static artoria.common.Constants.NEWLINE;

public class TxtTest {
    private static Logger log = LoggerFactory.getLogger(TxtTest.class);
    private static File testGenerated = new File("target\\test-classes\\test_generated.txt");
    private static File testRead = new File("src\\test\\resources\\test_read.txt");

    @Test
    public void test1() throws IOException {
        Txt txt = new Txt();
        txt.append("Hello, World! ")
                .append(NEWLINE)
                .append("    Hello, Artoria. ")
                .append(NEWLINE);
        txt.writeToFile(testGenerated);
    }

    @Test
    public void test2() throws IOException {
        Txt txt = new Txt();
        txt.readFromFile(testRead);
        log.info(NEWLINE + txt.writeToString());
        txt.append("        This is the test. ")
                .append(NEWLINE);
        txt.writeToFile(testGenerated);
    }

    @Test
    public void test3() throws IOException {
        Txt txt = new Txt();
//        log.info("Read length: " + txt.readFromClasspath("jdbc.properties"));
        log.info("Read length: " + txt.readFromClasspath("LICENSE"));
        log.info(txt.writeToString());
    }

    @Test
    public void test4() throws IOException {
        Txt txt = new Txt();
        txt.readFromClasspath("test_read.txt");
        log.info(txt.writeToString());
        txt.append("This is the test4. ").append(NEWLINE);
        txt.writeToClasspath("test_read_generated.txt");
    }

}
