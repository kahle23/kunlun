package com.github.kahlkn.artoria.util;

import org.junit.Test;

public class PathUtilsTest {

    @Test
    public void test() {
        System.out.println(PathUtils.findJarClasspath("logging.properties"));
    }

    @Test
    public void test1() {
        System.out.println(PathUtils.getExtension("C:\\windows\\system\\123.txt"));
        System.out.println(PathUtils.getExtension("C:\\windows\\system"));
        System.out.println(PathUtils.getExtension("C:\\windows\\system\\"));
        System.out.println(PathUtils.stripExtension("C:\\windows\\system\\123.txt"));
        System.out.println(PathUtils.stripExtension("C:\\windows\\system"));
        System.out.println(PathUtils.stripExtension("C:\\windows\\system\\"));
    }

    @Test
    public void test2() {
        System.out.println(PathUtils.getPackagePath(PathUtilsTest.class));
        System.out.println(PathUtils.getClassFilePath(PathUtilsTest.class));
    }

}
