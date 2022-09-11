package artoria.storage;

import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import artoria.storage.support.LocalFileStorage;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.util.Collection;

import static artoria.common.Constants.DEFAULT;

public class StorageUtilsTest {
    private static Logger log = LoggerFactory.getLogger(StorageUtilsTest.class);
    private static final String TEST_KEY = "/test/test_file.txt";
    private static final String TEST_DIR = "testDir";

    @Test
    public void test1() {
        StorageUtils.register(new LocalFileStorage(TEST_DIR, "\\test_dir"));
        StorageUtils.put(DEFAULT, TEST_KEY, "Hello, world! ");
        log.info(StorageUtils.get(DEFAULT, TEST_KEY, String.class));
    }

    @Test
    public void test2() {
        StorageUtils.put(DEFAULT, TEST_KEY, "Hello, world! ");
        log.info("{}", StorageUtils.remove(DEFAULT, TEST_KEY));
    }

    @Test
    public void test3() {
        Collection<File> files = StorageUtils.keys(DEFAULT, "", File.class);
        for (File file : files) {
            log.info("{}", file);
        }
    }

    @Ignore
    @Test
    public void test() {
        // This is just an example, and the configuration part is omitted.
        File inputFile = new File("/test/input_file.txt");
        String testPath = "/test/test_file.txt";
        // Save the file to a local folder.
        StorageUtils.put("local", testPath, inputFile);
        // Save the file to HuaWei cloud OBS.
        StorageUtils.put("obs",   testPath, inputFile);
        // Save the file to Ali cloud OSS.
        StorageUtils.put("oss",   testPath, inputFile);
        // Save the file to MinIO.
        StorageUtils.put("minio", testPath, inputFile);
    }

}
