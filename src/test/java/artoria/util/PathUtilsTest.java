package artoria.util;

import org.junit.Test;

public class PathUtilsTest {

    @Test
    public void testRootPathAndClasspath() {
        System.out.println(PathUtils.getRootPath());
        System.out.println(PathUtils.getClasspath());
    }

    @Test
    public void testFindClasspath() {
        System.out.println(PathUtils.findClasspath("logging.properties"));
        System.out.println(PathUtils.findClasspath("logging111.properties"));
    }

    @Test
    public void testGetExtension() {
        System.out.println(PathUtils.getExtension("C:\\windows\\system\\123.txt"));
        System.out.println(PathUtils.getExtension("C:\\win.dows\\system"));
        System.out.println(PathUtils.getExtension("C:\\windows\\system\\"));
    }

    @Test
    public void testStripExtension() {
        System.out.println(PathUtils.stripExtension("C:\\windows\\system\\123.txt"));
        System.out.println(PathUtils.stripExtension("C:\\windows\\system"));
        System.out.println(PathUtils.stripExtension("C:\\windows\\system\\"));
    }

    @Test
    public void testNotExistPath() {
        System.out.println(PathUtils.notExistPath("E:\\_cache\\123.csv"));
        System.out.println(PathUtils.notExistPath("E:\\_cache\\123_1.csv"));
        System.out.println(PathUtils.notExistPath("E:\\_cache\\123_2.csv"));
    }

    @Test
    public void testPath() {
        System.out.println(PathUtils.getPackagePath(PathUtilsTest.class));
        System.out.println(PathUtils.getClassFilePath(PathUtilsTest.class));
    }

}
