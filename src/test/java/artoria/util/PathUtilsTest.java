package artoria.util;

import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import org.junit.Test;

public class PathUtilsTest {
    private static Logger log = LoggerFactory.getLogger(PathUtilsTest.class);

    @Test
    public void testRootPathAndClasspath() {
        log.info(PathUtils.getRootPath());
        log.info(PathUtils.getClasspath());
    }

    @Test
    public void testFindClasspath() {
        log.info(PathUtils.findClasspath("logging.properties"));
        log.info(PathUtils.findClasspath("logging111.properties"));
    }

    @Test
    public void testGetExtension() {
        log.info(PathUtils.getExtension("C:\\windows\\system\\123.txt"));
        log.info(PathUtils.getExtension("C:\\win.dows\\system"));
        log.info(PathUtils.getExtension("C:\\windows\\system\\"));
    }

    @Test
    public void testStripExtension() {
        log.info(PathUtils.stripExtension("C:\\windows\\system\\123.txt"));
        log.info(PathUtils.stripExtension("C:\\windows\\system"));
        log.info(PathUtils.stripExtension("C:\\windows\\system\\"));
    }

    @Test
    public void testPath() {
        log.info(PathUtils.getPackagePath(PathUtilsTest.class));
        log.info(PathUtils.getClassFilePath(PathUtilsTest.class));
    }

}
