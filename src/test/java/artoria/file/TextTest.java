package artoria.file;

import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static artoria.common.Constants.NEWLINE;

public class TextTest {
    private static Logger log = LoggerFactory.getLogger(TextTest.class);
    private static File testGenerated = new File("target\\test-classes\\test_generated.txt");
    private static File testRead = new File("src\\test\\resources\\test_read.txt");

    @Test
    public void test1() throws IOException {
        Text text = new Text();
        text.append("Hello, World! ")
                .append(NEWLINE)
                .append("    Hello, Artoria. ")
                .append(NEWLINE);
        text.writeToFile(testGenerated);
    }

    @Test
    public void test2() throws IOException {
        Text text = new Text();
        text.readFromFile(testRead);
        log.info(NEWLINE + text.writeToString());
        text.append("        This is the test. ")
                .append(NEWLINE);
        text.writeToFile(testGenerated);
    }

    @Test
    public void test3() throws IOException {
        Text text = new Text();
//        log.info("Read length: {}", txt.readFromClasspath("jdbc.properties"));
        log.info("Read length: {}", text.readFromClasspath("jdbc.properties"));
        log.info(text.writeToString());
    }

    @Test
    public void test4() throws IOException {
        Text text = new Text();
        text.readFromClasspath("test_read.txt");
        log.info(text.writeToString());
        text.append("This is the test4. ").append(NEWLINE);
        text.writeToClasspath("test_read_generated.txt");
    }

}
