package artoria.file;

import org.junit.Test;

import java.io.IOException;

public class PropertiesFileTest {

    @Test
    public void test1() throws IOException {
        PropertiesFile file = new PropertiesFile();
        file.readFromClasspath("logging.properties");
        file.getProperties().list(System.out);
    }

    @Test
    public void test2() throws IOException {
        PropertiesFile file = new PropertiesFile();
        file.readFromClasspath("jdbc.properties");
        file.getProperties().list(System.out);
    }

}
