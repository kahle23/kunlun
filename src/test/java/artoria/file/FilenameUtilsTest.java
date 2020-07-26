package artoria.file;

import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import org.junit.Test;

public class FilenameUtilsTest {
    private static Logger log = LoggerFactory.getLogger(FilenameUtilsTest.class);

    @Test
    public void testRootPathAndClasspath() {
        log.info(FilenameUtils.getRootPath());
        log.info(FilenameUtils.getClasspath());
    }

    @Test
    public void testGetExtension() {
        log.info(FilenameUtils.getExtension("C:\\windows\\system\\123.txt"));
        log.info(FilenameUtils.getExtension("C:\\win.dows\\system"));
        log.info(FilenameUtils.getExtension("C:\\windows\\system\\"));
    }

    @Test
    public void testRemoveExtension() {
        log.info(FilenameUtils.removeExtension("C:\\windows\\system\\123.txt"));
        log.info(FilenameUtils.removeExtension("C:\\windows\\system"));
        log.info(FilenameUtils.removeExtension("C:\\windows\\system\\"));
        log.info(FilenameUtils.removeExtension("C:\\windows\\system\\.anonymous"));
        log.info(FilenameUtils.removeExtension("C:\\windows\\system\\1.anonymous"));
        log.info(FilenameUtils.removeExtension("C:\\windows\\system\\2.anonymous"));
    }

    @Test
    public void testPath() {
        log.info(FilenameUtils.getPackagePath(FilenameUtilsTest.class));
        log.info(FilenameUtils.getClassFilePath(FilenameUtilsTest.class));
    }

}
