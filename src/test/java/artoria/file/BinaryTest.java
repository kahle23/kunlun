package artoria.file;

import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

public class BinaryTest {
    private static Logger log = LoggerFactory.getLogger(BinaryTest.class);
    private static File testGenerated = new File("target\\test-classes\\test_generated.txt");
    private static File testRead = new File("src\\test\\resources\\test_read.txt");

    @Test
    public void testWriteToByteArray() throws IOException {
        Binary binary = new Binary();
        binary.readFromFile(testRead);
        log.info("{}", new String(binary.writeToByteArray()));
    }

    @Test
    public void testWriteToHexString() throws IOException {
        Binary binary = new Binary();
        binary.readFromFile(testRead);
        log.info("{}", binary.writeToHexString());
    }

}
