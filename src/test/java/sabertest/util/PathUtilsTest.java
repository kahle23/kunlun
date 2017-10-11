package sabertest.util;

import org.junit.Test;
import saber.util.PathUtils;

public class PathUtilsTest {

    @Test
    public void test1() throws Exception {
        System.out.println(PathUtils.FILE_SEPARATOR);
        System.out.println(PathUtils.PATH_SEPARATOR);
    }

    @Test
    public void test2() throws Exception {
        System.out.println(PathUtils.getRootPath());
        System.out.println(PathUtils.getClassPath());
    }

    @Test
    public void test3() throws Exception {
        System.out.println(PathUtils.getPackagePath(PathUtilsTest.class));
        System.out.println(PathUtils.getClassFilePath(PathUtilsTest.class));
    }

}
