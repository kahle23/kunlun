package artoriatest.util;

import artoria.util.PathUtils;
import org.junit.Test;

public class PathUtilsTest {

    @Test
    public void test() throws Exception {
        System.out.println();
    }

    @Test
    public void test1() throws Exception {
        System.out.println(PathUtils.getExtension("C:\\windows\\system\\123.txt"));
        System.out.println(PathUtils.getExtension("C:\\windows\\system"));
        System.out.println(PathUtils.getExtension("C:\\windows\\system\\"));
        System.out.println(PathUtils.stripExtension("C:\\windows\\system\\123.txt"));
        System.out.println(PathUtils.stripExtension("C:\\windows\\system"));
        System.out.println(PathUtils.stripExtension("C:\\windows\\system\\"));
    }

    @Test
    public void test2() throws Exception {
        System.out.println(PathUtils.getRootPath());
        System.out.println(PathUtils.getClasspath());
        System.out.println(PathUtils.getPackagePath(PathUtilsTest.class));
        System.out.println(PathUtils.getClassFilePath(PathUtilsTest.class));
    }

}
